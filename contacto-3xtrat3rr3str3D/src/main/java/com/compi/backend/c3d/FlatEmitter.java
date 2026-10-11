package com.compi.backend.c3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Construye un único {@code main} con cuartetas puras: sin funciones C, con la
 * pila ({@code stack}) como memoria, frames por invocación y {@code goto} para
 * el control. El retorno se resuelve con un despachador {@code switch} porque
 * C no permite un {@code goto} calculado.
 */
final class FlatEmitter {

    private final FlatLayout layout;
    private final StringBuilder out = new StringBuilder();
    private final List<Integer> callSites = new ArrayList<>();
    private final List<String> pending = new ArrayList<>();
    private String function = FlatLayout.ENTRY;
    private int nextCallId = 1;
    private int nextUid = 1;
    private boolean returnValueSet;

    FlatEmitter(FlatLayout layout) {
        this.layout = layout;
    }

    String build() {
        out.append(prologue());
        out.append("int main(void) {\n");
        out.append("    P = ").append(layout.globals.size()).append(";\n");
        emitMain();
        emitFunctions();
        if (hasFunctions() || !callSites.isEmpty()) {
            emitDispatch();
        }
        out.append("L_end:\n    return 0;\n}\n");
        return out.toString();
    }

    private boolean hasFunctions() {
        return layout.bodies.keySet().stream().anyMatch(name -> !FlatLayout.ENTRY.equals(name));
    }

    private String prologue() {
        return """
                /* Código de tres direcciones */
                #include <stdio.h>
                #include <stdlib.h>
                #include <string.h>
                double stack[100000];
                double heap[100000];
                double H = 0;
                double P = 0;
                double RET = 0;
                double RA = 0;
                double _ra = 0;
                double _ri = 0;
                char _txt[4096];
                char _rc = 0;
                """;
    }

    private void emitMain() {
        function = FlatLayout.ENTRY;
        returnValueSet = false;
        pending.clear();
        emitBody(layout.bodies.getOrDefault(FlatLayout.ENTRY, List.of()));
        line("goto L_end;");
    }

    private void emitFunctions() {
        for (var entry : layout.bodies.entrySet()) {
            if (FlatLayout.ENTRY.equals(entry.getKey())) {
                continue;
            }
            function = entry.getKey();
            returnValueSet = false;
            pending.clear();
            out.append("LF_").append(safe(entry.getKey())).append(":\n");
            emitBody(entry.getValue());
            line("RET = 0;");
            emitReturn();
        }
    }

    private void emitBody(List<Quadruple> instructions) {
        for (Quadruple instruction : instructions) {
            emitOne(instruction);
        }
    }

    private void emitOne(Quadruple q) {
        switch (q.getOp()) {
            case LABEL, GOTO, IF_TRUE, IF_FALSE, IF_EQ, IF_NE, IF_LT, IF_LE, IF_GT, IF_GE ->
                    emitControl(q);
            case PARAM, CALL, RETURN, STACK_SET -> emitFunction(q);
            case ASSIGN -> emitAssign(q);
            case ADD, SUB, MUL, DIV, INT_DIV, MOD, AND, OR, NOT, NEG -> emitArithmetic(q);
            case ARRAY_GET, ARRAY_SET, HEAP_GET, HEAP_SET, STACK_GET, SET_P, GET_P, SET_H, GET_H ->
                    emitMemory(q);
            case CONCAT -> emitConcat(q);
            case PRINT_STR -> emitPrintString(q);
            case READ_STR -> emitReadText(q);
            case READ_CHAR -> emitReadChar(q);
            default -> emitSimple(q);
        }
    }

    // --- Control de flujo -------------------------------------------------

    private void emitControl(Quadruple q) {
        String result = q.getResult();
        switch (q.getOp()) {
            case LABEL -> out.append(result).append(":;\n");
            case GOTO -> line("goto " + result + ";");
            case IF_TRUE -> line("if (" + operand(q.getArg1()) + ") goto " + result + ";");
            case IF_FALSE -> line("if (!(" + operand(q.getArg1()) + ")) goto " + result + ";");
            default -> line("if (" + operand(q.getArg1()) + " " + q.getOp().getSymbol() + " "
                    + operand(q.getArg2()) + ") goto " + result + ";");
        }
    }

    private void emitDispatch() {
        out.append("L_dispatch:;\n");
        if (callSites.isEmpty()) {
            line("goto L_end;");
            return;
        }
        out.append("    switch ((int)RA) {\n");
        for (int id : callSites) {
            out.append("        case ").append(id).append(": goto L_ret_").append(id).append(";\n");
        }
        out.append("        default: goto L_end;\n");
        out.append("    }\n");
    }

    // --- Llamadas y retornos ---------------------------------------------

    private void emitFunction(Quadruple q) {
        switch (q.getOp()) {
            case PARAM -> pending.add(operand(q.getArg1()));
            case CALL -> emitCall(q);
            case RETURN -> {
                if (!returnValueSet) {
                    line("RET = 0;");
                }
                emitReturn();
            }
            case STACK_SET -> emitStackSet(q);
            default -> { }
        }
    }

