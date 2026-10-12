package com.compi.backend;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

/** Regresiones estructurales del módulo nuevo de ensamblador RISC-V. */
class RiscvAssemblyTest {

    @TempDir Path temporaryDirectory;
    private final Path gradingDirectory =
            Path.of("..", "archivos", "archivos_calificacion").toAbsolutePath().normalize();

    private Compiler compile(Path source) throws Exception {
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(gradingDirectory.toFile());
        assertTrue(compiler.compile(Files.readString(source), "pig", source.toString()),
                compiler.getErrors().toString());
        return compiler;
    }

    @Test
    void helloWorldGeneratesStandaloneRarsAssembly() throws Exception {
        String asm = compile(gradingDirectory.resolve("00_hello_world/Minimo.pig")).getRiscvCode();
        assertTrue(asm.contains(".globl main"));
        assertTrue(asm.contains("main:"));
        assertTrue(asm.contains("stack: .space 800000"));
        assertTrue(asm.contains("heap:  .space 800000"));
        assertTrue(asm.contains("otype: .space 800000"));
        assertTrue(asm.contains(".rodata"));
        assertTrue(asm.contains("ecall"));
        assertTrue(asm.contains("L_end:"));
        assertTrue(asm.contains("li a7, 10"));
        // Sin llamadas del lenguaje no hay despachador ni subrutinas.
        assertFalse(asm.contains("L_dispatch"));
        assertFalse(asm.contains("LF_"));
    }

    @Test
    void functionsCallsAndRuntimeInlineAreEmitted() throws Exception {
        String asm = compile(gradingDirectory.resolve("01_calculadora/Calculadora.pig")).getRiscvCode();
        assertTrue(asm.contains("L_dispatch:"));
        assertTrue(asm.contains("L_ret_1:"));
        assertTrue(asm.contains("L_end:"));
        assertTrue(asm.contains("fadd.d fa0, fa0, fa1"));
        assertTrue(asm.contains("fdiv.d fa0, fa0, fa1"));
        assertTrue(asm.contains("feq.d"));
        assertTrue(asm.contains("slli t0"));
        assertTrue(asm.contains(".double 1.0"));
        // El retorno restaura el frame y salta al despachador.
        assertTrue(asm.contains("j L_dispatch"));
        assertTrue(asm.contains("fcvt.w.d t0, ft0"));
    }

    @Test
    void assemblyIsDeterministic() throws Exception {
        Path source = gradingDirectory.resolve("00_hello_world/Minimo.pig");
        assertEquals(compile(source).getRiscvCode(), compile(source).getRiscvCode());
    }

    @Test
    void toggleDisablesAndRestoresRiscvGeneration() throws Exception {
        Path source = gradingDirectory.resolve("00_hello_world/Minimo.pig");
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(gradingDirectory.toFile());
        assertTrue(compiler.compile(Files.readString(source), "pig", source.toString()),
                compiler.getErrors().toString());
        assertFalse(compiler.getRiscvCode().isBlank());
        compiler.setRiscvEnabled(false);
        assertEquals("", compiler.getRiscvCode());
        compiler.setRiscvEnabled(true);
        assertFalse(compiler.getRiscvCode().isBlank());
    }
}