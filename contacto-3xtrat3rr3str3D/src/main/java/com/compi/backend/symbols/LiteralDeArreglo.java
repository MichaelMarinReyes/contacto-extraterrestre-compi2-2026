package com.compi.backend.symbols;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostico;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Comprobacion de la forma de un arreglo: que el inicializador tenga exactamente
 * las dimensiones que dice la declaracion, del mismo tamano y con valores del
 * tipo del elemento.
 *
 * <p>Es la misma regla para los tres lenguajes, asi que vive aqui y no repetida
 * en cada visitante. Cada lenguaje solo tiene que traer el tipo del literal
 * (llaves anidadas incluidas) y llamarlo.</p>
 */
public final class LiteralDeArreglo {

    private LiteralDeArreglo() {
    }

    /**
     * Comprueba un inicializador contra el tipo declarado.
     *
     * @param nombre    nombre del arreglo, para el mensaje
     * @param declarado tipo tal y como lo escribio el fuente
     * @param obtenido  tipo del literal, con sus dimensiones deducidas
     * @return el primer error que se encuentre, o null si el literal encaja
     */
    public static CompilationError validar(String nombre, Type declarado, Type obtenido,
                                           ParserRuleContext ctx) {
        if (declarado == null || obtenido == null || !declarado.isArray() || !obtenido.isArray()) {
            return null;
        }

        int declaradas = declarado.getDimensions();
        int encontradas = obtenido.getDimensions();
        if (declaradas != encontradas) {
            return Diagnostico.formaDelInicializador(nombre, declaradas, encontradas, ctx);
        }

        int[] esperados = declarado.getSizes();
        int[] hallados = obtenido.getSizes();
        for (int d = 0; d < declaradas; d++) {
            // Una dimension sin tamano declarado (los "[]" de Zetariano) se
            // deduce del inicializador, asi que no se comprueba.
            if (esperados[d] > 0 && esperados[d] != hallados[d]) {
                return Diagnostico.filasDesiguales(nombre, d, esperados[d], hallados[d], ctx);
            }
        }

        Type elementoDeclarado = declarado.getElementType();
        Type elementoObtenido = obtenido.getElementType();
        if (elementoDeclarado != null && elementoObtenido != null
                && !elementoObtenido.isAssignableTo(elementoDeclarado)) {
            return Diagnostico.elementoIncompatible(nombre, elementoDeclarado, elementoObtenido, ctx);
        }
        return null;
    }

    /**
     * Tipo que queda para la variable cuando el fuente deja el tamano abierto.
     *
     * <p>Es el caso de Zetariano, que admite {@code int[][] m = {...}}: los
     * corchetes no dicen cuanto y lo dice el inicializador. Si el arreglo ya
     * venia con los tamanos escritos, se respeta lo declarado y el
     * inicializador solo se comprueba.</p>
     */
    public static Type completarTamanos(Type declarado, Type obtenido) {
        if (declarado == null || !declarado.isArray()) {
            return declarado;
        }
        if (declarado.tieneTamanos() || obtenido == null || !obtenido.isArray()) {
            return declarado;
        }
        return declarado.conTamanos(obtenido.getSizes());
    }
}
