package com.compi.backend.ast.expr;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.ASTVisitor;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Nodo del AST: InstanceInitNodeAST. Datos puros; la logica vive en los visitantes. */
public class InstanceInitNodeAST extends AbstractExpressionNodeAST {

    private final String typeName;
    private final List<AbstractExpressionNodeAST> positional;
    private final Map<String, AbstractExpressionNodeAST> named;
    private final AbstractExpressionNodeAST arrayRef;
    private final List<String> duplicateFields;

    public InstanceInitNodeAST(int line, int column, String typeName, List<AbstractExpressionNodeAST> positional, Map<String, AbstractExpressionNodeAST> named, AbstractExpressionNodeAST arrayRef) {
        this(line, column, typeName, positional, named, arrayRef, List.of());
    }

    public InstanceInitNodeAST(int line, int column, String typeName, List<AbstractExpressionNodeAST> positional,
                               Map<String, AbstractExpressionNodeAST> named, AbstractExpressionNodeAST arrayRef, List<String> duplicateFields) {
        super(line, column);
        this.duplicateFields = List.copyOf(duplicateFields);
        this.typeName = typeName;
        this.positional = positional;
        this.named = named;
        this.arrayRef = arrayRef;
    }

    @Override
    public void accept(ASTVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override
    public List<ASTNode> children() {
        List<ASTNode> children = new ArrayList<>();
        children.addAll(positional);
        children.addAll(named.values());
        children.add(arrayRef);
        return children;
    }

    public String typeName() {
        return typeName;
    }

    public List<String> duplicateFields() { return duplicateFields; }

    public List<AbstractExpressionNodeAST> positional() {
        return positional;
    }

    public Map<String, AbstractExpressionNodeAST> named() {
        return named;
    }

    public AbstractExpressionNodeAST arrayRef() {
        return arrayRef;
    }
}
