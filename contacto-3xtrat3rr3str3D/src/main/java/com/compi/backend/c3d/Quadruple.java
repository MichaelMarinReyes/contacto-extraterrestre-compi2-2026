package com.compi.backend.c3d;

import lombok.Getter;
import lombok.Setter;

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

    public String toC3D() {
        switch (op) {
            case LABEL:
                return result + ":";
            case GOTO:
                return "goto " + result;
            case IF_TRUE:
                return "if " + arg1 + " goto " + result;
            case IF_FALSE:
                return "ifFalse " + arg1 + " goto " + result;
            case IF_EQ:
            case IF_NE:
            case IF_LT:
            case IF_LE:
            case IF_GT:
            case IF_GE:
                return "if " + arg1 + " " + op.getSymbol() + " " + arg2 + " goto " + result;
            case ASSIGN:
                return result + " = " + arg1;
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
            case AND:
            case OR:
                return result + " = " + arg1 + " " + op.getSymbol() + " " + arg2;
            case CONCAT:
                return result + " = concat " + arg1 + " " + arg2;
            case NOT:
                return result + " = !" + arg1;
            case NEG:
                return result + " = -" + arg1;
            case ARRAY_GET:
                return result + " = " + arg1 + "[" + arg2 + "]";
            case ARRAY_SET:
                return arg1 + "[" + arg2 + "] = " + result;
            case CALL:
                return (result != null ? result + " = " : "") + "call " + arg1
                        + (arg2 != null ? " " + arg2 : "");
            case PARAM:
                return "param " + arg1;
            case RETURN:
                return "return" + (arg1 != null ? " " + arg1 : "");
            case FUNCTION_START:
                return "func " + result;
            case FUNCTION_END:
                return "finfunc";
            case STACK_SET:
                return "stack[" + arg1 + "] = " + arg2;
            case STACK_GET:
                return result + " = stack[" + arg1 + "]";
            case HEAP_SET:
                return "heap[" + arg1 + "] = " + arg2;
            case HEAP_GET:
                return result + " = heap[" + arg1 + "]";
            case SET_P:
                return "P = " + arg1;
            case GET_P:
                return result + " = P";
            case SET_H:
                return "H = " + arg1;
            case GET_H:
                return result + " = H";
            case PRINT_INT:
            case PRINT_FLOAT:
            case PRINT_CHAR:
            case PRINT_STR:
                return "print " + arg1;
            case PRINTLN:
                return "print \"\\n\"";
            case READ:
                return "scan " + arg1;
            case READ_STR:
                return "read_text " + arg1;
            default:
                return (op + " " + (arg1 == null ? "" : arg1) + " "
                        + (arg2 == null ? "" : arg2) + " "
                        + (result == null ? "" : result)).trim();
        }
    }

    public String toQuadruple() {
        return "(" + op + ", " + (arg1 == null ? "" : arg1) + ", "
                + (arg2 == null ? "" : arg2) + ", " + (result == null ? "" : result) + ")";
    }

    public String toString() {
        return toString(java.util.function.UnaryOperator.identity());
    }

    public String toString(java.util.function.UnaryOperator<String> translator) {
        String a1 = arg1 == null ? null : translator.apply(arg1);
        String a2 = arg2 == null ? null : translator.apply(arg2);
        String res = result == null ? null : translator.apply(result);
        switch (op) {
            case LABEL:
                return result + ":";
            case GOTO:
                return "goto " + result + ";";
            case IF_TRUE:
                return "if (" + a1 + ") goto " + result + ";";
            case IF_FALSE:
                return "if (!(" + a1 + ")) goto " + result + ";";
            case IF_EQ:
            case IF_NE:
            case IF_LT:
            case IF_LE:
            case IF_GT:
            case IF_GE:
                return "if (" + a1 + " " + op.getSymbol() + " " + a2 + ") goto " + result + ";";
            case ASSIGN:
                return res + " = " + a1 + ";";
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
            case AND:
            case OR:
                return res + " = " + a1 + " " + op.getSymbol() + " " + a2 + ";";
            case CONCAT:
                return res + " = concat_cadenas(" + a1 + ", " + a2 + ");";
            case NOT:
                return res + " = !" + a1 + ";";
            case NEG:
                return res + " = -(" + a1 + ");";
            case ARRAY_GET:
                return res + " = " + a1 + "[(int)" + a2 + "];";
            case ARRAY_SET:
                return arg1 + "[(int)" + a2 + "] = " + res + ";";
            case CALL:
                return (res != null ? res + " = " : "") + arg1 + "();";
            case PARAM:
                return "/* param " + a1 + " */";
            case RETURN:
                return "return" + (a1 != null ? " " + a1 : "") + ";";
            case FUNCTION_START:
                return "\ndouble " + result + "() {";
            case FUNCTION_END:
                return "    return 0;\n}\n";
            case STACK_SET:
                return "stack[(int)" + a1 + "] = " + a2 + ";";
            case STACK_GET:
                return res + " = stack[(int)" + a1 + "];";
            case HEAP_SET:
                return "heap[(int)" + a1 + "] = " + a2 + ";";
            case HEAP_GET:
                return res + " = heap[(int)" + a1 + "];";
            case SET_P:
                return "P = " + a1 + ";";
            case GET_P:
                return res + " = P;";
            case SET_H:
                return "H = " + a1 + ";";
            case GET_H:
                return res + " = H;";
            case PRINT_INT:
                return "printf(\"%d\", (int)" + a1 + ");";
            case PRINT_FLOAT:
                return "printf(\"%f\", " + a1 + ");";
            case PRINT_CHAR:
                return "printf(\"%c\", (char)" + a1 + ");";
            case PRINT_STR:
                return "print_string((int)" + a1 + ");";
            case PRINTLN:
                return "printf(\"\\n\");";
            case READ:
                return "scanf(\"%d\", &" + a1 + ");";
            case READ_STR:
                return a1 + " = read_text();";
            default:
                return op + " " + (a1 == null ? "" : a1) + " " + (a2 == null ? "" : a2) + " " + (res == null ? "" : res);
        }
    }
}