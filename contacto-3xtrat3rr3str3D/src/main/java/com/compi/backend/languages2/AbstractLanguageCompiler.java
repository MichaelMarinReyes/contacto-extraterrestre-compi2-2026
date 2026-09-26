package com.compi.backend.languages2;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostico;
import com.compi.backend.errors.ErrorType;
import java.io.File;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Base comun para los compiladores de lenguaje.
 *
 * Centraliza la creacion del listener de ANTLR que traduce los errores del
 * reconocedor en {@link CompilationError} con el {@link ErrorType} correcto
 * (LEXICO para el lexer, SINTACTICO para el parser) y con el texto en espanol
 * que usa todo el proyecto ({@link Diagnostico}).
 */
public abstract class AbstractLanguageCompiler implements LanguageCompiler {

    /** Carpeta del proyecto: contra ella se resuelven los imports. */
    private File workingDirectory = new File(".");

    /** Vocabulario del parser del ultimo parseo, para nombrar los tokens. */
    private Vocabulary vocabulary;

    /** Nombres de las reglas del parser del ultimo parseo. */
    private String[] ruleNames;

    @Override
    public void setWorkingDirectory(File directory) {
        this.workingDirectory = directory == null ? new File(".") : directory;
    }

    protected File getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public Vocabulary vocabulary() {
        return vocabulary;
    }

    @Override
    public String[] ruleNames() {
        return ruleNames;
    }

    /**
     * Aparta el vocabulario y los nombres de regla del parser.
     *
     * <p>Es lo que permite poner nombre a los simbolos del trazo de la pila: el
     * token que se desplaza con su nombre de gramatica (VARIABLE) y la regla a la
     * que se reduce con el suyo (declaracion).</p>
     */
    protected void rememberParser(Parser parser) {
        this.vocabulary = parser.getVocabulary();
        this.ruleNames = parser.getRuleNames();
    }

    /**
     * Crea un listener que acumula errores en la lista indicada, ya traducidos al
     * español del proyecto.
     *
     * <p>Se pasa el vocabulario del reconocedor porque es lo que permite escribir
     * «import» en vez del nombre interno {@code IMPORT} que suelta ANTLR.</p>
     *
     * @param vocabulario vocabulario del lexer o del parser que va a escuchar
     * @param errors      destino de los errores
     * @param type        tipo a asignar a cada error capturado
     */
    protected BaseErrorListener errorCollector(Vocabulary vocabulario,
                                               List<CompilationError> errors,
                                               ErrorType type) {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg,
                                    RecognitionException e) {
                errors.add(new CompilationError(type,
                        Diagnostico.desdeAntlr(vocabulario, msg,
                                offendingSymbol instanceof Token token ? token : null),
                        line, charPositionInLine));
            }
        };
    }

    /** Desconecta los listeners por defecto e instala el nuestro. */
    protected void installErrorListener(Lexer lexer, BaseErrorListener listener) {
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);
    }

    /** Desconecta los listeners por defecto e instala el nuestro. */
    protected void installErrorListener(Parser parser, BaseErrorListener listener) {
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
    }

    /** Traduce una excepcion inesperada en error semantico, sin posicion. */
    protected CompilationError internalError(String language, Exception e) {
        return internalError(language, e, null);
    }

    /**
     * Traduce una excepcion inesperada en error semantico.
     *
     * <p>Si se pasó el arbol al que le paso, el error se anota en su primer token:
     * un fallo interno con linea dice mucho mas que uno sin linea, que en la
     * tabla no dice donde mirar.</p>
     */
    protected CompilationError internalError(String language, Exception e, ParseTree tree) {
        String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        int line = 0;
        int column = 0;
        if (tree instanceof ParserRuleContext ctx && ctx.getStart() != null) {
            line = ctx.getStart().getLine();
            column = ctx.getStart().getCharPositionInLine();
        }
        return new CompilationError(ErrorType.SEMANTICO,
                "Error interno en " + language + ": " + msg, line, column);
    }
}
