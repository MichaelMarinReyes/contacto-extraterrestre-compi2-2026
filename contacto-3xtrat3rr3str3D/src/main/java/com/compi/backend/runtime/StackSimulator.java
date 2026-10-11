package com.compi.backend.runtime;

import com.compi.backend.StackAction;
import com.compi.backend.c3d.Quadruple;
import com.compi.backend.c3d.QuadrupleOp;
import java.util.ArrayList;
import java.util.List;

public class StackSimulator {

    private final List<StackState> states = new ArrayList<>();
    private final List<String> stack = new ArrayList<>();
    private final List<String> poppedThisStep = new ArrayList<>();
    private int step = 0;
    private int popsIntentados = 0;
    private String pushedThisStep = null;

    public List<StackState> simulate(List<Quadruple> quadruples) {
        states.clear();
        stack.clear();
        step = 0;

        if (quadruples == null || quadruples.isEmpty()) {
            states.add(new StackState(0, "inicio", new ArrayList<>(), "(sin instrucciones)"));
            return states;
        }

        for (Quadruple q : quadruples) {
            poppedThisStep.clear();
            pushedThisStep = null;
            popsIntentados = 0;
            apply(q);
            step++;
            states.add(new StackState(step, q.toString(), stack, q.toString(),
                    stepAction(), poppedThisStep, pushedThisStep));
        }
        return states;
    }

    private StackAction stepAction() {
        if (pushedThisStep != null) {
            return poppedThisStep.isEmpty() ? StackAction.SHIFT : StackAction.REDUCE;
        }
        return popsIntentados > 0 ? StackAction.REDUCE : StackAction.NEUTRAL;
    }

    private void apply(Quadruple q) {
        QuadrupleOp op = q.getOp();
        if (op == null) {
            return;
        }
        switch (op) {
            case ADD, SUB, MUL, DIV, INT_DIV, MOD, AND, OR, CONCAT,
                    IF_EQ, IF_NE, IF_LT, IF_LE, IF_GT, IF_GE -> binary(q);
            case NOT, NEG, ARRAY_GET -> unaryPopPush(q);
            case ASSIGN, STACK_SET, HEAP_SET, SET_P, SET_H, PRINT_INT, PRINT_FLOAT,
                    PRINT_CHAR, PRINT_STR, PRINTLN, PARAM, RETURN, IF_TRUE, IF_FALSE
                    -> pop(1);
            case ARRAY_SET -> pop(2);
            case STACK_GET, HEAP_GET, GET_P, GET_H -> push(q.getResult());
            case CALL -> maybePush(q.getResult());
            case READ, READ_STR, READ_CHAR -> push(q.getArg1());
            case GOTO, LABEL, FUNCTION_START, FUNCTION_END -> {
                // Instrucciones sin efecto sobre la pila visible.
            }
            default -> maybePush(q.getResult());
        }
    }

    private void binary(Quadruple q) {
        pop(2);
        push(q.getResult());
    }

    private void unaryPopPush(Quadruple q) {
        pop(1);
        push(q.getResult());
    }

    private void maybePush(String value) {
        if (value != null) {
            push(value);
        }
    }

    private void push(String value) {
        String v = value == null ? "?" : value;
        stack.add(v);
        pushedThisStep = v;
    }

    private void pop(int n) {
        popsIntentados += n;
        for (int i = 0; i < n; i++) {
            if (!stack.isEmpty()) {
                poppedThisStep.add(stack.remove(stack.size() - 1));
            }
        }
    }
}
