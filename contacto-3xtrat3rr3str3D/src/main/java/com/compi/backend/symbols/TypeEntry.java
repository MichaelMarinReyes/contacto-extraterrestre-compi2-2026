package com.compi.backend.symbols;

import java.util.List;

/** Entrada de la tabla de tipos; los límites son índices por dimensión. */
public record TypeEntry(int id, String name, Type type, Integer baseTypeId,
                        Integer parentId, int dimension, List<Integer> minimum,
                        List<Integer> maximum, int scopeId, String scopeName) {
    public TypeEntry {
        minimum = List.copyOf(minimum);
        maximum = List.copyOf(maximum);
    }
}
