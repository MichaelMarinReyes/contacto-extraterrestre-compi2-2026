package com.compi.backend.ast.declarations;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AccessModifier;
import com.compi.backend.ast.AbstractDefinitionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: ConstructorDefinitionNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ConstructorDefinitionNodeAST extends AbstractDefinitionNodeAST {

    private final List<ParameterNodeAST> parameters;
    private final AbstractStatementNodeAST body;
    private final String name;
    private final AccessModifier access;

    public ConstructorDefinitionNodeAST(int line, int column, List<ParameterNodeAST> parameters, AbstractStatementNodeAST body) {
        this(line, column, "", parameters, body);
    }

    public ConstructorDefinitionNodeAST(int line, int column, String name, List<ParameterNodeAST> parameters, AbstractStatementNodeAST body) {
        this(line, column, name, parameters, body, AccessModifier.DEFAULT);
    }

    public ConstructorDefinitionNodeAST(int line, int column, String name, List<ParameterNodeAST> parameters,
                                        AbstractStatementNodeAST body, AccessModifier access) {
        super(line, column);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.access = access;
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

    public List<ParameterNodeAST> parameters() {
        return parameters;
    }

    public String name() {
        return name;
    }

    public AbstractStatementNodeAST body() {
        return body;
    }

    public AccessModifier access() {
        return access;
    }
}
