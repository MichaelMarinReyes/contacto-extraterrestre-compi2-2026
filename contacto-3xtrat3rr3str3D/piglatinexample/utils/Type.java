/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.utils;

import igriega.piglatin.visitors.AstNode;
import lombok.AllArgsConstructor;

/**
 *
 * @author blue-dragon
 */
@AllArgsConstructor
public class Type implements AstNode{
    private String type;

    @Override
    public void toPigLatin(StringBuffer sb) {
        sb.append(PigLatinTranslater.convert(type));
    }
}
