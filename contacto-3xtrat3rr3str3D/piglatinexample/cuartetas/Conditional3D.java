/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.cuartetas;

import igriega.piglatin.antlr4.access.LabelAccess;
import igriega.piglatin.antlr4.access.MemoryAccess;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class Conditional3D extends Cuarteta{
    private MemoryAccess right;
    private MemoryAccess left;
    private Integer label;
    private String operator;

    @Override
    public void toCCode(StringBuilder sb) {
        sb.append("if(");
        right.toCCode(sb);
        sb.append(operator);
        left.toCCode(sb);
        sb.append(") goto et").append(label).append(";\n");
    }
}
