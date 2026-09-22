package com.compi.backend.languages.y.ast.statements;

import com.compi.backend.languages.y.ast.NodeASTY;
import com.compi.backend.languages.y.ast.YVisitorCustom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VariableDeclarationY extends NodeASTY {
    private String dataType;
    private String name;
    private List<NodeASTY> dimensions;
    private NodeASTY initializer;

    @Builder
    public VariableDeclarationY(String dataType, String name, List<NodeASTY> dimensions, NodeASTY initializer, int line, int column) {
        super(line, column);
        this.dataType = dataType;
        this.name = name;
        this.dimensions = dimensions;
        this.initializer = initializer;
    }

    @Override
    public Object accept(YVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}
