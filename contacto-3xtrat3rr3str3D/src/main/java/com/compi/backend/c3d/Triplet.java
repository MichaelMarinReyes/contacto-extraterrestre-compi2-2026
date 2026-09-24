package com.compi.backend.c3d;

public class Triplet {
    private final QuadrupleOp op;
    private final String arg1;
    private final String arg2;

    public Triplet(QuadrupleOp op, String arg1, String arg2){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public QuadrupleOp getOp(){ return op; }
    public String getArg1(){ return arg1; }
    public String getArg2(){ return arg2; }

    @Override public String toString(){
        return String.format("%s %s %s", op, arg1 == null ? "" : arg1, arg2 == null ? "" : arg2);
    }
}
