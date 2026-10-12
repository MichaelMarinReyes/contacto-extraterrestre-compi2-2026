package com.compi.backend.riscv;

import com.compi.backend.c3d.Quadruple;
import com.compi.backend.symbols.SymbolTable;

import java.util.List;

/**
 * Punto de entrada del módulo RISC-V. Traduce las cuartetas del código intermedio
 * a un único programa en ensamblador RISC-V (RV32IMF/D) ejecutable en RARS, con
 * la pila y el montículo como memoria y {@code ecall} para la entrada y salida.
 *
 * <p>Es un módulo nuevo e independiente del emisor de C: no reutiliza ninguna de
 * sus clases de emisión, solo consume el mismo código intermedio.</p>
 */
public final class RiscvAssembler {

    private RiscvAssembler() {
    }

    public static String generateAssembly(List<Quadruple> quadruples, SymbolTable symbolTable) {
        return new RiscvTranslator(quadruples, symbolTable).build();
    }
}
