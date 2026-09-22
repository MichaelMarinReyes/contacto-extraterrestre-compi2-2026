package com.compi.backend.languages.piglatin.ast.sentence;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Declaration extends NodeASTPig {
    private String dataType;
    private String name;
    private NodeASTPig expression;

    @Builder
    public Declaration(String dataType, String name, NodeASTPig expression, int line, int column) {
        super(line, column);
        this.dataType = dataType;
        this.name = name;
        this.expression = expression;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
