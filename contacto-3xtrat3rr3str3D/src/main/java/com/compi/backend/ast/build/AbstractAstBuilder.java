package com.compi.backend.ast.build;

import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.AbstractDefinitionNodeAST;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.BlockNodeAST;
import com.compi.backend.ast.statements.DefinitionStatementNodeAST;
import com.compi.backend.ast.statements.ExpressionStatementNodeAST;
import com.compi.backend.ast.statements.ProgramNodeAST;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidades compartidas de construcción; ningún nodo retiene el parse tree.
 */
public abstract class AbstractAstBuilder extends AbstractParseTreeVisitor<ASTNode> {
    private final String[] rules;

    protected AbstractAstBuilder(String[] rules) {
        this.rules = rules;
    }

    public ProgramNodeAST build(ParseTree tree, String sourceFile) {
        ProgramNodeAST program = (ProgramNodeAST) visit(tree);
        program.attachSource(sourceFile);
        return program;
    }

    @Override
    public ASTNode visitChildren(RuleNode tree) {
        ParserRuleContext context = (ParserRuleContext) tree;
        return buildRule(rule(context), context);
    }

    protected abstract ASTNode buildRule(String rule, ParserRuleContext context);

    protected String rule(ParserRuleContext c) {
        return rules[c.getRuleIndex()];
    }

    protected int line(ParserRuleContext c) {
        return c.getStart().getLine();
    }

    protected int col(ParserRuleContext c) {
        return c.getStart().getCharPositionInLine();
    }

    protected List<ParserRuleContext> contexts(ParserRuleContext c) {
        List<ParserRuleContext> result = new ArrayList<>();
        if (c.children != null) for (ParseTree child : c.children) {
            if (child instanceof ParserRuleContext context) result.add(context);
        }
        return result;
    }

    protected List<ParserRuleContext> all(ParserRuleContext c, String name) {
        return contexts(c).stream().filter(child -> rule(child).equals(name)).toList();
    }

    protected ParserRuleContext one(ParserRuleContext c, String name) {
        return all(c, name).stream().findFirst().orElse(null);
    }

    protected ASTNode node(ParserRuleContext c, String name) {
        ParserRuleContext child = one(c, name);
        return child == null ? null : visit(child);
    }

    protected AbstractExpressionNodeAST expr(ParserRuleContext c) {
        return c == null ? null : (AbstractExpressionNodeAST) visit(c);
    }

    protected AbstractExpressionNodeAST expr(ParserRuleContext c, String name) {
        return expr(one(c, name));
    }

    protected List<AbstractExpressionNodeAST> expressions(ParserRuleContext c, String name) {
        return all(c, name).stream().map(this::expr).toList();
    }

    protected List<String> tokens(ParserRuleContext c, String symbolic) {
        List<String> result = new ArrayList<>();
        if (c.children != null) for (ParseTree child : c.children) {
            if (child instanceof TerminalNode terminal && symbolic.equals(tokenName(terminal)))
                result.add(child.getText());
        }
        return result;
    }

    protected abstract String tokenName(TerminalNode terminal);

    protected boolean has(ParserRuleContext c, String text) {
        return c.children != null && c.children.stream().anyMatch(child -> child instanceof TerminalNode && child.getText().equals(text));
    }

    protected IdentifierNodeAST identifier(ParserRuleContext c, String name) {
        Token token = directToken(c, name);
        if (token != null) return new IdentifierNodeAST(token.getLine(), token.getCharPositionInLine(), name, null);
        return new IdentifierNodeAST(line(c), col(c), name, null);
    }

    private Token directToken(ParserRuleContext c, String text) {
        for (ParseTree child : c.children) {
            if (child instanceof TerminalNode terminal && child.getText().equals(text)) return terminal.getSymbol();
        }
        return null;
    }

    protected Type type(String name, boolean classType) {
        return switch (name) {
            case "numerus", "entero", "int" -> Type.INT;
            case "decimalis", "flotante", "double" -> Type.DOUBLE;
            case "textum", "cadena", "String" -> Type.STRING;
            case "littera", "caracter", "char" -> Type.CHAR;
            case "bool", "boolean" -> Type.BOOLEAN;
            case "void" -> Type.VOID;
            default -> classType ? Type.classType(name) : Type.structType(name);
        };
    }

    protected ASTNode unwrap(ParserRuleContext c) {
        List<ParserRuleContext> children = contexts(c);
        if (children.size() == 1) return visit(children.getFirst());
        throw new IllegalArgumentException("Regla AST sin implementación: " + rule(c));
    }

