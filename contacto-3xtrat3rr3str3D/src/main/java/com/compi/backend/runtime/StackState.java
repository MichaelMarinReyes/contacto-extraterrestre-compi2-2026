package com.compi.backend.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Instantanea de la pila de procesos en un instante concreto de la ejecucion.
 * Se usa para alimentar el panel de visualizacion de pila del frontend.
 */
public class StackState {

    private final int index;
    private final String operation;
    private final List<String> elements;
    private final String c3dInstruction;

    public StackState(int index, String operation, List<String> elements, String c3dInstruction) {
        this.index = index;
        this.operation = operation == null ? "" : operation;
        this.elements = elements == null ? new ArrayList<>() : new ArrayList<>(elements);
        this.c3dInstruction = c3dInstruction == null ? "" : c3dInstruction;
    }

    public int getIndex() {
        return index;
    }

    public String getOperation() {
        return operation;
    }

    /** Elementos de la pila de abajo (indice 0) hacia arriba (ultimo indice = tope). */
    public List<String> getStackElements() {
        return Collections.unmodifiableList(elements);
    }

    public List<String> getElements() {
        return getStackElements();
    }

    public String getC3dInstruction() {
        return c3dInstruction;
    }

    public int depth() {
        return elements.size();
    }

    public String getTop() {
        return elements.isEmpty() ? null : elements.get(elements.size() - 1);
    }

    @Override
    public String toString() {
        return String.format("[%02d] %-28s | pila=%s", index, operation, elements);
    }
}
