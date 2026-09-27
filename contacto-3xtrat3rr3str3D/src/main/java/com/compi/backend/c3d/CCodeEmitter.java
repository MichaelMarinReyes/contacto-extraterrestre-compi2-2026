package com.compi.backend.c3d;

import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CCodeEmitter {

    private CCodeEmitter() {
    }

    public static String generateCCode(List<Quadruple> quadruples, Set<String> declaredTemps,
                                       SymbolTable symbolTable) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> operandos = operandosDe(symbolTable);
        Set<String> enHeap = arreglosEnHeap(quadruples, symbolTable);

        sb.append(prologue(declaredTemps, declaracionesDe(symbolTable, enHeap)));

        StringBuilder declaracionesFunciones = new StringBuilder();
        for (Quadruple q : quadruples) {
            if (q.getOp() == QuadrupleOp.FUNCTION_START && !"main".equals(q.getResult())) {
                declaracionesFunciones.append("double ").append(q.getResult()).append("();\n");
            }
        }
        if (declaracionesFunciones.length() > 0) {
            sb.append("/* Funciones del programa */\n").append(declaracionesFunciones).append('\n');
        }

        StringBuilder main = new StringBuilder();
        StringBuilder funciones = new StringBuilder();
        boolean inFunction = false;
        boolean mainOpen = false;

        Map<String, List<String>> parametersByFunction = parametersByFunction(symbolTable);
        List<String> parametrosPendientes = new ArrayList<>();

        String returnValue = null;
        boolean functionAlreadyReturns = false;

        for (Quadruple q : quadruples) {
            if (q.getOp() == QuadrupleOp.FUNCTION_START) {
                if ("main".equals(q.getResult())) {
                    if (!mainOpen) {
                        main.append("int main() {\n");
                        mainOpen = true;
                    }
                } else {
                    inFunction = true;
                    funciones.append("\ndouble ").append(q.getResult()).append("() {\n");
                }
                continue;
            }
            if (q.getOp() == QuadrupleOp.FUNCTION_END) {
                if (inFunction) {

                    if (!functionAlreadyReturns) {
                        funciones.append("    return 0;\n");
                    }
                    funciones.append("}\n\n");
                    inFunction = false;
                    functionAlreadyReturns = false;
                }
                continue;
            }

            StringBuilder target = inFunction ? funciones : main;

            if (!inFunction && !mainOpen) {
                main.append("int main() {\n");
                mainOpen = true;
            }

            if (q.getOp() == QuadrupleOp.PARAM) {
                parametrosPendientes.add(operandoDe(operandos, q.getArg1()));
                continue;
            }
            if (q.getOp() == QuadrupleOp.CALL) {
                List<String> shape = parametersByFunction.getOrDefault(q.getArg1(), List.of());
                int cuantos = Math.min(parametrosPendientes.size(), shape.size());
                for (int i = 0; i < cuantos; i++) {
                    target.append("    ").append(operandoDe(operandos, shape.get(i)))
                            .append(" = ").append(parametrosPendientes.get(i)).append(";\n");
                }
                parametrosPendientes.clear();
            }

            if (q.getOp() == QuadrupleOp.STACK_SET && "P".equals(q.getArg1())) {
                returnValue = operandoDe(operandos, q.getArg2());
                continue;
            }
            if (q.getOp() == QuadrupleOp.RETURN) {
                if (returnValue != null) {
                    target.append("    return ").append(returnValue).append(";\n");
                    returnValue = null;
                    functionAlreadyReturns = true;
                } else if (q.getArg1() != null) {
                    target.append("    return ").append(operandoDe(operandos, q.getArg1()))
                            .append(";\n");
                    functionAlreadyReturns = true;
                }
                continue;
            }

            if (q.getOp() == QuadrupleOp.ASSIGN && q.getArg1() != null
                    && q.getArg1().startsWith("\"") && q.getArg1().endsWith("\"")) {
                writeString(target, q.getResult(), q.getArg1());
                continue;
            }

            String line = q.toString(name -> operandos.getOrDefault(name, name));

            if (q.getOp() == QuadrupleOp.LABEL) {
                target.append(line).append("\n");
            } else {
                target.append("    ").append(line).append("\n");
            }
        }

        sb.append(funciones);
        if (mainOpen) {
            main.append("    return 0;\n}\n\n");
            sb.append(main);
        }

        return sb.toString();
    }

    public static Map<String, String> operandosDe(SymbolTable symbolTable) {
        Map<String, String> operandos = new LinkedHashMap<>();
        if (symbolTable == null) {
            return operandos;
        }
        Map<String, Symbol> unicas = new LinkedHashMap<>();
        for (Symbol sym : symbolTable.getAllSymbols()) {
            if (sym.getCategory() != SymbolCategory.VARIABLE
                    && sym.getCategory() != SymbolCategory.PARAMETER
                    && sym.getCategory() != SymbolCategory.FIELD) {
                continue;
            }
            unicas.putIfAbsent(sym.getName(), sym);
        }
        int cell = 0;
        for (Symbol sym : unicas.values()) {
            Type type = sym.getType();
            if (type != null && type.isArray() && type.totalSize() > 0) {
                operandos.put(sym.getName(), sym.getName());
            } else {
                operandos.put(sym.getName(), "stack[(int)" + cell + "]");
                cell++;
            }
        }
        return operandos;
    }

    private static String operandoDe(Map<String, String> operandos, String name) {
        if (name == null) {
            return "0";
        }
        return operandos.getOrDefault(name, name);
    }

    public static Map<String, List<String>> parametersByFunction(SymbolTable symbolTable) {
        Map<String, List<String>> byFunction = new LinkedHashMap<>();
        if (symbolTable == null) {
            return byFunction;
        }
        for (Symbol sym : symbolTable.getAllSymbols()) {
            if (sym.getCategory() == SymbolCategory.FUNCTION) {
                List<String> nombres = new ArrayList<>();
                for (Symbol param : sym.getParameters()) {
                    nombres.add(param.getName());
                }
                byFunction.put(sym.getName(), nombres);
            }
        }
        return byFunction;
    }

    public static Set<String> arreglosEnHeap(List<Quadruple> quadruples,
                                             SymbolTable symbolTable) {
        Set<String> candidatos = new LinkedHashSet<>();
        if (quadruples == null) {
            return candidatos;
        }
        for (Quadruple q : quadruples) {
            if (q.getOp() != QuadrupleOp.ASSIGN) {
                continue;
            }
            String res = q.getResult();
            if (res != null && res.matches("[A-Za-z_][A-Za-z0-9_]*")) {
                candidatos.add(res);
            }
        }
        if (symbolTable == null) {
            return candidatos;
        }
        Set<String> enHeap = new LinkedHashSet<>();
        for (Symbol sym : symbolTable.getAllSymbols()) {
            String name = sym.getName();
            if (candidatos.contains(name) && sym.getType() != null && sym.getType().isArray()
                    && sym.getType().totalSize() > 0) {
                enHeap.add(name);
            }
        }
        return enHeap;
    }

    public static String declaracionesDe(SymbolTable symbolTable) {
        return declaracionesDe(symbolTable, Set.of());
    }

    public static String declaracionesDe(SymbolTable symbolTable, Set<String> enHeap) {
        StringBuilder declaraciones = new StringBuilder();
        if (symbolTable == null) {
            return "";
        }
        Map<String, Symbol> unicas = new LinkedHashMap<>();
        for (Symbol sym : symbolTable.getAllSymbols()) {
            if ((sym.getCategory() == SymbolCategory.VARIABLE
                    || sym.getCategory() == SymbolCategory.PARAMETER
                    || sym.getCategory() == SymbolCategory.FIELD)
                    && sym.getType() != null && sym.getType().isArray()
                    && sym.getType().totalSize() > 0) {
                unicas.putIfAbsent(sym.getName(), sym);
            }
        }
        for (Symbol sym : unicas.values()) {
            if (enHeap != null && enHeap.contains(sym.getName())) {
                declaraciones.append("double ").append(sym.getName()).append(";\n");
            } else {
                declaraciones.append("double ").append(sym.getName())
                        .append("[").append(sym.getType().totalSize()).append("];\n");
            }
        }
        return declaraciones.toString();
    }

    public static String prologue(Set<String> declaredTemps, String declaraciones) {
        StringBuilder sb = new StringBuilder();
        sb.append("/* Codigo de Tres Direcciones generado para C */\n");
        sb.append("#include <stdio.h>\n");
        sb.append("#include <stdlib.h>\n");
        sb.append("#include <string.h>\n\n");

        sb.append("/* Memoria Stack y Heap */\n");
        sb.append("double stack[100000];\n");
        sb.append("double heap[100000];\n");
        sb.append("double P = 0;\n");
        sb.append("double H = 0;\n\n");

        if (declaredTemps != null && !declaredTemps.isEmpty()) {
            sb.append("/* Variables temporales */\n");
            sb.append("double ");
            int count = 0;
            for (String t : declaredTemps) {
                if (count > 0) sb.append(", ");
                sb.append(t);
                count++;
                if (count % 15 == 0) sb.append("\n       ");
            }
            sb.append(";\n\n");
        }

        if (!declaraciones.isEmpty()) {
            sb.append("/* Arreglos del programa (aplanados) */\n");
            sb.append(declaraciones).append('\n');
        }

        sb.append("/* Funciones de soporte runtime */\n");
        sb.append("void print_string(int heap_idx) {\n");
        sb.append("    int i = heap_idx;\n");
        sb.append("    while (heap[i] != 0 && heap[i] != -1) {\n");
        sb.append("        printf(\"%c\", (char)heap[i]);\n");
        sb.append("        i++;\n");
        sb.append("    }\n");
        sb.append("}\n\n");
        sb.append("double concat_cadenas(double a, double b) {\n");
        sb.append("    int inicio = (int)H;\n");
        sb.append("    int i = (int)a;\n");
        sb.append("    while (heap[i] != 0 && heap[i] != -1) { heap[(int)H] = heap[i]; H++; i++; }\n");
        sb.append("    i = (int)b;\n");
        sb.append("    while (heap[i] != 0 && heap[i] != -1) { heap[(int)H] = heap[i]; H++; i++; }\n");
        sb.append("    heap[(int)H] = 0; H++;\n");
        sb.append("    return inicio;\n");
        sb.append("}\n\n");
        sb.append("double read_text(void) {\n");
        sb.append("    char buffer[4096];\n");
        sb.append("    int inicio = (int)H;\n");
        sb.append("    int i = 0;\n");
        sb.append("    if (fgets(buffer, sizeof(buffer), stdin) == NULL) { buffer[0] = 0; }\n");
        sb.append("    while (buffer[i] != 0 && buffer[i] != '\\n') { heap[(int)H] = buffer[i]; H++; i++; }\n");
        sb.append("    heap[(int)H] = 0; H++;\n");
        sb.append("    return inicio;\n");
        sb.append("}\n\n");
        return sb.toString();
    }

    public static void writeString(StringBuilder target, String res, String literal) {
        String content = literal.substring(1, literal.length() - 1);
        target.append("    ").append(res).append(" = H;\n");
        for (int i = 0; i < content.length(); i++) {
            target.append("    heap[(int)H] = ").append((int) content.charAt(i)).append(";\n");
            target.append("    H = H + 1;\n");
        }
        target.append("    heap[(int)H] = 0;\n");
        target.append("    H = H + 1;\n");
    }
}
