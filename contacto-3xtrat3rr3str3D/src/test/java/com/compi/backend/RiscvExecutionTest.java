package com.compi.backend;

import com.compi.backend.riscv.RiscvRunner;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Regresiones de la ejecución interna del ensamblador: el .s generado se
 * ensambla y corre dentro del simulador RARS embebido (rars.api), sin salir
 * de la aplicación ni usar herramientas externas.
 */
class RiscvExecutionTest {

    @TempDir Path temporaryDirectory;
    private final Path gradingDirectory =
            Path.of("..", "archivos", "archivos_calificacion").toAbsolutePath().normalize();

    private String compileToRiscv(Path source) throws Exception {
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(gradingDirectory.toFile());
        assertTrue(compiler.compile(Files.readString(source), "pig", source.toString()),
                compiler.getErrors().toString());
        return compiler.getRiscvCode();
    }

    private String compileSourceToRiscv(String source, Path directory) throws Exception {
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(directory.toFile());
        assertTrue(compiler.compile(source, "pig", directory.resolve("Prueba.pig").toString()),
                compiler.getErrors().toString());
        return compiler.getRiscvCode();
    }

    @Test
    void helloWorldSeEjecutaDentroDelSimulador() throws Exception {
        String asm = compileToRiscv(gradingDirectory.resolve("00_hello_world/Minimo.pig"));
        RiscvRunner.Result result = new RiscvRunner().run(asm, "");
        assertTrue(result.ok(), result.message());
        assertEquals("Hola Mundo", result.stdout().trim());
        assertEquals("NORMAL_TERMINATION", result.reason());
        assertEquals(0, result.exitCode());
    }

    @Test
    void programaConEntradaLeeYProduceSalida() throws Exception {
        String source = "##\n  Suma de dos numeros con lectura interna\n##\n"
                + "VARIABILES>\n"
                + "esto a : numerus 0;\n"
                + "esto b : numerus 0;\n"
                + "MAIOR>\n"
                + ">> \"Suma: \" ;\n"
                + "a <<\n"
                + "b <<\n"
                + ">> a + b ;\n"
                + "FINIS;\n";
        String asm = compileSourceToRiscv(source, temporaryDirectory);
        RiscvRunner.Result result = new RiscvRunner().run(asm, "3\n4\n");
        assertTrue(result.ok(), result.message());
        // Cada ">>" imprime en su propia línea, igual que la salida en C.
        assertTrue(result.stdout().contains("Suma:"), result.stdout());
        assertTrue(result.stdout().contains("7"), result.stdout());
        assertEquals("NORMAL_TERMINATION", result.reason());
    }

    @Test
    void calculadoraConImportsEjecutaYTermina() throws Exception {
        String asm = compileToRiscv(gradingDirectory.resolve("01_calculadora/Calculadora.pig"));
        // Nombre y opción 0 (salir) bastan para recorrer el menú completo.
        RiscvRunner.Result result = new RiscvRunner().run(asm, "Ana\n0\n");
        assertTrue(result.ok(), result.message());
        assertTrue(result.stdout().contains("Bienvenido Ana"), result.stdout());
        assertTrue(result.stdout().contains("Hasta luego, Ana"), result.stdout());
        assertEquals("NORMAL_TERMINATION", result.reason());
    }

    @Test
    void ensambladorConErroresInformaElDiagnostico() {
        RiscvRunner.Result result = new RiscvRunner().run(".text\nmain:\nfrotaficha t0, t1\n", "");
        assertFalse(result.ok());
        assertFalse(result.message().isBlank());
    }

    @Test
    void ensambladorVacioSeRechaza() {
        RiscvRunner.Result result = new RiscvRunner().run("   \n", "");
        assertFalse(result.ok());
    }
}