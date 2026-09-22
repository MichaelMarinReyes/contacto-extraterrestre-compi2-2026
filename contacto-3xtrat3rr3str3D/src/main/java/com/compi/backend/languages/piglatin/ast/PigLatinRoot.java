package com.compi.backend.languages.piglatin.ast;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PigLatinRoot extends NodeASTPig {
    private List<NodeASTPig> sentences;

    public PigLatinRoot(List<NodeASTPig> sentences, int line, int column) {
        super(line, column);
        this.sentences = sentences;
    }

    @Override
    public Object accept(PigLatinVisitorCustom visitor, Object arg) {
        return visitor.visit(this, arg);
    }
}