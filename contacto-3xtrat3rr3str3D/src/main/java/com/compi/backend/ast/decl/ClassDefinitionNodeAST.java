package com.compi.backend.ast.decl;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractDefinitionNodeAST;
import com.compi.backend.ast.AbstractNodeAST;
import java.util.ArrayList;
import java.util.List;

/** Nodo del AST: ClassDefinitionNodeAST. Datos puros; la logica vive en los visitantes. */
public class ClassDefinitionNodeAST extends AbstractDefinitionNodeAST {

    private final String name;
    private final List<AbstractNodeAST> members;

    public ClassDefinitionNodeAST(int line, int column, String name, List<AbstractNodeAST> members) {
        super(line, column);
        this.name = name;
        this.members = members;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(members);
        return children;
    }

    public String name() {
        return name;
    }

    public List<AbstractNodeAST> members() {
        return members;
    }
}
