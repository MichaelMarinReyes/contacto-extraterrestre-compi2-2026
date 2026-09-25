/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.expressions;

import igriega.piglatin.antlr4.access.Literal3D;
import igriega.piglatin.antlr4.access.MemoryAccess;
import igriega.piglatin.utils.IntermediateCodeUtils;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class Literal extends Expression{
    private String type;
    private Object content;

    @Override
    public void toPigLatin(StringBuffer sb) {
        sb.append(content.toString());
    }

    @Override
    public MemoryAccess toIntermediateCode(IntermediateCodeUtils u) {
        return new Literal3D(content);
    }
}
