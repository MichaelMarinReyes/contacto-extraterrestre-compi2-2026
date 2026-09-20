package com.compi.backend.symbols;

import java.util.ArrayList;
import java.util.List;

public class Symbol {
    private final String name;
    private final Type type;
    private final SymbolCategory category;
    private int offset; // Offset en Stack o Heap
    private boolean isGlobal;
    private boolean isByReference; // Para arrays y structs en 'Y'

    // Metadatos para funciones / métodos / constructores
    private List<Symbol> parameters = new ArrayList<>();
    private Type returnType;

    // Metadatos para structs / clases
    private List<Symbol> members = new ArrayList<>();

    public Symbol(String name, Type type, SymbolCategory category) {
        this.name = name;
        this.type = type;
        this.category = category;
    }

    public Symbol(String name, Type type, SymbolCategory category, int offset, boolean isGlobal) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.offset = offset;
        this.isGlobal = isGlobal;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public SymbolCategory getCategory() {
        return category;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public boolean isByReference() {
        return isByReference;
    }

    public void setByReference(boolean byReference) {
        isByReference = byReference;
    }

    public List<Symbol> getParameters() {
        return parameters;
    }

    public void setParameters(List<Symbol> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Symbol param) {
        this.parameters.add(param);
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public List<Symbol> getMembers() {
        return members;
    }

    public void setMembers(List<Symbol> members) {
        this.members = members;
    }

    public void addMember(Symbol member) {
        this.members.add(member);
    }

    public Symbol getMember(String memberName) {
        for (Symbol s : members) {
            if (s.getName().equals(memberName)) return s;
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("Symbol(%s, %s, %s, offset=%d, isGlobal=%s)", name, type, category, offset, isGlobal);
    }
}
