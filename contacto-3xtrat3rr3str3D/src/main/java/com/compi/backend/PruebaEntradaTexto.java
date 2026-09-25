package com.compi.backend;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Arnés de prueba del backend: simula lo que hace el frontend (texto -&gt;
 * compilador -&gt; paneles) sin necesidad de abrir la interfaz gráfica.
 */
public class PruebaEntradaTexto {

    public static void main(String[] args) throws Exception {
        testLang("pig", "src/test/resources/piglatin.prueba");
        testLang("y", "src/test/resources/y.prueba");
        testLang("zet", "src/test/resources/zetariano.prueba");
    }

    private static void testLang(String lang, String path) throws Exception {
        String source = Files.readString(Paths.get(path));
        Compiler compiler = new Compiler();
        boolean ok = compiler.compile(source, lang);

        System.out.println("=== LENGUAJE: " + lang.toUpperCase() + " ===");
        System.out.println("Compiló sin errores: " + ok);
        System.out.println("Errores: " + compiler.getErrors().size());
        for (var e : compiler.getErrors()) {
            System.out.println("   " + e);
        }
        System.out.println("--- Tripletes ---");
        System.out.println(compiler.getTriplets());
        System.out.println("--- Cuartetas ---");
        System.out.println(compiler.getQuadruples());
        System.out.println("--- Tabla de símbolos (" + compiler.getSymbols().size() + ") ---");
        compiler.getSymbols().forEach(s -> System.out.println("   " + s));
        System.out.println("--- Pila de procesos (" + compiler.getStackStates().size() + " estados) ---");
        compiler.getStackStates().stream().limit(8).forEach(s -> System.out.println("   " + s));
        System.out.println("--- C generado (" + compiler.getCCode().length() + " chars) ---");
        System.out.println("--- AST DOT ---");
        System.out.println(compiler.getAstDot());
        System.out.println();
    }
}
