package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostico;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.symbols.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class PigLatinSemanticVisitor extends PigLatinBaseVisitor<Type> {
    private final SymbolTable symbolTable;
    private final List<CompilationError> errors = new ArrayList<>();

    /**
     * Cuantos ciclos estan abiertos en este punto del recorrido.
     *
     * <p>Se lleva como contador y no como una bandera porque los ciclos se pueden
     * anidar: un {@code per} dentro de un {@code dum} sigue siendo un ciclo, y al
     * salir del interior tiene que quedar el exterior.</p>
     */
    private int enCiclo;

    public PigLatinSemanticVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    @Override
    public Type visitCiclo_dum(PigLatinParser.Ciclo_dumContext ctx) {
        return visitarCuerpo(ctx.condicion(), ctx.sentencia());
    }

    @Override
    public Type visitCiclo_facere(PigLatinParser.Ciclo_facereContext ctx) {
        return visitarCuerpo(ctx.condicion(), ctx.sentencia());
    }

    /** Recorre el cuerpo de un ciclo contando que estamos dentro de el. */
    private Type visitarCuerpo(PigLatinParser.CondicionContext condicion,
                              List<PigLatinParser.SentenciaContext> sentencias) {
        if (condicion != null) {
            visit(condicion);
        }
        enCiclo++;
        try {
            for (PigLatinParser.SentenciaContext s : sentencias) {
                visit(s);
            }
        } finally {
            enCiclo--;
        }
        return Type.VOID;
    }

    @Override
    public Type visitVariables(PigLatinParser.VariablesContext ctx) {
        // Declaraciones globales
        for (PigLatinParser.DeclaracionContext dCtx : ctx.declaracion()) {
            visit(dCtx);
        }
        for (PigLatinParser.Arreglo_declaracionContext aCtx : ctx.arreglo_declaracion()) {
            visit(aCtx);
        }
        return Type.VOID;
    }

    @Override
    public Type visitDeclaracion(PigLatinParser.DeclaracionContext ctx) {
        // "declaracion" incluye los arreglos como una alternativa mas, asi que un
        // "series ..." llega por aqui y no por la lista de arreglos del bloque.
        if (ctx.arreglo_declaracion() != null) {
            return visit(ctx.arreglo_declaracion());
        }
        if (!ctx.VARIABLE().isEmpty()) {
            String varName = ctx.VARIABLE(0).getText();
            Type varType = Type.UNKNOWN;

            if (ctx.tipo_dato() != null) {
                varType = resolveType(ctx.tipo_dato().getText());
            } else if (ctx.TEXTUM() != null) {
                varType = Type.STRING;
            } else if (ctx.LITTERA() != null) {
                varType = Type.CHAR;
            } else if (ctx.NOVUS() != null && ctx.VARIABLE().size() > 1) {
                varType = Type.classType(ctx.VARIABLE(1).getText());
            } else if (ctx.VARIABLE().size() > 1) {
                // "esto miObjeto: Persona {nombre: "ana"}": el nombre de la
                // estructura va justo antes de sus llaves. Sin esto la variable
                // queda de tipo desconocido y no se puede comprobar ni el valor
                // ni las llamadas a sus miembros.
                varType = resolveType(ctx.VARIABLE(1).getText());
            }

            int offset = symbolTable.getGlobalScope().allocateOffset(1);
            Symbol sym = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, true)
                    .at(ctx);

            if (!symbolTable.defineGlobal(sym)) {
                errors.add(Diagnostico.variableGlobalYaDeclarada(varName, ctx));
            }

            if (ctx.expresion() != null) {
                Type exprType = visit(ctx.expresion());
                if (esConocido(exprType) && esConocido(varType)
                        && !exprType.isAssignableTo(varType)) {
                    errors.add(Diagnostico.inicializacionIncompatible(
                            varName, varType, exprType, ctx));
                }
            }
        }
        return Type.VOID;
    }

    @Override
    public Type visitArreglo_declaracion(PigLatinParser.Arreglo_declaracionContext ctx) {
        String arrayName = ctx.VARIABLE(0).getText();
        Type elemType = Type.INT;
        if (ctx.tipo_dato() != null) {
            elemType = resolveType(ctx.tipo_dato().getText());
        } else if (ctx.VARIABLE().size() > 1) {
            elemType = Type.structType(ctx.VARIABLE(1).getText());
        }

        // Un corchete por dimension, en el orden en que se escribieron: un
        // "matriz[2][3]" son dos dimensiones, la primera de dos filas.
        int[] sizes = new int[ctx.NUMERO_ENTERO().size()];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = Integer.parseInt(ctx.NUMERO_ENTERO(i).getText());
        }
        Type arrayType = Type.arrayOf(elemType, sizes);

        if (ctx.elemento_arreglo() != null) {
            Type obtenido = tipoDeLiteralArreglo(arrayName, ctx.elemento_arreglo());
            CompilationError error = LiteralDeArreglo.validar(arrayName, arrayType, obtenido, ctx);
            if (error != null) {
                errors.add(error);
            }
        }

        int offset = symbolTable.getGlobalScope().allocateOffset(1);
        Symbol sym = new Symbol(arrayName, arrayType, SymbolCategory.VARIABLE, offset, true)
                .at(ctx);
        symbolTable.defineGlobal(sym);
        return Type.VOID;
    }

    /**
     * Tipo de un literal de arreglo, deducido de las llaves que lo envuelven.
     *
     * <p>Es recursivo porque las matrices se inicializan anidando llaves: una
     * lista de valores es una dimension, y cada nivel de llaves anidadas es una
     * dimension mas. Todas las filas tienen que tener la misma forma, asi que se
     * toma la primera y se compara el resto con ella.</p>
     */
    private Type tipoDeLiteralArreglo(String nombre,
                                   PigLatinParser.Elemento_arregloContext ctx) {
        List<PigLatinParser.Elemento_arreglo_valorContext> valores = ctx.elemento_arreglo_valor();
        int filas = valores.size();
        Type tipoFila = Type.UNKNOWN;
        int[] formaFila = new int[0];
        for (int f = 0; f < filas; f++) {
            Type tipo = tipoDeValorArreglo(nombre, valores.get(f));
            // Un valor que no es un arreglo no aporta ninguna dimension mas: la
            // lista de {1, 2, 3} es de una sola dimension.
            int[] forma = tipo.isArray() ? tipo.getSizes() : new int[0];
            if (f == 0) {
                tipoFila = tipo.isArray() ? tipo.getElementType() : tipo;
                formaFila = forma;
            } else if (!java.util.Arrays.equals(forma, formaFila)) {
                errors.add(Diagnostico.filasDesiguales(nombre, f + 1,
                        anchoDe(formaFila, valores.get(0)), anchoDe(forma, valores.get(f)),
                        valores.get(f)));
            }
        }
        int[] sizes = new int[formaFila.length + 1];
        sizes[0] = filas;
        System.arraycopy(formaFila, 0, sizes, 1, formaFila.length);
        return Type.arrayOf(tipoFila, sizes);
    }

    /** Cuantos elementos se ven en la primera dimension de una fila. */
    private static int anchoDe(int[] forma, PigLatinParser.Elemento_arreglo_valorContext fila) {
        if (forma.length > 0) {
            return forma[0];
        }
        // La fila es una lista de valores sueltos: su ancho es lo que haya.
        if (fila.LLAVE_IZQ() == null) {
            return 1;
        }
        return fila.elemento_arreglo() == null ? 0 : fila.elemento_arreglo().elemento_arreglo_valor().size();
    }

    /** Tipo de un valor dentro de un literal: un termino, o otro arreglo. */
    private Type tipoDeValorArreglo(String nombre,
                                    PigLatinParser.Elemento_arreglo_valorContext ctx) {
        if (ctx.LLAVE_IZQ() == null) {
            return visit(ctx.expresion());
        }
        if (ctx.elemento_arreglo() == null) {
            // "{}": un arreglo sin ningun valor, de tamano cero.
            return Type.arrayOf(Type.UNKNOWN, new int[]{0});
        }
        return tipoDeLiteralArreglo(nombre, ctx.elemento_arreglo());
    }

    @Override
    public Type visitMaior(PigLatinParser.MaiorContext ctx) {
        symbolTable.enterScope("maior", 0);

        for (PigLatinParser.SentenciaContext sCtx : ctx.sentencia()) {
            visit(sCtx);
        }

        symbolTable.exitScope();
        return Type.VOID;
    }

    @Override
    public Type visitAsignacion_sentencia(PigLatinParser.Asignacion_sentenciaContext ctx) {
        Type targetType = Type.UNKNOWN;
        if (!ctx.VARIABLE().isEmpty()) {
            String name = ctx.VARIABLE(0).getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostico.variableNoDeclarada(name, ctx));
            } else {
                targetType = s.getType();
            }
        } else if (ctx.acceso_miembro() != null) {
            targetType = visit(ctx.acceso_miembro());
        }

        if (ctx.expresion() != null) {
            Type exprType = visit(ctx.expresion());
            if (esConocido(targetType) && esConocido(exprType)
                    && !exprType.isAssignableTo(targetType)) {
                errors.add(Diagnostico.asignacionIncompatible(targetType, exprType, ctx));
            }
        }
        return targetType;
    }

    @Override
    public Type visitExpresion(PigLatinParser.ExpresionContext ctx) {
        if (ctx.termino() != null && ctx.termino().size() == 1) {
            return visit(ctx.termino(0));
        }

        if (!ctx.operacion_aritmetica().isEmpty()) {
            Type t1 = visit(ctx.termino(0));
            for (int i = 1; i < ctx.termino().size(); i++) {
                Type t2 = visit(ctx.termino(i));
                t1 = TypeCompatibility.checkArithmetic(t1, t2, ctx.operacion_aritmetica(i - 1).getText());
            }
            return t1;
        }

        return Type.UNKNOWN;
    }

    @Override
    public Type visitTermino(PigLatinParser.TerminoContext ctx) {
        if (ctx.NUMERO_ENTERO() != null) return Type.INT;
        if (ctx.NUMERO_DECIMAL() != null) return Type.DOUBLE;
        if (ctx.CADENA_TEXTO() != null) return Type.STRING;
        if (ctx.CARACTER() != null) return Type.CHAR;
        if (ctx.VERUM() != null || ctx.FALSUS() != null) return Type.BOOLEAN;

        if (ctx.VARIABLE() != null) {
            String varName = ctx.VARIABLE().getText();
            Symbol s = symbolTable.resolve(varName);
            if (s == null) {
                errors.add(Diagnostico.simboloNoResuelto(varName, ctx));
                return Type.UNKNOWN;
            }
            return s.getType();
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        if (ctx.NOVUS() != null) {
            String className = ctx.VARIABLE().getText();
            return Type.classType(className);
        }

        // El menos unario no cambia el tipo de lo que se le aplica.
        if (ctx.MENOS() != null) {
            Type t = visit(ctx.termino());
            if (t != null && t != Type.UNKNOWN && !t.isNumeric()) {
                errors.add(Diagnostico.operadorMenosNoNumerico(ctx));
                return Type.UNKNOWN;
            }
            return t == null ? Type.UNKNOWN : t;
        }

        return Type.UNKNOWN;
    }

    /**
     * Tipo de una cadena de acceso: {@code matriz[1][0]} o {@code obj.campo[2]}.
     *
     * <p>La gramatica permite cualquier mezcla de puntos y corchetes, asi que se
     * recorre en orden: cada punto baja al miembro indicado y cada corchete
     * quita una dimension del arreglo. Al final queda el tipo del elemento, que
     * es el que se puede imprimir, comparar o asignar.</p>
     *
     * <p>Usar menos indices que dimensiones esta bien, porque {@code matriz[0]} de
     * una matriz da una fila entera; lo que no tiene sentido es usar mas indices
     * de los que hay.</p>
     */
    @Override
    public Type visitAcceso_miembro(PigLatinParser.Acceso_miembroContext ctx) {
        String baseName = ctx.VARIABLE(0).getText();
        Symbol base = symbolTable.resolve(baseName);
        if (base == null) {
            errors.add(Diagnostico.variableNoDeclarada(baseName, ctx));
            return Type.UNKNOWN;
        }
        Type actual = base.getType();
        if (actual == null) {
            return Type.UNKNOWN;
        }

        int indices = contarIndices(ctx);
        if (indices > 0 && actual.isArray() && indices > actual.getDimensions()) {
            errors.add(Diagnostico.dimensionesDeIndice(baseName, indices,
                    actual.getDimensions(), ctx));
            return Type.UNKNOWN;
        }

        for (int i = 1; i < ctx.getChildCount() && actual != null; i++) {
            ParseTree hijo = ctx.getChild(i);

            if (hijo.getText().equals(".")) {
                // El siguiente hijo es el nombre del miembro.
                String nombreMiembro = ctx.getChild(++i).getText();
                Symbol miembro = buscarMiembro(actual, nombreMiembro);
                if (miembro == null) {
                    if (tieneMiembrosConocidos(actual)) {
                        errors.add(Diagnostico.miembroNoExiste(nombreMiembro, actual.label(), ctx));
                    }
                    return Type.UNKNOWN;
                }
                // Un miembro empieza su propia cuenta de dimensiones.
                actual = miembro.getType();
            } else if (hijo instanceof PigLatinParser.ExpresionContext) {
                visit(hijo);
                if (!actual.isArray()) {
                    errors.add(Diagnostico.noEsArreglo(baseName, actual, ctx));
                    return Type.UNKNOWN;
                }
                comprobarIndice(hijo, actual, baseName);
                actual = actual.desindexar(1);
            }
        }
        return actual == null ? Type.UNKNOWN : actual;
    }

    /** Cuantos corchetes lleva la cadena de acceso. */
    private static int contarIndices(PigLatinParser.Acceso_miembroContext ctx) {
        int indices = 0;
        for (int i = 1; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i).getText().equals("[")) {
                indices++;
            }
        }
        return indices;
    }

    /**
     * Avisa si el indice se sale del arreglo.
     *
     * <p>Solo se puede comprobar cuando el indice es un numero escrito en el
     * fuente; con una variable el valor no se conoce al compilar.</p>
     */
    private void comprobarIndice(ParseTree indice, Type arreglo, String nombre) {
        int[] sizes = arreglo.getSizes();
        String texto = indice.getText();
        if (sizes.length == 0 || sizes[0] <= 0 || !texto.matches("\\d+")) {
            return;
        }
        int valor = Integer.parseInt(texto);
        if (valor >= sizes[0]) {
            errors.add(Diagnostico.indiceFueraDeRango(nombre, valor, sizes[0],
                    (PigLatinParser.ExpresionContext) indice));
        }
    }

    /**
     * Miembro de una estructura o clase, o null si no lo tiene.
     *
     * <p>Cuando el tipo no declara ningun miembro no se puede afirmar que el
     * acceso este mal, asi que se devuelve null sin inventar un error.</p>
     */
    private Symbol buscarMiembro(Type contenedor, String nombreMiembro) {
        Symbol tipo = tipoDeclarado(contenedor);
        return tipo == null ? null : tipo.getMember(nombreMiembro);
    }

    /** true si el tipo tiene al menos un miembro registrado. */
    private boolean tieneMiembrosConocidos(Type contenedor) {
        Symbol tipo = tipoDeclarado(contenedor);
        return tipo != null && !tipo.getMembers().isEmpty();
    }

    /** La entrada de la tabla que declara este tipo, sea clase o estructura. */
    private Symbol tipoDeclarado(Type type) {
        if (type == null || type.getCustomTypeName() == null) {
            return null;
        }
        Symbol tipo = symbolTable.getClass(type.getCustomTypeName());
        return tipo != null ? tipo : symbolTable.getStruct(type.getCustomTypeName());
    }

    @Override
    public Type visitLlamada_funcion(PigLatinParser.Llamada_funcionContext ctx) {
        if (ctx.acceso_miembro() != null) {
            // Una llamada a metodo: "miObjeto.getNombre()". El tipo que retorna
            // depende de la clase importada que lo declara, que no se conoce aqui,
            // asi que se supone que es un entero, igual que con una funcion suelta.
            visit(ctx.acceso_miembro());
            return Type.INT;
        }
        String funcName = ctx.VARIABLE().getText();
        Symbol func = symbolTable.getFunction(funcName);
        if (func == null) {
            // PigLatin no declara funciones, solo las llama. Y la que se llama
            // puede venir de un archivo importado de otro lenguaje, asi que un
            // nombre desconocido aqui no es un error: lo que no se sabe es el
            // tipo que devuelve, y se supone que es un entero.
            return Type.INT;
        }
        return func.getReturnType() != null ? func.getReturnType() : Type.VOID;
    }

    /**
     * El ciclo "per" declara su variable de control en la inicializacion.
     * Sin este paso la condicion y el incremento del bucle fallarian al
     * resolver el identificador.
     *
     * <p>La variable se registra en el ambito global aunque el ciclo este dentro
     * de un bloque, para que siga viva cuando ese bloque se cierre.</p>
     */
    @Override
    public Type visitInicializacion_per(PigLatinParser.Inicializacion_perContext ctx) {
        if (ctx.ESTO() != null && ctx.VARIABLE() != null) {
            String name = ctx.VARIABLE().getText();
            Type type = ctx.tipo_dato() != null ? resolveType(ctx.tipo_dato().getText()) : Type.INT;
            if (symbolTable.resolve(name) == null) {
                int offset = symbolTable.getGlobalScope().allocateOffset(1);
                symbolTable.defineGlobal(
                        new Symbol(name, type, SymbolCategory.VARIABLE, offset, true)
                                .at(ctx));
            }
            if (ctx.expresion() != null) {
                visit(ctx.expresion());
            }
            return type;
        }
        // Caso: variable ya declarada a la que se le asigna el valor inicial
        return visit(ctx.expresion());
    }

    @Override
    public Type visitCondiciones_per(PigLatinParser.Condiciones_perContext ctx) {
        if (ctx.condicion() != null) {
            return visit(ctx.condicion());
        }
        return Type.BOOLEAN;
    }

    @Override
    public Type visitIncremento_per(PigLatinParser.Incremento_perContext ctx) {
        if (ctx.expresion() != null) {
            visit(ctx.expresion());
        }
        return Type.INT;
    }

    @Override
    public Type visitCiclo_per(PigLatinParser.Ciclo_perContext ctx) {
        visit(ctx.inicializacion_per());
        visit(ctx.condiciones_per());
        visit(ctx.incremento_per());
        enCiclo++;
        try {
            for (PigLatinParser.SentenciaContext s : ctx.sentencia()) {
                visit(s);
            }
        } finally {
            enCiclo--;
        }
        return Type.VOID;
    }

    /**
     * {@code perge} e {@code interrumpe} solo tienen sentido dentro de un ciclo:
     * fuera de ellos no hay iteracion a la que saltar ni que interrumpir.
     *
     * <p>El enunciado lo dice asi ("unicamente dentro de los ciclos") y la gramatica
     * no lo puede exigir, porque {@code salto_sentencia} es una sentencia mas y
     * aparece igual dentro que fuera de un bloque.</p>
     */
    @Override
    public Type visitSalto_sentencia(PigLatinParser.Salto_sentenciaContext ctx) {
        if (enCiclo == 0) {
            String palabra = ctx.PERGE() != null ? "perge" : "interrumpe";
            errors.add(new CompilationError(ErrorType.SEMANTICO,
                    "«" + palabra + "» solo se puede usar dentro de un ciclo"
                            + " (dum, facere o per)",
                    ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()));
        }
        return Type.VOID;
    }

    private Type resolveType(String text) {
        if (text == null) return Type.UNKNOWN;
        switch (text.toLowerCase()) {
            case "numerus": return Type.INT;
            case "decimalis": return Type.DOUBLE;
            case "textum": return Type.STRING;
            case "littera": return Type.CHAR;
            case "verum":
            case "falsus": return Type.BOOLEAN;
            default:
                if (symbolTable.getStruct(text) != null) return Type.structType(text);
                if (symbolTable.getClass(text) != null) return Type.classType(text);
                return Type.UNKNOWN;
        }
    }

    /**
     * true si el tipo se conoce de verdad.
     *
     * <p>Un tipo desconocido o nulo significa que la expresion que lo produce ya
     * fallo: el aviso de verdad se dio ahi. Comprobar los tipos con un
     * desconocido daria un segundo error, mas corto y mas generico, que solo
     * tapa el primero.</p>
     */
    private static boolean esConocido(Type t) {
        return t != null && t != Type.UNKNOWN;
    }
}
