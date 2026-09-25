/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.cuartetas;

import igriega.piglatin.antlr4.access.LabelAccess;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class Goto3D extends Cuarteta{
    private Integer label;

    @Override
    public void toCCode(StringBuilder sb) {
        sb.append("goto et").append(label).append(";\n");
    }
}
