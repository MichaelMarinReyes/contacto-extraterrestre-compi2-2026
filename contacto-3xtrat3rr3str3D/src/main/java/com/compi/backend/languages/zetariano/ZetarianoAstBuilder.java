package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoParser;
import com.compi.backend.ast.*;
import com.compi.backend.ast.build.AbstractAstBuilder;
import com.compi.backend.ast.declarations.*;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.*;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Construye clases, accesos y expresiones de Zetariano conservando su estructura.
 */
public class ZetarianoAstBuilder extends AbstractAstBuilder {
    public ZetarianoAstBuilder() {
        super(ZetarianoParser.ruleNames);
    }

    @Override
    protected String tokenName(TerminalNode node) {
        return ZetarianoParser.VOCABULARY.getSymbolicName(node.getSymbol().getType());
    }

    @Override
    protected ASTNode buildRule(String rule, ParserRuleContext c) {
        return switch (rule) {
            case "programa" -> new ProgramNodeAST(line(c), col(c), List.of(),
                    all(c, "claseDef").stream().map(child -> (AbstractDefinitionNodeAST) visit(child)).toList(), List.of(), List.of());
            case "claseDef" -> new ClassDefinitionNodeAST(line(c), col(c), tokens(c, "ID").getFirst(),
                    all(c, "miembroClase").stream().map(child -> (AbstractNodeAST) visit(child)).toList(), has(c, "public"));
            case "miembroClase", "forInit", "forUpdate" -> unwrap(c);
            case "sentencia" -> statementRule(c);
            case "bloque" -> block(c, "sentencia");
            case "atributoDef", "declaracionVariable" -> declaration(c);
            case "metodoDef", "constructorDef" -> callable(c);
            case "asignacion", "asignacionExpresion" -> assignment(c);
            case "incrementoDecremento" -> increment(c, false);
            case "sentenciaIf" -> conditional(c);
            case "sentenciaSwitch" -> selection(c);
            case "sentenciaWhile" ->
                    new WhileNodeAST(line(c), col(c), expr(c, "expresion"), statement(node(c, "bloque")));
            case "sentenciaDoWhile" ->
                    new DoWhileNodeAST(line(c), col(c), statement(node(c, "bloque")), expr(c, "expresion"));
            case "sentenciaFor" -> new ForNodeAST(line(c), col(c), statement(node(c, "forInit")), expr(c, "expresion"),
                    statement(node(c, "forUpdate")), statement(node(c, "bloque")));
            case "retorno" -> new ReturnNodeAST(line(c), col(c), expr(c, "expresion"));
            case "romper" -> new BreakNodeAST(line(c), col(c));
            case "continuar" -> new ContinueNodeAST(line(c), col(c));
            case "llamadaFuncionSemilla" -> call(c);
            case "accesoMiembro" -> memberAccess(c);
            case "literalArreglo" ->
                    new ArrayLiteralNodeAST(line(c), col(c), contexts(c).stream().map(this::expr).toList());
            case "expresion" -> expression(c);
            default -> throw new IllegalArgumentException("Regla Zetariano sin AST: " + rule);
        };
    }

    private List<AbstractExpressionNodeAST> dimensions(ParserRuleContext c) {
        List<AbstractExpressionNodeAST> result = new ArrayList<>();
        for (String ignored : tokens(c, "LBRACK")) result.add(null);
        return result;
    }

    private ASTNode statementRule(ParserRuleContext c) {
        if (contexts(c).isEmpty()) return null;
        ParserRuleContext call = one(c, "llamadaFuncionSemilla");
        if (call != null && (has(call, "println") || has(call, "print"))) {
            return new PrintNodeAST(line(call), col(call), arguments(call, "argumentos", "expresion"), has(call, "println"));
        }
        return unwrap(c);
    }

    private ASTNode declaration(ParserRuleContext c) {
        Type type = type(one(c, "tipoDato").getText(), true);
        String name = tokens(c, "ID").getFirst();
        if (rule(c).equals("atributoDef")) return new AttributeDefinitionNodeAST(line(c), col(c), type, name,
                dimensions(c), expr(c, "expresion"));
        return new DeclarationNodeAST(line(c), col(c), type, name, dimensions(c), expr(c, "expresion"));
    }

    private ASTNode callable(ParserRuleContext c) {
        List<ParameterNodeAST> parameters = new ArrayList<>();
        ParserRuleContext params = one(c, "parametros");
        if (params != null) for (ParserRuleContext param : all(params, "parametro")) {
            parameters.add(new ParameterNodeAST(line(param), col(param), type(one(param, "tipoDato").getText(), true),
                    tokens(param, "ID").getFirst(), false));
        }
        AbstractStatementNodeAST body = statement(node(c, "bloque"));
        if (rule(c).equals("constructorDef"))
            return new ConstructorDefinitionNodeAST(line(c), col(c), tokens(c, "ID").getFirst(), parameters, body);
        return new MethodDefinitionNodeAST(line(c), col(c), type(one(c, "tipoDato").getText(), true),
                tokens(c, "ID").getFirst(), parameters, body);
    }

