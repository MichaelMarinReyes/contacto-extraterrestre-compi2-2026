package com.compi.backend.runtime;

import com.compi.backend.StackAction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
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

    public List<String> getStackElements() {
        return Collections.unmodifiableList(elements);
    }

    public List<String> getElements() {
        return getStackElements();
    }

    public List<String> getPopped() {
        return Collections.unmodifiableList(popped);
    }

    public int depth() {
        return elements.size();
    }


    @Override
    public String toString() {
        return String.format("[%02d] %-6s %-24s | pila=%s", index, action.getTag(), operation, elements);
    }
}
