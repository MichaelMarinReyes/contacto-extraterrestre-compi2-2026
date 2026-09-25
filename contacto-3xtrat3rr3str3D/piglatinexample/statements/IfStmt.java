/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.statements;

import igriega.piglatin.antlr4.access.Literal3D;
import igriega.piglatin.antlr4.access.MemoryAccess;
import igriega.piglatin.cuartetas.Conditional3D;
import igriega.piglatin.cuartetas.Cuarteta;
import igriega.piglatin.cuartetas.Goto3D;
import igriega.piglatin.cuartetas.LabelDefinition3D;
import igriega.piglatin.visitors.CanAddStmt;
import igriega.piglatin.expressions.Expression;
import igriega.piglatin.utils.IdsCounter;
import igriega.piglatin.utils.IntermediateCodeUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author blue-dragon
 */
public class IfStmt extends Statement implements CanAddStmt{
    private Expression condition;
    private List<Statement> internalInstructions;
    
    public IfStmt(Expression condition){
        this.condition = condition;
        internalInstructions = new ArrayList<>();
    }
    
    @Override
    public void addStatement(Statement statement) {
        internalInstructions.add(statement);
    }

    @Override
    public void toPigLatin(StringBuffer sb) {
        sb.append("isay(");
        condition.toPigLatin(sb);
        sb.append("){\n");
        for (Statement internalInstruction : internalInstructions) {
            internalInstruction.toPigLatin(sb);
        }
        sb.append("}\n");
    }

    @Override
    public void toIntermediateCode(IntermediateCodeUtils u) {
        MemoryAccess conditionAccess = condition.toIntermediateCode(u);
        
        IdsCounter c = u.getCounter();
        int trueLabel = c.getAndIncrementLabelCount();
        int falseLabel = c.getAndIncrementLabelCount();
        int finalLabel = c.getAndIncrementLabelCount();
        
        List<Cuarteta> l = u.getCuartetas();
        l.add(new Conditional3D(conditionAccess, new Literal3D(1), trueLabel, "==") );
        l.add(new Goto3D(falseLabel) );
        //when its true
        l.add(new LabelDefinition3D(trueLabel));
        for (Statement stmt : internalInstructions) {
            stmt.toIntermediateCode(u);
        }
        l.add(new Goto3D(finalLabel));
        //when its false
        l.add(new LabelDefinition3D(falseLabel));
                    //hint: you can add an else case here
        
        l.add(new LabelDefinition3D(finalLabel));
    }
    
}
