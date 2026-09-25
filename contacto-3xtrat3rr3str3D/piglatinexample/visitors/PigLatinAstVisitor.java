/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.visitors;

import igriega.piglatin.antlr4.PigLatinBaseVisitor;
import igriega.piglatin.antlr4.PigLatinParser;
import igriega.piglatin.expressions.Expression;
import igriega.piglatin.expressions.Literal;
import igriega.piglatin.expressions.operations.Operation;
import igriega.piglatin.expressions.VariableAccess;
import igriega.piglatin.statements.Assignation;
import igriega.piglatin.statements.Declaration;
import igriega.piglatin.statements.IfStmt;
import igriega.piglatin.statements.PrintStmt;
import igriega.piglatin.statements.Statement;

/**
 *
 * @author blue-dragon
 */
public class PigLatinAstVisitor extends PigLatinBaseVisitor<AstNode> {

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public Ast visitProgram(PigLatinParser.ProgramContext ctx) {
        Ast ast = new Ast();
        for (PigLatinParser.StmtContext stmtCtx : ctx.stmt()) {
            Statement statement = (Statement) visit(stmtCtx);
            ast.addStatement(statement);
        }
        return ast;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public Statement visitStmt(PigLatinParser.StmtContext ctx) {
        return (Statement) visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public PrintStmt visitPrint(PigLatinParser.PrintContext ctx) {
        Expression expression = (Expression) visit(ctx.expression());
        return new PrintStmt(expression);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public IfStmt visitIf_stmt(PigLatinParser.If_stmtContext ctx) {

        Expression expression = (Expression) visit(ctx.expression());
        IfStmt ifStmt = new IfStmt(expression);

        for (PigLatinParser.StmtContext stmtCtx : ctx.stmt()) {
            Statement statement = (Statement) visit(stmtCtx);
            ifStmt.addStatement(statement);
        }

        return ifStmt;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public Declaration visitDeclaration(PigLatinParser.DeclarationContext ctx) {
        String id = ctx.ID().getText();
        igriega.piglatin.utils.Type type = (igriega.piglatin.utils.Type) visit(ctx.type());
        Expression expression = (Expression) visit(ctx.expression());
        return new Declaration(id, type, expression);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public Assignation visitAssignation(PigLatinParser.AssignationContext ctx) {
        String id = ctx.ID().getText();
        Expression expression = (Expression) visit(ctx.expression());
        return new Assignation(id, expression);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public Expression visitExpression(
            PigLatinParser.ExpressionContext ctx) {

        // ID
        if (ctx.ID() != null) {
            return new VariableAccess(ctx.ID().getText());
        }

        // INT
        if (ctx.INT() != null) {
            return new Literal(
                    "numerus",
                    Integer.valueOf(ctx.INT().getText())
            );
        }

        // Paréntesis
        if (ctx.LPAREN() != null) {
            return (Expression) visit(ctx.expression(0));
        }

        // Operación binaria
        Expression left
                = (Expression) visit(ctx.expression(0));

        Expression right
                = (Expression) visit(ctx.expression(1));

        String operator
                = ctx.getChild(1).getText();

        return Operation.create(left, right, operator);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override
    public igriega.piglatin.utils.Type visitType(PigLatinParser.TypeContext ctx) {
        return new igriega.piglatin.utils.Type(ctx.getText());
    }

}
