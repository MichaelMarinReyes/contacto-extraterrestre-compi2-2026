package com.compi.backend;

import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.CCodeEmitter;
import com.compi.backend.c3d.IntermediateCodeManager;
import com.compi.backend.c3d.Mode;
import com.compi.backend.c3d.Quadruple;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.LanguageCompiler;
import com.compi.backend.languages.LanguageCompilerFactory;
import com.compi.backend.parser.ParseStep;
import com.compi.backend.parser.ParseTrace;
import com.compi.backend.runtime.Stack;
import com.compi.backend.runtime.StackSimulator;
import com.compi.backend.runtime.StackState;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.utils.ParseTreeDotGenerator;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

@Getter
public class Compiler {

    private String source = "";
    private String language = "pig";
    private Mode cMode = Mode.QUADRUPLES;

    @Getter(AccessLevel.NONE)
    private String sourceFileName;

    private final SymbolTable symbolTable = new SymbolTable();
    private final Stack processStack = new Stack();
    private final List<CompilationError> errors = new ArrayList<>();
    private final List<StackState> stackStates = new ArrayList<>();
    private final List<ParseStep> parseSteps = new ArrayList<>();
    private final IntermediateCodeManager icm = new IntermediateCodeManager();

    @Getter(AccessLevel.NONE)
    private final C3DGenerator c3dGenerator = new C3DGenerator();

    private String astDot = "";
    private String translatedCode = "";

    @Getter(AccessLevel.NONE)
    private String c3dCode = "";

    @Getter(AccessLevel.NONE)
    private String tripletsCode = "";

    @Getter(AccessLevel.NONE)
    private String quadruplesCode = "";

    private String cCode = "";

    private File workingDirectory = new File(".");

    private List<String> importedFiles = List.of();

    public void setWorkingDirectory(File directory) {
        this.workingDirectory = directory == null ? new File(".") : directory;
    }

    public boolean compile(String source, String language) {
        return compile(source, language, null);
    }

