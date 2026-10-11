package com.compi.backend.c3d;

import com.compi.backend.symbols.SymbolTable;
import java.util.List;
import java.util.Set;

/**
 * Punto de entrada de la generación de C. Produce un único {@code main} con
 * código de tres direcciones puro (sin funciones ni métodos): los frames viven
 * en {@code stack}, las llamadas usan la pila y {@code goto}, y el retorno pasa
 * por un despachador {@code switch}.
 */
public final class CCodeEmitter {

    private CCodeEmitter() { }

    public static String generateCCode(List<Quadruple> quadruples, Set<String> declaredTemps,
                                       SymbolTable symbolTable) {
        FlatLayout layout = new FlatLayout(quadruples, symbolTable);
        return new FlatEmitter(layout).build();
    }
}
