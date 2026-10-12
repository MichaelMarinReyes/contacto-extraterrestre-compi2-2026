package com.compi.backend.riscv;

import com.compi.backend.c3d.Quadruple;

/**
 * Accesos a memoria (montículo, marcas de tipo y pila) y las operaciones
 * sencillas de impresión y lectura que se apoyan en {@code ecall}.
 */
final class RiscvMemory {

    private final RiscvContext ctx;
    private final RiscvAccess access;

    RiscvMemory(RiscvContext ctx) {
        this.ctx = ctx;
        this.access = ctx.access;
    }

    void operation(Quadruple q) {
        switch (q.getOp()) {
            case HEAP_GET -> indexedLoad("heap", q.getArg1(), q.getResult());
            case HEAP_SET -> indexedStore("heap", q.getArg1(), q.getArg2());
            case TYPE_GET -> indexedLoad("otype", q.getArg1(), q.getResult());
            case TYPE_SET -> indexedStore("otype", q.getArg1(), q.getArg2());
            case STACK_GET -> indexedLoad("stack", q.getArg1(), q.getResult());
            case SET_P -> access.copy(q.getArg1(), "P");
            case GET_P -> access.copy("P", q.getResult());
            case SET_H -> access.copy(q.getArg1(), "H");
            case GET_H -> access.copy("H", q.getResult());
            case ARRAY_GET -> arrayLoad(q);
            case ARRAY_SET -> arrayStore(q);
            default -> {
            }
        }
    }

    private void indexedLoad(String base, String index, String result) {
        access.load(index, "fa0");
        access.indexedAddress(base, "fa0");
        ctx.out.line("fld fa1, 0(t0)");
        access.store(result, "fa1");
    }

    private void indexedStore(String base, String index, String value) {
        access.load(value, "fa1");
        access.load(index, "fa0");
        access.indexedAddress(base, "fa0");
        ctx.out.line("fsd fa1, 0(t0)");
    }

    private void arrayLoad(Quadruple q) {
        access.load(q.getArg1(), "fa0");
        ctx.out.line("fcvt.w.d s0, fa0");
        access.load(q.getArg2(), "fa1");
        ctx.out.line("fcvt.w.d t1, fa1");
        heapAddress("s0", "t1");
        ctx.out.line("fld fa2, 0(t0)");
        access.store(q.getResult(), "fa2");
    }

    private void arrayStore(Quadruple q) {
        access.load(q.getResult(), "fa2");
        access.load(q.getArg1(), "fa0");
        ctx.out.line("fcvt.w.d s0, fa0");
        access.load(q.getArg2(), "fa1");
        ctx.out.line("fcvt.w.d t1, fa1");
        heapAddress("s0", "t1");
        ctx.out.line("fsd fa2, 0(t0)");
    }

    private void heapAddress(String first, String second) {
        ctx.out.line("add t0, " + first + ", " + second);
        ctx.out.line("slli t0, t0, 3");
        ctx.out.line("la t2, heap");
        ctx.out.line("add t0, t2, t0");
    }

    void simple(Quadruple q) {
        switch (q.getOp()) {
            case PRINT_INT -> {
                access.load(q.getArg1(), "fa0");
                ctx.out.line("fcvt.w.d a0, fa0");
                syscall(1);
            }
            case PRINT_FLOAT -> {
                access.load(q.getArg1(), "fa0");
                syscall(3);
            }
            case PRINT_CHAR -> {
                access.load(q.getArg1(), "fa0");
                ctx.out.line("fcvt.w.d a0, fa0");
                syscall(11);
            }
            case PRINTLN -> {
                ctx.out.line("li a0, 10");
                syscall(11);
            }
            case READ -> {
                syscall(7);
                access.store(q.getArg1(), "fa0");
            }
            default -> zero(q.getArg1());
        }
    }

    private void zero(String target) {
        if (target == null) {
            return;
        }
        access.loadConstant("0", "fa0");
        access.store(target, "fa0");
    }

    private void syscall(int number) {
        ctx.out.line("li a7, " + number);
        ctx.out.line("ecall");
    }
}
