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
public class BooleanOperation extends Operation{

    public BooleanOperation(Expression left, Expression right, String operator) {
        super(left, right, operator);
    }

    @Override
    public MemoryAccess toIntermediateCode(IntermediateCodeUtils u) {
        
        MemoryAccess leftMemory = left.toIntermediateCode(u);
        MemoryAccess rightMemory = right.toIntermediateCode(u);
        
        IdsCounter c = u.getCounter();
        int trueLabel = c.getAndIncrementLabelCount();
        int falseLabel = c.getAndIncrementLabelCount();
        int finalLabel = c.getAndIncrementLabelCount();
        int temporalResult = c.getAndIncrementTemporalCount();
        
        List<Cuarteta> l = u.getCuartetas();
        l.add(new Conditional3D(rightMemory, leftMemory, trueLabel, operator));
        l.add(new Goto3D(falseLabel) );
        //when its true
        l.add(new LabelDefinition3D(trueLabel));
        l.add(new Assignation3D(new TemporalAccess(temporalResult), new Literal3D(1)));
        l.add(new Goto3D(finalLabel));
        //when its false
        l.add(new LabelDefinition3D(falseLabel));
        l.add(new Assignation3D(new TemporalAccess(temporalResult), new Literal3D(0)));
        l.add(new Goto3D(finalLabel));
        
        l.add(new LabelDefinition3D(finalLabel));
        return new TemporalAccess(temporalResult);
    }
    
}
