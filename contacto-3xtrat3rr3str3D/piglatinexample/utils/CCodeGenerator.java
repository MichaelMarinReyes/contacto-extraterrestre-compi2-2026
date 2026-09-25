/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.utils;

import igriega.piglatin.cuartetas.Cuarteta;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author blue-dragon
 */
public class CCodeGenerator {

    private void generateFile(StringBuilder sb) {
        String directorioProyecto = System.getProperty("user.dir");

        String nombreArchivo = "programa.c";
        Path rutaArchivo = Paths.get(directorioProyecto, nombreArchivo);

        try {
            // 1. Escribe el archivo en la ruta del proyecto
            Files.writeString(rutaArchivo, sb.toString());
            System.out.println("¡Archivo .c guardado en la raíz del proyecto: " + rutaArchivo.toAbsolutePath() + "!");

            // 2. Compilar el archivo .c usando GCC
            // "gcc programa.c -o programa" (en Windows generará programa.exe, en Linux/Mac generará programa)
            ProcessBuilder processBuilder = new ProcessBuilder("gcc", nombreArchivo, "-o", "programa");

            // Establece el directorio de trabajo donde se ejecutará el comando (la raíz del proyecto)
            processBuilder.directory(Paths.get(directorioProyecto).toFile());

            // Redirige los errores para poder capturarlos si la compilación falla
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            // Leer la salida/errores del compilador
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Esperar a que termine el proceso de compilación y verificar el código de salida
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("¡Compilación exitosa! Se ha generado el ejecutable.");
            } else {
                System.err.println("La compilación falló con el código de salida: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ocurrió un error al guardar o compilar el archivo: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void generateCIntermediateCode(IntermediateCodeUtils u) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                  #include <stdio.h>
                  
                  int main() {
                  // with <3 by IGriega
                  """);
        for (int i = 0; i < u.getCounter().getTemporalCount(); i++) {
            sb.append("int t").append(i).append(";\n");
        }
        for (Cuarteta cuarteta : u.getCuartetas()) {
            cuarteta.toCCode(sb);
        }
        sb.append("return 0; \n}");
        generateFile(sb);
    }

}
