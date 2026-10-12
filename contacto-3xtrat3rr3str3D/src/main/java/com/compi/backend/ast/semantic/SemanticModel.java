package com.compi.backend.ast.semantic;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.symbols.Scope;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.Type;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Resultados semánticos por identidad de nodo; no depende de ANTLR.
 */
public final class SemanticModel {
    private final Map<ASTNode, Type> types = new IdentityHashMap<>();
    private final Map<ASTNode, Symbol> symbols = new IdentityHashMap<>();
    private final Map<ASTNode, Scope> scopes = new IdentityHashMap<>();

    public Type typeOf(ASTNode node) {
        return types.getOrDefault(node, Type.UNKNOWN);
    }

    public Symbol symbolOf(ASTNode node) {
        return symbols.get(node);
    }

    public Scope scopeOf(ASTNode node) {
        return scopes.get(node);
    }

    void type(ASTNode node, Type type) {
        types.put(node, type == null ? Type.UNKNOWN : type);
    }

    void symbol(ASTNode node, Symbol symbol) {
        if (symbol != null) symbols.put(node, symbol);
    }

    void scope(ASTNode node, Scope scope) {
        scopes.put(node, scope);
    }
}
