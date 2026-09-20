package com.compi.backend.c3d;

import java.util.List;
import java.util.Set;

public class CCodeEmitter {

    public static String generateCCode(List<Quadruple> quadruples, Set<String> declaredTemps) {
        StringBuilder sb = new StringBuilder();

        // Cabeceras de C
        sb.append("/* Codigo de Tres Direcciones generado para C */\n");
        sb.append("#include <stdio.h>\n");
        sb.append("#include <stdlib.h>\n");
        sb.append("#include <string.h>\n\n");

        // Simulación de Stack y Heap según especificación
        sb.append("/* Memoria Stack y Heap */\n");
        sb.append("double stack[100000];\n");
        sb.append("double heap[100000];\n");
        sb.append("double P = 0;\n");
        sb.append("double H = 0;\n\n");

        // Declaración global de temporales
        if (declaredTemps != null && !declaredTemps.isEmpty()) {
            sb.append("/* Variables temporales */\n");
            sb.append("double ");
            int count = 0;
            for (String t : declaredTemps) {
                if (count > 0) sb.append(", ");
                sb.append(t);
                count++;
                if (count % 15 == 0) sb.append("\n       ");
            }
            sb.append(";\n\n");
        }

        // Funciones auxiliares de runtime
        sb.append("/* Funciones de soporte runtime */\n");
        sb.append("void print_string(int heap_idx) {\n");
        sb.append("    int i = heap_idx;\n");
        sb.append("    while (heap[i] != 0 && heap[i] != -1) {\n");
        sb.append("        printf(\"%c\", (char)heap[i]);\n");
        sb.append("        i++;\n");
        sb.append("    }\n");
        sb.append("}\n\n");

        // Separar funciones intermedias de la función main
        boolean inMain = false;
        boolean inFunction = false;

        for (Quadruple q : quadruples) {
            if (q.getOp() == QuadrupleOp.FUNCTION_START) {
                if ("main".equals(q.getResult())) {
                    inMain = true;
                    sb.append("int main() {\n");
                } else {
                    inFunction = true;
                    sb.append(q.toString()).append("\n");
                }
            } else if (q.getOp() == QuadrupleOp.FUNCTION_END) {
                if (inMain) {
                    sb.append("    return 0;\n}\n\n");
                    inMain = false;
                } else {
                    sb.append(q.toString()).append("\n");
                    inFunction = false;
                }
            } else {
                if (q.getOp() == QuadrupleOp.LABEL) {
                    sb.append(q.toString()).append("\n");
                } else {
                    sb.append("    ").append(q.toString()).append("\n");
                }
            }
        }

        // Si nunca se cerró el main y está abierto
        if (inMain) {
            sb.append("    return 0;\n}\n");
        }

        return sb.toString();
    }
}
