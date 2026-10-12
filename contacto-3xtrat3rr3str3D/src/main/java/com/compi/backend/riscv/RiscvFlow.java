package com.compi.backend.riscv;

import com.compi.backend.c3d.Quadruple;

/**
 * Control de flujo, llamadas, retornos y despacho. Las llamadas del lenguaje
 * cambian de frame sobre {@code stack} y vuelven a través de un despachador,
 * igual que el código intermedio lo describe.
 */
final class RiscvFlow {

    private final RiscvContext ctx;
    private final RiscvAccess access;
    private final RiscvRuntime runtime;

    RiscvFlow(RiscvContext ctx, RiscvRuntime runtime) {
        this.ctx = ctx;
        this.access = ctx.access;
        this.runtime = runtime;
    }

    void control(Quadruple q) {
        switch (q.getOp()) {
            case LABEL -> ctx.out.label(q.getResult());
            case GOTO -> ctx.out.line("j " + q.getResult());
            case IF_TRUE -> truthJump(q.getArg1(), q.getResult(), true);
            case IF_FALSE -> truthJump(q.getArg1(), q.getResult(), false);
            default -> compareJump(q);
        }
    }

    private void truthJump(String value, String target, boolean whenTrue) {
        access.load(value, "fa0");
        access.loadConstant("0", "fa1");
        ctx.out.line("feq.d t0, fa0, fa1");
        ctx.out.line((whenTrue ? "beq " : "bne ") + "t0, x0, " + target);
    }

    private void compareJump(Quadruple q) {
        access.load(q.getArg1(), "fa0");
        access.load(q.getArg2(), "fa1");
        switch (q.getOp()) {
            case IF_EQ -> branch("feq.d", "bne", q.getResult());
            case IF_NE -> branch("feq.d", "beq", q.getResult());
            case IF_LT -> branch("flt.d", "bne", q.getResult());
            case IF_LE -> branch("fle.d", "bne", q.getResult());
            case IF_GT -> branchReversed("flt.d", q.getResult());
            case IF_GE -> branchReversed("fle.d", q.getResult());
            default -> {
            }
        }
    }

    private void branch(String setOp, String condition, String target) {
        ctx.out.line(setOp + " t0, fa0, fa1");
        ctx.out.line(condition + " t0, x0, " + target);
    }

    private void branchReversed(String setOp, String target) {
        ctx.out.line(setOp + " t0, fa1, fa0");
        ctx.out.line("bne t0, x0, " + target);
    }

    void function(Quadruple q) {
        switch (q.getOp()) {
            case PARAM -> ctx.pending.add(q.getArg1());
            case CALL -> call(q);
            case RETURN -> {
                if (!ctx.returnValueSet) {
                    zeroReturn();
                }
                doReturn();
            }
            case STACK_SET -> stackSet(q);
            default -> {
            }
        }
    }

    private void zeroReturn() {
        access.loadConstant("0", "ft0");
        access.store("RET", "ft0");
    }

    private void stackSet(Quadruple q) {
        if ("P".equals(q.getArg1())) {
            access.load(q.getArg2(), "fa0");
            access.store("RET", "fa0");
            ctx.returnValueSet = true;
            return;
        }
        access.load(q.getArg2(), "fa1");
        access.load(q.getArg1(), "fa0");
        access.indexedAddress("stack", "fa0");
        ctx.out.line("fsd fa1, 0(t0)");
    }

    private void call(Quadruple q) {
        String name = q.getArg1();
        int count = q.getArg2() == null ? 0 : Integer.parseInt(q.getArg2());
        if (runtime.isRuntime(name)) {
            runtime.call(name, q.getResult());
            return;
        }
        int start = ctx.pending.size() - count;
        if (start < 0) {
            throw new IllegalStateException("Faltan argumentos para «" + name + "»");
        }
        int id = ctx.nextCallId++;
        ctx.callSites.add(id);
        int frame = ctx.layout.frame(ctx.function);
        for (int i = 0; i < count; i++) {
            access.load(ctx.pending.get(start + i), "fa0");
            access.frameStore(frame + 2 + i, "fa0");
        }
        access.loadConstant(String.valueOf(id), "fa0");
        access.frameStore(frame, "fa0");
        access.load("P", "fa0");
        access.frameStore(frame + 1, "fa0");
        access.load("P", "fa0");
        access.loadConstant(String.valueOf(frame), "fa1");
        ctx.out.line("fadd.d fa0, fa0, fa1");
        access.store("P", "fa0");
        ctx.out.line("j " + RiscvLayout.label(name));
        ctx.out.label("L_ret_" + id);
        if (q.getResult() != null) {
            access.load("RET", "fa0");
            access.store(q.getResult(), "fa0");
        }
        ctx.pending.subList(start, ctx.pending.size()).clear();
        ctx.returnValueSet = false;
    }

    void doReturn() {
        access.frameLoad(0, "fa0");
        access.store("RA", "fa0");
        access.frameLoad(1, "fa0");
        access.store("P", "fa0");
        ctx.out.line("j L_dispatch");
        ctx.returnValueSet = false;
    }

    void dispatch() {
        ctx.out.label("L_dispatch");
        if (ctx.callSites.isEmpty()) {
            ctx.out.line("j L_end");
            return;
        }
        access.load("RA", "ft0");
        ctx.out.line("fcvt.w.d t0, ft0");
        for (int id : ctx.callSites) {
            ctx.out.line("li t1, " + id);
            ctx.out.line("beq t0, t1, L_ret_" + id);
        }
        ctx.out.line("j L_end");
    }
}
