/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin;

import igriega.piglatin.antlr4.PigLatinLexer;
import igriega.piglatin.antlr4.PigLatinParser;
import igriega.piglatin.utils.CCodeGenerator;
import igriega.piglatin.utils.IntermediateCodeUtils;
import igriega.piglatin.visitors.Ast;
import igriega.piglatin.visitors.PigLatinAstVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author blue-dragon
 */
public class IntermediateCode {
    public static void main(String[] args) {
        String input = """ 
                si ( 10 != 10 || 10 == 10) {
                    >> 10000 ;
                } finis;
                """;
        
        // Lexer
        PigLatinLexer lexer = new PigLatinLexer(
                CharStreams.fromString(input)
        );

        // Parser
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PigLatinParser parser = new PigLatinParser(tokens);

        // Regla inicial
        ParseTree tree = parser.program();

        // usar el visitor
        PigLatinAstVisitor visitor = new PigLatinAstVisitor();
        Ast ast = (Ast) visitor.visit(tree);
        
        //generar codigo 3d
        IntermediateCodeUtils u = new IntermediateCodeUtils();
        ast.toIntermediateCode(u);
        
        CCodeGenerator gen = new CCodeGenerator();
        gen.generateCIntermediateCode(u);
        
    }
}
