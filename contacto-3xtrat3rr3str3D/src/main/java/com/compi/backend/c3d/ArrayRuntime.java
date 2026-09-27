package com.compi.backend.c3d;

import java.util.List;

public final class ArrayRuntime {

    private ArrayRuntime() {
    }

    public static String allocate(C3DGenerator c3d, int celdas) {
        String base = c3d.newTemp();
        c3d.emit(QuadrupleOp.GET_H, null, null, base);
        String fin = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, base, String.valueOf(celdas), fin);
        c3d.emit(QuadrupleOp.SET_H, fin, null, null);
        return base;
    }

    public static String address(C3DGenerator c3d, String base, List<String> indices,
                                   int[] sizes) {
        String address = base;
        for (int d = 0; d < indices.size(); d++) {

            int step = productFrom(sizes, d + 1);
            String addend = indices.get(d);
            if (step != 1) {
                String scaled = c3d.newTemp();
                c3d.emit(QuadrupleOp.MUL, addend, String.valueOf(step), scaled);
                addend = scaled;
            }
            String next = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, address, addend, next);
            address = next;
        }
        return address;
    }

    public static String leer(C3DGenerator c3d, String base, List<String> indices,
                              int[] sizes) {
        String valor = c3d.newTemp();
        c3d.emit(QuadrupleOp.HEAP_GET, address(c3d, base, indices, sizes), null, valor);
        return valor;
    }

    public static void write(C3DGenerator c3d, String base, List<String> indices,
                                int[] sizes, String valor) {
        c3d.emit(QuadrupleOp.HEAP_SET, address(c3d, base, indices, sizes), valor, null);
    }

    public static void writeConstant(C3DGenerator c3d, String base, int[] indices,
                                         int[] sizes, String valor) {
        c3d.emit(QuadrupleOp.HEAP_SET, constantAddress(c3d, base, indices, sizes), valor, null);
    }

    public static String constantAddress(C3DGenerator c3d, String base, int[] indices,
                                             int[] sizes) {
        int offset = 0;
        for (int d = 0; d < indices.length; d++) {
            offset += indices[d] * productFrom(sizes, d + 1);
        }
        return add(c3d, base, offset);
    }

    public static String add(C3DGenerator c3d, String base, int offset) {
        if (offset == 0) {
            return base;
        }
        String address = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, base, String.valueOf(offset), address);
        return address;
    }

    public static int offset(int[] indices, int[] sizes) {
        int offset = 0;
        for (int d = 0; d < indices.length; d++) {
            offset += indices[d] * productFrom(sizes, d + 1);
        }
        return offset;
    }

    public static int productFrom(int[] sizes, int from) {
        int product = 1;
        for (int d = from; d < sizes.length; d++) {
            product *= Math.max(1, sizes[d]);
        }
        return product;
    }
}
