package com.compi.backend.languages.piglatin;

import com.compi.PigLatinParser;
import com.compi.backend.ast.ASTNode;
import com.compi.backend.ast.AbstractExpressionNodeAST;
import com.compi.backend.ast.AbstractStatementNodeAST;
import com.compi.backend.ast.build.AbstractAstBuilder;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.*;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Traduce la sintaxis de PigLatin a nodos compartidos del AST.
 */
public class PigLatinAstBuilder extends AbstractAstBuilder {
    public PigLatinAstBuilder() {
        super(PigLatinParser.ruleNames);
    }

    @Override
    protected String tokenName(TerminalNode node) {
        return PigLatinParser.VOCABULARY.getSymbolicName(node.getSymbol().getType());
    }

    @Override
    protected ASTNode buildRule(String rule, ParserRuleContext c) {
        return switch (rule) {
            case "init", "sentencia", "condiciones_per" -> wrapper(c);
            case "pig_latin" -> program(c);
            case "declaracion", "arreglo_declaracion", "inicializacion_per" -> declarationOrAssignment(c);
            case "asignacion_sentencia", "incremento_per" -> assignment(c);
            case "imprimir_sentencia" -> new PrintNodeAST(line(c), col(c), expressions(c, "expresion"));
            case "leer_sentencia" -> new ReadStatementNodeAST(line(c), col(c), target(c));
            case "si_sentencia" -> conditional(c);
            case "aliter_bloque" -> new ElseIfNodeAST(line(c), col(c), expr(c, "condicion"), block(c, "sentencia"));
            case "ciclo_dum" -> new WhileNodeAST(line(c), col(c), expr(c, "condicion"), block(c, "sentencia"));
            case "ciclo_facere" -> new DoWhileNodeAST(line(c), col(c), block(c, "sentencia"), expr(c, "condicion"));
            case "ciclo_per" -> new ForNodeAST(line(c), col(c), statement(node(c, "inicializacion_per")),
                    expr(c, "condiciones_per"), statement(node(c, "incremento_per")), block(c, "sentencia"));
            case "salto_sentencia" ->
                    has(c, "perge") ? new ContinueNodeAST(line(c), col(c)) : new BreakNodeAST(line(c), col(c));
            case "expresion" -> fold(c, "producto", "suma_resta");
            case "producto" -> fold(c, "termino", "multiplicacion");
            case "termino", "primaria_logica" -> primary(c);
            case "condicion", "conjuncion", "negacion_logica" -> logical(c);
            case "acceso_miembro", "arreglo_acceso" -> access(c, "VARIABLE", "expresion");
            case "llamada_funcion" ->
                    new CallNodeAST(line(c), col(c), target(c), arguments(c, "argumentos", "expresion"), null);
            case "arreglo_literal", "elemento_arreglo", "elemento_arreglo_valor", "elemento_arreglo_struct" -> array(c);
            case "structura_instanciacion" -> instance(c);
            default -> throw new IllegalArgumentException("Regla PigLatin sin AST: " + rule);
        };
    }

    private ASTNode wrapper(ParserRuleContext c) {
        if (has(c, "++") || has(c, "--")) return increment(c);
        return unwrap(c);
    }

    private ProgramNodeAST program(ParserRuleContext c) {
        List<ImportNodeAST> imports = new ArrayList<>();
        for (ParserRuleContext child : all(c, "import_declaracion")) {
            imports.add(new ImportNodeAST(line(child), col(child), String.join(".", tokens(child, "VARIABLE"))));
        }
        List<AbstractStatementNodeAST> globals = new ArrayList<>();
        ParserRuleContext variables = one(c, "variables");
        if (variables != null) for (ParserRuleContext child : contexts(variables)) globals.add(statement(visit(child)));
        ProgramNodeAST program = new ProgramNodeAST(line(c), col(c), imports, List.of(), globals, block(one(c, "maior"), "sentencia").statements());
        return program;
    }

    private ASTNode declarationOrAssignment(ParserRuleContext c) {
        if (one(c, "arreglo_declaracion") != null) return node(c, "arreglo_declaracion");
        if (rule(c).equals("inicializacion_per") && !has(c, "esto")) return assignment(c);
        List<String> ids = tokens(c, "VARIABLE");
        Type declared = Type.UNKNOWN;
        if (one(c, "tipo_dato") != null) declared = type(one(c, "tipo_dato").getText(), false);
        else if (has(c, "textum")) declared = Type.STRING;
        else if (has(c, "littera")) declared = Type.CHAR;
        else if (declaredObjectType(c, ids) != null) declared = declaredObjectType(c, ids);
        AbstractExpressionNodeAST value = initializer(c);
        if (value instanceof InstanceInitNodeAST init && init.typeName().isEmpty()) {
            value = new InstanceInitNodeAST(init.line(), init.column(), ids.get(1), init.positional(), init.named(), init.arrayRef(), init.duplicateFields());
        }
        if (has(c, "{")) {
            value = one(c, "elemento_arreglo") != null ? expr(c, "elemento_arreglo")
                    : one(c, "elemento_arreglo_struct") != null ? expr(c, "elemento_arreglo_struct")
                    : new ArrayLiteralNodeAST(line(c), col(c), List.of());
        }
        return new DeclarationNodeAST(line(c), col(c), declared, ids.getFirst(), fixedDimensions(c, "NUMERO_ENTERO"), value);
    }

