package com.compi.backend.languages.piglatin.ast.expressions;

import com.compi.backend.languages.piglatin.ast.NodeASTPig;
import com.compi.backend.languages.piglatin.ast.PigLatinVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class InstantiationStructurePig extends NodeASTPig {
    private String structName;
    private Map<String, NodeASTPig> fieldValues;

    public InstantiationStructurePig(String structName, Map<String, NodeASTPig> fieldValues, int line, int column) {
        super(line, column);
        this.structName = structName;
        this.fieldValues = fieldValues;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
