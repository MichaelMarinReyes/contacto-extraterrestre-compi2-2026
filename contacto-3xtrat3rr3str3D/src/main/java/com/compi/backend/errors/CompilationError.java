package com.compi.backend.errors;

import lombok.Getter;

@Getter
public class CompilationError {

    private final ErrorType type;
    private final String message;
    private final int line;
    private final int column;
    private String fileName;

    public CompilationError(ErrorType type, String message, int line, int column) {
        this.type = type;
        this.message = message;
        this.line = line;
        this.column = column < 0 ? -1 : column + 1;
    }

    public ErrorType getErrorType() {
        return type;
    }

    public String getType() {
        return type != null ? type.getTag() : "DESCONOCIDO";
    }

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

            sb.append("Línea ").append(line).append(", Columna ")
                    .append(column > 0 ? column : "?").append(": ");
        }
        return sb.append(message).toString();
    }
}
