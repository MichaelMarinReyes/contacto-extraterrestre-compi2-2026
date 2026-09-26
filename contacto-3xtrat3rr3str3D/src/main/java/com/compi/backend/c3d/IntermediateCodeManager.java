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

    /**
     * Carga un conjunto de cuartetas y deriva automáticamente los tripletes
     * correspondientes. Cada cuarteta {@code (op, a1, a2, r)} produce un unico
     * triplete {@code (op, a1, a2)}: el triplete es la misma instrucción en su
     * forma de tres campos, con el destino implícito. La etiqueta de los saltos
     * se lleva al segundo operando porque es el único hueco disponible.
     *
     * @param source cuartetas generadas por el visitor C3D del lenguaje
     */
    public void loadFromQuadruples(List<Quadruple> source){
        if(source == null) return;
        for(Quadruple q : source){
            quadruples.add(q);
            toTriplets(q);
        }
    }

    /** Descompone una cuarteta en su triplete equivalente. */
    private void toTriplets(Quadruple q){
        QuadrupleOp op = q.getOp();
        if(op == null) return;

        switch(op){
            // El destino de un salto viaja como segundo operando del triplete,
            // porque en la forma de tres campos no hay sitio para el.
            case LABEL -> tripletGen.emit(op, null, q.getResult(), null);
            case GOTO -> tripletGen.emit(op, null, q.getResult(), null);
            default -> tripletGen.emit(op, q.getArg1(), q.getArg2(), q.getResult());
        }
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
