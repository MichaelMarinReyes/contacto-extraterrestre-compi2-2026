package com.compi.backend.c3d;

import lombok.Getter;
import lombok.Setter;

import java.util.function.UnaryOperator;

@Getter
@Setter
public class Quadruple {
    private QuadrupleOp op;
    private String arg1;
    private String arg2;
    private String result;

    public Quadruple(QuadrupleOp op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    // ------------------------------------------------------------------
    // Código de tres direcciones (abstracto, sin punto y coma)
    // ------------------------------------------------------------------

    public String toC3D() {
        return switch (op) {
            case LABEL, GOTO, IF_TRUE, IF_FALSE, IF_EQ, IF_NE, IF_LT, IF_LE, IF_GT, IF_GE
                    -> c3dControl(op, arg1, arg2, result);
            case ASSIGN, ADD, SUB, MUL, DIV, MOD, AND, OR, CONCAT, INT_DIV, NOT, NEG
                    -> c3dValue(op, arg1, arg2, result);
            case ARRAY_GET, ARRAY_SET, STACK_GET, STACK_SET, HEAP_GET, HEAP_SET,
                    SET_P, GET_P, SET_H, GET_H -> c3dMemory(op, arg1, arg2, result);
            case CALL, PARAM, RETURN, FUNCTION_START, FUNCTION_END
                    -> c3dCall(op, arg1, arg2, result);
            case PRINT_INT, PRINT_FLOAT, PRINT_CHAR, PRINT_STR, PRINTLN,
                    READ, READ_STR, READ_CHAR -> c3dIO(op, arg1);
            default -> c3dDefault(op, arg1, arg2, result);
        };
    }

    private static String c3dControl(QuadrupleOp op, String a1, String a2, String r) {
        return switch (op) {
            case LABEL -> r + ":";
            case GOTO -> "goto " + r;
            case IF_TRUE -> "if " + a1 + " goto " + r;
            case IF_FALSE -> "ifFalse " + a1 + " goto " + r;
            default -> "if " + a1 + " " + op.getSymbol() + " " + a2 + " goto " + r;
        };
    }

    private static String c3dValue(QuadrupleOp op, String a1, String a2, String r) {
        return switch (op) {
            case ASSIGN -> r + " = " + a1;
            case CONCAT -> r + " = concat " + a1 + " " + a2;
            case INT_DIV -> r + " = trunc(" + a1 + " / " + a2 + ")";
            case NOT -> r + " = !" + a1;
            case NEG -> r + " = -" + a1;
            default -> r + " = " + a1 + " " + op.getSymbol() + " " + a2;
        };
    }

    private static String c3dMemory(QuadrupleOp op, String a1, String a2, String r) {
        return switch (op) {
            case ARRAY_GET -> r + " = " + a1 + "[" + a2 + "]";
            case ARRAY_SET -> a1 + "[" + a2 + "] = " + r;
            case STACK_SET -> "stack[" + a1 + "] = " + a2;
            case STACK_GET -> r + " = stack[" + a1 + "]";
            case HEAP_SET -> "heap[" + a1 + "] = " + a2;
            case HEAP_GET -> r + " = heap[" + a1 + "]";
            case SET_P -> "P = " + a1;
            case GET_P -> r + " = P";
            case SET_H -> "H = " + a1;
            default -> r + " = H";
        };
    }

    private static String c3dCall(QuadrupleOp op, String a1, String a2, String r) {
        return switch (op) {
            case CALL -> (r != null ? r + " = " : "") + "call " + a1
                    + (a2 != null ? " " + a2 : "");
            case PARAM -> "param " + a1;
            case RETURN -> "return" + (a1 != null ? " " + a1 : "");
            case FUNCTION_START -> "func " + r;
            default -> "finfunc";
        };
    }

    private static String c3dIO(QuadrupleOp op, String a1) {
        if (op == QuadrupleOp.PRINTLN) {
            return "print \"\\n\"";
        }
        if (op == QuadrupleOp.READ_STR) {
            return "read_text " + a1;
        }
        if (op == QuadrupleOp.READ_CHAR) {
            return "read_char " + a1;
        }
        if (op == QuadrupleOp.READ) {
            return "scan " + a1;
        }
        return "print " + a1;
    }

    private static String c3dDefault(QuadrupleOp op, String a1, String a2, String r) {
        return (op + " " + (a1 == null ? "" : a1) + " " + (a2 == null ? "" : a2) + " "
                + (r == null ? "" : r)).trim();
    }

    // ------------------------------------------------------------------
    // Forma de cuarteta clásica "(op, arg1, arg2, result)"
    // ------------------------------------------------------------------

    public String toQuadruple() {
        return "(" + op + ", " + (arg1 == null ? "" : arg1) + ", "
                + (arg2 == null ? "" : arg2) + ", " + (result == null ? "" : result) + ")";
    }

    // ------------------------------------------------------------------
    // Forma estilo C (con traductor de operandos)
    // ------------------------------------------------------------------

    public String toString() {
        return toString(UnaryOperator.identity());
    }

    public String toString(UnaryOperator<String> translator) {
        String a1 = arg1 == null ? null : translator.apply(arg1);
        String a2 = arg2 == null ? null : translator.apply(arg2);
        String res = result == null ? null : translator.apply(result);
        return switch (op) {
            case LABEL, GOTO, IF_TRUE, IF_FALSE, IF_EQ, IF_NE, IF_LT, IF_LE, IF_GT, IF_GE
                    -> cControl(op, a1, a2, result);
            case MOD -> res + " = fmod(" + a1 + ", " + a2 + ");";
            case ASSIGN, ADD, SUB, MUL, DIV, AND, OR, CONCAT, INT_DIV, NOT, NEG
                    -> cValue(op, a1, a2, res);
            case ARRAY_GET, ARRAY_SET, STACK_GET, STACK_SET, HEAP_GET, HEAP_SET,
                    SET_P, GET_P, SET_H, GET_H -> cMemory(op, arg1, a1, a2, res);
            case CALL, PARAM, RETURN, FUNCTION_START, FUNCTION_END
                    -> cCall(op, a1, arg1, res, result);
            case PRINT_INT, PRINT_FLOAT, PRINT_CHAR, PRINT_STR, PRINTLN,
                    READ, READ_STR, READ_CHAR -> cIO(op, a1);
            default -> cDefault(op, a1, a2, res);
        };
    }

    private static String cControl(QuadrupleOp op, String a1, String a2, String r) {
        return switch (op) {
            case LABEL -> r + ":";
            case GOTO -> "goto " + r + ";";
            case IF_TRUE -> "if (" + a1 + ") goto " + r + ";";
            case IF_FALSE -> "if (!(" + a1 + ")) goto " + r + ";";
            default -> "if (" + a1 + " " + op.getSymbol() + " " + a2 + ") goto " + r + ";";
        };
    }

    private static String cValue(QuadrupleOp op, String a1, String a2, String res) {
        return switch (op) {
            case ASSIGN -> res + " = " + a1 + ";";
            case CONCAT -> res + " = concat_cadenas(" + a1 + ", " + a2 + ");";
            case INT_DIV -> res + " = trunc(" + a1 + " / " + a2 + ");";
            case NOT -> res + " = !" + a1 + ";";
            case NEG -> res + " = -(" + a1 + ");";
            default -> res + " = " + a1 + " " + op.getSymbol() + " " + a2 + ";";
        };
    }

    private static String cMemory(QuadrupleOp op, String arg1, String a1, String a2, String res) {
        return switch (op) {
            case ARRAY_GET -> res + " = " + a1 + "[(int)" + a2 + "];";
            case ARRAY_SET -> arg1 + "[(int)" + a2 + "] = " + res + ";";
            case STACK_SET -> "stack[(int)" + a1 + "] = " + a2 + ";";
            case STACK_GET -> res + " = stack[(int)" + a1 + "];";
            case HEAP_SET -> "heap[(int)" + a1 + "] = " + a2 + ";";
            case HEAP_GET -> res + " = heap[(int)" + a1 + "];";
            case SET_P -> "P = " + a1 + ";";
            case GET_P -> res + " = P;";
            case SET_H -> "H = " + a1 + ";";
            default -> res + " = H;";
        };
    }

    private static String cCall(QuadrupleOp op, String a1, String arg1, String res, String r) {
        return switch (op) {
            case CALL -> (res != null ? res + " = " : "") + arg1 + "();";
            case PARAM -> "/* param " + a1 + " */";
            case RETURN -> "return" + (a1 != null ? " " + a1 : "") + ";";
            case FUNCTION_START -> "\ndouble " + r + "() {";
            default -> "    return 0;\n}\n";
        };
    }

    private static String cIO(QuadrupleOp op, String a1) {
        return switch (op) {
            case PRINT_INT -> "printf(\"%d\", (int)" + a1 + ");";
            case PRINT_FLOAT -> "printf(\"%f\", " + a1 + ");";
            case PRINT_CHAR -> "printf(\"%c\", (char)" + a1 + ");";
            case PRINT_STR -> "print_string((int)" + a1 + ");";
            case PRINTLN -> "printf(\"\\n\");";
            case READ -> "scanf(\"%lf\", &" + a1 + ");";
            case READ_STR -> a1 + " = read_text();";
            default -> a1 + " = read_character();";
        };
    }

    private static String cDefault(QuadrupleOp op, String a1, String a2, String res) {
        return op + " " + (a1 == null ? "" : a1) + " " + (a2 == null ? "" : a2) + " "
                + (res == null ? "" : res);
    }
}