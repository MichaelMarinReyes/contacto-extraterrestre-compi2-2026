package com.compi.backend.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Instantanea de la pila de procesos en un instante concreto de la ejecucion.
 * Se usa para alimentar el panel de visualizacion de pila del frontend.
 *
 * Ademas del contenido de la pila guarda que instruccion lo produjo, si la
 * instruccion fue un desplazamiento o una reduccion, y que valores se
 * desapilaron y apilaron en ese paso.
 */
public class StackState {

    private final int index;
    private final String operation;
    private final List<String> elements;
    private final String c3dInstruction;
    private final StackAction action;
    private final List<String> popped;
    private final String pushed;

    public StackState(int index, String operation, List<String> elements, String c3dInstruction) {
        this(index, operation, elements, c3dInstruction, StackAction.NEUTRAL, null, null);
    }

    public StackState(int index, String operation, List<String> elements, String c3dInstruction,
                      StackAction action, List<String> popped, String pushed) {
        this.index = index;
        this.operation = operation == null ? "" : operation;
        this.elements = elements == null ? new ArrayList<>() : new ArrayList<>(elements);
        this.c3dInstruction = c3dInstruction == null ? "" : c3dInstruction;
        this.action = action == null ? StackAction.NEUTRAL : action;
        this.popped = popped == null ? new ArrayList<>() : new ArrayList<>(popped);
        this.pushed = pushed;
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

    /** Desplazamiento o reduccion que ejecuto la instruccion de este paso. */
    public StackAction getAction() {
        return action;
    }

    /** Valores desapilados por la instruccion, en el orden en que salieron. */
    public List<String> getPopped() {
        return Collections.unmodifiableList(popped);
    }

    /** Valor apilado por la instruccion, o null si no apilo nada. */
    public String getPushed() {
        return pushed;
    }

    public int depth() {
        return elements.size();
    }

    public String getTop() {
        return elements.isEmpty() ? null : elements.get(elements.size() - 1);
    }

    @Override
    public String toString() {
        return String.format("[%02d] %-6s %-24s | pila=%s", index, action.tag(), operation, elements);
    }
}
