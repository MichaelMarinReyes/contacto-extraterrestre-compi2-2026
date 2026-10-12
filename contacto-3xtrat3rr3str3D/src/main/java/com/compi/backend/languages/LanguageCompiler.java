package com.compi.backend.languages;

import com.compi.backend.ast.semantic.SemanticModel;
import com.compi.backend.ast.statements.ProgramNodeAST;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.symbols.SymbolTable;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface LanguageCompiler {
    public String id();

    public String displayName();

    public List<String> extensions();

    public ParseTree parse(String source, List<CompilationError> errors);

    public ProgramNodeAST buildAst(ParseTree tree, String sourceFile);

    public default Map<String, ProgramNodeAST> importedAsts() {
        return Map.of();
    }

    public default Map<String, SemanticModel> importedSemanticModels() {
        return Map.of();
    }

    public default Vocabulary vocabulary() {
        return null;
    }

    public default String[] ruleNames() {
        return null;
    }

    public void analyze(ProgramNodeAST tree, SymbolTable symbolTable, List<CompilationError> errors);

    public SemanticModel semanticModel();

    public void generateIntermediate(ProgramNodeAST tree, SymbolTable symbolTable, C3DGenerator generator);

    public default List<String> loadImports(ProgramNodeAST tree, SymbolTable symbolTable,
                                     C3DGenerator generator, List<CompilationError> errors) {
        return loadImports(tree, symbolTable, generator, errors, new ArrayList<>());
    }

    public default List<String> loadImports(ProgramNodeAST tree, SymbolTable symbolTable,
                                     C3DGenerator generator, List<CompilationError> errors,
                                     List<String> yaCargados) {
        return List.of();
    }

    public default void setWorkingDirectory(java.io.File directory) {
    }

    public default void setProjectDirectory(java.io.File directory) {
    }

    public default void setSourceFileName(String fileName) {
    }
}
