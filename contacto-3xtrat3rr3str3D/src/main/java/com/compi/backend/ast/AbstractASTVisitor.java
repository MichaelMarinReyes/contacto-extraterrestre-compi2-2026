package com.compi.backend.ast;

import com.compi.backend.ast.decl.AttributeDefinitionNodeAST;
import com.compi.backend.ast.decl.ClassDefinitionNodeAST;
import com.compi.backend.ast.decl.ConstructorDefinitionNodeAST;
import com.compi.backend.ast.decl.FieldDefinitionNodeAST;
import com.compi.backend.ast.decl.FunctionDefinitionNodeAST;
import com.compi.backend.ast.decl.MethodDefinitionNodeAST;
import com.compi.backend.ast.decl.ParameterNodeAST;
import com.compi.backend.ast.decl.StructDefinitionNodeAST;
import com.compi.backend.ast.expr.ArrayLiteralNodeAST;
import com.compi.backend.ast.expr.AssignmentExpressionNodeAST;
import com.compi.backend.ast.expr.BinaryOperationNodeAST;
import com.compi.backend.ast.expr.CallNodeAST;
import com.compi.backend.ast.expr.IdentifierNodeAST;
import com.compi.backend.ast.expr.IncrementNodeAST;
import com.compi.backend.ast.expr.IndexNodeAST;
import com.compi.backend.ast.expr.InstanceInitNodeAST;
import com.compi.backend.ast.expr.LiteralNodeAST;
import com.compi.backend.ast.expr.MemberAccessNodeAST;
import com.compi.backend.ast.expr.NewArrayNodeAST;
import com.compi.backend.ast.expr.NewObjectNodeAST;
import com.compi.backend.ast.expr.ReadExpressionNodeAST;
import com.compi.backend.ast.expr.TernaryNodeAST;
import com.compi.backend.ast.expr.UnaryOperationNodeAST;
import com.compi.backend.ast.stmt.AssignmentNodeAST;
import com.compi.backend.ast.stmt.BlockNodeAST;
import com.compi.backend.ast.stmt.BreakNodeAST;
import com.compi.backend.ast.stmt.ContinueNodeAST;
import com.compi.backend.ast.stmt.DeclarationNodeAST;
import com.compi.backend.ast.stmt.DoWhileNodeAST;
import com.compi.backend.ast.stmt.ElseIfNodeAST;
import com.compi.backend.ast.stmt.ExpressionStatementNodeAST;
import com.compi.backend.ast.stmt.ForNodeAST;
import com.compi.backend.ast.stmt.IfNodeAST;
import com.compi.backend.ast.stmt.ImportNodeAST;
import com.compi.backend.ast.stmt.PrintNodeAST;
import com.compi.backend.ast.stmt.ProgramNodeAST;
import com.compi.backend.ast.stmt.ReadStatementNodeAST;
import com.compi.backend.ast.stmt.ReturnNodeAST;
import com.compi.backend.ast.stmt.SwitchCaseNodeAST;
import com.compi.backend.ast.stmt.SwitchNodeAST;
import com.compi.backend.ast.stmt.WhileNodeAST;

/** Implementacion por defecto: cada visita devuelve {@code null}. */
public abstract class AbstractASTVisitor<R> implements ASTVisitor<R> {

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
