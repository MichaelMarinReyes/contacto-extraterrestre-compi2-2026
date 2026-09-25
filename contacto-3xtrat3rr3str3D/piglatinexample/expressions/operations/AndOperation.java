/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.expressions.operations;

import igriega.piglatin.antlr4.access.Literal3D;
import igriega.piglatin.antlr4.access.MemoryAccess;
import igriega.piglatin.antlr4.access.TemporalAccess;
import igriega.piglatin.cuartetas.Assignation3D;
import igriega.piglatin.cuartetas.Conditional3D;
import igriega.piglatin.cuartetas.Cuarteta;
import igriega.piglatin.cuartetas.Goto3D;
import igriega.piglatin.cuartetas.LabelDefinition3D;
import igriega.piglatin.expressions.Expression;
import igriega.piglatin.utils.IdsCounter;
import igriega.piglatin.utils.IntermediateCodeUtils;
import java.util.List;

/**
 *
 * @author blue-dragon
 */
public class AndOperation extends Operation{

    public AndOperation(Expression left, Expression right) {
        super(left, right, null);
    }

    @Override
    public MemoryAccess toIntermediateCode(IntermediateCodeUtils u) {
        
        MemoryAccess leftAccess = left.toIntermediateCode(u);
  
        IdsCounter c = u.getCounter();
        int temporalResult = c.getAndIncrementTemporalCount();
                
        int globalTrueLabel = c.getAndIncrementLabelCount();
        int globalFalseLabel = c.getAndIncrementLabelCount();
        int needEvaluationLabel = c.getAndIncrementLabelCount();
        int globalEndLabel = c.getAndIncrementLabelCount();
        
        List<Cuarteta> l = u.getCuartetas();
        l.add(new Conditional3D(leftAccess, new Literal3D(1), needEvaluationLabel, "=="));
        l.add(new Goto3D(globalFalseLabel));
        
        l.add(new LabelDefinition3D(needEvaluationLabel));
        MemoryAccess rightAccess = right.toIntermediateCode(u);
        l.add(new Conditional3D(rightAccess, new Literal3D(1), globalTrueLabel, "=="));
        l.add(new Goto3D(globalFalseLabel));
        
        //when its true
        l.add(new LabelDefinition3D(globalTrueLabel));
        l.add(new Assignation3D(new TemporalAccess(temporalResult), new Literal3D(1)));
        l.add(new Goto3D(globalEndLabel));
        //when its false
        l.add(new LabelDefinition3D(globalFalseLabel));
        l.add(new Assignation3D(new TemporalAccess(temporalResult), new Literal3D(0)));
        l.add(new Goto3D(globalEndLabel));
        
        l.add(new LabelDefinition3D(globalEndLabel));
        return new TemporalAccess(temporalResult);
    }
    
}
