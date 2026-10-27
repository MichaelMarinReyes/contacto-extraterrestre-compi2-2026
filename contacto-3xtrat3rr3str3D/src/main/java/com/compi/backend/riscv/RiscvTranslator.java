package com.compi.backend.riscv;

import com.compi.backend.c3d.Quadruple;
import com.compi.backend.symbols.SymbolTable;

import java.util.List;

/**
 * Traduce el código intermedio a un único programa en ensamblador RISC-V. El
 * cuerpo principal queda etiquetado como {@code main}, las funciones se
 * convierten en subrutinas planas y el retorno se resuelve con un despachador.
 */
final class RiscvTranslator {

    private final RiscvContext ctx;
    private final RiscvFlow flow;
    private final RiscvArithmetic arithmetic;
    private final RiscvMemory memory;
    private final RiscvRuntime runtime;

    RiscvTranslator(List<Quadruple> quadruples, SymbolTable symbolTable) {
        ctx = new RiscvContext(new RiscvLayout(quadruples, symbolTable));
        runtime = new RiscvRuntime(ctx);
        flow = new RiscvFlow(ctx, runtime);
        arithmetic = new RiscvArithmetic(ctx, runtime);
        memory = new RiscvMemory(ctx);
    }

    private static String header() {
        return "# Ensamblador RISC-V\n\n";
    }

    private static String dataSection() {
        return """
                .data
                .align 3
                stack: .space 800000
                heap:  .space 800000
                otype: .space 800000
                H:     .double 0.0
                P:     .double 0.0
                RET:   .double 0.0
                RA:    .double 0.0
                _txt:  .space 4096
                \n""";
    }

    String build() {
        generateBody();
        return header() + dataSection() + ctx.constants.section() + textSection();
    }

    private void generateBody() {
        ctx.function = RiscvLayout.ENTRY;
        ctx.returnValueSet = false;
        ctx.pending.clear();
        ctx.out.label("main");
        ctx.out.line("li t0, " + ctx.layout.globals.size());
        ctx.out.line("fcvt.d.w ft0, t0");
        ctx.out.line("la t0, P");
        ctx.out.line("fsd ft0, 0(t0)");
        emitBody(ctx.layout.bodies.getOrDefault(RiscvLayout.ENTRY, List.of()));
        ctx.out.line("j L_end");
        emitFunctions();
        if (hasFunctions() || !ctx.callSites.isEmpty()) {
            flow.dispatch();
        }
        ctx.out.label("L_end");
        ctx.out.line("li a7, 10");
        ctx.out.line("ecall");
    }

    private void emitFunctions() {
        for (var entry : ctx.layout.bodies.entrySet()) {
            if (RiscvLayout.ENTRY.equals(entry.getKey())) {
                continue;
            }
            ctx.function = entry.getKey();
            ctx.returnValueSet = false;
            ctx.pending.clear();
            ctx.out.label(RiscvLayout.label(entry.getKey()));
            emitBody(entry.getValue());
            ctx.access.loadConstant("0", "ft0");
            ctx.access.store("RET", "ft0");
            flow.doReturn();
        }
    }

    private boolean hasFunctions() {
        return ctx.layout.bodies.keySet().stream()
                .anyMatch(name -> !RiscvLayout.ENTRY.equals(name));
    }

    private void emitBody(List<Quadruple> instructions) {
        for (Quadruple instruction : instructions) {
            emitOne(instruction);
        }
    }

    private void emitOne(Quadruple q) {
        switch (q.getOp()) {
            case LABEL, GOTO, IF_TRUE, IF_FALSE, IF_EQ, IF_NE, IF_LT, IF_LE, IF_GT, IF_GE -> flow.control(q);
            case PARAM, CALL, RETURN, STACK_SET -> flow.function(q);
            case ASSIGN -> arithmetic.assign(q);
            case ADD, SUB, MUL, DIV, INT_DIV, MOD, AND, OR, NOT, NEG -> arithmetic.operation(q);
            case CONCAT -> arithmetic.concat(q);
            case ARRAY_GET, ARRAY_SET, HEAP_GET, HEAP_SET, STACK_GET, SET_P, GET_P, SET_H, GET_H,
                 TYPE_GET, TYPE_SET -> memory.operation(q);
            case PRINT_STR -> runtime.printStringOps(q);
            case READ_STR -> runtime.readText(q.getArg1());
            case READ_CHAR -> runtime.readChar(q.getArg1());
            default -> memory.simple(q);
        }
    }

    private String textSection() {
        return ".text\n.globl main\n" + ctx.out.content();
    }
}
