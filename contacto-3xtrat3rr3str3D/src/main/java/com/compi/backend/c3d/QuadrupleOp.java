package com.compi.backend.c3d;

public enum QuadrupleOp {

    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),
    NEG("-"),
    CONCAT("concat"),

    AND("&&"),
    OR("||"),
    NOT("!"),

    ASSIGN("="),

    ARRAY_GET("[]"),
    ARRAY_SET("[]="),

    GOTO("goto"),
    IF_TRUE("if"),
    IF_FALSE("if_false"),
    LABEL("label"),

    IF_EQ("=="),
    IF_NE("!="),
    IF_LT("<"),
    IF_LE("<="),
    IF_GT(">"),
    IF_GE(">="),

    CALL("call"),
    PARAM("param"),
    RETURN("return"),
    FUNCTION_START("func_begin"),
    FUNCTION_END("func_end"),

    STACK_SET("stack_set"),
    STACK_GET("stack_get"),
    HEAP_SET("heap_set"),
    HEAP_GET("heap_get"),

    SET_P("set_P"),
    GET_P("get_P"),
    SET_H("set_H"),
    GET_H("get_H"),

    PRINT_INT("print_int"),
    PRINT_FLOAT("print_float"),
    PRINT_CHAR("print_char"),
    PRINT_STR("print_str"),
    PRINTLN("println"),
    READ("read"),
    READ_STR("read_str");

    private final String symbol;

    QuadrupleOp(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
