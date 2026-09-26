package com.compi.backend.c3d;

import java.util.List;

/**
 * Emision de 3D para arreglos y matrices, igual para los tres lenguajes.
 *
 * <p>Los arreglos se guardan <strong>aplanados</strong> en el heap, que es lo que
 * pide el enunciado ("los arreglos a bajo nivel deben estar aplanados"): una
 * matriz {@code [3][2]} son seis celdas contiguas, no un arreglo de arreglos. La
 * variable que lo contiene guarda la direccion de la primera celda, y un acceso
 * {@code a[i][j]} se traduce a {@code base + i*2 + j}, que es el mismo
 * direccionamiento por indice que usan las estructuras.</p>
 *
 * <p>No hace falta ningun opcode nuevo: la direccion se compone con {@code ADD} y
 * {@code MUL} y las celdas se tocan con {@code HEAP_SET} y {@code HEAP_GET}.</p>
 */
public final class Arreglos {

    private Arreglos() {
    }

    /**
     * Reserva {@code celdas} contiguas en el heap.
     *
     * @return el temporal que queda con la direccion de la primera celda, que es
     *         la que se guarda en la variable
     */
    public static String reservar(C3DGenerator c3d, int celdas) {
        String base = c3d.newTemp();
        c3d.emit(QuadrupleOp.GET_H, null, null, base);
        String fin = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, base, String.valueOf(celdas), fin);
        c3d.emit(QuadrupleOp.SET_H, fin, null, null);
        return base;
    }

    /**
     * Direccion de un elemento dentro de un arreglo.
     *
     * @param base    temporal con la direccion de la primera celda
     * @param indices un temporal por indice, en el orden en que se escribio
     * @param sizes   longitud de cada dimension, de la exterior a la interior
     * @return el temporal con la direccion de esa celda
     */
    public static String direccion(C3DGenerator c3d, String base, List<String> indices,
                                   int[] sizes) {
        String direccion = base;
        for (int d = 0; d < indices.size(); d++) {
            // Cuantas celdas se salta una fila al bajar una dimension.
            int paso = productoDe(sizes, d + 1);
            String sumando = indices.get(d);
            if (paso != 1) {
                String escalado = c3d.newTemp();
                c3d.emit(QuadrupleOp.MUL, sumando, String.valueOf(paso), escalado);
                sumando = escalado;
            }
            String siguiente = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, direccion, sumando, siguiente);
            direccion = siguiente;
        }
        return direccion;
    }

    /** Lee una celda del arreglo. */
    public static String leer(C3DGenerator c3d, String base, List<String> indices,
                              int[] sizes) {
        String valor = c3d.newTemp();
        c3d.emit(QuadrupleOp.HEAP_GET, direccion(c3d, base, indices, sizes), null, valor);
        return valor;
    }

    /** Escribe una celda del arreglo. */
    public static void escribir(C3DGenerator c3d, String base, List<String> indices,
                                int[] sizes, String valor) {
        c3d.emit(QuadrupleOp.HEAP_SET, direccion(c3d, base, indices, sizes), valor, null);
    }

    /**
     * Escribe una celda cuyos indices ya se conocen al compilar.
     *
     * <p>Es el caso del inicializador de un arreglo, donde la posicion de cada
     * valor sale de contar las comas.</p>
     */
    public static void escribirConstante(C3DGenerator c3d, String base, int[] indices,
                                         int[] sizes, String valor) {
        c3d.emit(QuadrupleOp.HEAP_SET, direccionConstante(c3d, base, indices, sizes), valor, null);
    }

    /**
     * Direccion de una celda cuyo indice se conoce al compilar.
     *
     * <p>Es el caso de un inicializador: los indices son constantes, as que no hace
     * falta emitir aritmetica y la direccion sale tal cual.</p>
     */
    public static String direccionConstante(C3DGenerator c3d, String base, int[] indices,
                                             int[] sizes) {
        int desplazamiento = 0;
        for (int d = 0; d < indices.length; d++) {
            desplazamiento += indices[d] * productoDe(sizes, d + 1);
        }
        return sumar(c3d, base, desplazamiento);
    }

    /**
     * Direccion de una celda que esta a una distancia constante de la base.
     *
     * <p>Es lo que necesitan las estructuras: el campo {@code promedio} de
     * {@code p.promedio} esta {@code n} celdas despues de la direccion de
     * {@code p}, porque los campos se guardan en aplanados y en el orden en que
     * se declararon.</p>
     */
    public static String sumar(C3DGenerator c3d, String base, int desplazamiento) {
        if (desplazamiento == 0) {
            return base;
        }
        String direccion = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, base, String.valueOf(desplazamiento), direccion);
        return direccion;
    }

    /**
     * Producto de las dimensiones desde {@code desde} hasta el final.
     *
     * <p>Con {@code [3][2]} y {@code desde = 1} devuelve 2: una fila tiene dos
     * celdas. Con {@code desde} igual al numero de dimensiones devuelve 1.</p>
     */
    public static int productoDe(int[] sizes, int desde) {
        int producto = 1;
        for (int d = desde; d < sizes.length; d++) {
            producto *= Math.max(1, sizes[d]);
        }
        return producto;
    }
}
