package com.compi.backend.c3d;

import lombok.Getter;

@Getter
public class Triplet {
    private final QuadrupleOp op;
    private final String arg1;
    private final String arg2;
    private final String result;

    public Triplet(QuadrupleOp op, String arg1, String arg2){
        this(op, arg1, arg2, null);
    }

    public Triplet(QuadrupleOp op, String arg1, String arg2, String result){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    @Override public String toString(){
        return String.format("%s %s %s", op, arg1 == null ? "" : arg1, arg2 == null ? "" : arg2);
    }
}
