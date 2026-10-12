package com.compi.backend.languages.y;

import com.compi.YParser;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.AbstractDefinitionNodeAST;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import com.compi.backend.ast.build.AbstractAstBuilder;
import com.compi.backend.ast.declarations.FieldDefinitionNodeAST;
import com.compi.backend.ast.declarations.FunctionDefinitionNodeAST;
import com.compi.backend.ast.declarations.ParameterNodeAST;
import com.compi.backend.ast.declarations.StructDefinitionNodeAST;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.*;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Normaliza bloques indentados, funciones y estructuras del lenguaje Y.
 */
public class YAstBuilder extends AbstractAstBuilder {
    public YAstBuilder() {
        super(YParser.ruleNames);
    }

    @Override
    protected String tokenName(TerminalNode node) {
        return YParser.VOCABULARY.getSymbolicName(node.getSymbol().getType());
    }

    @Override
    protected ASTNode buildRule(String rule, ParserRuleContext c) {
        return switch (rule) {
            case "init", "condicion" -> unwrap(c);
            case "archivo" -> program(c);
            case "sentencia" -> contexts(c).isEmpty() ? null : unwrap(c);
            case "estructura_def", "declaracion_estructura_local" -> structure(c);
            case "funcion_def" -> function(c);
            case "bloque", "bloque_interno_opcional" -> block(c, "sentencia");
            case "declaracion_variable" -> declaration(c);
            case "asignacion" -> assignment(c);
            case "incremento_decremento" -> increment(c);
            case "si_sentencia" -> conditional(c);
            case "mientras_sentencia" ->
                    new WhileNodeAST(line(c), col(c), expr(c, "condicion"), statement(node(c, "bloque")));
            case "hacer_mientras_sentencia" ->
                    new DoWhileNodeAST(line(c), col(c), statement(node(c, "bloque")), expr(c, "condicion"));
            case "para_sentencia" -> new ForNodeAST(line(c), col(c), statement(visit(contexts(c).getFirst())),
                    expr(c, "condicion"), statement(node(c, "incremento_decremento")), statement(node(c, "bloque")));
            case "elegir_sentencia" -> selection(c);
            case "retornar_sentencia" -> new ReturnNodeAST(line(c), col(c), expr(c, "expresion"));
            case "romper_sentencia" -> new BreakNodeAST(line(c), col(c));
            case "continuar_sentencia" -> new ContinueNodeAST(line(c), col(c));
            case "imprimir_sentencia" -> new PrintNodeAST(line(c), col(c), arguments(c, "argumentos", "expresion"));
            case "llamada_funcion" -> new CallNodeAST(line(c), col(c), identifier(c, tokens(c, "ID").getFirst()),
                    arguments(c, "argumentos", "expresion"), null);
            case "leer_funcion" -> new ReadExpressionNodeAST(line(c), col(c), ReadKind.INPUT);
            case "acceso_miembro" -> access(c, "ID", "expresion");
            case "expresion" -> expression(c);
            case "sumatoria" -> fold(c, "productoria", "operador_aditivo");
            case "productoria" -> fold(c, "termino", "operador_multiplicativo");
            case "termino" -> primary(c);
            case "expresion_inicializacion", "elemento_lista" -> array(c);
            default -> throw new IllegalArgumentException("Regla Y sin AST: " + rule);
        };
    }

    private ProgramNodeAST program(ParserRuleContext c) {
        List<AbstractDefinitionNodeAST> definitions = new ArrayList<>();
        for (ParserRuleContext section : contexts(c)) {
            for (ParserRuleContext child : contexts(section)) {
                if (rule(child).equals("estructura_def") || rule(child).equals("funcion_def")) {
                    definitions.add((AbstractDefinitionNodeAST) visit(child));
                }
            }
        }
        return new ProgramNodeAST(line(c), col(c), List.of(), definitions, List.of(), List.of());
    }

    private StructDefinitionNodeAST structure(ParserRuleContext c) {
        List<FieldDefinitionNodeAST> fields = new ArrayList<>();
        for (ParserRuleContext field : all(c, "campo_struct")) {
            ParserRuleContext nested = one(field, "estructura_anidada_o_variable");
            ParserRuleContext source = nested == null ? field : nested;
            List<String> ids = tokens(source, "ID");
            String typeName = one(field, "tipo_dato") == null ? ids.getFirst() : one(field, "tipo_dato").getText();
            fields.add(new FieldDefinitionNodeAST(line(field), col(field), type(typeName, false),
                    ids.getLast(), fixedDimensions(field, "NUMERO_ENTERO")));
        }
        List<String> ids = tokens(c, "ID");
        if (ids.isEmpty()) {
            ParserRuleContext nested = one(c, "estructura_anidada_o_variable");
            ids = tokens(nested, "ID");
        }
        return new StructDefinitionNodeAST(line(c), col(c), ids.getFirst(), fields);
    }

