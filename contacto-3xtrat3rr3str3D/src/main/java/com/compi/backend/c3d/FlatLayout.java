package com.compi.backend.c3d;

import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Distribución de memoria del main plano: celdas globales, frames por función y
 * el offset dentro del frame de cada parámetro, local y temporal.
 */
final class FlatLayout {

    /** Nombre de la función de entrada (cuerpo principal del programa). */
    static final String ENTRY = "main";

    final Map<String, List<Quadruple>> bodies = new LinkedHashMap<>();
    final Map<String, List<String>> signatures = new LinkedHashMap<>();
    final Map<String, Map<String, Integer>> slots = new LinkedHashMap<>();
    final Map<String, Integer> frames = new LinkedHashMap<>();
    final Map<String, Integer> globals = new LinkedHashMap<>();

    FlatLayout(List<Quadruple> quadruples, SymbolTable symbolTable) {
        split(quadruples);
        signatures.putAll(parametersByFunction(symbolTable));
        assignFrames();
        assignGlobals(quadruples);
    }

    private void split(List<Quadruple> quadruples) {
        bodies.put(ENTRY, new ArrayList<>());
        if (quadruples == null) {
            return;
        }
        String current = ENTRY;
        for (Quadruple instruction : quadruples) {
            if (instruction.getOp() == QuadrupleOp.FUNCTION_START) {
                current = instruction.getResult() == null ? ENTRY : instruction.getResult();
                bodies.computeIfAbsent(current, key -> new ArrayList<>());
            } else if (instruction.getOp() == QuadrupleOp.FUNCTION_END) {
                current = ENTRY;
            } else {
                bodies.computeIfAbsent(current, key -> new ArrayList<>()).add(instruction);
            }
        }
    }

    private void assignFrames() {
        for (Map.Entry<String, List<Quadruple>> entry : bodies.entrySet()) {
            List<String> parameters = signatures.getOrDefault(entry.getKey(), List.of());
            Map<String, Integer> slot = new LinkedHashMap<>();
            int next = 2;
            for (String parameter : parameters) {
                slot.put(parameter, next++);
            }
            List<String> locals = new ArrayList<>();
            for (String operand : operandsOf(entry.getValue())) {
                if ((isTemp(operand) || isLocal(operand)) && !slot.containsKey(operand)) {
                    locals.add(operand);
                }
            }
            Collections.sort(locals);
            for (String local : locals) {
                slot.put(local, next++);
            }
            slots.put(entry.getKey(), slot);
            frames.put(entry.getKey(), next);
        }
    }

    private void assignGlobals(List<Quadruple> quadruples) {
        if (quadruples == null) {
            return;
        }
        for (Quadruple instruction : quadruples) {
            for (String operand : instructionOperands(instruction)) {
                if (isGlobal(operand)) {
                    globals.putIfAbsent(operand, globals.size());
                }
            }
        }
    }

    private static LinkedHashSet<String> operandsOf(List<Quadruple> instructions) {
        LinkedHashSet<String> operands = new LinkedHashSet<>();
        for (Quadruple instruction : instructions) {
            operands.addAll(instructionOperands(instruction));
        }
        return operands;
    }

    private static List<String> instructionOperands(Quadruple instruction) {
        List<String> operands = new ArrayList<>();
        if (instruction.getArg1() != null) operands.add(instruction.getArg1());
        if (instruction.getArg2() != null) operands.add(instruction.getArg2());
        if (instruction.getResult() != null) operands.add(instruction.getResult());
        return operands;
    }

    /** Traduce un operando a su expresión C dentro del frame de la función indicada. */
    String operand(String function, String value) {
        if (value == null) {
            return "0";
        }
        if (isGlobal(value)) {
            return "stack[" + globals.getOrDefault(value, 0) + "]";
        }
        if (isTemp(value) || isLocal(value)) {
            Map<String, Integer> slot = slots.get(function);
            Integer offset = slot == null ? null : slot.get(value);
            return offset == null ? value : "stack[(int)P + " + offset + "]";
        }
        return value;
    }

    int frame(String function) {
        return frames.getOrDefault(function, 1);
    }

    static boolean isGlobal(String value) {
        return value.startsWith("g_");
    }

    static boolean isTemp(String value) {
        return value.matches("t[0-9]+");
    }

    static boolean isLocal(String value) {
        return value.startsWith("v_");
    }

    /** Firmas de las funciones y métodos a partir de la tabla de símbolos. */
    static Map<String, List<String>> parametersByFunction(SymbolTable symbolTable) {
        Map<String, List<String>> signatures = new LinkedHashMap<>();
        if (symbolTable == null) {
            return signatures;
        }
        for (Symbol symbol : symbolTable.getAllSymbols()) {
            if (symbol.getCategory() == SymbolCategory.FUNCTION) {
                signatures.put(symbol.getName(),
                        symbol.getParameters().stream().map(Symbol::getStorageName).toList());
            }
            if (symbol.getCategory() != SymbolCategory.CLASS) {
                continue;
            }
            for (Symbol method : symbol.getMembers()) {
                String name = FunctionRuntime.name(symbol.getName(), method.getName(),
                        method.getParameters().size());
                String scope = (method.getCategory() == SymbolCategory.CONSTRUCTOR
                        ? "constructor_" : "method_") + name;
                List<String> parameters = new ArrayList<>();
                symbolTable.getAllSymbols().stream()
                        .filter(value -> "this".equals(value.getName()) && scope.equals(value.getScope()))
                        .findFirst().ifPresent(value -> parameters.add(value.getStorageName()));
                method.getParameters().forEach(value -> parameters.add(value.getStorageName()));
                signatures.put(name, parameters);
            }
        }
        return signatures;
    }
}
