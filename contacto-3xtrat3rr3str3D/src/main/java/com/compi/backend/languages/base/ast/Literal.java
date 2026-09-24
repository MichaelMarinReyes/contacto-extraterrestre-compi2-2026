package com.compi.backend.languages.base.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Literal extends NodeAST {
    private Object value;

    public Literal(Object value, int line, int column) {
        super(line, column);
        this.value = value;
    }
}
