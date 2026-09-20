package com.compi.backend.languages.piglatin.ast;

import com.compi.backend.languages.piglatin.ast.expressions.ArithmeticOperation;
import com.compi.backend.languages.piglatin.ast.expressions.Literal;
import com.compi.backend.languages.piglatin.ast.sentence.Declaration;
import com.compi.backend.languages.piglatin.ast.sentence.IfSentence;

public interface PigLatinVisitorCustom {
    Object visit(ArithmeticOperation node, Object data);
    Object visit(Literal node, Object data);
    Object visit(Declaration node, Object data);
    Object visit(IfSentence node, Object data);
}