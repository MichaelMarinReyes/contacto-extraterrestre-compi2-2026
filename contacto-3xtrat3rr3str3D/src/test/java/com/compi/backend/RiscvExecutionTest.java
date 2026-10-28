package com.compi.backend;

import com.compi.backend.riscv.InteractiveInput;
import com.compi.backend.riscv.RiscvRunner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    void decimalesConservanSuParteEnteraYFraccion() throws Exception {
        String source = "VARIABILES>\n"
                + "esto a : decimalis 77.5;\n"
                + "esto b : decimalis 0.75;\n"
                + "esto c : decimalis -0.5;\n"
                + "MAIOR>\n"
                + ">> a;\n"
                + ">> b;\n"
                + ">> c;\n"
                + "FINIS;\n";
        String asm = compileSourceToRiscv(source, temporaryDirectory);
        RiscvRunner.Result result = new RiscvRunner().run(asm, "");
        assertTrue(result.ok(), result.message());
        assertEquals("77.5\n0.75\n-0.5", result.stdout().trim());
    }

    @Test
    void escapesDeTextoSeConsumenUnaSolaVez() throws Exception {
        String asm = compileSourceToRiscv("MAIOR> >> \"A\\nB\\tC\\\\D\"; FINIS;", temporaryDirectory);
        RiscvRunner.Result result = new RiscvRunner().run(asm, "");
        assertTrue(result.ok(), result.message());
        assertEquals("A\nB\tC\\D\n", result.stdout());
        assertFalse(result.stdout().contains("\nnB"), result.stdout());
    }

    @Test
    void superInvocaLaEtiquetaEspecializadaDelConstructorPadre() throws Exception {
        Files.writeString(temporaryDirectory.resolve("Animal.z"), """
                public class Animal {
                    protected String nombre;
                    public Animal(String nombre) { this.nombre = nombre; }
                    public String presentarse() { return nombre; }
                }
                """);
        Files.writeString(temporaryDirectory.resolve("Perro.z"), """
                public class Perro extends Animal {
                    public Perro(String nombre) { super(nombre); }
                }
                """);
        String asm = compileSourceToRiscv("""
                import Animal.z
                import Perro.z
                VARIABILES> esto perro : novus Perro("Rex") : Animal;
                MAIOR> >> perro.presentarse(); FINIS;
                """, temporaryDirectory);
        assertTrue(asm.contains("LF_Animal_Animal_a1:"), asm);
        assertTrue(asm.contains("j LF_Animal_Animal_a1"), asm);
        RiscvRunner.Result result = new RiscvRunner().run(asm, "");
        assertTrue(result.ok(), result.message());
        assertEquals("Rex\n", result.stdout());
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
    void laEjecucionSeDetieneConLaSolicitudDeParada() throws Exception {
        String source = "VARIABILES>\n"
                + "esto i : numerus 0;\n"
                + "MAIOR>\n"
                + "dum (verum) {\n"
                + "} finis;\n"
                + "FINIS;\n";
        String asm = compileSourceToRiscv(source, temporaryDirectory);
        AtomicInteger checks = new AtomicInteger();
        RiscvRunner.Result result = new RiscvRunner().run(asm, "", () -> checks.incrementAndGet() > 1);
        assertTrue(result.ok(), result.message());
        assertEquals("DETENIDO", result.reason());
    }

    @Test
    void entradaInteractivaSeConsumePorLinea() throws Exception {
        String source = "VARIABILES>\n"
                + "esto a : numerus 0;\n"
                + "esto b : numerus 0;\n"
                + "MAIOR>\n"
                + ">> \"Suma: \" ;\n"
                + "a <<\n"
                + "b <<\n"
                + ">> a + b ;\n"
                + "FINIS;\n";
        String asm = compileSourceToRiscv(source, temporaryDirectory);
        InteractiveInput input = new InteractiveInput();
        input.submit("3");
        input.submit("4");
        StringBuilder streamed = new StringBuilder();
        RiscvRunner.Result result = new RiscvRunner().runInteractive(asm, input,
                streamed::append, () -> false);
        assertTrue(result.ok(), result.message());
        assertTrue(result.stdout().contains("Suma:"), result.stdout());
        assertTrue(result.stdout().contains("7"), result.stdout());
        // La salida se publica mientras corre, no solo al terminar.
        assertTrue(streamed.toString().contains("7"), streamed.toString());
        assertEquals("NORMAL_TERMINATION", result.reason());
    }

    @Test
    void ejecucionInteractivaEsperaTextoAntesDeContinuar() throws Exception {
        String asm = compileSourceToRiscv("""
                VARIABILES> esto nombre : textum "";
                MAIOR>
                >> "Nombre:";
                nombre <<
                >> "Hola " >> nombre;
                FINIS;
                """, temporaryDirectory);
        InteractiveInput input = new InteractiveInput();
        StringBuffer streamed = new StringBuffer();
        CompletableFuture<RiscvRunner.Result> execution = CompletableFuture.supplyAsync(
                () -> new RiscvRunner().runInteractive(asm, input, streamed::append, () -> false));

        assertThrows(TimeoutException.class, () -> execution.get(250, TimeUnit.MILLISECONDS),
                "El ejecutable no debe avanzar sin recibir la línea solicitada");
        input.submit("Ada");
        RiscvRunner.Result result = execution.get(10, TimeUnit.SECONDS);
        assertTrue(result.ok(), result.message());
        assertTrue(result.stdout().contains("Hola Ada"), result.stdout());
        assertTrue(streamed.toString().contains("Hola Ada"), streamed.toString());
    }

    @Test
    void detenerInterrumpeUnaEsperaDeEntrada() throws Exception {
        String source = "VARIABILES>\n"
                + "esto n : numerus 0;\n"
                + "MAIOR>\n"
                + "dum (verum) {\n"
                + "  n <<\n"
                + "} finis;\n"
                + "FINIS;\n";
        String asm = compileSourceToRiscv(source, temporaryDirectory);
        InteractiveInput input = new InteractiveInput();
        AtomicBoolean stop = new AtomicBoolean(false);
        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
                return;
            }
            stop.set(true);
            input.close();
        });
        stopper.setDaemon(true);
        stopper.start();
        RiscvRunner.Result result = assertTimeoutPreemptively(java.time.Duration.ofSeconds(10),
                () -> new RiscvRunner().runInteractive(asm, input, text -> { }, stop::get));
        assertEquals("DETENIDO", result.reason());
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
