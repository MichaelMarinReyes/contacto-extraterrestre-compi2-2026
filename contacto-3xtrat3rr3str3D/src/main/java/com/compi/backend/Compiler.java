package com.compi.backend;

import com.compi.backend.ast.semantic.SemanticModel;
import com.compi.backend.ast.statements.ProgramNodeAST;
import com.compi.backend.c3d.*;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.languages.LanguageCompiler;
import com.compi.backend.languages.LanguageCompilerFactory;
import com.compi.backend.parser.ParseStep;
import com.compi.backend.parser.ParseTrace;
import com.compi.backend.riscv.RiscvAssembler;
import com.compi.backend.runtime.Stack;
import com.compi.backend.runtime.StackSimulator;
import com.compi.backend.runtime.StackState;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.TypeEntry;
import com.compi.backend.utils.ASTDotGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Compiler {

    private final SymbolTable symbolTable = new SymbolTable();
    private final Stack processStack = new Stack();
    private final List<CompilationError> errors = new ArrayList<>();
    private final List<StackState> stackStates = new ArrayList<>();
    private final List<ParseStep> parseSteps = new ArrayList<>();
    private final IntermediateCodeManager icm = new IntermediateCodeManager();
    @Getter(AccessLevel.NONE)
    private final C3DGenerator c3dGenerator = new C3DGenerator();
    private String source = "";
    private String language = "pig";
    private Mode cMode = Mode.QUADRUPLES;
    @Getter(AccessLevel.NONE)
    private String sourceFileName;
    private String astDot = "";
    private String translatedCode = "";

    @Getter(AccessLevel.NONE)
    private String c3dCode = "";

    @Getter(AccessLevel.NONE)
    private String tripletsCode = "";

    @Getter(AccessLevel.NONE)
    private String quadruplesCode = "";

    private String cCode = "";

    private String riscvCode = "";

    private boolean riscvEnabled = true;

    private File workingDirectory = new File(".");
    private boolean workingDirectoryConfigured;

    private List<String> importedFiles = List.of();
    private ProgramNodeAST ast;
    private SemanticModel semanticModel;
    private Map<String, SemanticModel> importedSemanticModels = Map.of();
    private java.util.Map<String, ProgramNodeAST> importedAsts = java.util.Map.of();

    public void setWorkingDirectory(File directory) {
        this.workingDirectory = directory == null ? new File(".") : directory;
        this.workingDirectoryConfigured = directory != null;
    }

    public boolean compile(String source, String language) {
        return compile(source, language, null);
    }

    public boolean compile(String source, String language, String fileName) {
        this.source = source == null ? "" : source;
        this.sourceFileName = fileName;
        reset();

        LanguageCompiler compiler = resolveCompiler(language);
        if (compiler == null) {
            return false;
        }
        this.language = compiler.id();
        configureWorkingDirectory(compiler, fileName);

        try {
            runPhases(compiler);
        } catch (Exception e) {
            recordInternalError(e);
        }

        return errors.isEmpty();
    }

    private LanguageCompiler resolveCompiler(String language) {
        LanguageCompiler compiler = LanguageCompilerFactory.getCompiler(language);
        if (compiler == null) {
            errors.add(new CompilationError(ErrorType.SEMANTIC,
                    "Lenguaje no soportado: " + language, -1, -1));
            importedFiles = List.of();
            return null;
        }
        return compiler;
    }

    private void configureWorkingDirectory(LanguageCompiler compiler, String fileName) {
        File sourceDirectory = workingDirectory;
        if (fileName != null && !fileName.isBlank()) {
            File sourceFile = new File(fileName);
            if (sourceFile.isAbsolute() || sourceFile.getParent() != null) {
                sourceDirectory = sourceFile.getAbsoluteFile().getParentFile();
            }
        }
        compiler.setWorkingDirectory(sourceDirectory);
        compiler.setProjectDirectory(workingDirectoryConfigured ? workingDirectory : sourceDirectory);
        compiler.setSourceFileName(fileName);
        importedFiles = List.of();
    }

    private void runPhases(LanguageCompiler compiler) {
        ParseTree tree = compiler.parse(this.source, errors);
        if (tree != null) {
            parseSteps.addAll(ParseTrace.build(tree, compiler.vocabulary(), compiler.ruleNames()));
        }

        if (tree != null && errors.isEmpty()) {
            ast = compiler.buildAst(tree, sourceFileName);
            astDot = new ASTDotGenerator().build(ast, "AST_" + compiler.id());
        }

        if (tree != null && errors.isEmpty()) {
            importedFiles = compiler.loadImports(ast, symbolTable, c3dGenerator, errors, rootFiles());
            importedAsts = compiler.importedAsts();
            importedSemanticModels = compiler.importedSemanticModels();
        }

        if (tree != null && errors.isEmpty()) {
            compiler.analyze(ast, symbolTable, errors);
            semanticModel = compiler.semanticModel();
        }

        if (tree != null && errors.isEmpty()) {
            compiler.generateIntermediate(ast, symbolTable, c3dGenerator);
        }

        publishIntermediate(compiler);
    }

    private List<String> rootFiles() {
        List<String> loadedFiles = new ArrayList<>();
        if (sourceFileName != null && !sourceFileName.isBlank()) {
            File rootFile = new File(sourceFileName);
            if (!rootFile.isAbsolute() && rootFile.getParent() == null) {
                rootFile = new File(workingDirectory, sourceFileName);
            }
            loadedFiles.add(rootFile.toPath().toAbsolutePath().normalize().toString());
        }
        return loadedFiles;
    }

    private void publishIntermediate(LanguageCompiler compiler) {
        List<Quadruple> quadruples = c3dGenerator.getQuadruples();

        icm.loadFromQuadruples(quadruples);
        tripletsCode = icm.getTripletsString();
        quadruplesCode = c3dGenerator.toQuadruplesString();
        c3dCode = c3dGenerator.toC3DString();

        stackStates.addAll(new StackSimulator().simulate(quadruples));
        translatedCode = buildTranslatedCode(compiler, quadruples);

        if (errors.isEmpty()) {
            cCode = generateCCode();
            generateRiscv();
        }
    }

    private void generateRiscv() {
        riscvCode = RiscvAssembler.generateAssembly(c3dGenerator.getQuadruples(), symbolTable);
    }

    private void recordInternalError(Exception e) {
        String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        errors.add(new CompilationError(ErrorType.SEMANTIC,
                "Error interno de compilación: " + msg, -1, -1));
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
        ast = null;
        semanticModel = null;
        importedSemanticModels = java.util.Map.of();
        importedAsts = java.util.Map.of();
        translatedCode = "";
        c3dCode = "";
        tripletsCode = "";
        quadruplesCode = "";
        cCode = "";
        riscvCode = "";
    }

    private String generateCCode() {
        return cMode == Mode.TRIPLETS ? tripletsToC() : quadruplesToC();
    }

    private String quadruplesToC() {
        return CCodeEmitter.generateCCode(c3dGenerator.getQuadruples(),
                c3dGenerator.getDeclaredTemps(), symbolTable);
    }

    private String tripletsToC() {
        List<Quadruple> instructions = new ArrayList<>();
        for (com.compi.backend.c3d.Triplet triplet : icm.getTriplets()) {
            boolean label = triplet.getOp() == QuadrupleOp.LABEL || triplet.getOp() == QuadrupleOp.GOTO;
            instructions.add(new Quadruple(triplet.getOp(), triplet.getArg1(),
                    label ? null : triplet.getArg2(), label ? triplet.getArg2() : triplet.getResult()));
        }
        // Ambos modos comparten las mismas convenciones de memoria y llamadas.
        return CCodeEmitter.generateCCode(instructions, c3dGenerator.getDeclaredTemps(), symbolTable);
    }

    private String buildTranslatedCode(LanguageCompiler compiler, List<Quadruple> quadruples) {
        int maxDepth = 0;
        for (StackState state : stackStates) {
            maxDepth = Math.max(maxDepth, state.depth());
        }
        String sb = "Lenguaje        : " + compiler.displayName() + '\n' +
                "Simbolos        : " + symbolTable.getAllSymbols().size() + '\n' +
                "Cuartetas       : " + quadruples.size() + '\n' +
                "Tripletes       : " + icm.getTriplets().size() + '\n' +
                "Errores         : " + errors.size() + '\n' +
                "Pila del parser : " + parseSteps.size() + " pasos\n" +
                "Pila maquina    : " + stackStates.size() +
                " pasos (profundidad maxima " + maxDepth + ")\n";
        return sb;
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

    public List<TypeEntry> getTypes() {
        return symbolTable.getTypeEntries();
    }

    public List<Quadruple> getQuadrupleList() {
        return c3dGenerator.getQuadruples();
    }

    public void setCMode(Mode mode) {
        this.cMode = mode == null ? Mode.QUADRUPLES : mode;
        this.cCode = errors.isEmpty() ? generateCCode() : "";
    }

    public void setRiscvEnabled(boolean enabled) {
        this.riscvEnabled = enabled;
        this.riscvCode = enabled && errors.isEmpty()
                ? RiscvAssembler.generateAssembly(c3dGenerator.getQuadruples(), symbolTable) : "";
    }

    public boolean isRiscvEnabled() {
        return riscvEnabled;
    }

}
