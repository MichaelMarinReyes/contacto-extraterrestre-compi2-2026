/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.visitors;

import igriega.piglatin.statements.Statement;
import igriega.piglatin.utils.IntermediateCodeUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author blue-dragon
 */
public class Ast implements AstNode, CanAddStmt{
    private List<Statement> statements;
    
    public Ast(){
        this.statements = new ArrayList<>();
    }
    
    @Override
    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void toPigLatin(StringBuffer sb) {
        for (Statement statement : statements) {
            statement.toPigLatin(sb);
        }
    }
    
    public void toIntermediateCode(IntermediateCodeUtils u){
        for (Statement statement : statements) {
            statement.toIntermediateCode(u);
        }
    }
    
    
}
