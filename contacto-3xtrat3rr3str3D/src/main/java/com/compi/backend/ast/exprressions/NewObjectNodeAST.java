package com.compi.backend.ast.exprressions;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.symbols.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: NewObjectNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class NewObjectNodeAST extends AbstractExpressionNodeAST {

    private final String typeName;
    private final List<AbstractExpressionNodeAST> arguments;
    private final Symbol classDef;

    public NewObjectNodeAST(int line, int column, String typeName, List<AbstractExpressionNodeAST> arguments, Symbol classDef) {
        super(line, column);
        this.typeName = typeName;
        this.arguments = arguments;
        this.classDef = classDef;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(arguments);
        return children;
    }

    public String typeName() {
        return typeName;
    }

    public List<AbstractExpressionNodeAST> arguments() {
        return arguments;
    }

    public Symbol classDef() {
        return classDef;
    }
}
