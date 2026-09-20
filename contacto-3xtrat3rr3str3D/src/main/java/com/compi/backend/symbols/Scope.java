package com.compi.backend.symbols;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {
    private final String name;
    private final Scope parent;
    private final Map<String, Symbol> symbols = new LinkedHashMap<>();
    private int currentOffset = 0;

    public Scope(String name, Scope parent) {
        this.name = name;
        this.parent = parent;
        this.currentOffset = (parent == null) ? 0 : parent.currentOffset;
    }

    public Scope(String name, Scope parent, int initialOffset) {
        this.name = name;
        this.parent = parent;
        this.currentOffset = initialOffset;
    }

    public String getName() {
        return name;
    }

    public Scope getParent() {
        return parent;
    }

    public boolean define(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) {
            return false; // Ya definido en este ámbito
        }
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    public Symbol resolveLocal(String name) {
        return symbols.get(name);
    }

    public Symbol resolve(String name) {
        Symbol s = symbols.get(name);
        if (s != null) return s;
        if (parent != null) return parent.resolve(name);
        return null;
    }

    public int allocateOffset(int size) {
        int allocated = currentOffset;
        currentOffset += size;
        return allocated;
    }

    public int getCurrentOffset() {
        return currentOffset;
    }

    public Collection<Symbol> getSymbols() {
        return symbols.values();
    }
}
