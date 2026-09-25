/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.statements;

import igriega.piglatin.cuartetas.Cuarteta;
import igriega.piglatin.expressions.Expression;
import igriega.piglatin.utils.IntermediateCodeUtils;
import igriega.piglatin.utils.PigLatinTranslater;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class Assignation extends Statement{
    private String id;
    private Expression expression;

    @Override
    public void toPigLatin(StringBuffer sb) {
        sb.append(PigLatinTranslater.convert(id))
                .append(" = ");
        expression.toPigLatin(sb);
        sb.append(";\n");
    }

    @Override
    public void toIntermediateCode(IntermediateCodeUtils u) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
