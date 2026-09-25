/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package igriega.piglatin;

import igriega.piglatin.antlr4.PigLatinLexer;
import igriega.piglatin.antlr4.PigLatinParser;
import igriega.piglatin.visitors.Ast;
import igriega.piglatin.visitors.PigLatinAstVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 *
 * @author blue-dragon
 */
public class PigLatin {

    public static void main(String[] args) {
        String input = """
                esto x : numerus 0;       
                si (x > 10) {
                    esto resultado : numerus x + 5;
                    x = x + 5 ;
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

        // Mostrar árbol
        //System.out.println(Trees.toStringTree(tree));
        //System.out.println(tree.toStringTree(parser));
        
        
        // usar el visitor
        PigLatinAstVisitor visitor = new PigLatinAstVisitor();
        Ast ast = (Ast) visitor.visit(tree);
        
        StringBuffer sb = new StringBuffer();
        ast.toPigLatin(sb);
        System.out.println("""
                           ----------------------------------
                                    This is Pig Latin
                                Nothig is suspicious here
                           ----------------------------------
                           ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                           ⠀⠀⠀⠀⣠⣾⣿⣿⣦⠀⠀⠀⠀⠀⢰⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀
                           ⠀⠀⠀⠀⣿⣿⣿⣿⣿⣤⣴⣶⣶⣶⣾⣿⣿⣿⣿⠟⠀⠀⠀⠀⠀⠀⠀⠀
                           ⠀⠀⠀⠀⢈⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣄⡀⠀⠀⠀⠀⠀
                           ⠀⠀⠀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀⠀⠀⠀
                           ⠀⢀⣾⣿⣿⣿⣧⣤⣤⣤⣿⣿⣿⣿⣿⣿⣿⣤⣤⣤⣬⣿⣿⣿⣿⣄⠀⠀
                           ⢀⣿⣿⣿⣿⠿⠟⠻⢿⣿⡿⠛⠉⠉⠉⠛⠻⣿⡿⠿⠛⠿⣿⣿⣿⣿⣦⠀
                           ⢸⣿⣿⣿⠃⠀⠀⣠⣄⠉⠀⠀⣀⠀⢀⡀⠀⠈⢀⣤⡀⠀⠈⢿⣿⣿⣿⡆
                           ⢸⣿⣿⣿⡀⠀⠀⠙⠋⠀⠀⢸⣿⠇⢿⡗⠀⠀⠈⠋⠁⠀⠀⣼⣿⣿⣿⡇
                           ⠸⣿⣿⣿⣷⣤⣄⣠⣴⣦⠀⠀⠀⠀⠀⠀⠀⣠⣦⣤⣀⣤⣼⣿⣿⣿⣿⡇
                           ⠀⠹⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣤⣀⣠⣤⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠀
                           ⠀⠀⠙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠀⠀
                           ⠀⠀⠀⠀⠙⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠋⠁⠀⠀⠀
                           ⠀⠀⠀⠀⠀⠀⠀⠈⠙⠛⠛⠻⠿⠿⠿⠿⠿⠛⠛⠛⠉⠀⠀⠀⠀⠀⠀⠀
                           """);
        System.out.println(sb.toString());
    
    }
}
