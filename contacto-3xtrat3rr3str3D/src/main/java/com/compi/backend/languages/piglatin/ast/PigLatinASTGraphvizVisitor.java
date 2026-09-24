package com.compi.backend.languages.piglatin.ast;

import com.compi.backend.languages.piglatin.ast.expressions.*;
import com.compi.backend.languages.piglatin.ast.sentence.*;
import com.compi.backend.utils.GraphvizASTGenerator;

public class PigLatinASTGraphvizVisitor implements PigLatinVisitorCustom {
    private final GraphvizASTGenerator g = new GraphvizASTGenerator();

    @Override public Object visit(PigLatinRoot node, Object arg){
        g.addNode(node,"PigLatinRoot");
        if(node.getSentences()!=null) node.getSentences().forEach(s->{ g.addEdge(node,s); s.accept(this,null); });
        return g.getDot();
    }
    @Override public Object visit(ArithmeticOperationPig node, Object arg){ g.addNode(node,"ArithOp"); return null; }
    @Override public Object visit(LiteralPig node, Object arg){ g.addNode(node,"Literal"); return null; }
    @Override public Object visit(Declaration node, Object arg){ g.addNode(node,"Decl"); return null; }
    @Override public Object visit(IfSentence node, Object arg){ g.addNode(node,"If"); return null; }
    @Override public Object visit(AccessArrayPig node, Object arg){ g.addNode(node,"AccessArray"); return null; }
    @Override public Object visit(FunctionCallPig node, Object arg){ g.addNode(node,"FuncCall"); return null; }
    @Override public Object visit(IdentifierPig node, Object arg){ g.addNode(node,"Identifier"); return null; }
    @Override public Object visit(InstantiationStructurePig node, Object arg){ g.addNode(node,"InstStruct"); return null; }
    @Override public Object visit(LiteralArrayPig node, Object arg){ g.addNode(node,"LiteralArray"); return null; }
    @Override public Object visit(LogicalOperationPig node, Object arg){ g.addNode(node,"LogicalOp"); return null; }
    @Override public Object visit(MemberAccessPig node, Object arg){ g.addNode(node,"MemberAccess"); return null; }
    @Override public Object visit(RelationalOperationPig node, Object arg){ g.addNode(node,"RelOp"); return null; }
    @Override public Object visit(ArrayDeclaration node, Object arg){ g.addNode(node,"ArrayDecl"); return null; }
    @Override public Object visit(DumLoop node, Object arg){ g.addNode(node,"Dum"); return null; }
    @Override public Object visit(FacereLoop node, Object arg){ g.addNode(node,"Facere"); return null; }
    @Override public Object visit(ImportSentence node, Object arg){ g.addNode(node,"Import"); return null; }
    @Override public Object visit(JumpSentence node, Object arg){ g.addNode(node,"Jump"); return null; }
    @Override public Object visit(PerLoop node, Object arg){ g.addNode(node,"Per"); return null; }
    @Override public Object visit(PrintSentence node, Object arg){ g.addNode(node,"Print"); return null; }
    @Override public Object visit(ReadSentence node, Object arg){ g.addNode(node,"Read"); return null; }
    @Override public Object visit(SentenceAssignment node, Object arg){ g.addNode(node,"Assign"); return null; }
}