    public boolean compile(String source, String language, String fileName) {
        this.source = source == null ? "" : source;
        this.sourceFileName = fileName;
        reset();

        LanguageCompiler compiler = LanguageCompilerFactory.getCompiler(language);
        if (compiler == null) {
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "Lenguaje no soportado: " + language, -1, -1));
            importedFiles = List.of();
            return false;
        }
        this.language = compiler.id();
        compiler.setWorkingDirectory(workingDirectory);
        compiler.setSourceFileName(fileName);
        importedFiles = List.of();

        try {

            ParseTree tree = compiler.parse(this.source, errors);

            if (tree != null) {
                parseSteps.addAll(ParseTrace.build(tree, compiler.vocabulary(),
                        compiler.ruleNames()));
            }

            if (tree != null) {
                astDot = new ParseTreeDotGenerator().build(tree, "AST_" + compiler.id());
            }

            if (tree != null && errors.isEmpty()) {
                importedFiles = compiler.loadImports(tree, symbolTable, c3dGenerator, errors);
            }

            if (tree != null) {
                if (errors.isEmpty()) {
                    compiler.analyze(tree, symbolTable, errors);
                } else {

                    List<CompilationError> descartados = new ArrayList<>();
                    try {
                        compiler.analyze(tree, symbolTable, descartados);
                    } catch (Exception ex) {

                    }
                }
            }

            if (tree != null) {
                if (errors.isEmpty()) {
                    compiler.generateIntermediate(tree, symbolTable, c3dGenerator);
                } else {

                    try {
                        compiler.generateIntermediate(tree, symbolTable, c3dGenerator);
                    } catch (Exception ex) {

                    }
                }
            }

            List<Quadruple> quadruples = c3dGenerator.getQuadruples();

            icm.loadFromQuadruples(quadruples);
            tripletsCode = icm.getTripletsString();
            quadruplesCode = c3dGenerator.toQuadruplesString();
            c3dCode = c3dGenerator.toC3DString();

            translatedCode = buildTranslatedCode(compiler, quadruples);

            cCode = generateCCode();

            stackStates.addAll(new StackSimulator().simulate(quadruples));

            translatedCode = buildTranslatedCode(compiler, quadruples);

        } catch (Exception e) {
            String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "Error interno de compilación: " + msg, -1, -1));
        }

        return errors.isEmpty();
    }

    private void reset() {
        errors.clear();
        stackStates.clear();
        parseSteps.clear();
        processStack.clear();
        symbolTable.clear();
        icm.clear();
        c3dGenerator.clear();
        astDot = "";
        translatedCode = "";
        c3dCode = "";
        tripletsCode = "";
        quadruplesCode = "";
        cCode = "";
    }

    private String generateCCode() {
        return cMode == Mode.TRIPLETS ? tripletsToC() : quadruplesToC();
    }

    private String quadruplesToC() {
        return CCodeEmitter.generateCCode(c3dGenerator.getQuadruples(),
                c3dGenerator.getDeclaredTemps(), symbolTable);
    }

    private String tripletsToC() {
        List<com.compi.backend.c3d.Triplet> triplets = icm.getTriplets();
        StringBuilder sb = new StringBuilder();
        sb.append("/* Traduccion C generada a partir de los TRIPLETES.\n");
        sb.append(" * El triplete es la cuarteta sin destino visible; el destino que\n");
        sb.append(" * necesita el codigo se lee del campo interno del triplete.\n");
        sb.append(" */\n");
        sb.append("#include <stdio.h>\n#include <stdlib.h>\n#include <string.h>\n\n");
        sb.append("/* Memoria Stack y Heap */\n");
        sb.append("double stack[100000];\n");
        sb.append("double heap[100000];\n");
        sb.append("double P = 0;\n");
        sb.append("double H = 0;\n\n");
        sb.append("void print_string(int heap_idx) {\n");
        sb.append("    int i = heap_idx;\n");
        sb.append("    while (heap[i] != 0 && heap[i] != -1) {\n");
        sb.append("        printf(\"%c\", (char)heap[i]);\n");
        sb.append("        i++;\n");
        sb.append("    }\n");
        sb.append("}\n\n");

        operandosC = CCodeEmitter.operandosDe(symbolTable);
        sb.append(CCodeEmitter.declaracionesDe(symbolTable));

        if (triplets.isEmpty()) {
            sb.append("/* Sin tripletes: la compilacion no llego a la fase de generacion. */\n");
            return sb.toString();
        }

        for (com.compi.backend.c3d.Triplet t : triplets) {
            String code = tripletToCStatement(t);
            if (code == null) {
                continue;
            }
            switch (t.getOp()) {
                case LABEL, FUNCTION_START, FUNCTION_END -> sb.append(code);
                default -> sb.append("    ").append(code);
            }
        }
        return sb.toString();
    }

    private java.util.Map<String, String> operandosC = java.util.Map.of();

    private String c(String operando) {
        String mapeado = operandosC.get(operando);
        return mapeado == null ? operando : mapeado;
    }

    private String tripletToCStatement(com.compi.backend.c3d.Triplet t) {
        String a1 = t.getArg1() == null ? "" : t.getArg1();
        String a2 = t.getArg2() == null ? "" : t.getArg2();
        String res = t.getResult() == null ? "" : t.getResult();
        QuadrupleOp op = t.getOp();
        return switch (op) {

            case LABEL -> a2 + ":\n";
            case GOTO -> "goto " + a2 + ";\n";
            case IF_TRUE -> "if (" + a1 + ") goto " + res + ";\n";
            case IF_FALSE -> "if (!(" + a1 + ")) goto " + res + ";\n";
            case IF_EQ, IF_NE, IF_LT, IF_LE, IF_GT, IF_GE ->
                    "if (" + a1 + " " + op.getSymbol() + " " + a2 + ") goto " + res + ";\n";
            case ASSIGN -> {

                if (!a1.isEmpty() && a1.startsWith("\"") && a1.endsWith("\"")) {
                    StringBuilder literal = new StringBuilder();
                    CCodeEmitter.writeString(literal, c(res), a1);
                    yield literal.toString();
                }
                yield c(res) + " = " + c(a1) + ";\n";
            }
            case ADD, SUB, MUL, DIV, MOD, AND, OR ->
                    c(res) + " = " + c(a1) + " " + op.getSymbol() + " " + c(a2) + ";\n";
            case CONCAT -> c(res) + " = concat_cadenas(" + c(a1) + ", " + c(a2) + ");\n";
            case NOT -> c(res) + " = !" + c(a1) + ";\n";
            case NEG -> c(res) + " = -(" + c(a1) + ");\n";
            case ARRAY_GET -> c(res) + " = " + a1 + "[(int)" + c(a2) + "];\n";
            case ARRAY_SET -> a1 + "[(int)" + c(a2) + "] = " + c(res) + ";\n";
            case CALL -> (res.isEmpty() ? "" : c(res) + " = ") + a1 + "();\n";
            case PARAM -> "/* parametro */ " + c(a1) + ";\n";
            case RETURN -> a1.isEmpty() ? "return 0;\n" : "return " + c(a1) + ";\n";
            case FUNCTION_START -> "\ndouble " + res + "() {\n";
            case FUNCTION_END -> "    return 0;\n}\n";
            case STACK_SET -> "stack[(int)" + a1 + "] = " + a2 + ";\n";
            case STACK_GET -> res + " = stack[(int)" + a1 + "];\n";
            case HEAP_SET -> "heap[(int)" + a1 + "] = " + a2 + ";\n";
            case HEAP_GET -> res + " = heap[(int)" + a1 + "];\n";
            case SET_P -> "P = " + a1 + ";\n";
            case GET_P -> res + " = P;\n";
            case SET_H -> "H = " + a1 + ";\n";
            case GET_H -> res + " = H;\n";
            case PRINT_INT -> "printf(\"%d\", (int)" + c(a1) + ");\n";
            case PRINT_FLOAT -> "printf(\"%f\", " + c(a1) + ");\n";
            case PRINT_CHAR -> "printf(\"%c\", (char)" + c(a1) + ");\n";
            case PRINT_STR -> "print_string((int)" + c(a1) + ");\n";
            case PRINTLN -> "printf(\"\\n\");\n";
            case READ -> "scanf(\"%d\", &" + c(a1) + ");\n";
            default -> null;
        };
    }

    private String buildTranslatedCode(LanguageCompiler compiler, List<Quadruple> quadruples) {
        int maxDepth = 0;
        for (StackState state : stackStates) {
            maxDepth = Math.max(maxDepth, state.depth());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Lenguaje        : ").append(compiler.displayName()).append('\n');
        sb.append("Simbolos        : ").append(symbolTable.getAllSymbols().size()).append('\n');
        sb.append("Cuartetas       : ").append(quadruples.size()).append('\n');
        sb.append("Tripletes       : ").append(icm.getTriplets().size()).append('\n');
        sb.append("Errores         : ").append(errors.size()).append('\n');
        sb.append("Pila del parser : ").append(parseSteps.size()).append(" pasos\n");
        sb.append("Pila maquina    : ").append(stackStates.size())
                .append(" pasos (profundidad maxima ").append(maxDepth).append(")\n");
        return sb.toString();
    }

    public String getC3DCode() {
        return c3dCode;
    }

    public String getTriplets() {
        return tripletsCode;
    }

    public String getQuadruples() {
        return quadruplesCode;
    }

    public List<Symbol> getSymbols() {
        return symbolTable.getAllSymbols();
    }

    public List<Quadruple> getQuadrupleList() {
        return c3dGenerator.getQuadruples();
    }

    public void setCMode(Mode mode) {
        this.cMode = mode == null ? Mode.QUADRUPLES : mode;
        this.cCode = generateCCode();
    }

}
