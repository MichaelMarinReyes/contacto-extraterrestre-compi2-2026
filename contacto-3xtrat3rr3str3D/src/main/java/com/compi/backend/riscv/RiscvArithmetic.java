package com.compi.backend.riscv;

import com.compi.backend.c3d.Quadruple;

/**
 * Operaciones aritméticas, lógicas y de concatenación. Todos los valores son
 * dobles (o códigos de carácter guardados como doble) y se resuelven con la
 * extensión de punto flotante doble.
 */
final class RiscvArithmetic {

    private final RiscvContext ctx;
    private final RiscvAccess access;
    private final RiscvRuntime runtime;

    RiscvArithmetic(RiscvContext ctx, RiscvRuntime runtime) {
        this.ctx = ctx;
        this.access = ctx.access;
        this.runtime = runtime;
    }

    void assign(Quadruple q) {
        String source = q.getArg1();
        if (source != null && source.startsWith("\"")) {
            runtime.writeString(q.getResult(), source);
            return;
        }
        if (q.getResult() != null) {
            access.copy(source, q.getResult());
        }
    }

    void operation(Quadruple q) {
        switch (q.getOp()) {
            case ADD -> binary(q, "fadd.d");
            case SUB -> binary(q, "fsub.d");
            case MUL -> binary(q, "fmul.d");
            case DIV -> binary(q, "fdiv.d");
            case INT_DIV -> truncateDivision(q);
            case MOD -> modulo(q);
            case NEG -> negate(q);
            case AND -> logicalAnd(q);
            case OR -> logicalOr(q);
            case NOT -> logicalNot(q);
            default -> {
            }
        }
    }

    private void binary(Quadruple q, String op) {
        access.load(q.getArg1(), "fa0");
        access.load(q.getArg2(), "fa1");
        ctx.out.line(op + " fa0, fa0, fa1");
        access.store(q.getResult(), "fa0");
    }

    private void truncateDivision(Quadruple q) {
        access.load(q.getArg1(), "fa0");
        access.load(q.getArg2(), "fa1");
        ctx.out.line("fdiv.d fa0, fa0, fa1");
        ctx.out.line("fcvt.w.d t0, fa0");
        ctx.out.line("fcvt.d.w fa0, t0");
        access.store(q.getResult(), "fa0");
    }

    private void modulo(Quadruple q) {
        access.load(q.getArg1(), "fa0");
        access.load(q.getArg2(), "fa1");
        ctx.out.line("fdiv.d ft2, fa0, fa1");
        ctx.out.line("fcvt.w.d t0, ft2");
        ctx.out.line("fcvt.d.w ft2, t0");
        ctx.out.line("fmul.d ft2, ft2, fa1");
        ctx.out.line("fsub.d fa0, fa0, ft2");
        access.store(q.getResult(), "fa0");
    }

    private void negate(Quadruple q) {
        access.load(q.getArg1(), "fa0");
        access.loadConstant("0", "fa1");
        ctx.out.line("fsub.d fa0, fa1, fa0");
        access.store(q.getResult(), "fa0");
    }

    private void logicalAnd(Quadruple q) {
        booleanOperands(q);
        ctx.out.line("and t0, t0, t1");
        storeBoolean(q.getResult());
    }

    private void logicalOr(Quadruple q) {
        booleanOperands(q);
        ctx.out.line("or t0, t0, t1");
        storeBoolean(q.getResult());
    }

    private void booleanOperands(Quadruple q) {
        access.load(q.getArg1(), "fa0");
        access.load(q.getArg2(), "fa1");
        access.loadConstant("0", "ft2");
        ctx.out.line("feq.d t0, fa0, ft2");
        ctx.out.line("xori t0, t0, 1");
        ctx.out.line("feq.d t1, fa1, ft2");
        ctx.out.line("xori t1, t1, 1");
    }

    private void storeBoolean(String result) {
        ctx.out.line("fcvt.d.w fa0, t0");
        access.store(result, "fa0");
    }

    private void logicalNot(Quadruple q) {
        access.load(q.getArg1(), "fa0");
        access.loadConstant("0", "ft2");
        ctx.out.line("feq.d t0, fa0, ft2");
        storeBoolean(q.getResult());
    }

    /** Copia dos cadenas del montículo a una nueva y devuelve su dirección. */
    void concat(Quadruple q) {
        access.loadConstant("0", "ft3");
        access.loadConstant("-1", "ft4");
        access.load("H", "ft2");
        String second = ctx.uid();
        String end = ctx.uid();
        access.load(q.getArg1(), "fa0");
        ctx.out.line("fcvt.w.d s0, fa0");
        copyLoop(second);
        ctx.out.label(second);
        access.load(q.getArg2(), "fa0");
        ctx.out.line("fcvt.w.d s0, fa0");
        copyLoop(end);
        ctx.out.label(end);
        access.heapAppendDouble("ft3");
        access.store(q.getResult(), "ft2");
    }

    private void copyLoop(String endLabel) {
        String loop = ctx.uid();
        ctx.out.label(loop);
        ctx.out.line("slli t0, s0, 3");
        ctx.out.line("la t2, heap");
        ctx.out.line("add t0, t2, t0");
        ctx.out.line("fld ft0, 0(t0)");
        ctx.out.line("feq.d t3, ft0, ft3");
        ctx.out.line("bne t3, x0, " + endLabel);
        ctx.out.line("feq.d t3, ft0, ft4");
        ctx.out.line("bne t3, x0, " + endLabel);
        access.heapAppendDouble("ft0");
        ctx.out.line("addi s0, s0, 1");
        ctx.out.line("j " + loop);
    }
}
