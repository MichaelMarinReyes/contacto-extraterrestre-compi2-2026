package com.compi.backend;

import java.io.File;
import java.nio.file.Files;

public class TestRunner {
    public static void main(String[] args) throws Exception {
        if(args.length==0){
            System.out.println("Uso: TestRunner <archivo>");
            return;
        }
        File f = new File(args[0]);
        String content = Files.readString(f.toPath());
        String ext = getExtension(f.getName());
        Compiler compiler = new Compiler();
        System.out.println("Archivo: "+f.getName());
        System.out.println("Lenguaje detectado: "+ext);
        System.out.println("Contenido:\n"+content);
        compiler.compile(content, ext);
        System.out.println("Compilación finalizada. Revisar consola para errores, AST y C3D.");
    }

    private static String getExtension(String name){
        int i = name.lastIndexOf('.');
        return i>=0 ? name.substring(i+1) : "";
    }
}
