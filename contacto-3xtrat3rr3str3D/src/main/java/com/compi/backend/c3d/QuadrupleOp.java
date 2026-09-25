package com.compi.backend.c3d;

public enum QuadrupleOp {
    // Aritmética
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),

    // Lógica
    AND("&&"),
    OR("||"),
    NOT("!"),

    // Asignación simple
    ASSIGN("="),

    // Saltos
    GOTO("goto"),
    IF_TRUE("if"),
    IF_FALSE("if_false"),
    LABEL("label"),

    // Relacionales condicionales (if arg1 op arg2 goto result)
    IF_EQ("=="),
    IF_NE("!="),
    IF_LT("<"),
    IF_LE("<="),
    IF_GT(">"),
    IF_GE(">="),

    // Funciones
    CALL("call"),
    PARAM("param"),
    RETURN("return"),
    FUNCTION_START("func_begin"),
    FUNCTION_END("func_end"),

    // Memoria Stack y Heap
    STACK_SET("stack_set"), // stack[(int)arg1] = arg2
    STACK_GET("stack_get"), // result = stack[(int)arg1]
    HEAP_SET("heap_set"),   // heap[(int)arg1] = arg2
    HEAP_GET("heap_get"),   // result = heap[(int)arg1]

    // Punteros de entorno
    SET_P("set_P"),         // P = arg1
    GET_P("get_P"),         // result = P
    SET_H("set_H"),         // H = arg1
    GET_H("get_H"),         // result = H

    // Entrada / Salida
    PRINT_INT("print_int"),
    PRINT_FLOAT("print_float"),
    PRINT_CHAR("print_char"),
    PRINT_STR("print_str"),
    PRINTLN("println"),
    READ("read");

    private final String symbol;

    QuadrupleOp(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
