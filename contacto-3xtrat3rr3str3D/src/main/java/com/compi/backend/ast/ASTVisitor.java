package com.compi.backend.ast;

import com.compi.backend.ast.declarations.*;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.*;

/**
 * Visitante tipado del AST (doble despacho).
 */
public interface ASTVisitor<R> {
    R visit(com.compi.backend.ast.statements.DefinitionStatementNodeAST node);

    R visit(LiteralNodeAST node);

    R visit(IdentifierNodeAST node);

    R visit(BinaryOperationNodeAST node);

    R visit(UnaryOperationNodeAST node);

    R visit(AssignmentExpressionNodeAST node);

    R visit(MemberAccessNodeAST node);

    R visit(IndexNodeAST node);

    R visit(CallNodeAST node);

    R visit(NewObjectNodeAST node);

    R visit(NewArrayNodeAST node);

    R visit(ArrayLiteralNodeAST node);

    R visit(TernaryNodeAST node);

    R visit(IncrementNodeAST node);

    R visit(ReadExpressionNodeAST node);

    R visit(InstanceInitNodeAST node);

    R visit(ProgramNodeAST node);

    R visit(BlockNodeAST node);

    R visit(DeclarationNodeAST node);

    R visit(AssignmentNodeAST node);

    R visit(IfNodeAST node);

    R visit(ElseIfNodeAST node);

    R visit(WhileNodeAST node);

    R visit(DoWhileNodeAST node);

    R visit(ForNodeAST node);

    R visit(SwitchNodeAST node);

    R visit(SwitchCaseNodeAST node);

    R visit(PrintNodeAST node);

    R visit(ReadStatementNodeAST node);

    R visit(ReturnNodeAST node);

    R visit(BreakNodeAST node);

    R visit(ContinueNodeAST node);

    R visit(ExpressionStatementNodeAST node);

    R visit(ImportNodeAST node);

    R visit(FunctionDefinitionNodeAST node);

    R visit(ParameterNodeAST node);

    R visit(StructDefinitionNodeAST node);

    R visit(FieldDefinitionNodeAST node);

    R visit(ClassDefinitionNodeAST node);

    R visit(MethodDefinitionNodeAST node);

    R visit(ConstructorDefinitionNodeAST node);

    R visit(AttributeDefinitionNodeAST node);
}