    private void emitStackSet(Quadruple q) {
        if ("P".equals(q.getArg1())) {
            line("RET = " + operand(q.getArg2()) + ";");
            returnValueSet = true;
        } else {
            line("stack[(int)" + operand(q.getArg1()) + "] = " + operand(q.getArg2()) + ";");
        }
    }

    private void emitCall(Quadruple q) {
        String name = q.getArg1();
        int count = q.getArg2() == null ? 0 : Integer.parseInt(q.getArg2());
        if (isRuntime(name)) {
            emitRuntimeCall(name, q.getResult());
            return;
        }
        int start = pending.size() - count;
        if (start < 0) {
            throw new IllegalStateException("Faltan argumentos para «" + name + "»");
        }
        int id = nextCallId++;
        callSites.add(id);
        int frame = layout.frame(function);
        for (int i = 0; i < count; i++) {
            line("stack[(int)P + " + (frame + 2 + i) + "] = " + pending.get(start + i) + ";");
        }
        line("stack[(int)P + " + frame + "] = " + id + ";");
        line("stack[(int)P + " + (frame + 1) + "] = P;");
        line("P = P + " + frame + ";");
        line("goto LF_" + safe(name) + ";");
        out.append("L_ret_").append(id).append(":;\n");
        if (q.getResult() != null) {
            line(operand(q.getResult()) + " = RET;");
        }
        pending.subList(start, pending.size()).clear();
        returnValueSet = false;
    }

    private void emitReturn() {
        line("RA = stack[(int)P];");
        line("P = stack[(int)P + 1];");
        line("goto L_dispatch;");
        returnValueSet = false;
    }

    // --- Variables y operaciones -----------------------------------------

    private void emitAssign(Quadruple q) {
        String source = q.getArg1();
        if (source != null && source.startsWith("\"")) {
            writeString(operand(q.getResult()), source);
            return;
        }
        line(operand(q.getResult()) + " = " + operand(source) + ";");
    }

    private void emitArithmetic(Quadruple q) {
        String left = operand(q.getArg1());
        String right = operand(q.getArg2());
        String result = operand(q.getResult());
        switch (q.getOp()) {
            case MOD -> line(result + " = " + left + " - " + right
                    + " * (double)(long long)(" + left + " / " + right + ");");
            case INT_DIV -> line(result + " = (double)(long long)(" + left + " / " + right + ");");
            case NOT -> line(result + " = !" + left + ";");
            case NEG -> line(result + " = -(" + left + ");");
            default -> line(result + " = " + left + " " + q.getOp().getSymbol() + " " + right + ";");
        }
    }

    private void emitMemory(Quadruple q) {
        String a = operand(q.getArg1());
        String b = operand(q.getArg2());
        String r = operand(q.getResult());
        switch (q.getOp()) {
            case ARRAY_GET -> line(r + " = " + a + "[(int)" + b + "];");
            case ARRAY_SET -> line(a + "[(int)" + b + "] = " + r + ";");
            case HEAP_GET -> line(r + " = heap[(int)" + a + "];");
            case HEAP_SET -> line("heap[(int)" + a + "] = " + b + ";");
            case STACK_GET -> line(r + " = stack[(int)" + a + "];");
            case SET_P -> line("P = " + a + ";");
            case GET_P -> line(r + " = P;");
            case SET_H -> line("H = " + a + ";");
            case GET_H -> line(r + " = H;");
            default -> { }
        }
    }

    private void emitSimple(Quadruple q) {
        String a = operand(q.getArg1());
        switch (q.getOp()) {
            case PRINT_INT -> line("printf(\"%d\", (int)" + a + ");");
            case PRINT_FLOAT -> line("printf(\"%f\", " + a + ");");
            case PRINT_CHAR -> line("printf(\"%c\", (char)" + a + ");");
            case PRINTLN -> line("printf(\"\\n\");");
            case READ -> line("scanf(\"%lf\", &" + a + ");");
            default -> line(a + " = 0;");
        }
    }

    // --- Rutinas de soporte en línea (sin funciones C) --------------------

    private void emitConcat(Quadruple q) {
        String first = uid();
        String second = uid();
        String third = uid();
        String end = uid();
        line("_ra = H;");
        line("_ri = " + operand(q.getArg1()) + ";");
        label(first);
        line("if (heap[(int)_ri] == 0) goto " + second + ";");
        line("if (heap[(int)_ri] == -1) goto " + second + ";");
        line("heap[(int)H] = heap[(int)_ri];");
        line("H = H + 1;");
        line("_ri = _ri + 1;");
        line("goto " + first + ";");
        label(second);
        line("_ri = " + operand(q.getArg2()) + ";");
        label(third);
        line("if (heap[(int)_ri] == 0) goto " + end + ";");
        line("if (heap[(int)_ri] == -1) goto " + end + ";");
        line("heap[(int)H] = heap[(int)_ri];");
        line("H = H + 1;");
        line("_ri = _ri + 1;");
        line("goto " + third + ";");
        label(end);
        line("heap[(int)H] = 0;");
        line("H = H + 1;");
        assign(q.getResult(), "_ra");
    }

