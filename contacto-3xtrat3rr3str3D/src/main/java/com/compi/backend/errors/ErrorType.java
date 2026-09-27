package com.compi.backend.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    LEXICAL("LEXICO"),
    SYNTACTIC("SINTACTICO"),
    SEMANTIC("SEMANTICO");

    private final String tag;

    @Override
    public String toString() {
        return tag;
    }
}
