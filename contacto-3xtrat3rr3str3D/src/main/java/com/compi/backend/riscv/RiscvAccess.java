package com.compi.backend.riscv;

/**
 * Carga, almacena y direcciona operandos sobre los arreglos {@code stack} y
 * {@code heap}. Un operando puede ser una celda global, una celda del frame
 * actual (offset sobre {@code P}), un escalar interno ({@code H}, {@code P},
 * {@code RET}, {@code RA}) o un literal numérico.
 */
final class RiscvAccess {

    private final RiscvContext ctx;

    RiscvAccess(RiscvContext ctx) {
        this.ctx = ctx;
    }

    /** Carga el valor del operando en el registro flotante indicado. */
    void load(String value, String freg) {
        if (value == null || RiscvConstants.isNumeric(value)) {
            loadConstant(value == null ? "0" : value, freg);
            return;
        }
        addressOf(value);
        ctx.out.line("fld " + freg + ", 0(t0)");
    }

    /** Guarda el registro flotante en el operando indicado. */
    void store(String value, String freg) {
        addressOf(value);
        ctx.out.line("fsd " + freg + ", 0(t0)");
    }

    void copy(String source, String target) {
        load(source, "fa0");
        store(target, "fa0");
    }

    void loadConstant(String value, String freg) {
        ctx.out.line("la t0, " + ctx.constants.label(value));
        ctx.out.line("fld " + freg + ", 0(t0)");
    }

    /** Deja en {@code t0} la dirección de la celda del operando. */
    void addressOf(String value) {
        if (RiscvLayout.isGlobal(value)) {
            globalAddress(ctx.layout.global(value));
        } else if (RiscvLayout.isTemp(value) || RiscvLayout.isLocal(value)) {
            frameAddress(ctx.layout.slot(ctx.function, value));
        } else {
            ctx.out.line("la t0, " + value);
        }
    }

    void globalAddress(int index) {
        ctx.out.line("la t0, stack");
        if (index != 0) {
            ctx.out.line("li t1, " + (index * 8));
            ctx.out.line("add t0, t0, t1");
        }
    }

    void frameAddress(int offset) {
        ctx.out.line("la t0, P");
        ctx.out.line("fld ft0, 0(t0)");
        ctx.out.line("fcvt.w.d t0, ft0");
        if (offset != 0 && offset < 2048) {
            ctx.out.line("addi t0, t0, " + offset);
        } else if (offset != 0) {
            ctx.out.line("li t1, " + offset);
            ctx.out.line("add t0, t0, t1");
        }
        ctx.out.line("slli t0, t0, 3");
        ctx.out.line("la t1, stack");
        ctx.out.line("add t0, t1, t0");
    }

    void frameLoad(int offset, String freg) {
        frameAddress(offset);
        ctx.out.line("fld " + freg + ", 0(t0)");
    }

    void frameStore(int offset, String freg) {
        frameAddress(offset);
        ctx.out.line("fsd " + freg + ", 0(t0)");
    }

    /** Deja en {@code t0} la dirección de {@code base + (int)indexReg * 8}. */
    void indexedAddress(String base, String indexReg) {
        ctx.out.line("fcvt.w.d t0, " + indexReg);
        ctx.out.line("slli t0, t0, 3");
        ctx.out.line("la t1, " + base);
        ctx.out.line("add t0, t1, t0");
    }

    void bumpHeap() {
        ctx.out.line("la t0, H");
        ctx.out.line("fld ft0, 0(t0)");
        loadConstant("1", "ft1");
        ctx.out.line("fadd.d ft0, ft0, ft1");
        ctx.out.line("la t0, H");
        ctx.out.line("fsd ft0, 0(t0)");
    }

    /** Añade al montículo el carácter cuyo código está en el registro entero. */
    void heapAppendInt(String codeReg) {
        ctx.out.line("la t0, H");
        ctx.out.line("fld ft0, 0(t0)");
        ctx.out.line("fcvt.w.d t1, ft0");
        ctx.out.line("slli t1, t1, 3");
        ctx.out.line("la t2, heap");
        ctx.out.line("add t0, t2, t1");
        ctx.out.line("fcvt.d.w ft1, " + codeReg);
        ctx.out.line("fsd ft1, 0(t0)");
        bumpHeap();
    }

    void heapAppendImmediate(int code) {
        ctx.out.line("li a0, " + code);
        heapAppendInt("a0");
    }

    /** Añade al montículo el valor doble del registro (no puede ser ft0/ft1). */
    void heapAppendDouble(String freg) {
        ctx.out.line("la t0, H");
        ctx.out.line("fld ft1, 0(t0)");
        ctx.out.line("fcvt.w.d t1, ft1");
        ctx.out.line("slli t1, t1, 3");
        ctx.out.line("la t2, heap");
        ctx.out.line("add t0, t2, t1");
        ctx.out.line("fsd " + freg + ", 0(t0)");
        bumpHeap();
    }
}
