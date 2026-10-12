package com.compi.backend.ast.declarations;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractDefinitionNodeAST;
import com.compi.backend.ast.AbstractNodeAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: ClassDefinitionNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ClassDefinitionNodeAST extends AbstractDefinitionNodeAST {

    private final String name;
    private final String parentName;
    private final List<AbstractNodeAST> members;
    private final boolean publicClass;

    public ClassDefinitionNodeAST(int line, int column, String name, List<AbstractNodeAST> members) {
        this(line, column, name, members, false);
    }

    public ClassDefinitionNodeAST(int line, int column, String name, List<AbstractNodeAST> members, boolean publicClass) {
        this(line, column, name, null, members, publicClass);
    }

    public ClassDefinitionNodeAST(int line, int column, String name, String parentName,
                                  List<AbstractNodeAST> members, boolean publicClass) {
        super(line, column);
        this.publicClass = publicClass;
        this.name = name;
        this.parentName = parentName;
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

    /** Nombre de la superclase declarada con {@code extends}, o {@code null}. */
    public String parentName() {
        return parentName;
    }

    public boolean publicClass() {
        return publicClass;
    }

    public List<AbstractNodeAST> members() {
        return members;
    }
}
