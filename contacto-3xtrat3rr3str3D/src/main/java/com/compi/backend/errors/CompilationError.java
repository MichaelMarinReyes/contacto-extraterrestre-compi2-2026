package com.compi.backend.errors;

/**
 * Error detectado por el compilador.
 *
 * <p>Al compilar un proyecto entero los errores de todos los archivos se juntan
 * en la misma tabla, asi que cada error lleva el nombre del archivo donde se
 * produjo. Ese dato se rellena despues, en la fachada, porque el backend
 * compila un texto sin saber de que archivo viene.</p>
 */
public class CompilationError {

    private final ErrorType type;
    private final String message;
    private final int line;
    private final int column;
    private String fileName;

    /**
     * @param line    linea en base 1, como las cuenta ANTLR
     * @param column  columna en base 0, como las cuenta ANTLR
     */
    public CompilationError(ErrorType type, String message, int line, int column) {
        this.type = type;
        this.message = message;
        this.line = line;
        // La columna se pasa en base 0 porque es lo que devuelve
        // Token.getCharPositionInLine(), pero se guarda en base 1: el primer
        // caracter de una linea es la columna 1, no la columna 0, que no existe
        // para quien lee la tabla de errores. Convertirlo aqui, una sola vez,
        // evita tener que acordarse en cada punto de entrada. Una columna
        // negativa significa "no se sabe" y se respeta tal cual.
        this.column = column < 0 ? -1 : column + 1;
    }

    public ErrorType getErrorType() {
        return type;
    }

    public String getType() {
        return type != null ? type.name() : "DESCONOCIDO";
    }

    public String getMessage() {
        return message;
    }

    /** Linea en base 1, o 0 si el error no tiene posicion en el fuente. */
    public int getLine() {
        return line;
    }

    /**
     * Columna en base 1, o -1 si no se sabe.
     *
     * <p>El constructor ya sumo el uno, asi que llega como 1 el primer caracter
     * de una linea.</p>
     */
    public int getColumn() {
        return column;
    }

    /** Nombre del archivo donde se produjo, o null si no se sabe. */
    public String getFileName() {
        return fileName;
    }

    /**
     * Anota el archivo de origen.
     *
     * @return este mismo error, para encadenar la llamada
     */
    public CompilationError inFile(String name) {
        this.fileName = name;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (fileName != null) {
            sb.append(fileName).append(": ");
        }
        sb.append('[').append(getType()).append("] ");
        if (line > 0) {
            // Un error sin posicion en el fuente (no se pudo leer el archivo, por
            // ejemplo) no puede inventarse una linea.
            sb.append("Línea ").append(line).append(", Columna ")
                    .append(column > 0 ? column : "?").append(": ");
        }
        return sb.append(message).toString();
    }
}
