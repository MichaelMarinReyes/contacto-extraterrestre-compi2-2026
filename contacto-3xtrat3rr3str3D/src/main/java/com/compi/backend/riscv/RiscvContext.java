package com.compi.backend.riscv;

import java.util.ArrayList;
import java.util.List;

/** Estado compartido por los emisores de ensamblador durante la traducción. */
final class RiscvContext {

    final RiscvWriter out = new RiscvWriter();
    final RiscvLayout layout;
    final RiscvConstants constants = new RiscvConstants();
    final RiscvAccess access;
    final List<String> pending = new ArrayList<>();
    final List<Integer> callSites = new ArrayList<>();
    String function = RiscvLayout.ENTRY;
    int nextCallId = 1;
    boolean returnValueSet;
    private int nextUid = 1;

    RiscvContext(RiscvLayout layout) {
        this.layout = layout;
        this.access = new RiscvAccess(this);
    }

    /** Etiqueta interna única para las rutinas de soporte en línea. */
    String uid() {
        return "L_r" + nextUid++;
    }
}
