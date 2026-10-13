package com.compi.backend.riscv;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import rars.ErrorList;
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

    /** Resultado de una ejecución: bandera, salidas, razón y tiempo. */
    public record Result(boolean ok, String stdout, String stderr, String reason,
                         int exitCode, long elapsedMillis, String message) { }

    /**
     * Ensambla y ejecuta el texto de ensamblador indicado con la entrada dada.
     * El IDE la invoca desde un hilo de trabajo para no bloquear la interfaz.
     */
    public Result run(String assembly, String input) {
        long start = System.nanoTime();
        if (assembly == null || assembly.isBlank()) {
            return new Result(false, "", "", null, -1, elapsed(start),
                    "No hay ensamblador que ejecutar.");
        }
        Options options = new Options();
        options.startAtMain = true;
        options.maxSteps = MAX_STEPS;
        options.pseudo = true;
        Program program = new Program(options);
        try {
            ErrorList errors = program.assembleString(assembly);
            if (errors.errorCount() > 0) {
                return new Result(false, "", "", null, -1, elapsed(start),
                        describe(errors));
            }
            program.setup(new ArrayList<>(), input == null ? "" : input);
            Simulator.Reason reason = program.simulate();
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