    protected AbstractStatementNodeAST statement(ASTNode node) {
        if (node instanceof AbstractStatementNodeAST stmt) return stmt;
        if (node instanceof AbstractExpressionNodeAST expr)
            return new ExpressionStatementNodeAST(expr.line(), expr.column(), expr);
        if (node instanceof AbstractDefinitionNodeAST def)
            return new DefinitionStatementNodeAST(def.line(), def.column(), def);
        if (node == null) return null;
        throw new IllegalArgumentException("Se esperaba una sentencia AST");
    }

    protected BlockNodeAST block(ParserRuleContext c, String statementRule) {
        List<AbstractStatementNodeAST> statements = new ArrayList<>();
        for (ParserRuleContext child : all(c, statementRule)) {
            AbstractStatementNodeAST stmt = statement(visit(child));
            if (stmt != null) statements.add(stmt);
        }
        return new BlockNodeAST(line(c), col(c), statements);
    }

    protected AbstractExpressionNodeAST binary(ParserRuleContext c, AbstractExpressionNodeAST left,
                                               String operator, AbstractExpressionNodeAST right) {
        OperationKind kind = switch (operator) {
            case "&&", "||" -> OperationKind.LOGICAL;
            case "<", ">", "<=", ">=", "==", "!=" -> OperationKind.RELATIONAL;
            default -> OperationKind.ARITHMETIC;
        };
        return new BinaryOperationNodeAST(line(c), col(c), left, operator, right, kind);
    }

    /**
     * Pliega las cadenas aritméticas por la izquierda, respetando la gramática.
     */
    protected AbstractExpressionNodeAST fold(ParserRuleContext c, String operandRule, String operatorRule) {
        List<ParserRuleContext> operands = all(c, operandRule);
        List<ParserRuleContext> operators = all(c, operatorRule);
        AbstractExpressionNodeAST result = expr(operands.getFirst());
        for (int i = 0; i < operators.size(); i++)
            result = binary(c, result, operators.get(i).getText(), expr(operands.get(i + 1)));
        return result;
    }

    protected LiteralNodeAST literal(ParserRuleContext c, String text) {
        Token token = directToken(c, text);
        return literal(token == null ? line(c) : token.getLine(),
                token == null ? col(c) : token.getCharPositionInLine(), text);
    }

    private LiteralNodeAST literal(int line, int column, String text) {
        Object value;
        LiteralKind kind;
        if (text.startsWith("\"")) {
            kind = LiteralKind.STRING;
            value = text.substring(1, text.length() - 1);
        } else if (text.startsWith("'")) {
            kind = LiteralKind.CHARACTER;
            value = text.charAt(1);
        } else if (List.of("verum", "falsus", "verdadero", "falso", "true", "false").contains(text)) {
            kind = LiteralKind.BOOLEAN;
            value = List.of("verum", "verdadero", "true").contains(text);
        } else if (text.equals("null")) {
            kind = LiteralKind.NULL;
            value = null;
        } else if (text.contains(".")) {
            kind = LiteralKind.DECIMAL;
            value = Double.valueOf(text);
        } else {
            kind = LiteralKind.INTEGER;
            value = new BigInteger(text);
        }
        return new LiteralNodeAST(line, column, value, kind);
    }

    protected AbstractExpressionNodeAST access(ParserRuleContext c, String idToken, String expressionRule) {
        AbstractExpressionNodeAST result = null;
        for (ParseTree child : c.children) {
            if (child instanceof TerminalNode terminal) {
                if (idToken.equals(tokenName(terminal))) {
                    result = result == null ? identifier(c, child.getText())
                            : new MemberAccessNodeAST(line(c), col(c), result, child.getText(), null);
                }
            } else if (child instanceof ParserRuleContext context && rule(context).equals(expressionRule)) {
                result = new IndexNodeAST(line(context), col(context), result, List.of(expr(context)));
            }
        }
        return result;
    }

    protected List<AbstractExpressionNodeAST> arguments(ParserRuleContext c, String argumentRule, String expressionRule) {
        ParserRuleContext args = one(c, argumentRule);
        return args == null ? List.of() : expressions(args, expressionRule);
    }

    protected List<AbstractExpressionNodeAST> fixedDimensions(ParserRuleContext c, String numberToken) {
        List<AbstractExpressionNodeAST> dimensions = new ArrayList<>();
        for (ParseTree child : c.children) {
            if (child instanceof TerminalNode terminal && numberToken.equals(tokenName(terminal))) {
                Token token = terminal.getSymbol();
                dimensions.add(literal(token.getLine(), token.getCharPositionInLine(), token.getText()));
            }
        }
        return dimensions;
    }
}
