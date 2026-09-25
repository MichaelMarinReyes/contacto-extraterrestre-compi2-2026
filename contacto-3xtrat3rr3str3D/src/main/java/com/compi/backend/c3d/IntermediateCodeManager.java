package com.compi.backend.c3d;

import java.util.ArrayList;
import java.util.List;

public class IntermediateCodeManager {
    private final TripletGenerator tripletGen = new TripletGenerator();
    private final List<Quadruple> quadruples = new ArrayList<>();
    private int tempCounter = 0;
    private int labelCounter = 0;

    public String newTemp(){ return "t"+(tempCounter++); }
    public String newLabel(){ return "L"+(labelCounter++); }

    public void emitTriplet(QuadrupleOp op, String arg1, String arg2){
        tripletGen.emit(op, arg1, arg2);
    }

    public void emitQuadruple(QuadrupleOp op, String arg1, String arg2, String result){
        quadruples.add(new Quadruple(op, arg1, arg2, result));
    }

    public List<Triplet> getTriplets(){ return tripletGen.getTriplets(); }
    public List<Quadruple> getQuadruples(){ return quadruples; }

    public void clear(){
        tripletGen.clear();
        quadruples.clear();
        tempCounter = 0;
        labelCounter = 0;
    }

    public String getTripletsString(){
        return tripletGen.toString();
    }

    public String getQuadruplesString(){
        StringBuilder sb = new StringBuilder();
        for(Quadruple q : quadruples) sb.append(q).append("\n");
        return sb.toString();
    }
}