    private AbstractExpressionNodeAST target(ParserRuleContext c) {
        if (one(c, "accesoMiembro") != null) return expr(c, "accesoMiembro");
        AbstractExpressionNodeAST target = identifier(c, tokens(c, "ID").getFirst());
        List<ParserRuleContext> operands = all(c, "expresion");
        if (has(c, "[")) target = new IndexNodeAST(line(c), col(c), target,
                operands.subList(0, operands.size() - 1).stream().map(this::expr).toList());
        return target;
    }

    private String assignmentOperator(ParserRuleContext c) {
        for (String operator : List.of("=", "+=", "-=", "*=")) if (has(c, operator)) return operator;
        throw new IllegalArgumentException("Asignación sin operador");
    }

    private ASTNode assignment(ParserRuleContext c) {
        AbstractExpressionNodeAST target = target(c);
        AbstractExpressionNodeAST value = expr(all(c, "expresion").getLast());
        String operator = assignmentOperator(c);
        if (rule(c).equals("asignacionExpresion"))
            return new AssignmentExpressionNodeAST(line(c), col(c), target, operator, value);
        return new AssignmentNodeAST(line(c), col(c), target, operator, value);
    }

    private ASTNode increment(ParserRuleContext c, boolean prefix) {
        return new IncrementNodeAST(line(c), col(c), target(c), has(c, "++") ? "++" : "--", prefix);
    }

    private AbstractExpressionNodeAST memberAccess(ParserRuleContext c) {
        AbstractExpressionNodeAST result = identifier(c, tokens(c, "ID").getFirst());
        for (ParserRuleContext member : all(c, "miembroAcceso")) {
            if (has(member, "."))
                result = new MemberAccessNodeAST(line(member), col(member), result, tokens(member, "ID").getFirst(), null);
            List<AbstractExpressionNodeAST> indices = expressions(member, "expresion");
            if (!indices.isEmpty()) result = new IndexNodeAST(line(member), col(member), result, indices);
        }
        return result;
    }

    private ASTNode call(ParserRuleContext c) {
        List<AbstractExpressionNodeAST> args = arguments(c, "argumentos", "expresion");
        if (has(c, "println") || has(c, "print")) return new CallNodeAST(line(c), col(c),
                identifier(c, has(c, "println") ? "println" : "print"), args, null);
        if (has(c, "readln")) return new ReadExpressionNodeAST(line(c), col(c), ReadKind.READLINE);
        return new CallNodeAST(line(c), col(c), target(c), args, null);
    }

    private ASTNode expression(ParserRuleContext c) {
        List<ParserRuleContext> operands = all(c, "expresion");
        if (has(c, "new")) {
            if (one(c, "tipoDato") != null) return new NewArrayNodeAST(line(c), col(c),
                    type(one(c, "tipoDato").getText(), true), expressions(c, "expresion"));
            return new NewObjectNodeAST(line(c), col(c), tokens(c, "ID").getFirst(), arguments(c, "argumentos", "expresion"), null);
        }
        if (has(c, "++") || has(c, "--")) return increment(c, true);
        if (operands.size() == 3)
            return new TernaryNodeAST(line(c), col(c), expr(operands.get(0)), expr(operands.get(1)), expr(operands.get(2)));
        if (operands.size() == 2)
            return binary(c, expr(operands.getFirst()), c.getChild(1).getText(), expr(operands.getLast()));
        if (operands.size() == 1) {
            if (has(c, "-") || has(c, "!"))
                return new UnaryOperationNodeAST(line(c), col(c), c.getChild(0).getText(), expr(operands.getFirst()));
            return expr(operands.getFirst());
        }
        if (!contexts(c).isEmpty()) {
            return unwrap(c);
        }
        if (!tokens(c, "ID").isEmpty()) return identifier(c, tokens(c, "ID").getFirst());
        return literal(c, c.getText());
    }

    private ASTNode conditional(ParserRuleContext c) {
        List<ParserRuleContext> conditions = all(c, "expresion");
        List<ParserRuleContext> bodies = all(c, "bloque");
        List<ElseIfNodeAST> alternatives = new ArrayList<>();
        for (int i = 1; i < conditions.size(); i++)
            alternatives.add(new ElseIfNodeAST(line(conditions.get(i)),
                    col(conditions.get(i)), expr(conditions.get(i)), statement(visit(bodies.get(i)))));
        return new IfNodeAST(line(c), col(c), expr(conditions.getFirst()), statement(visit(bodies.getFirst())), alternatives,
                bodies.size() > conditions.size() ? statement(visit(bodies.getLast())) : null);
    }

    private BlockNodeAST caseBody(ParserRuleContext c) {
        List<AbstractStatementNodeAST> statements = new ArrayList<>(block(c, "sentencia").statements());
        if (has(c, "break")) statements.add(new BreakNodeAST(line(c), col(c)));
        return new BlockNodeAST(line(c), col(c), statements);
    }

    private ASTNode selection(ParserRuleContext c) {
        List<SwitchCaseNodeAST> cases = all(c, "casoSwitch").stream().map(branch ->
                new SwitchCaseNodeAST(line(branch), col(branch), expr(branch, "expresion"), caseBody(branch))).toList();
        ParserRuleContext defaultCase = one(c, "defectoSwitch");
        return new SwitchNodeAST(line(c), col(c), expr(c, "expresion"), cases, defaultCase == null ? null : caseBody(defaultCase));
    }
}
