package com.compi;

import com.compi.backend.Compiler;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.symbols.Symbol;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Sonda temporal: imprime el C3D de un archivo. Se borra al terminar. */
public class ProbeC3D {
    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            Path ruta = Path.of(arg);
            String fuente = Files.readString(ruta);
            String nombre = ruta.getFileName().toString();
            String lenguaje = com.compi.backend.languages2.LanguageCompilerFactory
                    .detectLanguageId(nombre);
            Compiler c = new Compiler();
            c.setWorkingDirectory(ruta.getParent() == null ? Path.of(".").toFile()
                    : ruta.getParent().toFile());
            boolean ok = c.compile(fuente, lenguaje, nombre);
            System.out.println("=========== " + nombre + " [" + lenguaje + "] ok=" + ok);
            for (CompilationError e : c.getErrors()) {
                System.out.println("  ERROR " + e);
            }
            System.out.println("--- C3D ---");
            System.out.print(c.getC3DCode());
            System.out.println("--- Simbolos (lenguaje) ---");
            List<Symbol> simbolos = c.getSymbols();
            for (Symbol s : simbolos) {
                System.out.println("  " + s.getName() + " | " + s.getType()
                        + " | " + s.getCategory() + " | " + s.getLanguage()
                        + " | " + s.getScope() + " | linea " + s.getLine());
            }
        }
    }
}