    /** Tipo declarado para objeto: explícito tras {@code novus ... : Tipo} o tras {@code null : Tipo}. */
    private Type declaredObjectType(ParserRuleContext c, List<String> ids) {
        if (ids.size() < 2) return null;
        if (has(c, "novus")) return type(ids.size() >= 3 ? ids.get(2) : ids.get(1), true);
        if (has(c, "null")) return type(ids.get(1), true);
        return type(ids.get(1), false);
    }

    private AbstractExpressionNodeAST initializer(ParserRuleContext c) {
        if (has(c, "novus")) {
            List<String> ids = tokens(c, "VARIABLE");
            String constructor = ids.size() >= 2 ? ids.get(1) : ids.getLast();
            return new NewObjectNodeAST(line(c), col(c), constructor, arguments(c, "argumentos", "expresion"), null);
        }
        if (has(c, "null")) return literal(c, "null");
        for (String name : List.of("expresion", "condicion", "structura_instanciacion", "arreglo_literal")) {
            if (one(c, name) != null) return expr(c, name);
        }
        for (String token : List.of("CADENA_TEXTO", "CARACTER")) {
            List<String> values = tokens(c, token);
            if (!values.isEmpty()) return literal(c, values.getFirst());
        }
        return null;
    }

    private AbstractExpressionNodeAST target(ParserRuleContext c) {
        if (one(c, "acceso_miembro") != null) return expr(c, "acceso_miembro");
        List<String> ids = tokens(c, "VARIABLE");
        return ids.isEmpty() ? null : identifier(c, ids.getFirst());
    }

    private IncrementNodeAST increment(ParserRuleContext c) {
        return new IncrementNodeAST(line(c), col(c), target(c), has(c, "++") ? "++" : "--", false);
    }

    private ASTNode assignment(ParserRuleContext c) {
        if (has(c, "++") || has(c, "--")) return increment(c);
        return new AssignmentNodeAST(line(c), col(c), target(c), "=", initializer(c));
    }

    private ASTNode primary(ParserRuleContext c) {
        if (has(c, "novus")) return initializer(c);
        if (one(c, "operador_relacional") != null) {
            List<ParserRuleContext> operands = all(c, "expresion");
            return binary(c, expr(operands.getFirst()), one(c, "operador_relacional").getText(), expr(operands.getLast()));
        }
        if (has(c, "-") && one(c, "termino") != null)
            return new UnaryOperationNodeAST(line(c), col(c), "-", expr(c, "termino"));
        if (!contexts(c).isEmpty()) return unwrap(c);
        if (!tokens(c, "VARIABLE").isEmpty()) return target(c);
        return literal(c, c.getText());
    }

    private ASTNode logical(ParserRuleContext c) {
        List<ParserRuleContext> children = contexts(c);
        if (has(c, "non")) return new UnaryOperationNodeAST(line(c), col(c), "!", expr(children.getFirst()));
        if (children.size() == 1) return visit(children.getFirst());
        return binary(c, expr(children.getFirst()), has(c, "||") ? "||" : "&&", expr(children.getLast()));
    }

    private ASTNode array(ParserRuleContext c) {
        if (one(c, "expresion") != null) return expr(c, "expresion");
        if (rule(c).equals("elemento_arreglo_valor")) {
            return one(c, "elemento_arreglo") == null ? new ArrayLiteralNodeAST(line(c), col(c), List.of()) : node(c, "elemento_arreglo");
        }
        if (rule(c).equals("arreglo_literal") && one(c, "elemento_arreglo") != null) return node(c, "elemento_arreglo");
        return new ArrayLiteralNodeAST(line(c), col(c), contexts(c).stream().map(this::expr).toList());
    }

    private ASTNode instance(ParserRuleContext c) {
        List<String> ids = tokens(c, "VARIABLE");
        String name = ids.isEmpty() ? "" : ids.getFirst();
        if (has(c, "[")) return new InstanceInitNodeAST(line(c), col(c), "", List.of(), java.util.Map.of(),
                new IndexNodeAST(line(c), col(c), identifier(c, name), fixedDimensions(c, "NUMERO_ENTERO")));
        LinkedHashMap<String, AbstractExpressionNodeAST> named = new LinkedHashMap<>();
        List<String> duplicates = new ArrayList<>();
        for (ParserRuleContext attr : all(c, "atributo_asignacion")) {
            String field = tokens(attr, "VARIABLE").getFirst();
            if (named.containsKey(field)) duplicates.add(field);
            named.put(field, initializer(attr));
        }
        return new InstanceInitNodeAST(line(c), col(c), name, expressions(c, "expresion"), named, null, duplicates);
    }

    private ASTNode conditional(ParserRuleContext c) {
        List<AbstractStatementNodeAST> thenStatements = new ArrayList<>();
        List<AbstractStatementNodeAST> elseStatements = new ArrayList<>();
        boolean otherwise = false;
        for (ParseTree child : c.children) {
            if (child instanceof TerminalNode && child.getText().equals("aliter")) otherwise = true;
            if (child instanceof ParserRuleContext context && rule(context).equals("sentencia")) {
                (otherwise ? elseStatements : thenStatements).add(statement(visit(context)));
            }
        }
        List<ElseIfNodeAST> alternatives = all(c, "aliter_bloque").stream().map(child -> (ElseIfNodeAST) visit(child)).toList();
        return new IfNodeAST(line(c), col(c), expr(c, "condicion"), new BlockNodeAST(line(c), col(c), thenStatements),
                alternatives, otherwise ? new BlockNodeAST(line(c), col(c), elseStatements) : null);
    }
}
