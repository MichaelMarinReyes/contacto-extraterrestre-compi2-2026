package com.compi.backend.languages.zetariano.ast;

import com.compi.backend.languages.zetariano.ast.definitions.AttributeDefZet;
import com.compi.backend.languages.zetariano.ast.definitions.ClassDefZet;
import com.compi.backend.languages.zetariano.ast.definitions.ConstructorDefZet;
import com.compi.backend.languages.zetariano.ast.definitions.MethodDefZet;
import com.compi.backend.languages.zetariano.ast.expressions.ArithmeticOperationZet;
import com.compi.backend.languages.zetariano.ast.expressions.LiteralZet;
import com.compi.backend.languages.zetariano.ast.expressions.MemberAccessZet;
import com.compi.backend.languages.zetariano.ast.expressions.NewObjectExprZet;
import com.compi.backend.languages.zetariano.ast.statements.*;

public interface ZetarianVisitorCustom {
    Object visit(AttributeDefZet attributeDefZet, Object arg);

    Object visit(ClassDefZet classDefZet, Object arg);

    Object visit(ConstructorDefZet constructorDefZet, Object arg);

    Object visit(MethodDefZet methodDefZet, Object arg);

    Object visit(ArithmeticOperationZet arithmeticOperationZet, Object arg);

    Object visit(LiteralZet literalZet, Object arg);

    Object visit(MemberAccessZet memberAccessZet, Object arg);

    Object visit(NewObjectExprZet newObjectExprZet, Object arg);

    Object visit(AssignmentZet assignmentZet, Object arg);

    Object visit(IfStatementZet ifStatementZet, Object arg);

    Object visit(ReturnStatementZet returnStatementZet, Object arg);

    Object visit(VariableDeclarationZet variableDeclarationZet, Object arg);

    Object visit(WhileStatementZet whileStatementZet, Object arg);
}
