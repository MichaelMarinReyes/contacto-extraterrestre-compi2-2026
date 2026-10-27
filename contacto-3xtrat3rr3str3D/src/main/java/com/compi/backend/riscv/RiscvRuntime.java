package com.compi.backend.riscv;

import com.compi.backend.c3d.Quadruple;

/**
 * Rutinas de soporte en línea: concatenación de cadenas, conversión de números a
 * texto y entrada/salida de texto y caracteres. Toda la E/S se hace con
 * {@code ecall} de RARS, por lo que no dependen de ninguna biblioteca externa.
 */
final class RiscvRuntime {

    private final RiscvContext ctx;
    private final RiscvAccess access;

    RiscvRuntime(RiscvContext ctx) {
        this.ctx = ctx;
        this.access = ctx.access;
    }

    boolean isRuntime(String name) {
        return switch (name) {
            case "number_to_text", "char_to_text", "print_string", "read_text",
                 "read_character", "concat_cadenas" -> true;
            default -> false;
        };
    }

    void call(String name, String result) {
        String argument = ctx.pending.isEmpty() ? "0" : ctx.pending.remove(ctx.pending.size() - 1);
        switch (name) {
            case "number_to_text" -> numberToText(argument, result);
            case "char_to_text" -> charToText(argument, result);
            case "print_string" -> printString(argument);
            case "read_text" -> readText(result);
            case "read_character" -> readChar(result);
            default -> throw new IllegalStateException("Rutina de soporte desconocida: " + name);
        }
        ctx.returnValueSet = false;
    }

    /** Vuelca un literal de cadena al montículo y guarda su dirección. */
    void writeString(String result, String literal) {
        String content = literal.substring(1, literal.length() - 1);
        access.load("H", "ft2");
        if (result != null) {
            access.store(result, "ft2");
        }
        for (int i = 0; i < content.length(); i++) {
            access.heapAppendImmediate(characterCode(content, i));
        }
        access.heapAppendImmediate(0);
    }

    private static int characterCode(String content, int index) {
        char value = content.charAt(index);
        if (value == '\\' && index + 1 < content.length()) {
            char next = content.charAt(index + 1);
            return switch (next) {
                case 'n' -> '\n';
                case 't' -> '\t';
                case 'r' -> '\r';
                case '\\' -> '\\';
                case '"' -> '"';
                default -> '\\';
            };
        }
        return value;
    }

    private void charToText(String argument, String result) {
        access.load("H", "ft2");
        access.load(argument, "fa0");
        access.loadConstant("0", "ft3");
        access.heapAppendDouble("fa0");
        access.heapAppendDouble("ft3");
        if (result != null) {
            access.store(result, "ft2");
        }
    }

    void printStringOps(Quadruple q) {
        printString(q.getArg1());
    }

    private void printString(String argument) {
        String loop = ctx.uid();
        String end = ctx.uid();
        access.loadConstant("0", "ft3");
        access.loadConstant("-1", "ft4");
        access.load(argument, "fa0");
        ctx.out.line("fcvt.w.d t1, fa0");
        ctx.out.label(loop);
        ctx.out.line("slli t0, t1, 3");
        ctx.out.line("la t2, heap");
        ctx.out.line("add t0, t2, t0");
        ctx.out.line("fld ft0, 0(t0)");
        ctx.out.line("feq.d t3, ft0, ft3");
        ctx.out.line("bne t3, x0, " + end);
        ctx.out.line("feq.d t3, ft0, ft4");
        ctx.out.line("bne t3, x0, " + end);
        ctx.out.line("fcvt.w.d a0, ft0");
        ctx.out.line("li a7, 11");
        ctx.out.line("ecall");
        ctx.out.line("addi t1, t1, 1");
        ctx.out.line("j " + loop);
        ctx.out.label(end);
    }

