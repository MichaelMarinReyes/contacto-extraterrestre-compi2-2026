/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.antlr4.access;

import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class TemporalAccess extends MemoryAccess{
    private Integer temp;

    @Override
    public void toCCode(StringBuilder sb) {
        sb.append("t").append(temp);
    }
}
