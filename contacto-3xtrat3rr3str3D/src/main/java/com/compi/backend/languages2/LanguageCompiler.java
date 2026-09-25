package com.compi.backend.languages2;

import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.symbols.SymbolTable;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Contrato que debe cumplir cada lenguaje soportado.
 *
 * El flujo completo es:
 * <pre>
 *   source --parse()--> ParseTree --analyze()--> SymbolTable + errores
 *          --generateIntermediate()--> C3DGenerator (cuartetas)
 * </pre>
 *
 * Cada implementacion reutiliza los visitantes que ya existen en
 * {@code com.compi.backend.languages.*} (semantico y C3D) y aporta unicamente
 * el pegamento con el lexer/parser de ANTLR de su gramática.
 */
public interface LanguageCompiler {

    /** Identificador corto: pig, y, zet. */
    String id();

    /** Nombre legible para la interfaz. */
    String displayName();

    /**
     * Extensiones de archivo (.pig, .y, .z) que el frontend puede abrir.
     *
     * <p>Es la unica lista que decide que archivos se aceptan, asi que anadir
     * un lenguaje con una extension nueva no obliga a tocar la interfaz.</p>
     */
    List<String> extensions();

    /**
     * Fase 1: analisis lexico y sintactico.
     *
     * @param source texto fuente
     * @param errors lista donde se acumulan los errores LEXICO / SINTACTICO
     * @return arbol de parseo o null si fallo
     */
    ParseTree parse(String source, List<CompilationError> errors);

    /**
     * Fase 2: analisis semantico. Llena la tabla de simbolos y añade
     * errores SEMANTICO a la lista.
     */
    void analyze(ParseTree tree, SymbolTable symbolTable, List<CompilationError> errors);

    /**
     * Fase 3: generacion de codigo de tres direcciones a partir del arbol.
     */
    void generateIntermediate(ParseTree tree, SymbolTable symbolTable, C3DGenerator generator);
}
