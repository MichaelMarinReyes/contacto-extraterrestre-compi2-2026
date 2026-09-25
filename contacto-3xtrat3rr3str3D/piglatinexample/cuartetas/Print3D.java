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
public class Print3D extends Cuarteta {
    private MemoryAccess memoryAccess;

    @Override
    public void toCCode(StringBuilder sb) {
        sb.append("printf(\"%d\", "); 
        memoryAccess.toCCode(sb);    
        sb.append(");\n");
    }
}
