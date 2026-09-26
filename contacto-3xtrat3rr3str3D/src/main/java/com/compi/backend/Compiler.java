package com.compi.backend;

import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.CCodeEmitter;
import com.compi.backend.c3d.IntermediateCodeManager;
import com.compi.backend.c3d.Mode;
import com.compi.backend.c3d.Quadruple;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages2.LanguageCompiler;
import com.compi.backend.languages2.LanguageCompilerFactory;
import com.compi.backend.parser.ParseStep;
import com.compi.backend.parser.ParseTrace;
import com.compi.backend.runtime.Stack;
import com.compi.backend.runtime.StackSimulator;
import com.compi.backend.runtime.StackState;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.utils.ParseTreeDotGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Fachada del backend.
 *
 * Orquesta el pipeline completo para los tres lenguajes soportados:
 *
 * <pre>
 *   fuente -> Lexer -> Parser -> ParseTree -> AST (DOT)
 *          -> SemanticVisitor -> SymbolTable + errores
 *          -> C3DVisitor -> cuartetas -> tripletes
 *          -> CCodeEmitter -> C
 *          -> StackSimulator -> pila de procesos
 * </pre>
 */
public class Compiler {

    private String source = "";
    private String language = "pig";
    private Mode cMode = Mode.QUADRUPLES;

    private final SymbolTable symbolTable = new SymbolTable();
    private final Stack processStack = new Stack();
    private final List<CompilationError> errors = new ArrayList<>();
    private final List<StackState> stackStates = new ArrayList<>();
    private final List<ParseStep> parseSteps = new ArrayList<>();
    private final IntermediateCodeManager icm = new IntermediateCodeManager();
    private final C3DGenerator c3dGenerator = new C3DGenerator();

    private String astDot = "";
    private String translatedCode = "";
    private String c3dCode = "";
    private String tripletsCode = "";
    private String quadruplesCode = "";
    private String cCode = "";

    /** Raiz del proyecto, contra la que se resuelven los imports. */
    private File workingDirectory = new File(".");

    /** Archivos que se traido con los imports en la ultima compilacion. */
    private List<String> importedFiles = List.of();

