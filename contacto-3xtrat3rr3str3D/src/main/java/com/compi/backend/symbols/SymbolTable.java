package com.compi.backend.symbols;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

@Getter
public class SymbolTable {
    private final Scope globalScope;
    @Getter(AccessLevel.NONE)
    private final Map<String, Symbol> structs = new HashMap<>();
    @Getter(AccessLevel.NONE)
    private final Map<Scope, Map<String, Symbol>> localStructs = new IdentityHashMap<>();
    @Getter(AccessLevel.NONE)
    private final Map<String, Symbol> classes = new HashMap<>();
    @Getter(AccessLevel.NONE)
    private final Map<String, Symbol> functions = new HashMap<>();
    private final TypeTable typeTable;
    @Getter(AccessLevel.NONE)
    private final List<Scope> closedScopes = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private final Map<Symbol, Scope> typeScopes = new IdentityHashMap<>();
    @Getter(AccessLevel.NONE)
    private final Map<com.compi.backend.ast.ASTNode, Scope> astScopes = new IdentityHashMap<>();
    private Scope currentScope;
    private boolean generatingCode;

    public SymbolTable() {
        this.globalScope = new Scope("global", null);
        this.currentScope = this.globalScope;
        this.typeTable = new TypeTable(globalScope);
    }

    public void enterScope(String name) {
        enterScope(name, currentScope.getCurrentOffset());
    }

    public void enterScope(String name, int initialOffset) {
        if (generatingCode) {
            for (Scope scope : closedScopes) {
                if (scope.getName().equals(name) && scope.getParent() == currentScope) {
                    currentScope = scope;
                    return;
                }
            }
        }
        currentScope = new Scope(name, currentScope, initialOffset);
        typeTable.scopeId(currentScope);
    }

    public void exitScope() {
        if (currentScope.getParent() != null) {
            if (!generatingCode) closedScopes.add(currentScope);
            currentScope = currentScope.getParent();
        }
    }

    public void enterAstScope(String name, int offset, com.compi.backend.ast.ASTNode node) {
        if (generatingCode) {
            currentScope = astScopes.get(node);
            if (currentScope == null) throw new IllegalStateException("El nodo no tiene ámbito semántico");
        } else {
            currentScope = new Scope(name, currentScope, offset);
            typeTable.scopeId(currentScope);
            astScopes.put(node, currentScope);
        }
    }

    public Scope scopeOf(com.compi.backend.ast.ASTNode node) {
        return astScopes.get(node);
    }

    public void clear() {
        currentScope = globalScope;
        globalScope.clear();
        structs.clear();
        localStructs.clear();
        classes.clear();
        functions.clear();
        closedScopes.clear();
        generatingCode = false;
        typeScopes.clear();
        typeTable.clear();
        astScopes.clear();
    }

    /**
     * Reutiliza los símbolos comprobados, sin volver a declararlos durante C3D.
     */
    public void beginCodeGeneration() {
        generatingCode = true;
    }

    public void endCodeGeneration() {
        generatingCode = false;
        currentScope = globalScope;
    }

    public boolean define(Symbol symbol) {

        symbol.setScopeIfAbsent(currentScope.getName());
        boolean defined = currentScope.define(symbol);
        if (defined && !generatingCode && !symbol.isWorking()) registerSymbolType(symbol, currentScope);
        return defined;
    }

    public boolean defineGlobal(Symbol symbol) {
        symbol.setScopeIfAbsent(globalScope.getName());
        boolean defined = globalScope.define(symbol);
        if (defined && !generatingCode && !symbol.isWorking()) registerSymbolType(symbol, globalScope);
        return defined;
    }


    public Type resolveType(String typeName) {
        return typeTable.resolve(typeName, currentScope);
    }

    public Symbol resolve(String name) {
        return currentScope.resolve(name);
    }

    public void addStruct(Symbol struct) {

        struct.setScopeIfAbsent(struct.getName());
        if (currentScope == globalScope) structs.put(struct.getName(), struct);
        else localStructs.computeIfAbsent(currentScope, scope -> new HashMap<>()).put(struct.getName(), struct);
        typeScopes.put(struct, currentScope);
        typeTable.define(struct.getName(), struct.getType(), currentScope);
    }

    public Symbol getStruct(String name) {
        for (Scope scope = currentScope; scope != null; scope = scope.getParent()) {
            Map<String, Symbol> local = localStructs.get(scope);
            if (local != null && local.containsKey(name)) return local.get(name);
        }
        return structs.get(name);
    }

    public Symbol getStructInCurrentScope(String name) {
        if (currentScope == globalScope) return structs.get(name);
        Map<String, Symbol> local = localStructs.get(currentScope);
        return local == null ? null : local.get(name);
    }

    public void addClass(Symbol clazz) {
        clazz.setScopeIfAbsent(clazz.getName());
        classes.put(clazz.getName(), clazz);
        typeScopes.put(clazz, currentScope);
        typeTable.define(clazz.getName(), clazz.getType(), currentScope);
    }

    public Symbol getClass(String name) {
        return classes.get(name);
    }

    /** Clases registradas, en orden de declaración. */
    public java.util.Collection<Symbol> getClasses() {
        return classes.values();
    }

    public void addFunction(Symbol function) {
        function.setScopeIfAbsent(function.getName());
        functions.put(function.getName(), function);
        registerSymbolType(function, currentScope);
    }

    public Symbol getFunction(String name) {
        return functions.get(name);
    }

    private void registerSymbolType(Symbol symbol, Scope scope) {
        typeScopes.put(symbol, scope);
        typeTable.register(symbol.getType(), scope);
        if (symbol.getReturnType() != null) typeTable.register(symbol.getReturnType(), scope);
    }

    /**
     * Incluye los tipos de firmas y miembros añadidos después de registrar su dueño.
     */
    public List<TypeEntry> getTypeEntries() {
        for (Symbol owner : structs.values()) registerNestedTypes(owner, typeScopes.get(owner));
        for (Map<String, Symbol> local : localStructs.values()) {
            for (Symbol owner : local.values()) registerNestedTypes(owner, typeScopes.get(owner));
        }
        for (Symbol owner : classes.values()) registerNestedTypes(owner, typeScopes.get(owner));
        for (Symbol owner : functions.values()) registerNestedTypes(owner, typeScopes.get(owner));
        return typeTable.getEntries();
    }

    private void registerNestedTypes(Symbol symbol, Scope enclosingScope) {
        Scope scope = typeScopes.getOrDefault(symbol, enclosingScope == null ? globalScope : enclosingScope);
        registerSymbolType(symbol, scope);
        for (Symbol member : symbol.getMembers()) registerNestedTypes(member, scope);
        for (Symbol parameter : symbol.getParameters()) registerNestedTypes(parameter, scope);
    }

    public List<Symbol> getAllSymbols() {
        List<Symbol> all = new ArrayList<>();
        Set<Symbol> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        collectAllSymbols(globalScope, all, seen);
        for (Scope scope : closedScopes) {
            collectAllSymbols(scope, all, seen);
        }
        addNested(structs.values(), all, seen);
        for (Map<String, Symbol> local : localStructs.values()) addNested(local.values(), all, seen);
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