    private void emitPrintString(Quadruple q) {
        String loop = uid();
        String end = uid();
        line("_ri = " + operand(q.getArg1()) + ";");
        label(loop);
        line("if (heap[(int)_ri] == 0) goto " + end + ";");
        line("if (heap[(int)_ri] == -1) goto " + end + ";");
        line("putchar((char)heap[(int)_ri]);");
        line("_ri = _ri + 1;");
        line("goto " + loop + ";");
        label(end);
    }

    private void emitReadText(Quadruple q) {
        String loop = uid();
        String end = uid();
        line("_ra = H;");
        line("if (scanf(\" %4095[^\\n]\", _txt) != 1) _txt[0] = 0;");
        line("_ri = 0;");
        label(loop);
        line("if (_txt[(int)_ri] == 0) goto " + end + ";");
        line("heap[(int)H] = _txt[(int)_ri];");
        line("H = H + 1;");
        line("_ri = _ri + 1;");
        line("goto " + loop + ";");
        label(end);
        line("heap[(int)H] = 0;");
        line("H = H + 1;");
        assign(q.getArg1(), "_ra");
    }

    private void emitReadChar(Quadruple q) {
        line("scanf(\" %c\", &_rc);");
        assign(q.getArg1(), "(unsigned char)_rc");
    }

    private void emitRuntimeCall(String name, String result) {
        String argument = pending.isEmpty() ? "0" : pending.remove(pending.size() - 1);
        if ("number_to_text".equals(name)) {
            emitNumberToText(argument, result);
        } else if ("char_to_text".equals(name)) {
            line("_ra = H;");
            line("heap[(int)H] = " + argument + ";");
            line("H = H + 1;");
            line("heap[(int)H] = 0;");
            line("H = H + 1;");
            assign(result, "_ra");
        } else {
            emitRuntimeCallByName(name, argument, result);
        }
        returnValueSet = false;
    }

    private void emitRuntimeCallByName(String name, String argument, String result) {
        switch (name) {
            case "concat_cadenas" -> throw new IllegalStateException(
                    "concat_cadenas se emite como CONCAT, no como llamada");
            case "print_string" -> line("_ri = " + argument + ";");
            default -> throw new IllegalStateException("Rutina de soporte desconocida: " + name);
        }
    }

    private void emitNumberToText(String argument, String result) {
        String loop = uid();
        String end = uid();
        line("_ra = H;");
        line("snprintf(_txt, sizeof(_txt), \"%.15g\", " + argument + ");");
        line("_ri = 0;");
        label(loop);
        line("if (_txt[(int)_ri] == 0) goto " + end + ";");
        line("heap[(int)H] = _txt[(int)_ri];");
        line("H = H + 1;");
        line("_ri = _ri + 1;");
        line("goto " + loop + ";");
        label(end);
        line("heap[(int)H] = 0;");
        line("H = H + 1;");
        assign(result, "_ra");
    }

    private void writeString(String result, String literal) {
        String content = literal.substring(1, literal.length() - 1);
        line(result + " = H;");
        for (int i = 0; i < content.length(); i++) {
            int code = characterCode(content, i);
            if (code < 0) {
                i++;
                code = characterCode(content, i);
            }
            line("heap[(int)H] = " + code + ";");
            line("H = H + 1;");
        }
        line("heap[(int)H] = 0;");
        line("H = H + 1;");
    }

    private static int characterCode(String content, int index) {
        char value = content.charAt(index);
        if (value == '\\' && index + 1 < content.length()) {
            char next = content.charAt(index + 1);
            return switch (next) {
                case 'n' -> '\n';
                case 't' -> '\t';
                case 'r' -> '\r';
                case '\\' -> '\\';
                case '"' -> '"';
                default -> '\\';
            };
        }
        return value;
    }

    // --- Utilidades -------------------------------------------------------

    private boolean isRuntime(String name) {
        return "number_to_text".equals(name) || "char_to_text".equals(name)
                || "print_string".equals(name) || "concat_cadenas".equals(name)
                || "read_text".equals(name) || "read_character".equals(name);
    }

    private void assign(String target, String value) {
        if (target != null) {
            line(operand(target) + " = " + value + ";");
        }
    }

    private String operand(String value) {
        return layout.operand(function, value);
    }

    private void line(String text) {
        out.append("    ").append(text).append('\n');
    }

    private void label(String name) {
        out.append(name).append(":;\n");
    }

    private String uid() {
        return "_l" + nextUid++;
    }

    private static String safe(String name) {
        return name.replaceAll("[^A-Za-z0-9_]", "_");
    }
}
