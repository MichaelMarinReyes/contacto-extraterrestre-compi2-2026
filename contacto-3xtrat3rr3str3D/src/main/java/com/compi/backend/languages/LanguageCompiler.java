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
    String id();

    String displayName();

    List<String> extensions();

    ParseTree parse(String source, List<CompilationError> errors);

    ProgramNodeAST buildAst(ParseTree tree, String sourceFile);

    default Map<String, ProgramNodeAST> importedAsts() {
        return Map.of();
    }

    default Map<String, SemanticModel> importedSemanticModels() {
        return Map.of();
    }

    default Vocabulary vocabulary() {
        return null;
    }

    default String[] ruleNames() {
        return null;
    }

    void analyze(ProgramNodeAST tree, SymbolTable symbolTable, List<CompilationError> errors);

    SemanticModel semanticModel();

    void generateIntermediate(ProgramNodeAST tree, SymbolTable symbolTable, C3DGenerator generator);

    default List<String> loadImports(ProgramNodeAST tree, SymbolTable symbolTable,
                                     C3DGenerator generator, List<CompilationError> errors) {
        return loadImports(tree, symbolTable, generator, errors, new ArrayList<>());
    }

    default List<String> loadImports(ProgramNodeAST tree, SymbolTable symbolTable,
                                     C3DGenerator generator, List<CompilationError> errors,
                                     List<String> yaCargados) {
        return List.of();
    }

    default void setWorkingDirectory(java.io.File directory) {
    }

    default void setProjectDirectory(java.io.File directory) {
    }

    default void setSourceFileName(String fileName) {
    }
}
