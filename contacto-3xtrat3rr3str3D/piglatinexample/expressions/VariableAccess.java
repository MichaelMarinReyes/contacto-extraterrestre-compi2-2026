/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.expressions;

import igriega.piglatin.antlr4.access.MemoryAccess;
import igriega.piglatin.utils.IntermediateCodeUtils;
import igriega.piglatin.utils.PigLatinTranslater;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class VariableAccess extends Expression{
    private String id;

    @Override
    public void toPigLatin(StringBuffer sb) {
        sb.append(PigLatinTranslater.convert(id));
    }

    @Override
    public MemoryAccess toIntermediateCode(IntermediateCodeUtils u) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
