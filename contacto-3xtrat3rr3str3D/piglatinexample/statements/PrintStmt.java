/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.statements;

import igriega.piglatin.antlr4.access.MemoryAccess;
import igriega.piglatin.cuartetas.Cuarteta;
import igriega.piglatin.cuartetas.Print3D;
import igriega.piglatin.expressions.Expression;
import igriega.piglatin.utils.IntermediateCodeUtils;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class PrintStmt extends Statement{
    private Expression expression;

    @Override
    public void toPigLatin(StringBuffer sb) {
        sb.append("%OINK");
        expression.toPigLatin(sb);
    }

    @Override
    public void toIntermediateCode(IntermediateCodeUtils u) {
        MemoryAccess m = expression.toIntermediateCode(u);
        u.getCuartetas().add(new Print3D(m));
    }
}
