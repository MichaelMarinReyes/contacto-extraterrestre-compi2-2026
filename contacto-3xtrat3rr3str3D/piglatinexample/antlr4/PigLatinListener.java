// Generated from PigLatin.g4 by ANTLR 4.9.2

    package igriega.piglatin.antlr4;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PigLatinParser}.
 */
public interface PigLatinListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PigLatinParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(PigLatinParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PigLatinParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(PigLatinParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PigLatinParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(PigLatinParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PigLatinParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(PigLatinParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PigLatinParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(PigLatinParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link PigLatinParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(PigLatinParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by {@link PigLatinParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(PigLatinParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link PigLatinParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(PigLatinParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link PigLatinParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(PigLatinParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PigLatinParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(PigLatinParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PigLatinParser#assignation}.
	 * @param ctx the parse tree
	 */
	void enterAssignation(PigLatinParser.AssignationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PigLatinParser#assignation}.
	 * @param ctx the parse tree
	 */
	void exitAssignation(PigLatinParser.AssignationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PigLatinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(PigLatinParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PigLatinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(PigLatinParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PigLatinParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(PigLatinParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PigLatinParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(PigLatinParser.TypeContext ctx);
}