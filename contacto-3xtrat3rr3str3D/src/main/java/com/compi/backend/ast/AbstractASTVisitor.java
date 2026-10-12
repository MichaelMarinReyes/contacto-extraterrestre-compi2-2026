package com.compi.backend.ast;

import com.compi.backend.ast.declarations.*;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.*;

/**
 * Implementación por defecto: cada visita devuelve {@code null}.
 */
public abstract class AbstractASTVisitor<R> implements ASTVisitor<R> {
    @Override
    public R visit(com.compi.backend.ast.statements.DefinitionStatementNodeAST node) {
        return null;
    }

    @Override
    public R visit(LiteralNodeAST node) {
        return null;
    }

    @Override
    public R visit(IdentifierNodeAST node) {
        return null;
    }

    @Override
    public R visit(BinaryOperationNodeAST node) {
        return null;
    }

    @Override
    public R visit(UnaryOperationNodeAST node) {
        return null;
    }

    @Override
    public R visit(AssignmentExpressionNodeAST node) {
        return null;
    }

    @Override
    public R visit(MemberAccessNodeAST node) {
        return null;
    }

    @Override
    public R visit(IndexNodeAST node) {
        return null;
    }

    @Override
    public R visit(CallNodeAST node) {
        return null;
    }

    @Override
    public R visit(NewObjectNodeAST node) {
        return null;
    }

    @Override
    public R visit(NewArrayNodeAST node) {
        return null;
    }

    @Override
    public R visit(ArrayLiteralNodeAST node) {
        return null;
    }

    @Override
    public R visit(TernaryNodeAST node) {
        return null;
    }

    @Override
    public R visit(IncrementNodeAST node) {
        return null;
    }

    @Override
    public R visit(ReadExpressionNodeAST node) {
        return null;
    }

    @Override
    public R visit(InstanceInitNodeAST node) {
        return null;
    }

    @Override
    public R visit(ProgramNodeAST node) {
        return null;
    }

    @Override
    public R visit(BlockNodeAST node) {
        return null;
    }

    @Override
    public R visit(DeclarationNodeAST node) {
        return null;
    }

    @Override
    public R visit(AssignmentNodeAST node) {
        return null;
    }

    @Override
    public R visit(IfNodeAST node) {
        return null;
    }

    @Override
    public R visit(ElseIfNodeAST node) {
        return null;
    }

    @Override
    public R visit(WhileNodeAST node) {
        return null;
    }

    @Override
    public R visit(DoWhileNodeAST node) {
        return null;
    }

    @Override
    public R visit(ForNodeAST node) {
        return null;
    }

    @Override
    public R visit(SwitchNodeAST node) {
        return null;
    }

    @Override
    public R visit(SwitchCaseNodeAST node) {
        return null;
    }

    @Override
    public R visit(PrintNodeAST node) {
        return null;
    }

    @Override
    public R visit(ReadStatementNodeAST node) {
        return null;
    }

    @Override
    public R visit(ReturnNodeAST node) {
        return null;
    }

    @Override
    public R visit(BreakNodeAST node) {
        return null;
    }

    @Override
    public R visit(ContinueNodeAST node) {
        return null;
    }

    @Override
    public R visit(ExpressionStatementNodeAST node) {
        return null;
    }

    @Override
    public R visit(ImportNodeAST node) {
        return null;
    }

    @Override
    public R visit(FunctionDefinitionNodeAST node) {
        return null;
    }

    @Override
    public R visit(ParameterNodeAST node) {
        return null;
    }

    @Override
    public R visit(StructDefinitionNodeAST node) {
        return null;
    }

    @Override
    public R visit(FieldDefinitionNodeAST node) {
        return null;
    }

    @Override
    public R visit(ClassDefinitionNodeAST node) {
        return null;
    }

    @Override
    public R visit(MethodDefinitionNodeAST node) {
        return null;
    }

    @Override
    public R visit(ConstructorDefinitionNodeAST node) {
        return null;
    }

    @Override
    public R visit(AttributeDefinitionNodeAST node) {
        return null;
    }
}
