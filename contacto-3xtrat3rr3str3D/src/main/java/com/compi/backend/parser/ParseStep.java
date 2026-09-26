package com.compi.backend.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Un paso del trazo de la pila del parser: el simbolo que entra y como queda la
 * pila.
 *
 * <p>Lo que se apila es un simbolo de la gramatica, nunca una accion. En un
 * desplazamiento entra el token que se leyo de la entrada; en una reduccion
 * entra el no terminal al que se reduce. El contenido de la pila son esos
 * simbolos, de la base a la cima, con la marca de inicio {@value #MARCA_INICIO}
 * abajo del todo.</p>
 */
public class ParseStep {

    /** Marca de inicio: lo primero que hay en la pila de un parser. */
    public static final String MARCA_INICIO = "$";

    private final int numero;
    private final AccionPila tipo;
    private final String simbolo;
    private final String terminal;
    private final int linea;
    private final int columna;
    private final boolean invalido;
    private final List<String> pila;
    private final List<String> consumidos;

    /**
     * @param numero     numero de paso, empezando en 1
     * @param tipo       si el paso desplaza un token o reduce una regla
     * @param simbolo    simbolo que entra en la pila: lexema o nombre de regla
     * @param terminal   nombre del token en la gramatica, solo en un shift
     * @param linea      linea del fuente del simbolo, 0 si no la tiene
     * @param columna    columna del fuente, en base 1
     * @param invalido   si el token no encaja en la regla que se esta leyendo
     * @param pila       contenido de la pila al terminar el paso
     * @param consumidos simbolos que el reduce retira, en orden de salida
     */
    public ParseStep(int numero, AccionPila tipo, String simbolo, String terminal,
                     int linea, int columna, boolean invalido,
                     List<String> pila, List<String> consumidos) {
        this.numero = numero;
        this.tipo = tipo == null ? AccionPila.SHIFT : tipo;
        this.simbolo = simbolo == null ? "" : simbolo;
        this.terminal = terminal;
        this.linea = linea;
        this.columna = columna;
        this.invalido = invalido;
        this.pila = pila == null ? new ArrayList<>() : new ArrayList<>(pila);
        this.consumidos = consumidos == null ? new ArrayList<>() : new ArrayList<>(consumidos);
    }

    /** Numero de paso, empezando en 1. */
    public int getNumero() {
        return numero;
    }

    /** Si el paso apila un token (shift) o lo sustituye por una regla (reduce). */
    public AccionPila getTipo() {
        return tipo;
    }

    /** Simbolo que entra en la pila en este paso. */
    public String getSimbolo() {
        return simbolo;
    }

    /** Nombre del token en la gramatica, por ejemplo VARIABLE. Solo en un shift. */
    public String getTerminal() {
        return terminal;
    }

    /** Linea del fuente del simbolo, o 0 si no se sabe. */
    public int getLinea() {
        return linea;
    }

    /** Columna del fuente del simbolo en base 1, o 0 si no se sabe. */
    public int getColumna() {
        return columna;
    }

    /** Si el token del paso no encaja en la regla que se estaba leyendo. */
    public boolean esInvalido() {
        return invalido;
    }

    /** Contenido de la pila al terminar el paso, de la base a la cima. */
    public List<String> getPila() {
        return Collections.unmodifiableList(pila);
    }

    /** Simbolos que este reduce retira de la pila, en orden de salida. */
    public List<String> getConsumidos() {
        return Collections.unmodifiableList(consumidos);
    }

    public int getProfundidad() {
        return pila.size();
    }

    @Override
    public String toString() {
        String detalle;
        if (tipo == AccionPila.SHIFT) {
            detalle = terminal == null ? simbolo : terminal + ": " + simbolo;
            if (invalido) {
                detalle += "   (no encaja en la regla)";
            }
            detalle += "   (" + linea + ":" + columna + ")";
        } else {
            detalle = simbolo + "   (retira " + consumidos.size() + ": "
                    + String.join(", ", consumidos) + ")";
        }
        return String.format("[Paso %3d] %-6s %s", numero, tipo.tag(), detalle);
    }
}