    void readText(String target) {
        String loop = ctx.uid();
        String end = ctx.uid();
        access.load("H", "ft2");
        ctx.out.line("la a0, _txt");
        ctx.out.line("li a1, 4096");
        ctx.out.line("li a7, 8");
        ctx.out.line("ecall");
        ctx.out.line("li s0, 0");
        ctx.out.label(loop);
        ctx.out.line("la t0, _txt");
        ctx.out.line("add t0, t0, s0");
        ctx.out.line("lbu a0, 0(t0)");
        ctx.out.line("beq a0, x0, " + end);
        access.heapAppendInt("a0");
        ctx.out.line("addi s0, s0, 1");
        ctx.out.line("j " + loop);
        ctx.out.label(end);
        access.heapAppendImmediate(0);
        if (target != null) {
            access.store(target, "ft2");
        }
    }

    void readChar(String target) {
        ctx.out.line("li a7, 12");
        ctx.out.line("ecall");
        ctx.out.line("fcvt.d.w fa0, a0");
        if (target != null) {
            access.store(target, "fa0");
        }
    }

    /** Convierte el doble indicado a su texto decimal y guarda su dirección. */
    private void numberToText(String argument, String result) {
        String positive = ctx.uid();
        access.load("H", "ft2");
        access.loadConstant("0", "ft3");
        access.loadConstant("10", "ft6");
        access.load(argument, "fa0");
        ctx.out.line("flt.d t0, fa0, ft3");
        ctx.out.line("beq t0, x0, " + positive);
        access.heapAppendImmediate('-');
        ctx.out.line("fsub.d fa0, ft3, fa0");
        ctx.out.label(positive);
        ctx.out.line("fcvt.w.d s3, fa0, rtz");
        ctx.out.line("fcvt.d.w ft4, s3");
        ctx.out.line("fsub.d ft5, fa0, ft4");
        integerDigits("s3");
        fractionDigits("ft5", "ft3", "ft6");
        access.heapAppendImmediate(0);
        if (result != null) {
            access.store(result, "ft2");
        }
    }

    private void integerDigits(String reg) {
        String count = ctx.uid();
        String power = ctx.uid();
        String powerDone = ctx.uid();
        String emitLoop = ctx.uid();
        String emitDone = ctx.uid();
        ctx.out.line("li s1, 1");
        ctx.out.line("mv s4, " + reg);
        ctx.out.line("li s5, 10");
        ctx.out.label(count);
        ctx.out.line("blt s4, s5, " + power);
        ctx.out.line("div s4, s4, s5");
        ctx.out.line("addi s1, s1, 1");
        ctx.out.line("j " + count);
        ctx.out.label(power);
        ctx.out.line("li s2, 1");
        ctx.out.line("addi s4, s1, -1");
        String multiply = ctx.uid();
        ctx.out.label(multiply);
        ctx.out.line("beq s4, x0, " + powerDone);
        ctx.out.line("mul s2, s2, s5");
        ctx.out.line("addi s4, s4, -1");
        ctx.out.line("j " + multiply);
        ctx.out.label(powerDone);
        ctx.out.label(emitLoop);
        ctx.out.line("beq s2, x0, " + emitDone);
        ctx.out.line("div a0, " + reg + ", s2");
        ctx.out.line("addi a0, a0, 48");
        access.heapAppendInt("a0");
        ctx.out.line("rem " + reg + ", " + reg + ", s2");
        ctx.out.line("div s2, s2, s5");
        ctx.out.line("j " + emitLoop);
        ctx.out.label(emitDone);
    }

    private void fractionDigits(String frac, String zero, String ten) {
        String done = ctx.uid();
        String loop = ctx.uid();
        ctx.out.line("feq.d t0, " + frac + ", " + zero);
        ctx.out.line("bne t0, x0, " + done);
        access.heapAppendImmediate('.');
        ctx.out.line("li s6, 0");
        ctx.out.label(loop);
        ctx.out.line("fmul.d " + frac + ", " + frac + ", " + ten);
        ctx.out.line("fcvt.w.d a0, " + frac);
        ctx.out.line("fcvt.d.w ft7, a0");
        ctx.out.line("fsub.d " + frac + ", " + frac + ", ft7");
        ctx.out.line("addi a0, a0, 48");
        access.heapAppendInt("a0");
        ctx.out.line("addi s6, s6, 1");
        ctx.out.line("feq.d t0, " + frac + ", " + zero);
        ctx.out.line("bne t0, x0, " + done);
        ctx.out.line("li t1, 12");
        ctx.out.line("blt s6, t1, " + loop);
        ctx.out.label(done);
    }
}
