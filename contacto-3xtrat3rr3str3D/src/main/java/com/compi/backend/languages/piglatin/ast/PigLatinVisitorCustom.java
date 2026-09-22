package com.compi.backend.languages.piglatin.ast;

import com.compi.backend.languages.piglatin.ast.expressions.*;
import com.compi.backend.languages.piglatin.ast.sentence.*;

public interface PigLatinVisitorCustom {
    Object visit(ArithmeticOperationPig node, Object data);

    Object visit(LiteralPig node, Object data);

    Object visit(Declaration node, Object data);

    Object visit(IfSentence node, Object data);

    Object visit(PigLatinRoot pigLatinRoot, Object arg);

    Object visit(AccessArrayPig accessArray, Object arg);

    Object visit(FunctionCallPig functionCallPig, Object arg);

    Object visit(IdentifierPig identifierPig, Object arg);

    Object visit(InstantiationStructurePig instantiationStructurePig, Object arg);

    Object visit(LiteralArrayPig literalArrayPig, Object arg);

    Object visit(LogicalOperationPig logicalOperationPig, Object arg);

    Object visit(MemberAccessPig memberAccess, Object arg);

    Object visit(RelationalOperationPig relationalOperationPig, Object arg);

    Object visit(ArrayDeclaration arrayDeclaration, Object arg);

    Object visit(DumLoop dumLoop, Object arg);

    Object visit(FacereLoop facereLoop, Object arg);

    Object visit(ImportSentence importSentence, Object arg);

    Object visit(JumpSentence jumpSentence, Object arg);

    Object visit(PerLoop perLoop, Object arg);

    Object visit(PrintSentence printSentence, Object arg);

    Object visit(ReadSentence readSentence, Object arg);

    Object visit(SentenceAssignment sentenceAssignment, Object arg);
}