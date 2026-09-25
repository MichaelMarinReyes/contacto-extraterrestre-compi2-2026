/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.expressions.operations;

import igriega.piglatin.antlr4.access.MemoryAccess;
import igriega.piglatin.antlr4.access.TemporalAccess;
import igriega.piglatin.cuartetas.Operation3D;
import igriega.piglatin.expressions.Expression;
import igriega.piglatin.utils.IntermediateCodeUtils;

/**
 *
 * @author blue-dragon
 */
public class AritmeticOperation extends Operation{

    public AritmeticOperation(Expression left, Expression right, String operator) {
        super(left, right, operator);
    }

    @Override
    public MemoryAccess toIntermediateCode(IntermediateCodeUtils u) {
        MemoryAccess leftMemoryAccess = left.toIntermediateCode(u);
        MemoryAccess rightMemoryAccess = right.toIntermediateCode(u);
        int temp = u.getCounter().getAndIncrementTemporalCount();
        u.getCuartetas().add(
                new Operation3D(
                        new TemporalAccess(temp), 
                        leftMemoryAccess, 
                        rightMemoryAccess, 
                        operator
                )
        );
        return new TemporalAccess(temp);
    }
    
}
