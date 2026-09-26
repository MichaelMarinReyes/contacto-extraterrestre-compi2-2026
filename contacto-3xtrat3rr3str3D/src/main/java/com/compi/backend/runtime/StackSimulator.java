package com.compi.backend.runtime;

import com.compi.backend.c3d.Quadruple;
import com.compi.backend.c3d.QuadrupleOp;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulador de pila de maquina abstracta.
 *
 * Recorre las cuartetas generadas y registra una {@link StackState} por cada
 * instruccion, de forma que el frontend pueda dibujar la pila de procesos
 * paso a paso.
 */
public class StackSimulator {

    private final List<StackState> states = new ArrayList<>();
    private final List<String> stack = new ArrayList<>();
    private final List<String> poppedThisStep = new ArrayList<>();
    private int step = 0;
    private int popsIntentados = 0;
    private String pushedThisStep = null;

    /**
     * Simula la pila para una lista de cuartetas.
     *
     * @param quadruples cuartetas del codigo de tres direcciones
     * @return lista ordenada de estados
     */
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
                    accionDelPaso(), poppedThisStep, pushedThisStep));
        }
        return states;
    }

    /**
     * Clasifica la accion del paso actual a partir de lo que la instruccion
     * hizo con la pila: si apilo un valor sin consumir nada es un desplazamiento
     * (shift); si consumio operandos, con o sin resultado que apilar, es una
     * reduccion (reduce), aunque la pila estuviera vacia y no hubiera nada que
     * desapilar; si no toco la pila no hay cambio.
     */
    private StackAction accionDelPaso() {
        if (pushedThisStep != null) {
            return poppedThisStep.isEmpty() ? StackAction.SHIFT : StackAction.REDUCE;
        }
        return popsIntentados > 0 ? StackAction.REDUCE : StackAction.NEUTRAL;
    }

    /** Aplica la semantica de pila de una instruccion. */
    private void apply(Quadruple q) {
        QuadrupleOp op = q.getOp();
        if (op == null) {
            return;
        }
        switch (op) {
            // Consumen 2 operandos y producen 1 resultado
            case ADD: case SUB: case MUL: case DIV: case MOD:
            case AND: case OR:
            case IF_EQ: case IF_NE: case IF_LT: case IF_LE: case IF_GT: case IF_GE:
                pop(2);
                push(q.getResult());
                break;

            // Consume 1 y produce 1
            case NOT:
                pop(1);
                push(q.getResult());
                break;

            // Consumen 1 y no producen nada
            case ASSIGN: case STACK_SET: case HEAP_SET:
            case SET_P: case SET_H:
            case PRINT_INT: case PRINT_FLOAT: case PRINT_CHAR: case PRINT_STR: case PRINTLN:
            case PARAM: case RETURN:
                pop(1);
                break;

            // Leen un valor de la memoria y lo apilan, sin consumir nada
            case STACK_GET: case HEAP_GET: case GET_P: case GET_H:
                push(q.getResult());
                break;

            // Saltos condicionales: consumen la condicion y nada mas
            case IF_TRUE: case IF_FALSE:
                pop(1);
                break;

            case GOTO: case LABEL: case FUNCTION_START: case FUNCTION_END: case CALL:
                // No alteran la pila
                break;

            default:
                if (q.getResult() != null) {
                    push(q.getResult());
                }
                break;
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
