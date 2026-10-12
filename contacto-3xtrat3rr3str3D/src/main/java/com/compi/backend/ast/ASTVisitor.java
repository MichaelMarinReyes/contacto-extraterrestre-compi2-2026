package com.compi.backend.ast;

import com.compi.backend.ast.declarations.*;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.*;

/**
 * Visitante tipado del AST (doble despacho).
 */
public interface ASTVisitor<R> {
    public R visit(com.compi.backend.ast.statements.DefinitionStatementNodeAST node);

    public R visit(LiteralNodeAST node);

    public R visit(IdentifierNodeAST node);

    public R visit(BinaryOperationNodeAST node);

    public R visit(UnaryOperationNodeAST node);

    public R visit(AssignmentExpressionNodeAST node);

    public R visit(MemberAccessNodeAST node);

    public R visit(IndexNodeAST node);

    public R visit(CallNodeAST node);

    public R visit(NewObjectNodeAST node);

    public R visit(NewArrayNodeAST node);

    public R visit(ArrayLiteralNodeAST node);

    public R visit(TernaryNodeAST node);

    public R visit(IncrementNodeAST node);

    public R visit(ReadExpressionNodeAST node);

    public R visit(InstanceInitNodeAST node);

    public R visit(ProgramNodeAST node);

    public R visit(BlockNodeAST node);

    public R visit(DeclarationNodeAST node);

    public R visit(AssignmentNodeAST node);

    public R visit(IfNodeAST node);

    public R visit(ElseIfNodeAST node);

    public R visit(WhileNodeAST node);

    public R visit(DoWhileNodeAST node);

    public R visit(ForNodeAST node);

    public R visit(SwitchNodeAST node);

    public R visit(SwitchCaseNodeAST node);

    public R visit(PrintNodeAST node);

    public R visit(ReadStatementNodeAST node);

    public R visit(ReturnNodeAST node);

    public R visit(BreakNodeAST node);

    public R visit(ContinueNodeAST node);

    public R visit(ExpressionStatementNodeAST node);

    public R visit(ImportNodeAST node);

    public R visit(FunctionDefinitionNodeAST node);

    public R visit(ParameterNodeAST node);

    public R visit(StructDefinitionNodeAST node);

    public R visit(FieldDefinitionNodeAST node);

    public R visit(ClassDefinitionNodeAST node);

    public R visit(MethodDefinitionNodeAST node);

    public R visit(ConstructorDefinitionNodeAST node);

    public R visit(AttributeDefinitionNodeAST node);
}
