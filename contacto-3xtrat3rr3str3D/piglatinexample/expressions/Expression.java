/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.expressions;

import igriega.piglatin.antlr4.access.MemoryAccess;
import igriega.piglatin.cuartetas.Cuarteta;
import igriega.piglatin.utils.IntermediateCodeUtils;
import igriega.piglatin.visitors.AstNode;
import java.util.List;

/**
 *
 * @author blue-dragon
 */
public abstract class Expression implements AstNode{
    public abstract MemoryAccess toIntermediateCode(IntermediateCodeUtils u);
}
