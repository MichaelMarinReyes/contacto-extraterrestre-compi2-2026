package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.Arreglos;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;

public class PigLatinC3DVisitor extends PigLatinBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;

    /** A donde salta un "interrumpe": el final del ciclo mas interno. */
    private final Deque<String> salidaCiclo = new ArrayDeque<>();

    /** A donde salta un "perge": la prueba (o el paso) del ciclo mas interno. */
    private final Deque<String> vueltaCiclo = new ArrayDeque<>();

    public PigLatinC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    @Override
    public String visitVariables(PigLatinParser.VariablesContext ctx) {
        for (PigLatinParser.DeclaracionContext d : ctx.declaracion()) {
            visit(d);
        }
        for (PigLatinParser.Arreglo_declaracionContext a : ctx.arreglo_declaracion()) {
            visit(a);
        }
        return null;
    }

    @Override
    public String visitDeclaracion(PigLatinParser.DeclaracionContext ctx) {
        // Un "series ..." llega como una alternativa de "declaracion".
        if (ctx.arreglo_declaracion() != null) {
            return visit(ctx.arreglo_declaracion());
        }
        if (!ctx.VARIABLE().isEmpty() && ctx.expresion() != null) {
            String varName = ctx.VARIABLE(0).getText();
            Symbol s = symbolTable.resolve(varName);
            String val = visit(ctx.expresion());
            if (s != null) {
                c3d.emit(QuadrupleOp.STACK_SET, String.valueOf(s.getOffset()), val, null);
            }
        }
        return null;
    }

    @Override
    public String visitMaior(PigLatinParser.MaiorContext ctx) {
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, "main");
        symbolTable.enterScope("maior", 0);

        for (PigLatinParser.SentenciaContext s : ctx.sentencia()) {
            visit(s);
        }

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, "main");
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitImprimir_sentencia(PigLatinParser.Imprimir_sentenciaContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            String childText = ctx.getChild(i).getText();
            if (">".equals(childText) || ">>".equals(childText) || ";".equals(childText)) {
                continue;
            }

            if (childText.startsWith("\"")) {
                // Cadena literal
                String content = childText.substring(1, childText.length() - 1);
                String strStart = c3d.newTemp();
                c3d.emitAssign(strStart, "H");
                for (int c = 0; c < content.length(); c++) {
                    c3d.emit(QuadrupleOp.HEAP_SET, "H", String.valueOf((int) content.charAt(c)), null);
                    c3d.emit(QuadrupleOp.ADD, "H", "1", "H");
                }
                c3d.emit(QuadrupleOp.HEAP_SET, "H", "0", null);
                c3d.emit(QuadrupleOp.ADD, "H", "1", "H");
                c3d.emit(QuadrupleOp.PRINT_STR, strStart, null, null);
            } else {
                String temp;
                if (ctx.getChild(i) instanceof PigLatinParser.Acceso_miembroContext acceso) {
                    // ">> matriz[0][1]" imprime una celda del arreglo.
                    String elemento = leerElemento(acceso);
                    temp = elemento != null ? elemento : visit(acceso);
                } else if (ctx.getChild(i) instanceof PigLatinParser.Llamada_funcionContext llamada) {
                    // ">> miObjeto.getNombre()" imprime lo que retorne la llamada.
                    temp = visit(llamada);
                } else {
                    Symbol s = symbolTable.resolve(childText);
                    if (s == null) {
                        continue;
                    }
                    temp = c3d.newTemp();
                    c3d.emit(QuadrupleOp.STACK_GET, String.valueOf(s.getOffset()), null, temp);
                }
                c3d.emit(QuadrupleOp.PRINT_INT, temp, null, null);
            }
        }
        c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
        return null;
    }

    @Override
    public String visitAsignacion_sentencia(PigLatinParser.Asignacion_sentenciaContext ctx) {
        if (ctx.acceso_miembro() != null) {
            // "matriz[1][0] = 7": el valor va a una celda del heap, no a la pila.
            if (ctx.expresion() != null) {
                escribirElemento(ctx.acceso_miembro(), visit(ctx.expresion()));
            }
            return null;
        }
        if (!ctx.VARIABLE().isEmpty() && ctx.expresion() != null) {
            String varName = ctx.VARIABLE(0).getText();
            Symbol s = symbolTable.resolve(varName);
            String val = visit(ctx.expresion());
            if (s != null) {
                c3d.emit(QuadrupleOp.STACK_SET, String.valueOf(s.getOffset()), val, null);
            }
        }
        return null;
    }

    @Override
    public String visitSi_sentencia(PigLatinParser.Si_sentenciaContext ctx) {
        // Toda la cadena comparte una sola etiqueta de final: cada rama que se
        // cumple salta a ella y la ultima cae sola, sin un goto de mas.
        String fin = c3d.newLabel();
        emitirCadenaSi(ctx, fin);
        c3d.emitLabel(fin);
        return null;
    }

    /**
     * Emite el "si" con la cadena de "aliter" que lo sigue.
     *
     * <p>La rama falsa de cada prueba es la rama que viene despues, y todas las
     * ramas que se ejecutan terminan saltando al final de la cadena:</p>
     * <pre>
     * if (x &gt; z) goto et1
     * goto et2
     * et1:
     *   ...
     *   goto et3
     * et2:
     *   if (a &gt; b) goto et4
     *   ...
     * et3:
     * </pre>
     */
    private void emitirCadenaSi(PigLatinParser.Si_sentenciaContext ctx, String fin) {
        String verdadera = c3d.newLabel();
        String falsa = c3d.newLabel();
        emitirSaltoDeCondicion(ctx.condicion(), verdadera, falsa);

        c3d.emitLabel(verdadera);
        visitarSentencias(sentenciasDelSi(ctx));
        c3d.emitGoto(fin);

        c3d.emitLabel(falsa);
        List<PigLatinParser.Aliter_bloqueContext> aliteres = ctx.aliter_bloque();
        int indice = 0;
        // Un "aliter" con condicion es un "si" mas de la misma cadena: se emite
        // con su propia prueba y vuelve a caer en la rama que le siga.
        if (!aliteres.isEmpty() && aliteres.get(0).condicion() != null) {
            emitirRamaConCondicion(aliteres.get(0), fin);
            indice = 1;
        }
        for (; indice < aliteres.size(); indice++) {
            visitarSentencias(aliteres.get(indice).sentencia());
        }
        visitarSentencias(sentenciasDelAliterFinal(ctx));
    }

    /** Emite una rama con su propia condicion, como un "si" mas. */
    private void emitirRamaConCondicion(PigLatinParser.Aliter_bloqueContext aliter, String fin) {
        String verdadera = c3d.newLabel();
        String falsa = c3d.newLabel();
        emitirSaltoDeCondicion(aliter.condicion(), verdadera, falsa);

        c3d.emitLabel(verdadera);
        visitarSentencias(aliter.sentencia());
        c3d.emitGoto(fin);

        c3d.emitLabel(falsa);
    }

    /**
     * Emite la prueba de una condicion con la forma que dio el docente: el
     * salto relacional directo cuando la condicion es una comparacion
     * ({@code if (x &gt; z) goto et1}) y, detras, el salto al camino falso.
     *
     * <p>Una condicion mas compuesta (con {@code et} o {@code non} de por
     * medio) se evalua antes en un temporal booleano, que es lo unico que se
     * puede saltar.</p>
     */
    private void emitirSaltoDeCondicion(PigLatinParser.CondicionContext condicion,
                                        String verdadera, String falsa) {
        PigLatinParser.Primaria_logicaContext comparacion = comparacionSimple(condicion);
        if (comparacion != null) {
            String izquierda = visit(comparacion.expresion(0));
            String derecha = visit(comparacion.expresion(1));
            c3d.emit(relacionalDe(comparacion.operador_relacional().getText()),
                    izquierda, derecha, verdadera);
            c3d.emitGoto(falsa);
            return;
        }
        c3d.emit(QuadrupleOp.IF_TRUE, visit(condicion), null, verdadera);
        c3d.emitGoto(falsa);
    }

    /**
     * La comparacion que hay dentro de una condicion, si es la unica prueba que
     * lleva; null en cuanto hay un {@code et} o un {@code non} alrededor.
     */
    private PigLatinParser.Primaria_logicaContext comparacionSimple(
            PigLatinParser.CondicionContext condicion) {
        if (condicion.condicion() != null) {
            return null;
        }
        PigLatinParser.ConjuncionContext conjuncion = condicion.conjuncion();
        if (conjuncion == null || conjuncion.conjuncion() != null) {
            return null;
        }
        PigLatinParser.Negacion_logicaContext negacion = conjuncion.negacion_logica();
        if (negacion == null || negacion.NEGACION() != null) {
            return null;
        }
        PigLatinParser.Primaria_logicaContext primaria = negacion.primaria_logica();
        if (primaria == null || primaria.expresion().size() != 2
                || primaria.operador_relacional() == null) {
            return null;
        }
        return primaria;
    }

    /** Traduce el operador relacional de la gramatica a su cuarteta de salto. */
    private QuadrupleOp relacionalDe(String operador) {
        return switch (operador) {
            case "==" -> QuadrupleOp.IF_EQ;
            case "!=" -> QuadrupleOp.IF_NE;
            case "<" -> QuadrupleOp.IF_LT;
            case "<=" -> QuadrupleOp.IF_LE;
            case ">" -> QuadrupleOp.IF_GT;
            case ">=" -> QuadrupleOp.IF_GE;
            default -> QuadrupleOp.IF_EQ;
        };
    }

    private void visitarSentencias(List<PigLatinParser.SentenciaContext> sentencias) {
        for (PigLatinParser.SentenciaContext s : sentencias) {
            visit(s);
        }
    }

    /**
     * Sentencias del bloque del "si".
     *
     * <p>Son las primeras del {@code si_sentencia}: las de los "aliter" viven en
     * su propio contexto, y las del "aliter" final, que no lleva condicion,
     * tambien cuelgan directamente de aqui y por eso no se pueden leer con
     * {@code sentencia()} a secas.</p>
     */
    private List<PigLatinParser.SentenciaContext> sentenciasDelSi(
            PigLatinParser.Si_sentenciaContext ctx) {
        List<PigLatinParser.SentenciaContext> propias = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree hijo = ctx.getChild(i);
            if (hijo instanceof PigLatinParser.Aliter_bloqueContext) {
                break;
            }
            if (hijo instanceof PigLatinParser.SentenciaContext s) {
                propias.add(s);
            }
        }
        return propias;
    }

    /** Sentencias del "aliter" final, el que no lleva condicion. */
    private List<PigLatinParser.SentenciaContext> sentenciasDelAliterFinal(
            PigLatinParser.Si_sentenciaContext ctx) {
        List<PigLatinParser.SentenciaContext> propias = new ArrayList<>();
        boolean despuesDeAliter = false;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree hijo = ctx.getChild(i);
            if (hijo instanceof PigLatinParser.Aliter_bloqueContext) {
                despuesDeAliter = true;
                continue;
            }
            if (despuesDeAliter && hijo instanceof PigLatinParser.SentenciaContext s) {
                propias.add(s);
            }
        }
        return propias;
    }

    // ===================== Ciclos =====================

    @Override
    public String visitCiclo_dum(PigLatinParser.Ciclo_dumContext ctx) {
        // dum ( condicion ) { ... } finis:  la prueba va antes del cuerpo y
        // cada vuelta vuelve a ella.
        String prueba = c3d.newLabel();
        String cuerpo = c3d.newLabel();
        String fin = c3d.newLabel();

        c3d.emitLabel(prueba);
        emitirSaltoDeCondicion(ctx.condicion(), cuerpo, fin);

        c3d.emitLabel(cuerpo);
        salidaCiclo.push(fin);
        vueltaCiclo.push(prueba);
        visitarSentencias(ctx.sentencia());
        salidaCiclo.pop();
        vueltaCiclo.pop();
        c3d.emitGoto(prueba);

        c3d.emitLabel(fin);
        return null;
    }

    @Override
    public String visitCiclo_facere(PigLatinParser.Ciclo_facereContext ctx) {
        // facere { ... } dum ( condicion ):  el cuerpo se ejecuta al menos una
        // vez, asi que la prueba va detras, como en el "hacer ... mientras".
        String cuerpo = c3d.newLabel();
        String prueba = c3d.newLabel();
        String fin = c3d.newLabel();

        c3d.emitLabel(cuerpo);
        salidaCiclo.push(fin);
        vueltaCiclo.push(prueba);
        visitarSentencias(ctx.sentencia());
        salidaCiclo.pop();
        vueltaCiclo.pop();

        c3d.emitLabel(prueba);
        emitirSaltoDeCondicion(ctx.condicion(), cuerpo, fin);

        c3d.emitLabel(fin);
        return null;
    }

    @Override
    public String visitCiclo_per(PigLatinParser.Ciclo_perContext ctx) {
        // per ( inicio; condicion; incremento ) { ... }:  el incremento es la
        // vuelta del ciclo, asi que un "perge" salta a el y no a la prueba.
        String prueba = c3d.newLabel();
        String cuerpo = c3d.newLabel();
        String paso = c3d.newLabel();
        String fin = c3d.newLabel();

        if (ctx.inicializacion_per() != null) {
            visit(ctx.inicializacion_per());
        }
        c3d.emitLabel(prueba);
        emitirSaltoDeCondicion(ctx.condiciones_per().condicion(), cuerpo, fin);

        c3d.emitLabel(cuerpo);
        salidaCiclo.push(fin);
        vueltaCiclo.push(paso);
        visitarSentencias(ctx.sentencia());
        salidaCiclo.pop();
        vueltaCiclo.pop();

        c3d.emitLabel(paso);
        if (ctx.incremento_per() != null) {
            visit(ctx.incremento_per());
        }
        c3d.emitGoto(prueba);

        c3d.emitLabel(fin);
        return null;
    }

    @Override
    public String visitSalto_sentencia(PigLatinParser.Salto_sentenciaContext ctx) {
        // "perge" sigue con la vuelta del ciclo e "interrumpe" lo termina. Fuera
        // de un ciclo no hay a donde saltar, y el error ya lo dio el visitante
        // semantico, asi que aqui no se emite nada.
        if (ctx.PERGE() != null && !vueltaCiclo.isEmpty()) {
            c3d.emitGoto(vueltaCiclo.peek());
        } else if (ctx.INTERRUMPE() != null && !salidaCiclo.isEmpty()) {
            c3d.emitGoto(salidaCiclo.peek());
        }
        return null;
    }

    // ===================== Condicionales =====================

    @Override
    public String visitCondicion(PigLatinParser.CondicionContext ctx) {
        if (ctx.conjuncion() != null) {
            return visit(ctx.conjuncion());
        }
        return visit(ctx.condicion());
    }

    @Override
    public String visitConjuncion(PigLatinParser.ConjuncionContext ctx) {
        if (ctx.conjuncion() != null) {
            String acc = visit(ctx.conjuncion());
            String right = visit(ctx.negacion_logica());
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.AND, acc, right, temp);
            return temp;
        }
        return visit(ctx.negacion_logica());
    }

    @Override
    public String visitNegacion_logica(PigLatinParser.Negacion_logicaContext ctx) {
        if (ctx.NEGACION() != null) {
            String inner = visit(ctx.negacion_logica());
            String temp = c3d.newTemp();
            // non x  ->  x == 0
            c3d.emit(QuadrupleOp.IF_EQ, inner, "0", temp);
            return temp;
        }
        return visit(ctx.primaria_logica());
    }

    @Override
    public String visitPrimaria_logica(PigLatinParser.Primaria_logicaContext ctx) {
        if (ctx.expresion().size() >= 2) {
            String left = visit(ctx.expresion(0));
            String right = visit(ctx.expresion(1));
            String symbol = ctx.operador_relacional().getText();
            QuadrupleOp op = relacionalDe(symbol);
            String temp = c3d.newTemp();
            c3d.emit(op, left, right, temp);
            return temp;
        }
        if (ctx.expresion() != null) {
            return visit(ctx.expresion(0));
        }
        if (ctx.VERUM() != null) return "1";
        if (ctx.FALSUS() != null) return "0";
        if (ctx.VARIABLE() != null) {
            Symbol s = symbolTable.resolve(ctx.VARIABLE().getText());
            if (s != null) {
                String temp = c3d.newTemp();
                c3d.emit(QuadrupleOp.STACK_GET, String.valueOf(s.getOffset()), null, temp);
                return temp;
            }
        }
        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }
        return "0";
    }

    @Override
    public String visitExpresion(PigLatinParser.ExpresionContext ctx) {
        if (ctx.termino() != null && ctx.termino().size() == 1) {
            return visit(ctx.termino(0));
        }

        if (!ctx.operacion_aritmetica().isEmpty()) {
            String current = visit(ctx.termino(0));
            for (int i = 1; i < ctx.termino().size(); i++) {
                String next = visit(ctx.termino(i));
                String op = ctx.operacion_aritmetica(i - 1).getText();
                String temp = c3d.newTemp();
                QuadrupleOp qOp = "+".equals(op) ? QuadrupleOp.ADD :
                        "-".equals(op) ? QuadrupleOp.SUB :
                        "*".equals(op) ? QuadrupleOp.MUL : QuadrupleOp.DIV;
                c3d.emit(qOp, current, next, temp);
                current = temp;
            }
            return current;
        }

        return "0";
    }

    @Override
    public String visitTermino(PigLatinParser.TerminoContext ctx) {
        if (ctx.NUMERO_ENTERO() != null) return ctx.NUMERO_ENTERO().getText();
        if (ctx.NUMERO_DECIMAL() != null) return ctx.NUMERO_DECIMAL().getText();
        if (ctx.VERUM() != null) return "1";
        if (ctx.FALSUS() != null) return "0";

        if (ctx.VARIABLE() != null) {
            String name = ctx.VARIABLE().getText();
            Symbol s = symbolTable.resolve(name);
            if (s != null) {
                String temp = c3d.newTemp();
                c3d.emit(QuadrupleOp.STACK_GET, String.valueOf(s.getOffset()), null, temp);
                return temp;
            }
            return name;
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        // Menos unario: "-x" es una instruccion propia, no un 0 menos x.
        if (ctx.MENOS() != null) {
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.NEG, visit(ctx.termino()), null, temp);
            return temp;
        }

        return "0";
    }

    // ===================== Arreglos y matrices =====================

    /**
     * Declara un arreglo: reserva sus celdas en el heap y guarda la direccion de
     * la primera en la variable.
     *
     * <p>El enunciado pide que los arreglos se guarden aplanados, asi que una
     * matriz de 2x3 son seis celdas contiguas y no un arreglo de filas.</p>
     */
    @Override
    public String visitArreglo_declaracion(PigLatinParser.Arreglo_declaracionContext ctx) {
        Symbol s = symbolTable.resolve(ctx.VARIABLE(0).getText());
        if (s == null || s.getType() == null || !s.getType().isArray()) {
            return null;
        }
        Type tipo = s.getType();
        int celdas = tipo.totalSize();
        if (celdas <= 0) {
            // Sin tamanos conocidos no hay nada que reservar. El analizador
            // semantico ya se encargo de avisar de ello.
            return null;
        }
        String base = Arreglos.reservar(c3d, celdas);
        c3d.emit(QuadrupleOp.STACK_SET, String.valueOf(s.getOffset()), base, null);
        if (ctx.elemento_arreglo() != null) {
            int[] sizes = tipo.getSizes();
            llenar(base, ctx.elemento_arreglo(), sizes, new int[sizes.length], 0);
        }
        return null;
    }

    /**
     * Escribe los valores de un inicializador, celda a celda.
     *
     * <p>El recorrido va siguiendo las llaves: cada nivel de llaves es una
     * dimension mas, y el numero de elemento dentro de ese nivel es el indice de
     * esa dimension.</p>
     */
    private void llenar(String base, PigLatinParser.Elemento_arregloContext ctx, int[] sizes,
                        int[] indices, int dimension) {
        List<PigLatinParser.Elemento_arreglo_valorContext> valores = ctx.elemento_arreglo_valor();
        for (int i = 0; i < valores.size(); i++) {
            PigLatinParser.Elemento_arreglo_valorContext valor = valores.get(i);
            if (dimension >= indices.length) {
                break;
            }
            indices[dimension] = i;
            if (valor.LLAVE_IZQ() != null) {
                if (valor.elemento_arreglo() != null) {
                    llenar(base, valor.elemento_arreglo(), sizes, indices, dimension + 1);
                }
            } else {
                String dato = visit(valor.expresion());
                Arreglos.escribirConstante(c3d, base, indices, sizes, dato);
            }
        }
    }

    /**
     * Lee el elemento al que apunta una cadena de indices.
     *
     * <p>Solo cuando la cadena agota todas las dimensiones del arreglo: pedir una
     * fila entera ({@code matriz[0]}) no tiene un valor simple que devolver, y el
     * analizador semantico no lo marca como error porque en un lenguaje con
     * arreglos de arreglo si lo seria.</p>
     *
     * @return el temporal con el valor, o null si no es un acceso a elemento
     */
    private String leerElemento(PigLatinParser.Acceso_miembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.VARIABLE(0).getText());
        if (base == null || base.getType() == null || !base.getType().isArray()) {
            return null;
        }
        int[] sizes = base.getType().getSizes();
        if (!base.getType().tieneTamanos() || contarIndices(ctx) != sizes.length) {
            return null;
        }
        String direccionBase = leerVariable(base);
        return Arreglos.leer(c3d, direccionBase, indicesDe(ctx), sizes);
    }

    /** Escribe en el elemento al que apunta una cadena de indices. */
    private void escribirElemento(PigLatinParser.Acceso_miembroContext ctx, String valor) {
        Symbol base = symbolTable.resolve(ctx.VARIABLE(0).getText());
        if (base == null || base.getType() == null || !base.getType().isArray()) {
            return;
        }
        int[] sizes = base.getType().getSizes();
        if (!base.getType().tieneTamanos() || contarIndices(ctx) != sizes.length) {
            return;
        }
        Arreglos.escribir(c3d, leerVariable(base), indicesDe(ctx), sizes, valor);
    }

    /** La direccion de la primera celda, que es lo que guarda la variable. */
    private String leerVariable(Symbol s) {
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.STACK_GET, String.valueOf(s.getOffset()), null, temp);
        return temp;
    }

    /** Los temporales de cada indice, en el orden en que se escribieron. */
    private List<String> indicesDe(PigLatinParser.Acceso_miembroContext ctx) {
        List<String> indices = new ArrayList<>();
        for (int i = 1; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof PigLatinParser.ExpresionContext) {
                indices.add(visit(ctx.getChild(i)));
            }
        }
        return indices;
    }

    private static int contarIndices(PigLatinParser.Acceso_miembroContext ctx) {
        int indices = 0;
        for (int i = 1; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().equals("[")) {
                indices++;
            }
        }
        return indices;
    }

    @Override
    public String visitAcceso_miembro(PigLatinParser.Acceso_miembroContext ctx) {
        String elemento = leerElemento(ctx);
        if (elemento != null) {
            return elemento;
        }
        // Un acceso con puntos (un atributo de un objeto) todavia no se genera:
        // aqui se devuelve el valor de la variable base para no romper la
        // generacion. El analizador semantico es quien avisa de estos accesos.
        return visit(ctx.VARIABLE(0));
    }

    // ===================== Llamadas a metodos =====================

    /**
     * Emite una llamada y devuelve el temporal donde queda lo que retorna.
     *
     * <p>PigLatin solo declara funciones en otros lenguajes, asi que aqui no se
     * mira si la llamada existe: se genera siempre y es el analizador semantico
     * quien avisa si no encuentra el simbolo.</p>
     */
    @Override
    public String visitLlamada_funcion(PigLatinParser.Llamada_funcionContext ctx) {
        String ret = c3d.newTemp();
        c3d.emit(QuadrupleOp.CALL, nombreDeLaLlamada(ctx), "0", ret);
        return ret;
    }

    /**
     * El nombre de la funcion a la que se llama.
     *
     * <p>En "hablar()" es el identificador de la regla. En "miObjeto.getNombre()"
     * el receptor forma parte de la cadena de acceso, asi que se le antepone al
     * metodo y la llamada dice sobre que objeto se hace; los indices se
     * escriben como "_" porque un identificador no admite corchetes.</p>
     */
    private String nombreDeLaLlamada(PigLatinParser.Llamada_funcionContext ctx) {
        if (ctx.acceso_miembro() == null) {
            return ctx.VARIABLE().getText();
        }
        PigLatinParser.Acceso_miembroContext acceso = ctx.acceso_miembro();
        StringBuilder nombre = new StringBuilder(acceso.VARIABLE(0).getText());
        for (int i = 1; i < acceso.getChildCount(); i++) {
            String texto = acceso.getChild(i).getText();
            if ("[".equals(texto)) {
                nombre.append('_');
            } else if (")".equals(texto) || "]".equals(texto) || ",".equals(texto)) {
                // Cierra un indice: "misObjetos[9]" ya aporta el objeto.
                continue;
            } else if (!".".equals(texto)) {
                nombre.append('_').append(texto);
            }
        }
        return nombre.toString();
    }
}
