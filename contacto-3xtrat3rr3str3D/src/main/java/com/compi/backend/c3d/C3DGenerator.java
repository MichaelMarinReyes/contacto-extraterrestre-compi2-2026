package com.compi.backend.c3d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class C3DGenerator {
    private int tempCounter = 0;
    private int labelCounter = 0;
    private final List<Quadruple> quadruples = new ArrayList<>();
    private final Set<String> declaredTemps = new HashSet<>();

    public C3DGenerator() {}

    public String newTemp() {
        String t = "t" + (tempCounter++);
        declaredTemps.add(t);
        return t;
    }

    public String newLabel() {
        return "L" + (labelCounter++);
    }

    public void emit(Quadruple quad) {
        quadruples.add(quad);
    }

    public void emit(QuadrupleOp op, String arg1, String arg2, String result) {
        quadruples.add(new Quadruple(op, arg1, arg2, result));
    }

    public void emitLabel(String label) {
        quadruples.add(new Quadruple(QuadrupleOp.LABEL, null, null, label));
    }

    public void emitGoto(String label) {
        quadruples.add(new Quadruple(QuadrupleOp.GOTO, null, null, label));
    }

    public void emitAssign(String target, String source) {
        quadruples.add(new Quadruple(QuadrupleOp.ASSIGN, source, null, target));
    }

    public List<Quadruple> getQuadruples() {
        return quadruples;
    }

    public Set<String> getDeclaredTemps() {
        return declaredTemps;
    }

    public void clear() {
        quadruples.clear();
        declaredTemps.clear();
        tempCounter = 0;
        labelCounter = 0;
    }

    public String toC3DString() {
        StringBuilder sb = new StringBuilder();
        for (Quadruple q : quadruples) {
            sb.append(q.toString()).append("\n");
        }
        return sb.toString();
    }
}
