package com.compi.backend.languages.piglatin.ast.sentence;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrayDeclaration extends NodeASTPig {
    private String name;
    private String dataType;
    private NodeASTPig expression;

    @Builder
    public ArrayDeclaration(String name, String dataType, NodeASTPig expression, int line, int column) {
        super(line, column);
        this.name = name;
        this.dataType = dataType;
        this.expression = expression;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
