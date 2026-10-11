package com.compi.backend.ast.stmt;

import com.compi.backend.ast.*;
import java.util.List;

/** Conserva una definición local dentro del bloque donde fue declarada. */
public class DefinitionStatementNodeAST extends AbstractStatementNodeAST {
    private final AbstractDefinitionNodeAST definition;

    public DefinitionStatementNodeAST(int line, int column, AbstractDefinitionNodeAST definition) {
        super(line, column);
        this.definition = definition;
    }

    public AbstractDefinitionNodeAST definition() { return definition; }
    @Override public List<ASTNode> children() { return List.of(definition); }
    @Override public void accept(ASTVisitor<?> visitor) { visitor.visit(this); }
}
