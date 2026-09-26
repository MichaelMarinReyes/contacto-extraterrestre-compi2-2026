package com.compi.backend.languages2;

import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.symbols.SymbolTable;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Vocabulary;
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
     * Vocabulario del parser del ultimo {@link #parse}, con el nombre que la
     * gramatica le da a cada token.
     *
     * <p>Lo necesita el trazo de la pila para distinguir el token que se
     * desplaza ({@code VARIABLE}) de su lexema ({@code x}), porque el lexema solo
     * no alcanza para saber que simbolo se esta moviendo.</p>
     *
     * @return el vocabulario, o null si todavia no se ha parseado nada
     */
    default Vocabulary vocabulary() {
        return null;
    }

    /**
     * Nombres de las reglas del parser del ultimo {@link #parse}, en el orden en
     * que las tiene el parser.
     *
     * <p>Es la forma fiable de saber a que regla pertenece un nodo del arbol: al
     * recorrer una regla recursiva izquierda, ANTLR genera una clase por cada
     * alternativa y esas clases no son reglas de la gramatica, asi que su nombre
     * no sirve. El indice de regla del nodo, en cambio, siempre es el de la regla
     * de verdad.</p>
     *
     * @return los nombres de regla, o null si todavia no se ha parseado nada
     */
    default String[] ruleNames() {
        return null;
    }

    /**
     * Fase 2: analisis semantico. Llena la tabla de simbolos y añade
     * errores SEMANTICO a la lista.
     */
    void analyze(ParseTree tree, SymbolTable symbolTable, List<CompilationError> errors);

    /**
     * Fase 3: generacion de codigo de tres direcciones a partir del arbol.
     */
    void generateIntermediate(ParseTree tree, SymbolTable symbolTable, C3DGenerator generator);

    /**
     * Fase previa a la 2: carga los archivos que el fuente importa.
     *
     * <p>Solo la implementan los lenguajes que tienen la instruccion
     * {@code import} en su gramatica. Lo que se importa se compila en la misma
     * tabla de simbolos y el mismo generador, justo antes de analizar el archivo
     * que importa, para que sus simbolos ya existan cuando se mire el fuente que
     * los usa. Los errores que salgan de un archivo importado se anotan con su
     * nombre.</p>
     *
     * <p>Un lenguaje sin imports no hace nada y devuelve una lista vacia.</p>
     *
     * @return rutas de los archivos que se han cargado, en el orden en que se
     *         cargaron, relativas a la carpeta del proyecto
     */
    default List<String> loadImports(ParseTree tree, SymbolTable symbolTable,
                                     C3DGenerator generator, List<CompilationError> errors) {
        return loadImports(tree, symbolTable, generator, errors, new ArrayList<>());
    }

    /**
     * Igual que {@link #loadImports(ParseTree, SymbolTable, C3DGenerator, List)},
     * pero reutilizando la lista de archivos ya cargados.
     *
     * <p>Un archivo importado puede importar a su vez, y asi la lista de lo
     * cargado es la misma para toda la recursion: asi un archivo que se importa
     * dos veces, o un ciclo de imports, no se carga ni se duplica por el
     * camino.</p>
     *
     * @param yaCargados archivos ya cargados; se amplia con los nuevos
     */
    default List<String> loadImports(ParseTree tree, SymbolTable symbolTable,
                                     C3DGenerator generator, List<CompilationError> errors,
                                     List<String> yaCargados) {
        return List.of();
    }

    /**
     * Carpeta contra la que se resuelven los imports.
     *
     * <p>La pone la fachada antes de compilar: es la raiz del proyecto, que es
     * donde estan los archivos que se importan.</p>
     */
    default void setWorkingDirectory(java.io.File directory) {
    }
}
