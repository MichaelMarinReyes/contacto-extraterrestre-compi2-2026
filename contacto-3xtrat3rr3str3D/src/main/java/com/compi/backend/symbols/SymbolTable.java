package com.compi.backend.symbols;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SymbolTable {
    private Scope currentScope;
    private final Scope globalScope;
    private final Map<String, Symbol> structs = new HashMap<>();
    private final Map<String, Symbol> classes = new HashMap<>();
    private final Map<String, Symbol> functions = new HashMap<>();
    private final TypeTable typeTable;

    public SymbolTable() {
        this.globalScope = new Scope("global", null);
        this.currentScope = this.globalScope;
        this.typeTable = new TypeTable();
    }

    public TypeTable getTypeTable() {
        return typeTable;
    }

    public void enterScope(String name) {
        currentScope = new Scope(name, currentScope);
    }

    public void enterScope(String name, int initialOffset) {
        currentScope = new Scope(name, currentScope, initialOffset);
    }

    public void exitScope() {
        if (currentScope.getParent() != null) {
            currentScope = currentScope.getParent();
        }
    }

    public Scope getCurrentScope() {
        return currentScope;
    }

    /**
     * Devuelve la tabla al estado inicial para empezar una compilacion limpia.
     *
     * <p>Sin esto la segunda compilacion de la misma sesion volveria a declarar
     * los simbolos de la primera y cada variable globals aparecia duplicada.</p>
     */
    public void clear() {
        currentScope = globalScope;
        globalScope.clear();
        structs.clear();
        classes.clear();
        functions.clear();
    }

    public Scope getGlobalScope() {
        return globalScope;
    }

    public boolean define(Symbol symbol) {
        return currentScope.define(symbol);
    }

    public boolean defineByTypeName(String name, String typeName, SymbolCategory category) {
        Type type = typeTable.resolve(typeName);
        if (type == null) {
            // crear tipo personalizado si no está en la tabla
            type = new Type(DataType.UNKNOWN, typeName);
            typeTable.define(typeName, type);
        }
        Symbol symbol = new Symbol(name, type, category);
        return currentScope.define(symbol);
    }

    public Type resolveType(String typeName) {
        return typeTable.resolve(typeName);
    }

    public Symbol resolve(String name) {
        return currentScope.resolve(name);
    }

    public void addStruct(Symbol struct) {
        structs.put(struct.getName(), struct);
    }

    public Symbol getStruct(String name) {
        return structs.get(name);
    }

    public void addClass(Symbol clazz) {
        classes.put(clazz.getName(), clazz);
    }

    public Symbol getClass(String name) {
        return classes.get(name);
    }

    public void addFunction(Symbol function) {
        functions.put(function.getName(), function);
    }

    public Symbol getFunction(String name) {
        return functions.get(name);
    }

    /**
     * Todos los simbolos visibles tras la compilacion.
     *
     * <p>Los ambitos anidados (clases, metodos) se cierran al terminar cada
     * pasada, asi que ademas de los globales se incluyen los miembros de cada
     * clase y estructura, que es donde quedan registrados.</p>
     */
    public List<Symbol> getAllSymbols() {
        List<Symbol> all = new ArrayList<>();
        Set<Symbol> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        collectAllSymbols(globalScope, all, seen);
        addMembers(structs.values(), all, seen);
        addMembers(classes.values(), all, seen);
        addMembers(functions.values(), all, seen);
        return all;
    }

    private void addMembers(Collection<Symbol> owners, List<Symbol> result, Set<Symbol> seen) {
        for (Symbol owner : owners) {
            if (seen.add(owner)) {
                result.add(owner);
            }
            addMembers(owner.getMembers(), result, seen);
        }
    }

    private void collectAllSymbols(Scope scope, List<Symbol> result, Set<Symbol> seen) {
        if (scope == null) {
            return;
        }
        for (Symbol s : scope.getSymbols()) {
            if (seen.add(s)) {
                result.add(s);
            }
        }
    }

    private void collectAllSymbols(Scope scope, List<Symbol> result) {
        if (scope == null) return;
        result.addAll(scope.getSymbols());
    }
}
