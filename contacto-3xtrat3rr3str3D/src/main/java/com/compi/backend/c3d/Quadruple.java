package com.compi.backend.c3d;

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

    public QuadrupleOp getOp() {
        return op;
    }

    public void setOp(QuadrupleOp op) {
        this.op = op;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        switch (op) {
            case LABEL:
                return result + ":";
            case GOTO:
                return "goto " + result + ";";
            case IF_TRUE:
                return "if (" + arg1 + ") goto " + result + ";";
            case IF_FALSE:
                return "if_false (" + arg1 + ") goto " + result + ";";
            case IF_EQ:
            case IF_NE:
            case IF_LT:
            case IF_LE:
            case IF_GT:
            case IF_GE:
                return "if (" + arg1 + " " + op.getSymbol() + " " + arg2 + ") goto " + result + ";";
            case ASSIGN:
                return result + " = " + arg1 + ";";
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
                return result + " = " + arg1 + " " + op.getSymbol() + " " + arg2 + ";";
            case CALL:
                return (result != null ? result + " = " : "") + "call " + arg1 + (arg2 != null ? ", " + arg2 : "") + ";";
            case PARAM:
                return "param " + arg1 + ";";
            case RETURN:
                return "return" + (arg1 != null ? " " + arg1 : "") + ";";
            case FUNCTION_START:
                return "\nvoid " + result + "() {";
            case FUNCTION_END:
                return "}\n";
            case STACK_SET:
                return "stack[(int)" + arg1 + "] = " + arg2 + ";";
            case STACK_GET:
                return result + " = stack[(int)" + arg1 + "];";
            case HEAP_SET:
                return "heap[(int)" + arg1 + "] = " + arg2 + ";";
            case HEAP_GET:
                return result + " = heap[(int)" + arg1 + "];";
            case SET_P:
                return "P = " + arg1 + ";";
            case GET_P:
                return result + " = P;";
            case SET_H:
                return "H = " + arg1 + ";";
            case GET_H:
                return result + " = H;";
            case PRINT_INT:
                return "printf(\"%d\", (int)" + arg1 + ");";
            case PRINT_FLOAT:
                return "printf(\"%f\", " + arg1 + ");";
            case PRINT_CHAR:
                return "printf(\"%c\", (char)" + arg1 + ");";
            case PRINT_STR:
                return "print_string((int)" + arg1 + ");";
            case PRINTLN:
                return "printf(\"\\n\");";
            default:
                return op + " " + arg1 + " " + arg2 + " " + result;
        }
    }
}
