package com.compi.backend.ast.decl;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractDefinitionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: ConstructorDefinitionNodeAST. Datos puros; la logica vive en los visitantes. */
public class ConstructorDefinitionNodeAST extends AbstractDefinitionNodeAST {

    private final List<ParameterNodeAST> parameters;
    private final AbstractStatementNodeAST body;

    public ConstructorDefinitionNodeAST(int line, int column, List<ParameterNodeAST> parameters, AbstractStatementNodeAST body) {
        super(line, column);
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

    public List<ParameterNodeAST> parameters() {
        return parameters;
    }

    public AbstractStatementNodeAST body() {
        return body;
    }
}
