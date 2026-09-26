package com.compi.backend.languages.zetariano;

import com.compi.ZetarianoBaseVisitor;
import com.compi.ZetarianoParser;
import com.compi.backend.c3d.Arreglos;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ZetarianoC3DVisitor extends ZetarianoBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;
    private String currentClassName = "";
    private final Stack<String> breakLabels = new Stack<>();
    private final Stack<String> continueLabels = new Stack<>();

    public ZetarianoC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    @Override
    public String visitClaseDef(ZetarianoParser.ClaseDefContext ctx) {
        currentClassName = ctx.ID().getText();
        symbolTable.enterScope("class_" + currentClassName, 0);

        // Los atributos se vuelven a declarar aqui: cuando corre esta segunda
        // pasada la tabla de simbolos ya no tiene el ambito de la clase. El
        // offset es el mismo que asigno el analizador semantico (orden de
        // declaracion), para que this.campo calcule la misma direccion. Se
        // marcan como copias de trabajo para no salir duplicados en la tabla.
        int offset = 0;
        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            if (mc.atributoDef() == null) {
                continue;
            }
            symbolTable.define(new Symbol(mc.atributoDef().ID().getText(), typeOfField(mc.atributoDef()),
                    SymbolCategory.FIELD, offset++, false).at(mc.atributoDef()).markWorking());
        }

        for (ZetarianoParser.MiembroClaseContext mc : ctx.miembroClase()) {
            visit(mc);
        }

        symbolTable.exitScope();
        currentClassName = "";
        return null;
    }

    // ====================== Direccionamiento ======================

    /**
     * Emite las cuartetas necesarias para obtener la direccion de un simbolo
     * y devuelve el temporal que la contiene.
     *
     * <p>Los atributos no viven en la pila sino en el heap: se alcanza el
     * puntero del objeto guardado en {@code stack[P + 0]} (la referencia
     * implicita {@code this}) y se le suma el offset del campo.</p>
     */
    private String addressOf(Symbol s) {
        if (s.getCategory() == SymbolCategory.FIELD) {
            String thisPos = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, "P", "0", thisPos);
            String objectPtr = c3d.newTemp();
            c3d.emit(QuadrupleOp.STACK_GET, thisPos, null, objectPtr);
            String fieldAddr = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, objectPtr, String.valueOf(s.getOffset()), fieldAddr);
            return fieldAddr;
        }
        String pos = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(s.getOffset()), pos);
        return pos;
    }

    /** Escribe {@code value} en la direccion de {@code s}. */
    private void store(Symbol s, String value) {
        String addr = addressOf(s);
        c3d.emit(s.getCategory() == SymbolCategory.FIELD
                ? QuadrupleOp.HEAP_SET : QuadrupleOp.STACK_SET, addr, value, null);
    }

    /** Lee el valor de {@code s} y lo devuelve como temporal. */
    private String load(Symbol s) {
        String addr = addressOf(s);
        String value = c3d.newTemp();
        c3d.emit(s.getCategory() == SymbolCategory.FIELD
                ? QuadrupleOp.HEAP_GET : QuadrupleOp.STACK_GET, addr, null, value);
        return value;
    }

    /** Declara un parametro formal en el offset que le toca. */
    private void defineParameter(ZetarianoParser.ParametroContext pCtx) {
        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        symbolTable.define(new Symbol(pCtx.ID().getText(), Type.UNKNOWN,
                SymbolCategory.PARAMETER, offset, false).at(pCtx).markWorking());
    }

    /** Tipo declarado de un atributo de la clase en curso. */
    /**
     * El tipo de un atributo, tal y como lo dejo el analizador semantico.
     *
     * <p>Un atributo de arreglo se declara sin tamano, asi que aqui se completa
     * con la forma de su inicializador: sin ella no se podria colocar
     * {@code tabla[1][0]}.</p>
     */
    private Type typeOfField(ZetarianoParser.AtributoDefContext ctx) {
        Type type = Type.UNKNOWN;
        Symbol cls = symbolTable.getClass(currentClassName);
        if (cls != null) {
            Symbol member = cls.getMember(ctx.ID().getText());
            if (member != null && member.getType() != null) {
                type = member.getType();
            }
        }
        if (type.isArray() && !type.tieneTamanos()
                && ctx.expresion() instanceof ZetarianoParser.LiteralArregloExprContext arreglo) {
            type = Type.arrayOf(type.getElementType(), formasDe(arreglo.literalArreglo()));
        }
        return type;
    }

    /**
     * Declara la referencia implicita {@code this} en el offset 0, igual que
     * hace el analizador semantico.
     *
     * <p>Se anota con la posicion del metodo o constructor al que pertenece, como
     * alli. Es una copia de trabajo ({@code markWorking}) y no sale en la tabla,
     * pero asi las dos declaraciones coinciden en todo.</p>
     */
    private void defineThis(org.antlr.v4.runtime.ParserRuleContext owner) {
        Symbol cls = symbolTable.getClass(currentClassName);
        if (cls != null) {
            symbolTable.define(new Symbol("this", cls.getType(), SymbolCategory.VARIABLE, 0, false)
                    .at(owner).markWorking());
        }
    }

    @Override
    public String visitConstructorDef(ZetarianoParser.ConstructorDefContext ctx) {
        String funcName = currentClassName + "_" + ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, funcName);
        symbolTable.enterScope("constructor_" + funcName, 1);
        defineThis(ctx);
        if (ctx.parametros() != null) {
            for (ZetarianoParser.ParametroContext pCtx : ctx.parametros().parametro()) {
                defineParameter(pCtx);
            }
        }

        // Stack[P] contiene el puntero 'this' hacia el objeto en el Heap
        visit(ctx.bloque());

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, funcName);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitMetodoDef(ZetarianoParser.MetodoDefContext ctx) {
        String methodName = currentClassName + "_" + ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, methodName);
        symbolTable.enterScope("method_" + methodName, 1);
        defineThis(ctx);
        if (ctx.parametros() != null) {
            for (ZetarianoParser.ParametroContext pCtx : ctx.parametros().parametro()) {
                defineParameter(pCtx);
            }
        }

        visit(ctx.bloque());

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, methodName);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitAtributoDef(ZetarianoParser.AtributoDefContext ctx) {
        if (ctx.expresion() == null) {
            return null;
        }
        // El valor inicial se guarda en el hueco del atributo. Si es un arreglo,
        // lo que se guarda es la direccion de la primera celda, que es lo que
        // devuelve la reserva.
        Symbol field = symbolTable.resolve(ctx.ID().getText());
        if (field != null) {
            store(field, visit(ctx.expresion()));
        }
        return null;
    }

    @Override
    public String visitDeclaracionVariable(ZetarianoParser.DeclaracionVariableContext ctx) {
        String varName = ctx.ID().getText();

        // En Zetariano el tamano no se escribe, asi que un arreglo solo tiene
        // forma si viene con valores: se la copia del inicializador. Sin ella
        // solo se sabe cuantas dimensiones tiene, que es lo que hace falta para
        // colocar "cubo[1][0][1]".
        Type tipo = Type.UNKNOWN;
        if (ctx.LBRACK().size() > 0) {
            if (ctx.expresion() instanceof ZetarianoParser.LiteralArregloExprContext arreglo) {
                tipo = Type.arrayOf(Type.UNKNOWN, formasDe(arreglo.literalArreglo()));
            } else {
                tipo = Type.arrayOf(Type.UNKNOWN, new int[ctx.LBRACK().size()]);
            }
        }

        // La declaracion reserva su hueco en la pila aunque no lleve valor
        // inicial: sin esto las referencias posteriores quedarian colgando.
        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol declared = new Symbol(varName, tipo,
                SymbolCategory.VARIABLE, offset, false).at(ctx).markWorking();
        symbolTable.define(declared);
        Symbol s = symbolTable.resolve(varName);
        if (s == null) {
            s = declared;
        }

        if (ctx.expresion() != null) {
            // Para un arreglo entre llaves, visit() devuelve la direccion de la
            // primera celda, que es lo que guarda la variable.
            store(s, visit(ctx.expresion()));
        }
        return null;
    }

    // ===================== Arreglos y matrices =====================

    /**
     * Un arreglo entre llaves reserva sus celdas en el heap y deja en el temporal
     * la direccion de la primera.
     *
     * <p>Las dimensiones no se escriben en Zetariano ({@code int[][] cubo = ...}),
     * asi que los tamanos se cuentan sobre las llaves del propio literal. Como en
     * los otros lenguajes, las celdas quedan contiguas: una matriz de 2x2 son
     * cuatro celdas, no un arreglo de filas.</p>
     */
    @Override
    public String visitLiteralArregloExpr(ZetarianoParser.LiteralArregloExprContext ctx) {
        int[] sizes = formasDe(ctx.literalArreglo());
        int celdas = Type.arrayOf(Type.UNKNOWN, sizes).totalSize();
        if (celdas <= 0) {
            return "0";
        }
        String base = Arreglos.reservar(c3d, celdas);
        llenar(base, ctx.literalArreglo(), sizes, new int[sizes.length], 0);
        return base;
    }

    /**
     * Los tamanos de un literal, deducidos de como estan escritas las llaves.
     *
     * <p>Se toma la forma de la primera fila y se supone que todas las demas
     * tienen la misma; si no, el analizador semantico ya lo ha avisado.</p>
     */
    private int[] formasDe(ZetarianoParser.LiteralArregloContext ctx) {
        List<ParseTree> valores = valoresDe(ctx);
        int[] formaFila = new int[0];
        for (ParseTree valor : valores) {
            ZetarianoParser.LiteralArregloContext fila = filaDe(valor);
            if (fila != null) {
                formaFila = formasDe(fila);
                break;
            }
        }
        int[] sizes = new int[formaFila.length + 1];
        sizes[0] = valores.size();
        System.arraycopy(formaFila, 0, sizes, 1, formaFila.length);
        return sizes;
    }

    /** Los valores de una lista, en el orden en que estan escritos. */
    private static List<ParseTree> valoresDe(ZetarianoParser.LiteralArregloContext ctx) {
        List<ParseTree> valores = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree hijo = ctx.getChild(i);
            if (hijo instanceof ZetarianoParser.ExpresionContext) {
                valores.add(hijo);
            }
        }
        return valores;
    }

    /**
     * La lista de llaves de un valor, o null si el valor no es una lista.
     *
     * <p>Una lista anidada llega como una "expresion" mas, porque es una
     * alternativa de esa regla, asi que hay que mirar primero ese caso.</p>
     */
    private static ZetarianoParser.LiteralArregloContext filaDe(ParseTree valor) {
        if (valor instanceof ZetarianoParser.LiteralArregloExprContext anidada) {
            return anidada.literalArreglo();
        }
        if (valor instanceof ZetarianoParser.LiteralArregloContext directa) {
            return directa;
        }
        return null;
    }

    /**
     * Escribe los valores de un inicializador celda a celda.
     *
     * <p>El recorrido sigue las llaves: cada nivel es una dimension y el numero
     * de valor dentro del nivel es el indice de esa dimension.</p>
     */
    private void llenar(String base, ZetarianoParser.LiteralArregloContext ctx, int[] sizes,
                        int[] indices, int dimension) {
        List<ParseTree> valores = valoresDe(ctx);
        for (int i = 0; i < valores.size() && dimension < indices.length; i++) {
            ParseTree valor = valores.get(i);
            indices[dimension] = i;
            ZetarianoParser.LiteralArregloContext fila = filaDe(valor);
            if (fila != null) {
                llenar(base, fila, sizes, indices, dimension + 1);
            } else {
                Arreglos.escribirConstante(c3d, base, indices, sizes,
                        visit((ZetarianoParser.ExpresionContext) valor));
            }
        }
    }

    /**
     * Lee el elemento al que apunta una cadena de indices.
     *
     * @return el temporal con el valor, o null si la cadena no agota todas las
     *         dimensiones del arreglo
     */
    private String leerCelda(ZetarianoParser.AccesoMiembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.ID().getText());
        if (base == null || base.getType() == null || !base.getType().isArray()) {
            return null;
        }
        int[] sizes = base.getType().getSizes();
        List<String> indices = indicesDe(ctx);
        if (!base.getType().tieneTamanos() || indices == null || indices.size() != sizes.length) {
            return null;
        }
        return Arreglos.leer(c3d, load(base), indices, sizes);
    }

    /**
     * Los indices de una cadena de acceso, en el orden en que se escribieron.
     *
     * @return null si la cadena baja a un miembro, porque entonces los indices
     *         pertenecen a arreglos distintos
     */
    private List<String> indicesDe(ZetarianoParser.AccesoMiembroContext ctx) {
        List<String> indices = new ArrayList<>();
        for (ZetarianoParser.MiembroAccesoContext mac : ctx.miembroAcceso()) {
            if (mac.DOT() != null) {
                return null;
            }
            for (ZetarianoParser.ExpresionContext indice : mac.expresion()) {
                indices.add(visit(indice));
            }
        }
        return indices;
    }

    /**
     * Escribe en el elemento de un arreglo al que apunta una cadena de indices.
     *
     * @return true si la cadena era un acceso a arreglo y se ha escrito la celda
     */
    private boolean escribirCelda(ZetarianoParser.AccesoMiembroContext ctx, String valor) {
        Symbol base = symbolTable.resolve(ctx.ID().getText());
        if (base == null || base.getType() == null || !base.getType().isArray()) {
            return false;
        }
        int[] sizes = base.getType().getSizes();
        List<String> indices = indicesDe(ctx);
        if (!base.getType().tieneTamanos() || indices == null || indices.size() != sizes.length) {
            return false;
        }
        Arreglos.escribir(c3d, load(base), indices, sizes, valor);
        return true;
    }

    @Override
    public String visitAsignacion(ZetarianoParser.AsignacionContext ctx) {
        String val = visit(ctx.expresion(ctx.expresion().size() - 1));

        if (ctx.ID() != null) {
            Symbol target = symbolTable.resolve(ctx.ID().getText());
            if (target != null) {
                store(target, applyCompound(ctx, target, val));
            }
        } else if (ctx.accesoMiembro() != null) {
            // "cubo[1][0] = 7": el valor va a una celda del heap, no al atributo.
            if (escribirCelda(ctx.accesoMiembro(), val)) {
                return null;
            }
            Symbol field = resolveMember(ctx.accesoMiembro());
            if (field != null) {
                store(field, applyCompound(ctx, field, val));
            }
        }
        return null;
    }

    /**
     * Resuelve {@code obj.campo} al simbolo del atributo dentro de la clase.
     *
     * @return el atributo, o null si la cadena nodesigna un campo conocido
     */
    private Symbol resolveMember(ZetarianoParser.AccesoMiembroContext acceso) {
        if (acceso.miembroAcceso(0) == null || acceso.miembroAcceso(0).ID() == null) {
            return null;
        }
        Symbol base = symbolTable.resolve(acceso.ID().getText());
        if (base == null) {
            return null;
        }
        Symbol cls = symbolTable.getClass(base.getType().getCustomTypeName());
        return cls == null ? null : cls.getMember(acceso.miembroAcceso(0).ID().getText());
    }

    /**
     * Para {@code x = v} devuelve {@code v}; para {@code x += v} genera la
     * lectura previa y la suma correspondiente.
     */
    private String applyCompound(ZetarianoParser.AsignacionContext ctx, Symbol target, String val) {
        QuadrupleOp op = switch (ctx.getChild(1).getText()) {
            case "+=" -> QuadrupleOp.ADD;
            case "-=" -> QuadrupleOp.SUB;
            case "*=" -> QuadrupleOp.MUL;
            default -> null;
        };
        if (op == null) {
            return val;
        }
        String current = load(target);
        String temp = c3d.newTemp();
        c3d.emit(op, current, val, temp);
        return temp;
    }

    @Override
    public String visitMemberAccessExpr(ZetarianoParser.MemberAccessExprContext ctx) {
        // "cubo[1][0]" es una celda del heap; "obj.campo" es un atributo.
        String celda = leerCelda(ctx.accesoMiembro());
        if (celda != null) {
            return celda;
        }
        Symbol field = resolveMember(ctx.accesoMiembro());
        return field == null ? visit(ctx.accesoMiembro().ID()) : load(field);
    }

    @Override
    public String visitSentenciaIf(ZetarianoParser.SentenciaIfContext ctx) {
        // El "if / else if / else" se recorre como una cadena: la rama falsa de
        // cada prueba es la que sigue, y todas las ramas saltan al mismo final.
        // Asi el codigo sale como lo escribio el docente:
        //   if (x > z) goto et1
        //   goto et2
        //   et1: ... goto et3
        //   et2: ... (else if) ...
        //   et3:
        String fin = c3d.newLabel();

        List<ZetarianoParser.ExpresionContext> condiciones = new ArrayList<>();
        List<ZetarianoParser.BloqueContext> bloques = new ArrayList<>();
        condiciones.add(ctx.expresion(0));
        bloques.add(ctx.bloque(0));
        // Los "else if" son bloques mas, en el orden en que aparecen.
        for (int i = 1; i < ctx.bloque().size(); i++) {
            condiciones.add(ctx.expresion(i));
            bloques.add(ctx.bloque(i));
        }

        int conPrueba = condiciones.size();
        for (int i = 0; i < conPrueba; i++) {
            String verdadera = c3d.newLabel();
            boolean ultima = i == conPrueba - 1 && bloques.size() == conPrueba;
            // La prueba de la ultima rama cae directo al final: detras no queda
            // nada mas que ejecutar, asi que su goto falso se ahorra.
            String falsa = ultima ? fin : c3d.newLabel();

            emitirSaltoDeCondicion(condiciones.get(i), verdadera, falsa);
            c3d.emitLabel(verdadera);
            visit(bloques.get(i));
            c3d.emitGoto(fin);
            if (!ultima) {
                c3d.emitLabel(falsa);
            }
        }
        // El "else" final, el que no lleva condicion, entra en la rama falsa de
        // la ultima prueba y cae al final de la cadena sin saltar.
        for (int i = conPrueba; i < bloques.size(); i++) {
            visit(bloques.get(i));
        }

        c3d.emitLabel(fin);
        return null;
    }

    /**
     * Emite la prueba de una condicion: si es una comparacion, el salto
     * relacional directo ({@code if (x &gt; z) goto et1}); si es una expresion mas
     * complicated, se evalua en un temporal booleano y se salta ese.
     *
     * @param verdadera etiqueta a la que se salta cuando la condicion se cumple
     * @param falsa      etiqueta del camino contrario, o null si no hace falta
     *                  saltar (en el {@code do while} la caida ya va al final)
     */
    private void emitirSaltoDeCondicion(ZetarianoParser.ExpresionContext condicion,
                                        String verdadera, String falsa) {
        if (condicion instanceof ZetarianoParser.RelationalExprContext relacional) {
            c3d.emit(relacionalDe(relacional.getChild(1).getText()),
                    visit(relacional.expresion(0)), visit(relacional.expresion(1)),
                    verdadera);
        } else if (condicion instanceof ZetarianoParser.EqualityExprContext igualdad) {
            c3d.emit(igualdadDe(igualdad.getChild(1).getText()),
                    visit(igualdad.expresion(0)), visit(igualdad.expresion(1)),
                    verdadera);
        } else {
            c3d.emit(QuadrupleOp.IF_TRUE, visit(condicion), null, verdadera);
        }
        if (falsa != null) {
            c3d.emitGoto(falsa);
        }
    }

    private QuadrupleOp relacionalDe(String operador) {
        return switch (operador) {
            case "<" -> QuadrupleOp.IF_LT;
            case "<=" -> QuadrupleOp.IF_LE;
            case ">" -> QuadrupleOp.IF_GT;
            case ">=" -> QuadrupleOp.IF_GE;
            default -> QuadrupleOp.IF_EQ;
        };
    }

    private QuadrupleOp igualdadDe(String operador) {
        return "!=".equals(operador) ? QuadrupleOp.IF_NE : QuadrupleOp.IF_EQ;
    }

    @Override
    public String visitIncrementoDecremento(ZetarianoParser.IncrementoDecrementoContext ctx) {
        Symbol target = symbolTable.resolve(ctx.ID().getText());
        if (target == null) {
            return null;
        }
        String updated = c3d.newTemp();
        c3d.emit(ctx.INCREMENT() != null ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                load(target), "1", updated);
        store(target, updated);
        return null;
    }

    @Override
    public String visitSentenciaFor(ZetarianoParser.SentenciaForContext ctx) {
        if (ctx.forInit() != null) {
            visit(ctx.forInit());
        }

        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        String stepLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(stepLabel);

        c3d.emitLabel(condLabel);
        if (ctx.expresion() != null) {
            emitirSaltoDeCondicion(ctx.expresion(), bodyLabel, endLabel);
        } else {
            c3d.emitGoto(bodyLabel);
            c3d.emitGoto(endLabel);
        }

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());

        c3d.emitLabel(stepLabel);
        if (ctx.forUpdate() != null) {
            visit(ctx.forUpdate());
        }
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);
        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitSentenciaDoWhile(ZetarianoParser.SentenciaDoWhileContext ctx) {
        String bodyLabel = c3d.newLabel();
        String condLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());

        c3d.emitLabel(condLabel);
        emitirSaltoDeCondicion(ctx.expresion(), bodyLabel, null);

        c3d.emitLabel(endLabel);
        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitSentenciaWhile(ZetarianoParser.SentenciaWhileContext ctx) {
        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        c3d.emitLabel(condLabel);
        emitirSaltoDeCondicion(ctx.expresion(), bodyLabel, endLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);
        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitRetorno(ZetarianoParser.RetornoContext ctx) {
        if (ctx.expresion() != null) {
            String val = visit(ctx.expresion());
            c3d.emit(QuadrupleOp.STACK_SET, "P", val, null);
        }
        c3d.emit(QuadrupleOp.RETURN, null, null, null);
        return null;
    }

    @Override
    public String visitRomper(ZetarianoParser.RomperContext ctx) {
        if (!breakLabels.isEmpty()) {
            c3d.emitGoto(breakLabels.peek());
        }
        return null;
    }

    @Override
    public String visitContinuar(ZetarianoParser.ContinuarContext ctx) {
        if (!continueLabels.isEmpty()) {
            c3d.emitGoto(continueLabels.peek());
        }
        return null;
    }

    @Override
    public String visitLlamadaFuncionSemilla(ZetarianoParser.LlamadaFuncionSemillaContext ctx) {
        if (ctx.PRINTLN() != null || ctx.PRINT() != null) {
            if (ctx.argumentos() != null) {
                for (ZetarianoParser.ExpresionContext eCtx : ctx.argumentos().expresion()) {
                    String val = visit(eCtx);
                    c3d.emit(QuadrupleOp.PRINT_STR, val, null, null);
                }
            }
            if (ctx.PRINTLN() != null) {
                c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
            }
            return null;
        }
        return null;
    }

    @Override
    public String visitNewObjectExpr(ZetarianoParser.NewObjectExprContext ctx) {
        String className = ctx.ID().getText();
        Symbol cls = symbolTable.getClass(className);
        // En el Heap se reserva una casilla por atributo. Los metodos y el
        // constructor tambien son miembros de la clase, pero no ocupan memoria.
        int fieldCount = 0;
        if (cls != null) {
            for (Symbol m : cls.getMembers()) {
                if (m.getCategory() == SymbolCategory.FIELD) {
                    fieldCount++;
                }
            }
        }

        // Reservar memoria en Heap: t_heap = H; H = H + size;
        String heapStart = c3d.newTemp();
        c3d.emitAssign(heapStart, "H");
        c3d.emit(QuadrupleOp.ADD, "H", String.valueOf(fieldCount), "H");

        // Llamar constructor pasando la dirección en Heap como parámetro 0
        String constructorName = className + "_" + className;
        c3d.emit(QuadrupleOp.PARAM, heapStart, null, null);
        if (ctx.argumentos() != null) {
            for (ZetarianoParser.ExpresionContext argCtx : ctx.argumentos().expresion()) {
                String argVal = visit(argCtx);
                c3d.emit(QuadrupleOp.PARAM, argVal, null, null);
            }
        }
        c3d.emit(QuadrupleOp.CALL, constructorName, String.valueOf(1 + (ctx.argumentos() != null ? ctx.argumentos().expresion().size() : 0)), null);

        return heapStart;
    }

    @Override
    public String visitAddSubExpr(ZetarianoParser.AddSubExprContext ctx) {
        String left = visit(ctx.expresion(0));
        String right = visit(ctx.expresion(1));
        String temp = c3d.newTemp();
        c3d.emit(ctx.PLUS() != null ? QuadrupleOp.ADD : QuadrupleOp.SUB, left, right, temp);
        return temp;
    }

    @Override
    public String visitMulDivModExpr(ZetarianoParser.MulDivModExprContext ctx) {
        String left = visit(ctx.expresion(0));
        String right = visit(ctx.expresion(1));
        String temp = c3d.newTemp();
        QuadrupleOp op = ctx.MUL() != null ? QuadrupleOp.MUL : (ctx.DIV() != null ? QuadrupleOp.DIV : QuadrupleOp.MOD);
        c3d.emit(op, left, right, temp);
        return temp;
    }

    @Override
    public String visitIntLiteralExpr(ZetarianoParser.IntLiteralExprContext ctx) {
        return ctx.LITERAL_ENTERO().getText();
    }

    @Override
    public String visitDoubleLiteralExpr(ZetarianoParser.DoubleLiteralExprContext ctx) {
        return ctx.LITERAL_DECIMAL().getText();
    }

    @Override
    public String visitStringLiteralExpr(ZetarianoParser.StringLiteralExprContext ctx) {
        String raw = ctx.CADENA_TEXTO().getText();
        String content = raw.substring(1, raw.length() - 1);

        // Guardar caracteres de la cadena en el Heap terminando en 0
        String strStart = c3d.newTemp();
        c3d.emitAssign(strStart, "H");

        for (int i = 0; i < content.length(); i++) {
            c3d.emit(QuadrupleOp.HEAP_SET, "H", String.valueOf((int) content.charAt(i)), null);
            c3d.emit(QuadrupleOp.ADD, "H", "1", "H");
        }
        c3d.emit(QuadrupleOp.HEAP_SET, "H", "0", null);
        c3d.emit(QuadrupleOp.ADD, "H", "1", "H");

        return strStart;
    }

    @Override
    public String visitIdExpr(ZetarianoParser.IdExprContext ctx) {
        String name = ctx.ID().getText();
        Symbol s = symbolTable.resolve(name);
        if (s != null) {
            return load(s);
        }
        // Sin simbolo no se puede emitir acceso a memoria: se deja el nombre
        // tal cual para que el error semantico previo ya lo haya reportado.
        return name;
    }

    @Override
    public String visitParenExpr(ZetarianoParser.ParenExprContext ctx) {
        return visit(ctx.expresion());
    }

    // ====================== Operadores ======================

    @Override
    public String visitRelationalExpr(ZetarianoParser.RelationalExprContext ctx) {
        return compare(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    @Override
    public String visitEqualityExpr(ZetarianoParser.EqualityExprContext ctx) {
        return compare(ctx.expresion(0), ctx.expresion(1), ctx.getChild(1).getText());
    }

    /** Materializa una comparacion en un temporal con el valor 1 o 0. */
    private String compare(ZetarianoParser.ExpresionContext leftCtx,
                          ZetarianoParser.ExpresionContext rightCtx,
                          String operator) {
        String left = visit(leftCtx);
        String right = visit(rightCtx);
        QuadrupleOp op = "==".equals(operator) || "!=".equals(operator)
                ? igualdadDe(operator)
                : relacionalDe(operator);
        String temp = c3d.newTemp();
        c3d.emit(op, left, right, temp);
        return temp;
    }

    @Override
    public String visitAndExpr(ZetarianoParser.AndExprContext ctx) {
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.AND, visit(ctx.expresion(0)), visit(ctx.expresion(1)), temp);
        return temp;
    }

    @Override
    public String visitOrExpr(ZetarianoParser.OrExprContext ctx) {
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.OR, visit(ctx.expresion(0)), visit(ctx.expresion(1)), temp);
        return temp;
    }

    @Override
    public String visitNotExpr(ZetarianoParser.NotExprContext ctx) {
        // La negacion logica se expresa como "igual a cero".
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.IF_EQ, visit(ctx.expresion()), "0", temp);
        return temp;
    }

    @Override
    public String visitNegExpr(ZetarianoParser.NegExprContext ctx) {
        // El menos unario es una instruccion propia, no un 0 menos: asi el
        // codigo de tres direcciones se lee "t3 = -x".
        String temp = c3d.newTemp();
        c3d.emit(QuadrupleOp.NEG, visit(ctx.expresion()), null, temp);
        return temp;
    }

    @Override
    public String visitTrueExpr(ZetarianoParser.TrueExprContext ctx) {
        return "1";
    }

    @Override
    public String visitFalseExpr(ZetarianoParser.FalseExprContext ctx) {
        return "0";
    }

    @Override
    public String visitNullExpr(ZetarianoParser.NullExprContext ctx) {
        return "-1";
    }
}
