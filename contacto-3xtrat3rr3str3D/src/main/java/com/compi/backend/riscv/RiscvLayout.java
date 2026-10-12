package com.compi.backend.riscv;

import com.compi.backend.c3d.FunctionRuntime;
import com.compi.backend.c3d.Quadruple;
import com.compi.backend.c3d.QuadrupleOp;
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
 * Distribución de memoria del programa RISC-V: celdas globales en la base de la
 * pila, frames por invocación y el offset de cada parámetro, local y temporal.
 * Calcula lo mismo que necesita el emisor plano, pero dentro de su propio módulo.
 */
final class RiscvLayout {

    /** Nombre de la función de entrada (cuerpo principal del programa). */
    static final String ENTRY = "main";

    final Map<String, List<Quadruple>> bodies = new LinkedHashMap<>();
    final Map<String, List<String>> signatures = new LinkedHashMap<>();
    final Map<String, Map<String, Integer>> slots = new LinkedHashMap<>();
    final Map<String, Integer> frames = new LinkedHashMap<>();
    final Map<String, Integer> globals = new LinkedHashMap<>();

    RiscvLayout(List<Quadruple> quadruples, SymbolTable symbolTable) {
        split(quadruples);
        signatures.putAll(signaturesOf(symbolTable));
        assignFrames();
        assignGlobals(quadruples);
    }

    static boolean isGlobal(String value) {
        return value != null && value.startsWith("g_");
    }

    static boolean isTemp(String value) {
        return value != null && value.matches("t[0-9]+");
    }

    static boolean isLocal(String value) {
        return value != null && value.startsWith("v_");
    }

    static String safe(String name) {
        return name.replaceAll("[^A-Za-z0-9_]", "_");
    }

    /** Nombre de etiqueta de una función del lenguaje. */
    static String label(String function) {
        return "LF_" + safe(function);
    }

    /** Firmas de las funciones y métodos a partir de la tabla de símbolos. */
    private static Map<String, List<String>> signaturesOf(SymbolTable symbolTable) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (symbolTable == null) {
            return result;
        }
        for (Symbol symbol : symbolTable.getAllSymbols()) {
            if (symbol.getCategory() == SymbolCategory.FUNCTION) {
                result.put(symbol.getName(),
                        symbol.getParameters().stream().map(Symbol::getStorageName).toList());
            }
            if (symbol.getCategory() != SymbolCategory.CLASS) {
                continue;
            }
            for (Symbol method : symbol.getMembers()) {
                addMethod(result, symbolTable, symbol, method);
            }
        }
        return result;
    }

    private static void addMethod(Map<String, List<String>> result, SymbolTable symbolTable,
                                  Symbol type, Symbol method) {
        String name = FunctionRuntime.name(type.getName(), method.getName(),
                method.getParameters().size());
        String scope = (method.getCategory() == SymbolCategory.CONSTRUCTOR
                ? "constructor_" : "method_") + name;
        List<String> parameters = new ArrayList<>();
        symbolTable.getAllSymbols().stream()
                .filter(value -> "this".equals(value.getName()) && scope.equals(value.getScope()))
                .findFirst().ifPresent(value -> parameters.add(value.getStorageName()));
        method.getParameters().forEach(value -> parameters.add(value.getStorageName()));
        result.put(name, parameters);
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

    int slot(String function, String value) {
        Map<String, Integer> map = slots.get(function);
        return map == null ? 0 : map.getOrDefault(value, 0);
    }

    int frame(String function) {
        return frames.getOrDefault(function, 1);
    }

    int global(String value) {
        return globals.getOrDefault(value, 0);
    }
}
