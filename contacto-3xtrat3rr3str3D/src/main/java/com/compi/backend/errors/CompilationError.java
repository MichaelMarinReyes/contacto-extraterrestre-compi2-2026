package com.compi.backend.errors;

public class CompilationError {
    private final ErrorType type;
    private final String message;
    private final int line;
    private final int column;

    public CompilationError(ErrorType type, String message, int line, int column) {
        this.type = type;
        this.message = message;
        this.line = line;
        this.column = column;
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

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("[%s] Línea %d, Columna %d: %s", getType(), line, column, message);
    }
}
