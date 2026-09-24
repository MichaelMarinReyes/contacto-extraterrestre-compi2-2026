package com.compi.backend.languages.piglatin.ast.expressions;
import com.compi.backend.languages.piglatin.ast.NodeASTPig;

import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FunctionCallPig extends NodeASTPig {
    private String functionName;
    private List<NodeASTPig> arguments;

    @Builder
    public FunctionCallPig(String functionName, List<NodeASTPig> arguments, int line, int column) {
        super(line, column);
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}