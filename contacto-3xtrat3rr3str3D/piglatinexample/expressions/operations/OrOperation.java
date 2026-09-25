/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.expressions.operations;

import igriega.piglatin.antlr4.access.LabelAccess;
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
public class OrOperation extends Operation{

    public OrOperation(Expression left, Expression right) {
        super(left, right, null);
    }

    @Override
    public MemoryAccess toIntermediateCode(IntermediateCodeUtils u) {
        MemoryAccess firstMemory = left.toIntermediateCode(u);
        IdsCounter c = u.getCounter();
        int globalTrue = c.getAndIncrementLabelCount();
        int globalFalse = c.getAndIncrementLabelCount();
        int needVerification = c.getAndIncrementLabelCount();
        int exitLabel = c.getAndIncrementLabelCount();
        int temporalResult = c.getAndIncrementTemporalCount();
        
        List<Cuarteta> l = u.getCuartetas();
        l.add(new Conditional3D(firstMemory, new Literal3D(1), globalTrue, "=="));
        l.add(new Goto3D(needVerification));
        
        l.add(new LabelDefinition3D(needVerification));
        MemoryAccess secondMemory = right.toIntermediateCode(u);
        l.add(new Conditional3D(secondMemory, new Literal3D(1), globalTrue, "=="));
        l.add(new Goto3D(globalFalse));
        
        //global true
        l.add(new LabelDefinition3D(globalTrue));
        l.add(new Assignation3D(new TemporalAccess(temporalResult), new Literal3D(1)));
        l.add(new Goto3D(exitLabel));
        
        //global false
        l.add(new LabelDefinition3D(globalFalse));
        l.add(new Assignation3D(new TemporalAccess(temporalResult), new Literal3D(0)));
        l.add(new Goto3D(exitLabel));
        
        l.add(new LabelDefinition3D(exitLabel));
        return new TemporalAccess(temporalResult);
    }
    
    
}
