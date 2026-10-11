package com.compi.backend.c3d;

import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CCodeEmitter {
    private CCodeEmitter() { }

    /** Emite funciones con parámetros y temporales locales para preservar la recursividad. */
    public static String generateCCode(List<Quadruple> quadruples, Set<String> declaredTemps,
                                      SymbolTable symbolTable) {
        Map<String, List<Quadruple>> bodies = new LinkedHashMap<>();
        bodies.put("main", new ArrayList<>());
        String currentFunction = "main";
        for (Quadruple quadruple : quadruples) {
            if (quadruple.getOp() == QuadrupleOp.FUNCTION_START) {
                currentFunction = quadruple.getResult();
                bodies.computeIfAbsent(currentFunction, key -> new ArrayList<>());
            } else if (quadruple.getOp() == QuadrupleOp.FUNCTION_END) {
                currentFunction = "main";
            } else {
                bodies.get(currentFunction).add(quadruple);
            }
        }
        Map<String, List<String>> signatures = parametersByFunction(symbolTable);
        Map<String, String> operands = globalOperands(symbolTable);
        StringBuilder output = new StringBuilder(runtimePrologue());
        for (String name : bodies.keySet()) {
            if (!"main".equals(name)) output.append(signature(name, signatures)).append(";\n");
        }
        for (Map.Entry<String, List<Quadruple>> function : bodies.entrySet()) {
            String name = function.getKey();
            List<String> parameters = signatures.getOrDefault(name, List.of());
            output.append('\n').append("main".equals(name) ? "int main(void)" : signature(name, signatures))
                    .append(" {\n");
            Set<String> locals = new LinkedHashSet<>();
            for (Quadruple instruction : function.getValue()) {
                for (String operand : new String[]{instruction.getArg1(), instruction.getArg2(), instruction.getResult()}) {
                    if (operand != null && operand.matches("(?:t[0-9]+|v_[A-Za-z0-9_]+)")) locals.add(operand);
                }
            }
            locals.removeAll(parameters);
            for (String local : locals) output.append("    double ").append(local).append(" = 0;\n");
            List<String> pendingArguments = new ArrayList<>();
            String returnValue = null;
            int argumentIndex = 0;
            for (Quadruple instruction : function.getValue()) {
                String first = operand(operands, instruction.getArg1());
                String result = operand(operands, instruction.getResult());
                switch (instruction.getOp()) {
                    case PARAM -> {
                        String argument = "argument_" + argumentIndex++;
                        output.append("    double ").append(argument).append(" = ").append(first).append(";\n");
                        pendingArguments.add(argument);
                    }
                    case CALL -> {
                        int count = Integer.parseInt(instruction.getArg2() == null ? "0" : instruction.getArg2());
                        int start = pendingArguments.size() - count;
                        if (start < 0) throw new IllegalStateException("Faltan argumentos para «" + instruction.getArg1() + "»");
                        output.append("    ");
                        if (instruction.getResult() != null) output.append(result).append(" = ");
                        output.append(instruction.getArg1()).append('(')
                                .append(String.join(", ", pendingArguments.subList(start, pendingArguments.size())))
                                .append(");\n");
                        pendingArguments.subList(start, pendingArguments.size()).clear();
                    }
                    case STACK_SET -> {
                        if ("P".equals(instruction.getArg1())) returnValue = operand(operands, instruction.getArg2());
                        else output.append("    ").append(instruction.toString(value -> operand(operands, value))).append('\n');
                    }
                    case RETURN -> {
                        output.append("    return ").append(returnValue != null ? returnValue
                                : instruction.getArg1() == null ? "0" : first).append(";\n");
                        returnValue = null;
                    }
                    case ASSIGN -> {
                        if (instruction.getArg1() != null && instruction.getArg1().startsWith("\""))
                            writeString(output, result, instruction.getArg1());
                        else output.append("    ").append(result).append(" = ").append(first).append(";\n");
                    }
                    case LABEL -> output.append(instruction.getResult()).append(":;\n");
                    default -> output.append("    ").append(instruction.toString(value -> operand(operands, value))).append('\n');
                }
            }
            if (!pendingArguments.isEmpty()) throw new IllegalStateException("Quedaron argumentos sin una llamada");
            output.append("    return 0;\n}\n");
        }
        return output.toString();
    }

    private static String signature(String name, Map<String, List<String>> signatures) {
        List<String> parameters = signatures.getOrDefault(name, List.of());
        return "double " + name + "(" + (parameters.isEmpty() ? "void"
                : String.join(", ", parameters.stream().map(parameter -> "double " + parameter).toList())) + ")";
    }

    private static String operand(Map<String, String> operands, String value) {
        return value == null ? "0" : operands.getOrDefault(value, value);
    }

    private static Map<String, String> globalOperands(SymbolTable symbolTable) {
        Map<String, String> operands = new LinkedHashMap<>();
        if (symbolTable == null) return operands;
        int cell = 0;
        for (Symbol symbol : symbolTable.getAllSymbols()) {
            if (symbol.isGlobal() && symbol.getCategory() == SymbolCategory.VARIABLE) {
                operands.put(symbol.getStorageName(), "stack[" + cell++ + "]");
            }
        }
        return operands;
    }

    public static Map<String, List<String>> parametersByFunction(SymbolTable symbolTable) {
        Map<String, List<String>> signatures = new LinkedHashMap<>();
        if (symbolTable == null) return signatures;
        for (Symbol symbol : symbolTable.getAllSymbols()) {
            if (symbol.getCategory() == SymbolCategory.FUNCTION) {
                signatures.put(symbol.getName(), symbol.getParameters().stream().map(Symbol::getStorageName).toList());
            }
            if (symbol.getCategory() == SymbolCategory.CLASS) {
                for (Symbol method : symbol.getMembers()) {
                    if (method.getCategory() != SymbolCategory.METHOD && method.getCategory() != SymbolCategory.CONSTRUCTOR) continue;
                    String name = FunctionRuntime.name(symbol.getName(), method.getName(), method.getParameters().size());
                    String scope = (method.getCategory() == SymbolCategory.CONSTRUCTOR ? "constructor_" : "method_") + name;
                    List<String> parameters = new ArrayList<>();
                    symbolTable.getAllSymbols().stream().filter(value -> "this".equals(value.getName()) && scope.equals(value.getScope()))
                            .findFirst().ifPresent(value -> parameters.add(value.getStorageName()));
                    method.getParameters().forEach(value -> parameters.add(value.getStorageName()));
                    signatures.put(name, parameters);
                }
            }
        }
        return signatures;
    }

    private static String runtimePrologue() {
        return """
                /* Código de tres direcciones: variables globales y referencias a memoria. */
                #include <stdio.h>
                #include <stdlib.h>
                #include <string.h>
                #include <math.h>
                double stack[100000];
                double heap[100000];
                /* P identifica la base de pila; H señala la siguiente celda libre del heap. */
                double P = 0;
                double H = 0;
                void print_string(int index) {
                    while (heap[index] != 0 && heap[index] != -1) putchar((char)heap[index++]);
                }
                double concat_cadenas(double left, double right) {
                    int start = (int)H;
                    for (int i = (int)left; heap[i] != 0 && heap[i] != -1; i++) heap[(int)H++] = heap[i];
                    for (int i = (int)right; heap[i] != 0 && heap[i] != -1; i++) heap[(int)H++] = heap[i];
                    heap[(int)H++] = 0;
                    return start;
                }
                double number_to_text(double value) {
                    char buffer[64];
                    snprintf(buffer, sizeof(buffer), "%.15g", value);
                    int start = (int)H;
                    for (int i = 0; buffer[i]; i++) heap[(int)H++] = buffer[i];
                    heap[(int)H++] = 0;
                    return start;
                }
                double read_text(void) {
                    char buffer[4096];
                    int start = (int)H;
                    if (scanf(" %4095[^\\n]", buffer) != 1) buffer[0] = 0;
                    for (int i = 0; buffer[i]; i++) heap[(int)H++] = buffer[i];
                    heap[(int)H++] = 0;
                    return start;
                }
                double char_to_text(double value) {
                    int start = (int)H;
                    heap[(int)H++] = value;
                    heap[(int)H++] = 0;
                    return start;
                }
                double read_character(void) {
                    char value = 0;
                    scanf(" %c", &value);
                    return (unsigned char)value;
                }
                """;
    }

    public static void writeString(StringBuilder target, String result, String literal) {
        String content = literal.substring(1, literal.length() - 1);
        target.append("    ").append(result).append(" = H;\n");
        for (int i = 0; i < content.length(); i++) {
            char character = content.charAt(i);
            if (character == '\\' && i + 1 < content.length()) {
                character = switch (content.charAt(++i)) {
                    case 'n' -> '\n'; case 't' -> '\t'; case 'r' -> '\r'; case '0' -> '\0';
                    default -> content.charAt(i);
                };
            }
            target.append("    heap[(int)H++] = ").append((int)character).append(";\n");
        }
        target.append("    heap[(int)H++] = 0;\n");
    }
}
