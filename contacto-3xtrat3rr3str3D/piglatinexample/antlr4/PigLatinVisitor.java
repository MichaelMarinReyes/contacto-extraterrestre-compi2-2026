// Generated from PigLatin.g4 by ANTLR 4.9.2

    package igriega.piglatin.antlr4;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PigLatinParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PigLatinVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PigLatinParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(PigLatinParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PigLatinParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(PigLatinParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PigLatinParser#print}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(PigLatinParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PigLatinParser#if_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stmt(PigLatinParser.If_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link PigLatinParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(PigLatinParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PigLatinParser#assignation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignation(PigLatinParser.AssignationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PigLatinParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(PigLatinParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PigLatinParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(PigLatinParser.TypeContext ctx);
}