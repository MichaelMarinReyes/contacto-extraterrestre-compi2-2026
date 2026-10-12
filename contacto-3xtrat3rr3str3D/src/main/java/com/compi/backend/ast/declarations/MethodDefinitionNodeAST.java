package com.compi.backend.ast.declarations;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractDefinitionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import com.compi.backend.symbols.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: MethodDefinitionNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class MethodDefinitionNodeAST extends AbstractDefinitionNodeAST {

    private final Type returnType;
    private final String name;
    private final List<ParameterNodeAST> parameters;
    private final AbstractStatementNodeAST body;

    public MethodDefinitionNodeAST(int line, int column, Type returnType, String name, List<ParameterNodeAST> parameters, AbstractStatementNodeAST body) {
        super(line, column);
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(parameters);
        children.add(body);
        return children;
    }

    public Type returnType() {
        return returnType;
    }

    public String name() {
        return name;
    }

    public List<ParameterNodeAST> parameters() {
        return parameters;
    }

    public AbstractStatementNodeAST body() {
        return body;
    }
}