    private FunctionDefinitionNodeAST function(ParserRuleContext c) {
        List<ParameterNodeAST> parameters = new ArrayList<>();
        ParserRuleContext params = one(c, "parametros");
        if (params != null) for (ParserRuleContext param : all(params, "parametro")) {
            Type type = type(one(param, "tipo_dato").getText(), false);
            if (has(param, "[")) type = Type.array(type, 1);
            else if (has(param, "{")) type = Type.structType(one(param, "tipo_dato").getText());
            parameters.add(new ParameterNodeAST(line(param), col(param), type, tokens(param, "ID").getFirst(),
                    has(param, "[") || has(param, "{")));
        }
        Type returnType = one(c, "tipo_dato") == null ? Type.VOID : type(one(c, "tipo_dato").getText(), false);
        return new FunctionDefinitionNodeAST(line(c), col(c), returnType, tokens(c, "ID").getFirst(),
                parameters, statement(node(c, "bloque")));
    }

    private DeclarationNodeAST declaration(ParserRuleContext c) {
        List<String> ids = tokens(c, "ID");
        String name = ids.isEmpty() ? "boolVar" : ids.getLast();
        String typeName = one(c, "tipo_dato") != null ? one(c, "tipo_dato").getText() : ids.isEmpty() ? "bool" : ids.getFirst();
        return new DeclarationNodeAST(line(c), col(c), type(typeName, false), name,
                fixedDimensions(c, "NUMERO_ENTERO"), expr(c, "expresion_inicializacion"));
    }

    private AbstractExpressionNodeAST target(ParserRuleContext c) {
        if (one(c, "acceso_miembro") != null) return expr(c, "acceso_miembro");
        AbstractExpressionNodeAST target = identifier(c, tokens(c, "ID").getFirst());
        List<ParserRuleContext> expressions = all(c, "expresion");
        if (has(c, "[")) target = new IndexNodeAST(line(c), col(c), target,
                expressions.subList(0, expressions.size() - 1).stream().map(this::expr).toList());
        return target;
    }

    private ASTNode assignment(ParserRuleContext c) {
        return new AssignmentNodeAST(line(c), col(c), target(c), "=", expr(all(c, "expresion").getLast()));
    }

    private ASTNode increment(ParserRuleContext c) {
        String operator = has(c, "++") ? "++" : "--";
        return new IncrementNodeAST(line(c), col(c), target(c), operator, c.getChild(0).getText().equals(operator));
    }

    private ASTNode expression(ParserRuleContext c) {
        if (one(c, "operador_negacion") != null)
            return new UnaryOperationNodeAST(line(c), col(c), "!", expr(c, "expresion"));
        for (String operator : List.of("operador_relacional", "operador_logico")) {
            if (one(c, operator) != null) {
                List<ParserRuleContext> operands = all(c, "expresion");
                return binary(c, expr(operands.getFirst()), one(c, operator).getText(), expr(operands.getLast()));
            }
        }
        return unwrap(c);
    }

    private ASTNode primary(ParserRuleContext c) {
        if (has(c, "{")) return array(c);
        if (has(c, "-") && one(c, "termino") != null)
            return new UnaryOperationNodeAST(line(c), col(c), "-", expr(c, "termino"));
        if (!contexts(c).isEmpty()) return unwrap(c);
        if (!tokens(c, "ID").isEmpty()) return identifier(c, tokens(c, "ID").getFirst());
        return literal(c, c.getText());
    }

    private ASTNode array(ParserRuleContext c) {
        if (one(c, "expresion") != null) return expr(c, "expresion");
        return new ArrayLiteralNodeAST(line(c), col(c), expressions(c, "elemento_lista"));
    }

    private ASTNode conditional(ParserRuleContext c) {
        List<ElseIfNodeAST> alternatives = new ArrayList<>();
        AbstractStatementNodeAST otherwise = null;
        for (ParserRuleContext branch : contexts(c)) {
            if (rule(branch).equals("sino_si_bloque") || rule(branch).equals("sino_bloque")) {
                if (one(branch, "condicion") == null) otherwise = statement(node(branch, "bloque"));
                else
                    alternatives.add(new ElseIfNodeAST(line(branch), col(branch), expr(branch, "condicion"), statement(node(branch, "bloque"))));
            } else if (rule(branch).equals("contrario_bloque")) otherwise = statement(node(branch, "bloque"));
        }
        return new IfNodeAST(line(c), col(c), expr(c, "condicion"), statement(node(c, "bloque")), alternatives, otherwise);
    }

    private BlockNodeAST caseBody(ParserRuleContext c) {
        BlockNodeAST body = (BlockNodeAST) node(c, "bloque_interno_opcional");
        List<AbstractStatementNodeAST> statements = new ArrayList<>(body.statements());
        if (has(c, "romper")) statements.add(new BreakNodeAST(line(c), col(c)));
        return new BlockNodeAST(body.line(), body.column(), statements);
    }

    private ASTNode selection(ParserRuleContext c) {
        List<SwitchCaseNodeAST> cases = all(c, "caso_bloque").stream().map(branch ->
                new SwitchCaseNodeAST(line(branch), col(branch), expr(branch, "expresion"), caseBody(branch))).toList();
        ParserRuleContext defaultCase = one(c, "siempre_bloque");
        return new SwitchNodeAST(line(c), col(c), expr(c, "expresion"), cases,
                defaultCase == null ? null : caseBody(defaultCase));
    }
}
