package com.compi.backend.languages.y.ast;

import com.compi.backend.languages.y.ast.definitions.FunctionDef;
import com.compi.backend.languages.y.ast.definitions.StructDef;
import com.compi.backend.languages.y.ast.expressions.*;
import com.compi.backend.languages.y.ast.statements.*;

public interface YVisitorCustom {
    Object visit(FunctionDef functionDef, Object arg);

    Object visit(StructDef structDef, Object arg);

    Object visit(ArithmeticOperationY arithmeticOperationY, Object arg);

    Object visit(FunctionCallY functionCallY, Object arg);

    Object visit(IdentifierY identifier, Object arg);

    Object visit(LiteralY literal, Object arg);

    Object visit(LogicalOperationY logicalOperationY, Object arg);

    Object visit(RelationalOperationY relationalOperationY, Object arg);

    Object visit(AssignmentY assignmentY, Object arg);

    Object visit(BreakStatementY breakStatement, Object arg);

    Object visit(ContinueStatementY continueStatementY, Object arg);

    Object visit(DoWhileStatementY doWhileStatementY, Object arg);

    Object visit(ForStatementY forStatementY, Object arg);

    Object visit(IfStatementY ifStatementY, Object arg);

    Object visit(ElseIfY elseIfY, Object arg);

    Object visit(PrintStatementY printStatementY, Object arg);

    Object visit(ReadStatementY readStatementY, Object arg);

    Object visit(ReturnStatementY returnStatementY, Object arg);

    Object visit(SwitchCaseY switchCaseY, Object arg);

    Object visit(SwitchStatementY switchStatementY, Object arg);

    Object visit(VariableDeclarationY variableDeclaration, Object arg);

    Object visit(WhileStatementY whileStatementY, Object arg);
}