    /**
     * Fija la carpeta del proyecto.
     *
     * <p>Es donde se buscan los archivos que el fuente importa. Si no se dice
     * nada, los imports se resuelven contra el directorio de trabajo del
     * proceso.</p>
     */
    public void setWorkingDirectory(File directory) {
        this.workingDirectory = directory == null ? new File(".") : directory;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Archivos cargados por los {@code import} en la ultima compilacion, con su
     * ruta respecto a la carpeta del proyecto.
     */
    public List<String> getImportedFiles() {
        return importedFiles;
    }

    /**
     * Compila el texto fuente del lenguaje indicado.
     *
     * @param source   texto completo del archivo
     * @param language pig, y o zet (tambien acepta el nombre del archivo)
     * @return true si no se registraron errores
     */
    public boolean compile(String source, String language) {
        this.source = source == null ? "" : source;
        reset();

        LanguageCompiler compiler = LanguageCompilerFactory.getCompiler(language);
        if (compiler == null) {
            errors.add(new CompilationError(ErrorType.SEMANTICO,
                    "Lenguaje no soportado: " + language, -1, -1));
            importedFiles = List.of();
            return false;
        }
        this.language = compiler.id();
        compiler.setWorkingDirectory(workingDirectory);
        importedFiles = List.of();

        try {
            // 1. Lexico + sintactico
            ParseTree tree = compiler.parse(this.source, errors);

            // 1b. Trazo de la pila del parser: reglas que entra, tokens que
            // reconoce y reducciones, tal y como ocurrieron.
            if (tree != null) {
                parseSteps.addAll(ParseTrace.build(tree, compiler.vocabulary(),
                        compiler.ruleNames()));
            }

            // 2. Arbol de parseo -> representacion DOT
            if (tree != null) {
                astDot = new ParseTreeDotGenerator().build(tree, "AST_" + compiler.id());
            }

            // 3. Imports: lo que el fuente trae de otros archivos se compila antes
            // de mirarlo a el, en la misma tabla de simbolos, para que sus
            // simbolos ya existan cuando se resuelvan sus usos.
            if (tree != null && errors.isEmpty()) {
                importedFiles = compiler.loadImports(tree, symbolTable, c3dGenerator, errors);
            }

            // 4. Semantico -> tabla de simbolos
            if (tree != null) {
                if (errors.isEmpty()) {
                    compiler.analyze(tree, symbolTable, errors);
                } else {
                    // El parseo ya fallo, pero el arbol suele estar incompleto solo
                    // al final. Se analiza igualmente para completar la tabla con
                    // lo que si se reconocio, en vez de dejarla vacia y no
                    // informar de nada. Los errores que salgan aqui se guardan
                    // aparte y se tiran: serian reacciones en cascada de un error
                    // de sintaxis y solo taparian el problema real.
                    List<CompilationError> descartados = new ArrayList<>();
                    try {
                        compiler.analyze(tree, symbolTable, descartados);
                    } catch (Exception ex) {
                        // El arbol esta incompleto: se conserva la tabla parcial.
                    }
                }
            }

            // 5. Codigo de tres direcciones -> cuartetas
            if (tree != null) {
                if (errors.isEmpty()) {
                    compiler.generateIntermediate(tree, symbolTable, c3dGenerator);
                } else {
                    // Con errores ya registrados el codigo no es de fiar, pero
                    // intentarlo da la pila de ejecucion de la parte que si se
                    // entendio, que es justo lo que sirve para ver por donde se
                    // salio. Si el generador se tropieza, se queda con las
                    // cuartetas que hubiera emitido hasta ese punto.
                    try {
                        compiler.generateIntermediate(tree, symbolTable, c3dGenerator);
                    } catch (Exception ex) {
                        // El arbol no cuadra con la tabla: la pila sale a medias.
                    }
                }
            }

            List<Quadruple> quadruples = c3dGenerator.getQuadruples();

            // 6. Tripletes derivados de las cuartetas
            icm.loadFromQuadruples(quadruples);
            tripletsCode = icm.getTripletsString();
            quadruplesCode = icm.getQuadruplesString();
            c3dCode = c3dGenerator.toC3DString();

            // 7. Traduccion intermedia legible
            translatedCode = buildTranslatedCode(compiler, quadruples);

            // 8. Codigo C segun el modo elegido (tripletes o cuartetas)
            cCode = generateCCode();

            // 9. Pila de procesos
            stackStates.addAll(new StackSimulator().simulate(quadruples));

            // 10. El resumen se rehace para incluir la profundidad final de la pila.
            translatedCode = buildTranslatedCode(compiler, quadruples);

        } catch (Exception e) {
            String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            errors.add(new CompilationError(ErrorType.SEMANTICO,
                    "Error interno de compilación: " + msg, -1, -1));
        }

        // Cada simbolo se anota con el lenguaje en el que se declaro. Se hace
        // aqui, en la fachada, y no en cada visitor: los tres visitantes no
        // tienen por que saber como se llama su lenguaje.
        tagSymbolsLanguage(compiler.displayName());

        return errors.isEmpty();
    }

    /**
     * Anota el lenguaje de origen de todos los simbolos de la tabla.
     *
     * <p>Se usa el nombre legible ({@code PigLatin}) y no el identificador
     * interno ({@code pig}) porque esto sale tal cual en la tabla de simbolos de
     * la interfaz.</p>
     */
    private void tagSymbolsLanguage(String languageName) {
        for (Symbol s : symbolTable.getAllSymbols()) {
            s.inLanguage(languageName);
        }
    }

    /** Limpia todo el estado de una compilacion anterior. */
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

    /** Genera el codigo C a partir de tripletes o cuartetas segun el modo. */
    private String generateCCode() {
        return cMode == Mode.TRIPLETS ? tripletsToC() : quadruplesToC();
    }

    private String quadruplesToC() {
        return CCodeEmitter.generateCCode(c3dGenerator.getQuadruples(),
                c3dGenerator.getDeclaredTemps());
    }

    /**
     * Transcribe los tripletes como un listado de C.
     *
     * <p>El triplete es una vista reducida de la cuarteta (no lleva destino de
     * la operacion), asi que la lectura se apoya en dos convenciones:</p>
     * <ul>
     *   <li>una operacion aritmetica o logica se transcribe como expresion
     *       cuyo valor recoje el {@code ASSIGN} siguiente;</li>
     *   <li>un condicional marca el salto y la etiqueta de destino es la del
     *       {@code GOTO} que va justo detras.</li>
     * </ul>
     * <p>Cada linea repite su triplete de origen para poder contrastarla.</p>
     */
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

    /**
     * Traduce un triplete a una sentencia de C, siguiendo la misma convencion
     * que {@link com.compi.backend.c3d.Quadruple#toString()}.
     *
     * @return la sentencia con su salto de linea, o null si no tiene equivalente
     */
    private String tripletToCStatement(com.compi.backend.c3d.Triplet t) {
        String a1 = t.getArg1() == null ? "" : t.getArg1();
        String a2 = t.getArg2() == null ? "" : t.getArg2();
        String res = t.getResult() == null ? "" : t.getResult();
        QuadrupleOp op = t.getOp();
        return switch (op) {
            // La etiqueta del salto viaja en el segundo operando del triplete.
            case LABEL -> a2 + ":\n";
            case GOTO -> "goto " + a2 + ";\n";
            case IF_TRUE -> "if (" + a1 + ") goto " + res + ";\n";
            case IF_FALSE -> "if (!(" + a1 + ")) goto " + res + ";\n";
            case IF_EQ, IF_NE, IF_LT, IF_LE, IF_GT, IF_GE ->
                    "if (" + a1 + " " + op.getSymbol() + " " + a2 + ") goto " + res + ";\n";
            case ASSIGN -> res + " = " + a1 + ";\n";
            case ADD, SUB, MUL, DIV, MOD, AND, OR ->
                    res + " = " + a1 + " " + op.getSymbol() + " " + a2 + ";\n";
            case NOT -> res + " = !" + a1 + ";\n";
            case CALL -> a1 + "(" + a2 + ");\n";
            case PARAM -> "/* parametro */ " + a1 + ";\n";
            case RETURN -> a1.isEmpty() ? "return;\n" : "return " + a1 + ";\n";
            case FUNCTION_START -> "\nvoid " + res + "() {\n";
            case FUNCTION_END -> "}\n";
            case STACK_SET -> "stack[(int)" + a1 + "] = " + a2 + ";\n";
            case STACK_GET -> res + " = stack[(int)" + a1 + "];\n";
            case HEAP_SET -> "heap[(int)" + a1 + "] = " + a2 + ";\n";
            case HEAP_GET -> res + " = heap[(int)" + a1 + "];\n";
            case SET_P -> "P = " + a1 + ";\n";
            case GET_P -> res + " = P;\n";
            case SET_H -> "H = " + a1 + ";\n";
            case GET_H -> res + " = H;\n";
            case PRINT_INT -> "printf(\"%d\", (int)" + a1 + ");\n";
            case PRINT_FLOAT -> "printf(\"%f\", " + a1 + ");\n";
            case PRINT_CHAR -> "printf(\"%c\", (char)" + a1 + ");\n";
            case PRINT_STR -> "print_string((int)" + a1 + ");\n";
            case PRINTLN -> "printf(\"\\n\");\n";
            case READ -> "scanf(\"%d\", &" + a1 + ");\n";
            default -> null;
        };
    }

    /** Resumen legible de lo generado, mostrado en la consola. */
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

    // ===================== Accesores =====================

    public String getAstDot() {
        return astDot;
    }

    public Stack getProcessStack() {
        return processStack;
    }

    /** Estados de la pila de procesos, uno por instruccion simulada. */
    public List<StackState> getStackStates() {
        return stackStates;
    }

    /** Trazo de la pila del parser: un paso por regla, token o reduccion. */
    public List<ParseStep> getParseSteps() {
        return parseSteps;
    }

    public String getTranslatedCode() {
        return translatedCode;
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

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<Symbol> getSymbols() {
        return symbolTable.getAllSymbols();
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    public String getCCode() {
        return cCode;
    }

    public List<Quadruple> getQuadrupleList() {
        return c3dGenerator.getQuadruples();
    }

    public IntermediateCodeManager getIcm() {
        return icm;
    }

    public String getSource() {
        return source;
    }

    public String getLanguage() {
        return language;
    }

    public Mode getCMode() {
        return cMode;
    }

    /** Cambia el modo de generacion de C (tripletes o cuartetas) y regenera el C. */
    public void setCMode(Mode mode) {
        this.cMode = mode == null ? Mode.QUADRUPLES : mode;
        this.cCode = generateCCode();
    }

    /** Numero de instrucciones de la ultima compilacion. */
    public int getInstructionCount() {
        return c3dGenerator.getQuadruples().size();
    }

    /** Primer opcode usado, util para depuracion. */
    public QuadrupleOp firstOp() {
        List<Quadruple> qs = c3dGenerator.getQuadruples();
        return qs.isEmpty() ? null : qs.get(0).getOp();
    }
}
