/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.statements;

import igriega.piglatin.cuartetas.Cuarteta;
import igriega.piglatin.utils.IntermediateCodeUtils;
import igriega.piglatin.visitors.AstNode;
import java.util.List;

/**
 *
 * @author blue-dragon
 */
public abstract class Statement implements AstNode{
   public abstract void toIntermediateCode(IntermediateCodeUtils u);
}
