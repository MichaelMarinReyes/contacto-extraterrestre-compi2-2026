package com.compi.backend.ast.statements;

import com.compi.backend.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del AST: ProgramNodeAST. Datos puros; la logica vive en los visitantes.
 */
public class ProgramNodeAST extends AbstractNodeAST {

    private final List<ImportNodeAST> imports;
    private final List<AbstractDefinitionNodeAST> definitions;
    private final List<AbstractStatementNodeAST> globals;
    private final List<AbstractStatementNodeAST> statements;

    public ProgramNodeAST(int line, int column, List<ImportNodeAST> imports, List<AbstractDefinitionNodeAST> definitions, List<AbstractStatementNodeAST> globals, List<AbstractStatementNodeAST> statements) {
        super(line, column);
        this.imports = imports;
        this.definitions = definitions;
        this.globals = globals;
        this.statements = statements;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(imports);
        children.addAll(definitions);
        children.addAll(globals);
        children.addAll(statements);
        return children;
    }

    public List<ImportNodeAST> imports() {
        return imports;
    }

    public List<AbstractDefinitionNodeAST> definitions() {
        return definitions;
    }

    public List<AbstractStatementNodeAST> globals() {
        return globals;
    }

    public List<AbstractStatementNodeAST> statements() {
        return statements;
    }
}
