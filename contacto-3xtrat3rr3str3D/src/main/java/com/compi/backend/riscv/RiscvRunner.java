package com.compi.backend.riscv;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import rars.ErrorList;
import rars.SimulationException;
import rars.api.Options;
import rars.api.Program;
import rars.simulator.Simulator;

/**
 * Ejecuta internamente el ensamblador RISC-V generado usando el simulador RARS
 * empotrado en {@code lib/rars1_6.jar} (paquete público {@code rars.api}). El
 * {@code .s} se ensambla a partir de su texto y se ejecuta desde {@code main}
 * con la entrada que recibe; la salida queda disponible para la consola del
 * IDE. De este modo el usuario no sale de la aplicación ni usa herramientas
 * externas para compilar y correr su ensamblador.
 */
public class RiscvRunner {

    /** Límite de instrucciones simuladas; acota los lazos infinitos. */
    private static final int MAX_STEPS = 20_000_000;

    /**
     * Instrucciones por tramo. La simulación se ejecuta en bloques de este
     * tamaño para poder atender una solicitud de parada entre tramos sin
     * recurrir a APIs internas de RARS.
     */
    private static final int CHUNK_STEPS = 500_000;

    /** Resultado de una ejecución: bandera, salidas, razón y tiempo. */
    public record Result(boolean ok, String stdout, String stderr, String reason,
                         int exitCode, long elapsedMillis, String message) { }

    /**
     * Ensambla y ejecuta el texto de ensamblador indicado con la entrada dada.
     * El IDE la invoca desde un hilo de trabajo para no bloquear la interfaz.
     */
    public Result run(String assembly, String input) {
        return run(assembly, input, () -> false);
    }

    /**
     * Igual que {@link #run(String, String)} pero consulta {@code stopRequested}
     * entre tramos: cuando devuelve {@code true} la simulación se interrumpe y el
     * resultado queda con la razón {@code DETENIDO} y la salida producida hasta
     * ese momento. La solicitud se lee en el mismo hilo que simula, por lo que
     * no requiere acceso concurrente a las estructuras de RARS.
     */
    public Result run(String assembly, String input, BooleanSupplier stopRequested) {
        long start = System.nanoTime();
        if (assembly == null || assembly.isBlank()) {
            return new Result(false, "", "", null, -1, elapsed(start),
                    "No hay ensamblador que ejecutar.");
        }
        Program program = new Program(options());
        try {
            ErrorList errors = program.assembleString(assembly);
            if (errors.errorCount() > 0) {
                return new Result(false, "", "", null, -1, elapsed(start),
                        describe(errors));
            }
            program.setup(new ArrayList<>(), input == null ? "" : input);
            long executed = 0;
            Simulator.Reason reason;
            while (true) {
                if (stopRequested != null && stopRequested.getAsBoolean()) {
                    return new Result(true, program.getSTDOUT(), program.getSTDERR(),
                            "DETENIDO", program.getExitCode(), elapsed(start), null);
                }
                reason = program.simulate();
                if (reason != Simulator.Reason.MAX_STEPS) break;
                executed += CHUNK_STEPS;
                if (executed >= MAX_STEPS) break;
            }
            return new Result(true, program.getSTDOUT(), program.getSTDERR(),
                    reason == null ? null : reason.toString(), program.getExitCode(),
                    elapsed(start), null);
        } catch (rars.AssemblyException e) {
            return new Result(false, "", "", null, -1, elapsed(start),
                    e.errors() == null ? "Error de ensamblaje." : describe(e.errors()));
        } catch (Exception e) {
            return new Result(false, "", "", null, -1, elapsed(start),
                    "Error de simulación: " + e);
        }
    }

    /**
     * Igual que {@link #run(String, String, BooleanSupplier)} pero con entrada
     * interactiva: el programa lee líneas de {@code input} a medida que las
     * necesita y su salida se publica en {@code output} en cuanto se produce,
     * para que el usuario vea los mensajes antes de responder. {@code input}
     * también permite desbloquear una lectura pendiente al detener.
     */
    public Result runInteractive(String assembly, InteractiveInput input,
                                 Consumer<String> output, BooleanSupplier stopRequested) {
        long start = System.nanoTime();
        if (assembly == null || assembly.isBlank()) {
            return new Result(false, "", "", null, -1, elapsed(start),
                    "No hay ensamblador que ejecutar.");
        }
        Program program = new Program(options());
        StreamingOutputStream stdout = new StreamingOutputStream(output);
        StreamingOutputStream stderr = new StreamingOutputStream(output);
        InputStream savedIn = System.in;
        PrintStream savedOut = System.out;
        PrintStream savedErr = System.err;
        try {
            ErrorList errors = program.assembleString(assembly);
            if (errors.errorCount() > 0) {
                return new Result(false, "", "", null, -1, elapsed(start), describe(errors));
            }
            program.setup(new ArrayList<>(), null);
            System.setIn(input);
            System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));
            Simulator.Reason reason = simulateInteractive(program, stopRequested);
            boolean stopped = stopRequested != null && stopRequested.getAsBoolean();
            return new Result(true, stdout.text(), stderr.text(),
                    stopped ? "DETENIDO" : (reason == null ? null : reason.toString()),
                    program.getExitCode(), elapsed(start), null);
        } catch (rars.AssemblyException e) {
            return new Result(false, "", "", null, -1, elapsed(start),
                    e.errors() == null ? "Error de ensamblaje." : describe(e.errors()));
        } catch (Exception e) {
            return interactiveFailure(stdout, stderr, program, start, stopRequested, e);
        } finally {
            System.setIn(savedIn);
            System.setOut(savedOut);
            System.setErr(savedErr);
        }
    }

    private static Options options() {
        Options options = new Options();
        options.startAtMain = true;
        options.maxSteps = CHUNK_STEPS;
        options.pseudo = true;
        return options;
    }

    /** Simula por tramos; devuelve {@code null} si se pidió parar entre tramos. */
    private static Simulator.Reason simulateInteractive(Program program, BooleanSupplier stopRequested)
            throws SimulationException {
        for (int executed = 0; ; executed += CHUNK_STEPS) {
            if (stopRequested != null && stopRequested.getAsBoolean()) {
                return null;
            }
            Simulator.Reason reason = program.simulate();
            if (reason != Simulator.Reason.MAX_STEPS) {
                return reason;
            }
            if (executed + CHUNK_STEPS >= MAX_STEPS) {
                return reason;
            }
        }
    }

    private static Result interactiveFailure(StreamingOutputStream stdout, StreamingOutputStream stderr,
                                             Program program, long start, BooleanSupplier stopRequested,
                                             Exception error) {
        if (stopRequested != null && stopRequested.getAsBoolean()) {
            return new Result(true, stdout.text(), stderr.text(), "DETENIDO",
                    program.getExitCode(), elapsed(start), null);
        }
        return new Result(false, stdout.text(), stderr.text(), null, -1, elapsed(start),
                "Error de simulación: " + error);
    }

    private static String describe(ErrorList errors) {
        StringBuilder text = new StringBuilder("Errores de ensamblaje (")
                .append(errors.errorCount()).append("):\n");
        for (rars.ErrorMessage error : errors.getErrorMessages()) {
            text.append("  Línea ").append(error.getLine()).append(": ")
                    .append(error.getMessage()).append('\n');
        }
        return text.toString();
    }

    private static long elapsed(long start) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    }
}