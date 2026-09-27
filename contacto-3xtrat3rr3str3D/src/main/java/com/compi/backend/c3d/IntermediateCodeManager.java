package com.compi.backend.c3d;

import java.util.ArrayList;
import java.util.List;

public class IntermediateCodeManager {
    private final List<Triplet> triplets = new ArrayList<>();

    public void loadFromQuadruples(List<Quadruple> source) {
        if (source == null) return;
        for (Quadruple q : source) {
            toTriplet(q);
        }
    }

    private void toTriplet(Quadruple q) {
        QuadrupleOp op = q.getOp();
        if (op == null) return;

        switch (op) {
            case LABEL, GOTO -> triplets.add(new Triplet(op, null, q.getResult()));
            default -> triplets.add(new Triplet(op, q.getArg1(), q.getArg2(), q.getResult()));
        }
    }

    public List<Triplet> getTriplets() {
        return triplets;
    }

    public void clear() {
        triplets.clear();
    }

    public String getTripletsString() {
        StringBuilder sb = new StringBuilder();
        for (Triplet t : triplets) {
            sb.append(t).append("\n");
        }
        return sb.toString();
    }
}
