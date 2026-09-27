package com.compi.backend.symbols;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class SymbolTable {
    private Scope currentScope;
    private final Scope globalScope;

    @Getter(AccessLevel.NONE)
    private final Map<String, Symbol> structs = new HashMap<>();
    @Getter(AccessLevel.NONE)
    private final Map<String, Symbol> classes = new HashMap<>();
    @Getter(AccessLevel.NONE)
    private final Map<String, Symbol> functions = new HashMap<>();
    private final TypeTable typeTable;

    @Getter(AccessLevel.NONE)
    private final List<Scope> closedScopes = new ArrayList<>();

    public SymbolTable() {
        this.globalScope = new Scope("global", null);
        this.currentScope = this.globalScope;
        this.typeTable = new TypeTable();
    }

    public void enterScope(String name) {
        currentScope = new Scope(name, currentScope);
    }

    public void enterScope(String name, int initialOffset) {
        currentScope = new Scope(name, currentScope, initialOffset);
    }

    public void exitScope() {
        if (currentScope.getParent() != null) {
            closedScopes.add(currentScope);
            currentScope = currentScope.getParent();
        }
    }

    public void clear() {
        currentScope = globalScope;
        globalScope.clear();
        structs.clear();
        classes.clear();
        functions.clear();
        closedScopes.clear();
    }

    public boolean define(Symbol symbol) {

        symbol.setScopeIfAbsent(currentScope.getName());
        return currentScope.define(symbol);
    }

    public boolean defineGlobal(Symbol symbol) {
        symbol.setScopeIfAbsent(globalScope.getName());
        return globalScope.define(symbol);
    }


    public Type resolveType(String typeName) {
        return typeTable.resolve(typeName);
    }

    public Symbol resolve(String name) {
        return currentScope.resolve(name);
    }

    public void addStruct(Symbol struct) {

        struct.setScopeIfAbsent(struct.getName());
        structs.put(struct.getName(), struct);
    }

    public Symbol getStruct(String name) {
        return structs.get(name);
    }

    public void addClass(Symbol clazz) {
        clazz.setScopeIfAbsent(clazz.getName());
        classes.put(clazz.getName(), clazz);
    }

    public Symbol getClass(String name) {
        return classes.get(name);
    }

    public void addFunction(Symbol function) {
        function.setScopeIfAbsent(function.getName());
        functions.put(function.getName(), function);
    }

    public Symbol getFunction(String name) {
        return functions.get(name);
    }

    public List<Symbol> getAllSymbols() {
        List<Symbol> all = new ArrayList<>();
        Set<Symbol> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        collectAllSymbols(globalScope, all, seen);
        for (Scope scope : closedScopes) {
            collectAllSymbols(scope, all, seen);
        }
        addNested(structs.values(), all, seen);
        addNested(classes.values(), all, seen);
        addNested(functions.values(), all, seen);
        return all;
    }

    private void addNested(Collection<Symbol> owners, List<Symbol> result, Set<Symbol> seen) {
        for (Symbol owner : owners) {
            if (!owner.isWorking() && seen.add(owner)) {
                result.add(owner);
            }
            addNested(owner.getMembers(), result, seen);
            addNested(owner.getParameters(), result, seen);
        }
    }

    private void collectAllSymbols(Scope scope, List<Symbol> result, Set<Symbol> seen) {
        if (scope == null) {
            return;
        }
        for (Symbol s : scope.getSymbols()) {

            if (!s.isWorking() && seen.add(s)) {
                result.add(s);
            }
        }
    }
}
