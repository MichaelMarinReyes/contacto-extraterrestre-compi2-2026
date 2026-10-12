package com.compi.backend.symbols;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Symbol {
    private final String name;
    private final Type type;
    private final SymbolCategory category;
    private int offset;
    private boolean isGlobal;
    private boolean isByReference;

    private List<Symbol> parameters = new ArrayList<>();
    private Type returnType;

    private List<Symbol> members = new ArrayList<>();

    /** Clase de la que hereda esta clase, o {@code null}. */
    private Symbol parentClass;

    /** Clase que declara este miembro; permite resolver la etiqueta C3D correcta. */
    private Symbol ownerClass;

    /** Identificador de tipo en tiempo de ejecución para el despacho virtual. */
    private int typeTag;

    /** Visibilidad declarada del miembro ({@code PUBLIC}, {@code PRIVATE}, ...). */
    private String access;

    private int line = -1;
    private int column = -1;
    private String scope;
    private String language;
    private String sourceFile;

    private boolean working;

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

    public void addParameter(Symbol param) {
        this.parameters.add(param);

        param.setScopeIfAbsent(name);
    }

    public void addMember(Symbol member) {
        this.members.add(member);

        member.setScopeIfAbsent(name);
    }

    /** Busca un miembro propio o heredado de la clase. */
    public Symbol getMember(String memberName) {
        for (Symbol s : members) {
            if (s.getName().equals(memberName)) return s;
        }
        return parentClass == null ? null : parentClass.getMember(memberName);
    }

    /** Busca un método o constructor propio. */
    public Symbol getCallable(String memberName, int argumentCount) {
        for (Symbol member : members) {
            if (member.name.equals(memberName) && member.parameters.size() == argumentCount
                    && (member.category == SymbolCategory.METHOD || member.category == SymbolCategory.CONSTRUCTOR))
                return member;
        }
        return null;
    }

    /** Resuelve un método propio o heredado (el más específico primero). */
    public Symbol resolveMethod(String memberName, int argumentCount) {
        for (Symbol member : members) {
            if (member.name.equals(memberName) && member.parameters.size() == argumentCount
                    && member.category == SymbolCategory.METHOD)
                return member;
        }
        return parentClass == null ? null : parentClass.getInheritedMethod(memberName, argumentCount);
    }

    /** Busca un método heredado de las superclases; los constructores no se heredan. */
    public Symbol getInheritedMethod(String memberName, int argumentCount) {
        for (Symbol member : members) {
            if (member.name.equals(memberName) && member.parameters.size() == argumentCount
                    && member.category == SymbolCategory.METHOD)
                return member;
        }
        return parentClass == null ? null : parentClass.getInheritedMethod(memberName, argumentCount);
    }

    public Symbol inFile(String fileName) {
        this.sourceFile = fileName;
        return this;
    }

    public void setScopeIfAbsent(String scopeName) {
        if (this.scope == null) {
            this.scope = scopeName;
        }
    }

    public Symbol inLanguage(String languageId) {
        this.language = languageId;
        return this;
    }

    public Symbol markWorking() {
        this.working = true;
        return this;
    }

    /**
     * Identificador interno que distingue variables de ámbitos diferentes.
     */
    public String getStorageName() {
        if (isGlobal) return "g_" + name;
        return "v_" + (scope == null ? "local" : scope.replaceAll("[^A-Za-z0-9_]", "_"))
                + "_" + name + "_" + Math.max(line, 0) + "_" + Math.max(column, 0);
    }

    @Override
    public String toString() {
        return String.format("Symbol(%s, %s, %s, offset=%d, isGlobal=%s, ambito=%s, linea=%d)",
                name, type, category, offset, isGlobal, scope, line);
    }
}
