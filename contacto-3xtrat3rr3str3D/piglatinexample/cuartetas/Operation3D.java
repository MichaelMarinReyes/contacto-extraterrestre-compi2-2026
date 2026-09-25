/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.cuartetas;

import igriega.piglatin.antlr4.access.MemoryAccess;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class Operation3D extends Cuarteta{
    private MemoryAccess assignable;
    private MemoryAccess first;
    private MemoryAccess second;
    private String operation;

    @Override
    public void toCCode(StringBuilder sb) {
        assignable.toCCode(sb);
        sb.append("=");
        first.toCCode(sb);
        sb.append(operation);
        second.toCCode(sb);
        sb.append(";\n");
    }
}
