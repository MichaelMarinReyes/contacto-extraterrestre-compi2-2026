package com.compi.backend.c3d;

import java.util.ArrayList;
import java.util.List;

public class TripletGenerator {
    private int tempCounter = 0;
    private int labelCounter = 0;
    private final List<Triplet> triplets = new ArrayList<>();

    public String newTemp(){ return "t"+(tempCounter++); }
    public String newLabel(){ return "L"+(labelCounter++); }

    public void emit(QuadrupleOp op, String arg1, String arg2){
        emit(op, arg1, arg2, null);
    }

    /** Emite un triplete indicando adonde va el resultado de la operacion. */
    public void emit(QuadrupleOp op, String arg1, String arg2, String result){
        triplets.add(new Triplet(op, arg1, arg2, result));
    }
    public void emitLabel(String label){ triplets.add(new Triplet(QuadrupleOp.LABEL, null, label)); }
    public void emitGoto(String label){ triplets.add(new Triplet(QuadrupleOp.GOTO, null, label)); }
    public void emitAssign(String target, String source){ triplets.add(new Triplet(QuadrupleOp.ASSIGN, source, target)); }

    public List<Triplet> getTriplets(){ return triplets; }
    public void clear(){ triplets.clear(); tempCounter=0; labelCounter=0; }

    public String toString(){
        StringBuilder sb=new StringBuilder();
        for(Triplet t: triplets) sb.append(t).append("\n");
        return sb.toString();
    }
}
