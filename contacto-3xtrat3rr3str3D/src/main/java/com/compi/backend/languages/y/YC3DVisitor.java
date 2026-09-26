package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.c3d.Arreglos;
import com.compi.backend.c3d.C3DGenerator;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolCategory;
import com.compi.backend.symbols.SymbolTable;
import com.compi.backend.symbols.Type;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class YC3DVisitor extends YBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final C3DGenerator c3d;
    private final Stack<String> breakLabels = new Stack<>();
    private final Stack<String> continueLabels = new Stack<>();

    public YC3DVisitor(SymbolTable symbolTable, C3DGenerator c3d) {
        this.symbolTable = symbolTable;
        this.c3d = c3d;
    }

    @Override
    public String visitFuncion_def(YParser.Funcion_defContext ctx) {
        String funcName = ctx.ID().getText();
        c3d.emit(QuadrupleOp.FUNCTION_START, null, null, funcName);

        symbolTable.enterScope("func_" + funcName, 0);

        // Si la función tiene parámetros, se leen desde el stack relativo a P
        if (ctx.parametros() != null) {
            int paramIndex = 0;
            for (YParser.ParametroContext paramCtx : ctx.parametros().parametro()) {
                String paramName = paramCtx.ID().getText();
                Symbol paramSymbol = symbolTable.resolve(paramName);
                if (paramSymbol != null) {
                    String paramTemp = c3d.newTemp();
                    String stackPosTemp = c3d.newTemp();
                    c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(paramIndex), stackPosTemp);
                    c3d.emit(QuadrupleOp.STACK_GET, stackPosTemp, null, paramTemp);
                }
                paramIndex++;
            }
        }

        visit(ctx.bloque());

        c3d.emit(QuadrupleOp.FUNCTION_END, null, null, funcName);
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitDeclaracion_variable(YParser.Declaracion_variableContext ctx) {
        String varName = !ctx.ID().isEmpty() ? ctx.ID(ctx.ID().size() - 1).getText() : "bool";

        // La generacion de codigo trabaja con su propio ambito, donde las locales
        // se declaran otra vez con el hueco que les toca en el marco. Son copias
        // de trabajo: no vuelven a salir en la tabla de simbolos.
        //
        // El tipo se deduce otra vez de la declaracion porque el del analizador
        // semantico ya no esta a mano al cerrar aquel ambito. Para el codigo
        // importa sobre todo si es un arreglo, de que forma tiene, y si es una
        // estructura, porque de eso depende como se ejecutan sus accesos.
        String nombreTipo = ctx.tipo_dato() != null ? ctx.tipo_dato().getText()
                : (ctx.ID().size() >= 2 ? ctx.ID(0).getText() : null);
        int[] sizes = new int[ctx.CORCHETE_IZQ().size()];
        List<TerminalNode> numeros = ctx.NUMERO_ENTERO();
        for (int i = 0; i < sizes.length && i < numeros.size(); i++) {
            sizes[i] = Integer.parseInt(numeros.get(i).getText());
        }
        Type base = nombreTipo == null ? Type.UNKNOWN : tipoDeNombre(nombreTipo);
        Type tipo = sizes.length == 0 ? base : Type.arrayOf(base, sizes);

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol declarada = new Symbol(varName, tipo, SymbolCategory.VARIABLE,
                offset, false).at(ctx).markWorking();
        symbolTable.define(declarada);
        Symbol sym = symbolTable.resolve(varName);
        if (sym == null) {
            sym = declarada;
        }

        // "Puntos p1 = {10, 20, 85.5}": la variable guarda la direccion de un
        // bloque de celdas, una por campo, en el orden en que se declararon.
        Symbol estructura = nombreTipo == null ? null : symbolTable.getStruct(nombreTipo);
        if (estructura != null && sizes.length == 0) {
            return declararEstructura(sym, estructura, ctx.expresion_inicializacion());
        }

        if (tipo.isArray()) {
            return declararArreglo(sym, tipo, ctx.expresion_inicializacion());
        }

        if (ctx.expresion_inicializacion() != null && ctx.expresion_inicializacion().expresion() != null) {
            String val = visit(ctx.expresion_inicializacion().expresion());
            String targetPos = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), targetPos);
            c3d.emit(QuadrupleOp.STACK_SET, targetPos, val, null);
        }
        return null;
    }

    /** Tipo que corresponde a un nombre escrito en la declaracion. */
    private Type tipoDeNombre(String nombre) {
        return switch (nombre.toLowerCase()) {
            case "entero" -> Type.INT;
            case "flotante" -> Type.DOUBLE;
            case "cadena" -> Type.STRING;
            case "caracter" -> Type.CHAR;
            case "bool" -> Type.BOOLEAN;
            default -> Type.structType(nombre);
        };
    }

    /**
     * Declara una variable de tipo estructura: reserva sus celdas en el heap y
     * guarda la direccion de la primera en la variable del marco.
     *
     * <p>El inicializador se escribe en el mismo orden en que se declararon los
     * campos, y las celdas que se dejen sin valor quedan en cero.</p>
     */
    private String declararEstructura(Symbol sym, Symbol estructura,
                                      YParser.Expresion_inicializacionContext inicializador) {
        String base = Arreglos.reservar(c3d, celdasDe(estructura));
        String posicion = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), posicion);
        c3d.emit(QuadrupleOp.STACK_SET, posicion, base, null);

        if (inicializador != null && !inicializador.elemento_lista().isEmpty()) {
            llenarCampos(base, inicializador.elemento_lista(), estructura, 0);
        } else if (inicializador != null && inicializador.expresion() != null) {
            // "Puntos p2 = p1" copia la direccion, igual que haria Java.
            c3d.emit(QuadrupleOp.STACK_SET, posicion, visit(inicializador.expresion()), null);
        }
        return null;
    }

    /** Cuantas celdas ocupa un tipo: un arreglo ocupa todas las suyas, todo lo demas una. */
    private int celdasDe(Type tipo) {
        if (tipo != null && tipo.isArray()) {
            return Math.max(tipo.totalSize(), 1);
        }
        return 1;
    }

    /** Cuantas celdas ocupa la estructura: la suma de las de sus campos. */
    private int celdasDe(Symbol estructura) {
        int total = 0;
        for (Symbol campo : estructura.getMembers()) {
            total += celdasDe(campo.getType());
        }
        return Math.max(total, 1);
    }

    /**
     * Escribe los valores de un inicializador de estructura, campo por campo.
     *
     * @param celda celda del bloque donde arranca este campo
     */
    private void llenarCampos(String base, List<YParser.Elemento_listaContext> elementos,
                              Symbol estructura, int celda) {
        int i = 0;
        for (Symbol campo : estructura.getMembers()) {
            if (i >= elementos.size()) {
                return;
            }
            YParser.Elemento_listaContext elemento = elementos.get(i);
            Type tipoCampo = campo.getType();
            String inicio = Arreglos.sumar(c3d, base, celda);

            if (elemento.LLAVE_IZQ() != null && tipoCampo != null && tipoCampo.isArray()) {
                // Un campo de arreglo: "{1, 2}" rellena sus celdas.
                int[] sizes = tipoCampo.getSizes();
                llenar(inicio, elemento.elemento_lista(), sizes, new int[sizes.length], 0);
            } else if (elemento.LLAVE_IZQ() != null && tipoCampo != null && tipoCampo.isStruct()) {
                // Un campo de estructura: "{10, 20}" se reparte entre sus campos.
                Symbol anidada = symbolTable.getStruct(tipoCampo.getCustomTypeName());
                if (anidada != null) {
                    llenarCampos(inicio, elemento.elemento_lista(), anidada, 0);
                }
            } else {
                // Un campo simple: "85.5". Si es una estructura, lo que se copia
                // es su direccion.
                c3d.emit(QuadrupleOp.HEAP_SET, inicio, visit(elemento.expresion()), null);
            }

            celda += celdasDe(tipoCampo);
            i++;
        }
    }

    /**
     * Declara un arreglo: reserva sus celdas en el heap y guarda la direccion de
     * la primera en la variable del marco.
     *
     * <p>Los arreglos se guardan aplanados, como pide el enunciado, asi que una
     * matriz de 3x2 son seis celdas contiguas.</p>
     */
    private String declararArreglo(Symbol sym, Type tipo,
                                   YParser.Expresion_inicializacionContext inicializador) {
        int celdas = tipo.totalSize();
        if (celdas <= 0) {
            return null;
        }
        String base = Arreglos.reservar(c3d, celdas);
        String targetPos = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), targetPos);
        c3d.emit(QuadrupleOp.STACK_SET, targetPos, base, null);

        if (inicializador != null && !inicializador.elemento_lista().isEmpty()) {
            int[] sizes = tipo.getSizes();
            llenar(base, inicializador.elemento_lista(), sizes, new int[sizes.length], 0);
        }
        return null;
    }

    /**
     * Escribe los valores de un inicializador celda a celda.
     *
     * <p>El recorrido sigue las llaves: cada nivel es una dimension y el numero
     * de elemento dentro del nivel es el indice de esa dimension.</p>
     */
    private void llenar(String base, List<YParser.Elemento_listaContext> elementos, int[] sizes,
                        int[] indices, int dimension) {
        for (int i = 0; i < elementos.size() && dimension < indices.length; i++) {
            YParser.Elemento_listaContext elemento = elementos.get(i);
            indices[dimension] = i;
            if (elemento.LLAVE_IZQ() != null) {
                llenar(base, elemento.elemento_lista(), sizes, indices, dimension + 1);
            } else {
                String dato = visit(elemento.expresion());
                Arreglos.escribirConstante(c3d, base, indices, sizes, dato);
            }
        }
    }

    @Override
    public String visitAsignacion(YParser.AsignacionContext ctx) {
        String val = ctx.expresion().isEmpty() ? "0" : visit(ctx.expresion(ctx.expresion().size() - 1));

        // "matriz[1][0] = 7" entra por la alternativa del acceso a miembro, que es
        // la primera que encaja con lo escrito.
        if (ctx.acceso_miembro() != null) {
            if (escribirEnAcceso(ctx.acceso_miembro(), val)) {
                return null;
            }
        }
        if (ctx.ID() != null) {
            String varName = ctx.ID().getText();
            Symbol sym = symbolTable.resolve(varName);
            if (sym == null) {
                return null;
            }
            String targetPos = c3d.newTemp();
            c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), targetPos);
            c3d.emit(QuadrupleOp.STACK_SET, targetPos, val, null);
        }
        return null;
    }

    /**
     * Escribe en la celda a la que apunta una cadena de acceso.
     *
     * @return true si la cadena era un acceso a arreglo o a un campo de
     *         estructura y se ha escrito la celda
     */
    private boolean escribirEnAcceso(YParser.Acceso_miembroContext ctx, String valor) {
        String direccion = direccionDeAcceso(ctx);
        if (direccion == null) {
            return false;
        }
        c3d.emit(QuadrupleOp.HEAP_SET, direccion, valor, null);
        return true;
    }

    /**
     * Direccion de la celda a la que lleva una cadena de acceso, sea un arreglo
     * ({@code m[1][0]}) o una estructura ({@code p.promedio}, {@code p.tabla[2]}).
     *
     * <p>Se recorre en dos pasos: primero se separa la cadena en sus pasos (un
     * indice o un nombre de campo, en el orden en que estan escritos) y despues
     * se ejecutan sobre la direccion. Asi {@code m[1][0]} son dos pasos de indice
     * y {@code p.tabla[2]} es un paso de campo seguido de uno de indice.</p>
     *
     * @return el temporal con la direccion, o null si la cadena no lleva a una
     *         celda simple (por ejemplo, si se pide una fila entera)
     */
    private String direccionDeAcceso(YParser.Acceso_miembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.ID(0).getText());
        if (base == null) {
            return null;
        }
        String direccion = leerVariable(base);
        Type tipo = base.getType();

        for (Object paso : pasosDeAcceso(ctx)) {
            if (paso instanceof String nombreCampo) {
                Symbol campo = campoDe(tipo, nombreCampo);
                if (campo == null) {
                    return null;
                }
                direccion = Arreglos.sumar(c3d, direccion, posicionDeCampo(campo)[0]);
                tipo = campo.getType();
            } else {
                if (tipo == null || !tipo.isArray() || !tipo.tieneTamanos()) {
                    return null;
                }
                direccion = Arreglos.direccion(c3d, direccion,
                        List.of(visit((YParser.ExpresionContext) paso)), tipo.getSizes());
                tipo = tipo.desindexar(1);
            }
        }
        if (tipo == null || tipo.isArray() || tipo.isStruct()) {
            return null;
        }
        return direccion;
    }

    /**
     * Separa una cadena de acceso en sus pasos, en el orden en que se escribieron.
     *
     * @return un elemento por paso: el nombre del campo como {@link String}, o la
     *         expresion del indice como contexto del parser
     */
    private List<Object> pasosDeAcceso(YParser.Acceso_miembroContext ctx) {
        List<Object> pasos = new ArrayList<>();
        int i = 1;
        while (i < ctx.getChildCount()) {
            if (".".equals(ctx.getChild(i).getText())) {
                // Punto: el siguiente hijo es el nombre del campo.
                pasos.add(ctx.getChild(i + 1).getText());
                i += 2;
            } else {
                // Corchete: apertura, expresion del indice y cierre.
                pasos.add(ctx.getChild(i + 1));
                i += 3;
            }
        }
        return pasos;
    }

    /** Campo de una estructura por su nombre, o null si no existe. */
    private Symbol campoDe(Type tipo, String nombre) {
        if (tipo == null || !tipo.isStruct()) {
            return null;
        }
        Symbol estructura = symbolTable.getStruct(tipo.getCustomTypeName());
        return estructura == null ? null : estructura.getMember(nombre);
    }

    /**
     * Celda del bloque donde arranca un campo.
     *
     * <p>Los campos se guardan aplanados y en el orden de la declaracion, asi que
     * un campo de arreglo ocupa todas sus celdas y el siguiente campo no empieza
     * hasta despues.</p>
     */
    private int[] posicionDeCampo(Symbol campo) {
        Symbol estructura = symbolTable.getStruct(campo.getScope());
        if (estructura == null) {
            return new int[]{0};
        }
        int celda = 0;
        for (Symbol otro : estructura.getMembers()) {
            if (otro == campo) {
                return new int[]{celda};
            }
            celda += celdasDe(otro.getType());
        }
        return new int[]{0};
    }

    /** Lee una variable del marco actual. */
    private String leerVariable(Symbol sym) {
        String posicion = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), posicion);
        String valor = c3d.newTemp();
        c3d.emit(QuadrupleOp.STACK_GET, posicion, null, valor);
        return valor;
    }

    @Override
    public String visitAcceso_miembro(YParser.Acceso_miembroContext ctx) {
        String valor = leerCelda(ctx);
        if (valor != null) {
            return valor;
        }
        // Acceso a un miembro de una estructura: aqui se devuelve el valor de la
        // variable base. El analizador semantico es quien avisa de estos casos.
        return ctx.ID(0).getText();
    }

    /** Lee la celda a la que apunta una cadena de acceso. */
    private String leerCelda(YParser.Acceso_miembroContext ctx) {
        String direccion = direccionDeAcceso(ctx);
        if (direccion == null) {
            return null;
        }
        String valor = c3d.newTemp();
        c3d.emit(QuadrupleOp.HEAP_GET, direccion, null, valor);
        return valor;
    }

    @Override
    public String visitSi_sentencia(YParser.Si_sentenciaContext ctx) {
        // La cadena "si / sino si / sino / contrario" se recorre de arriba abajo y
        // cada rama recibe la etiqueta del falso de la prueba que la precede, de
        // modo que solo se evalua la primera condicion que sale verdadera.
        List<YParser.CondicionContext> condiciones = new ArrayList<>();
        List<YParser.BloqueContext> bloques = new ArrayList<>();

        condiciones.add(ctx.condicion());
        bloques.add(ctx.bloque());
        for (YParser.Sino_si_bloqueContext sinoSi : ctx.sino_si_bloque()) {
            condiciones.add(sinoSi.condicion());
            bloques.add(sinoSi.bloque());
        }
        // Un "sino" puede llevar condicion propia; si no la lleva, es la ultima
        // rama y entra donde cae el falso de la prueba anterior.
        YParser.Sino_bloqueContext sinSi = ctx.sino_bloque();
        if (sinSi != null && sinSi.condicion() != null) {
            condiciones.add(sinSi.condicion());
            bloques.add(sinSi.bloque());
            sinSi = null;
        }
        YParser.BloqueContext contrario =
                ctx.contrario_bloque() == null ? null : ctx.contrario_bloque().bloque();

        String endLabel = c3d.newLabel();
        for (int i = 0; i < condiciones.size(); i++) {
            boolean ultima = i == condiciones.size() - 1;
            String verdaderaLabel = c3d.newLabel();
            // El falso de la ultima prueba lleva al final de la cadena, salvo que
            // detras quede un "sino" o un "contrario": esos se ejecutan siempre
            // que ninguna condicion se cumple, asi que el falso cae en ellos.
            boolean alFinal = ultima && sinSi == null && contrario == null;
            String falsaLabel = alFinal ? endLabel : c3d.newLabel();

            emitirSaltoDeCondicion(condiciones.get(i), verdaderaLabel, falsaLabel);
            c3d.emitLabel(verdaderaLabel);
            visit(bloques.get(i));
            c3d.emitGoto(endLabel);
            if (!alFinal) {
                c3d.emitLabel(falsaLabel);
            }
        }

        if (sinSi != null) {
            visit(sinSi.bloque());
            c3d.emitGoto(endLabel);
        }
        if (contrario != null) {
            visit(contrario);
        }
        c3d.emitLabel(endLabel);

        return null;
    }

    /**
     * Emite la prueba de una condicion con la forma que dio el docente: si es
     * una comparacion, el salto relacional directo ({@code if (x &gt; z) goto et1})
     * y, detras, el salto al camino contrario. Una condicion que no sea una
     * comparacion se evalua antes en un temporal booleano.
     *
     * @param verdadera etiqueta a la que se salta cuando la condicion se cumple
     * @param falsa      etiqueta del camino contrario, o null si no hace falta
     *                  saltar (en el "hacer ... mientras" la caida ya es el final)
     */
    private void emitirSaltoDeCondicion(YParser.CondicionContext condicion,
                                        String verdadera, String falsa) {
        YParser.ExpresionContext expresion = condicion.expresion();
        // Solo es una comparacion si trae su operador relacional y los dos
        // operandos: "expresion operador_relacional expresion".
        if (expresion.operador_relacional() != null && expresion.expresion().size() == 2) {
            c3d.emit(relacionalDe(expresion.operador_relacional().getText()),
                    visit(expresion.expresion(0)), visit(expresion.expresion(1)), verdadera);
        } else {
            c3d.emit(QuadrupleOp.IF_TRUE, visit(expresion), null, verdadera);
        }
        if (falsa != null) {
            c3d.emitGoto(falsa);
        }
    }

    /** Traduce el operador relacional de la gramatica a su cuarteta de salto. */
    private QuadrupleOp relacionalDe(String operador) {
        if ("==".equals(operador)) {
            return QuadrupleOp.IF_EQ;
        }
        if ("!=".equals(operador)) {
            return QuadrupleOp.IF_NE;
        }
        if ("<".equals(operador)) {
            return QuadrupleOp.IF_LT;
        }
        if (">".equals(operador)) {
            return QuadrupleOp.IF_GT;
        }
        return QuadrupleOp.IF_EQ;
    }

    @Override
    public String visitMientras_sentencia(YParser.Mientras_sentenciaContext ctx) {
        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        c3d.emitLabel(condLabel);
        emitirSaltoDeCondicion(ctx.condicion(), bodyLabel, endLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitHacer_mientras_sentencia(YParser.Hacer_mientras_sentenciaContext ctx) {
        String condLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(condLabel);

        // El cuerpo se ejecuta al menos una vez, asi que se entra a el sin probar
        // nada: la prueba va despues, como en el "hacer ... mientras" de Pascal.
        visit(ctx.bloque());

        c3d.emitLabel(condLabel);
        emitirSaltoDeCondicion(ctx.condicion(), condLabel, null);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitPara_sentencia(YParser.Para_sentenciaContext ctx) {
        String condLabel = c3d.newLabel();
        String bodyLabel = c3d.newLabel();
        // "continuar" en un "para" tiene que ejecutar el incremento antes de
        // volver a probar, asi que salta aqui y no a la prueba.
        String incrementoLabel = c3d.newLabel();
        String endLabel = c3d.newLabel();

        breakLabels.push(endLabel);
        continueLabels.push(incrementoLabel);

        // La inicializacion va una sola vez, antes de la primera prueba.
        if (ctx.declaracion_variable() != null) {
            visit(ctx.declaracion_variable());
        } else if (ctx.asignacion() != null) {
            visit(ctx.asignacion());
        }

        c3d.emitLabel(condLabel);
        emitirSaltoDeCondicion(ctx.condicion(), bodyLabel, endLabel);

        c3d.emitLabel(bodyLabel);
        visit(ctx.bloque());

        c3d.emitLabel(incrementoLabel);
        visit(ctx.incremento_decremento());
        c3d.emitGoto(condLabel);

        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    @Override
    public String visitIncremento_decremento(YParser.Incremento_decrementoContext ctx) {
        // "i++" y "++i" se compilan igual: en Y el incremento es una sentencia,
        // no un valor, asi que solo se actualiza la variable.
        YParser.Acceso_miembroContext acceso = ctx.acceso_miembro();
        String nombre = acceso != null ? acceso.ID(0).getText()
                : (ctx.ID() == null ? null : ctx.ID().getText());
        Symbol sym = nombre == null ? null : symbolTable.resolve(nombre);
        if (sym == null) {
            return null;
        }
        boolean esArreglo = sym.getType() != null && sym.getType().isArray();
        // La celda de un arreglo se lee antes de sumarle uno; si la cadena de
        // indices no llega a una celda (una fila, por ejemplo) no hay nada que
        // incrementar y el analizador semantico ya lo avisa.
        String antes = esArreglo ? leerCelda(acceso) : leerVariable(sym);
        if (antes == null) {
            return null;
        }
        String despues = c3d.newTemp();
        c3d.emit(ctx.SUMA_ABREVIADA() != null ? QuadrupleOp.ADD : QuadrupleOp.SUB,
                antes, "1", despues);

        if (esArreglo) {
            escribirEnAcceso(acceso, despues);
            return null;
        }
        String posicion = c3d.newTemp();
        c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), posicion);
        c3d.emit(QuadrupleOp.STACK_SET, posicion, despues, null);
        return null;
    }

    @Override
    public String visitElegir_sentencia(YParser.Elegir_sentenciaContext ctx) {
        // Se prueba el valor contra cada caso, uno detras de otro, y el que
        // coincide salta a su bloque. Si no coincide con ninguno, se va al
        // "siempre", o al final del elegir si no lo hay.
        String endLabel = c3d.newLabel();
        YParser.Siempre_bloqueContext siempre = ctx.siempre_bloque();
        String defectoLabel = siempre != null ? c3d.newLabel() : endLabel;

        // Dentro de un caso no hay ciclo al que volver, asi que "continuar"
        // sale de la seleccion igual que "romper".
        breakLabels.push(endLabel);
        continueLabels.push(endLabel);

        String valor = visit(ctx.expresion());
        List<YParser.Caso_bloqueContext> casos = ctx.caso_bloque();
        List<String> etiquetas = new ArrayList<>();
        for (YParser.Caso_bloqueContext caso : casos) {
            String etiqueta = c3d.newLabel();
            etiquetas.add(etiqueta);
            c3d.emit(QuadrupleOp.IF_EQ, valor, visit(caso.expresion()), etiqueta);
        }
        c3d.emitGoto(defectoLabel);

        for (int i = 0; i < casos.size(); i++) {
            c3d.emitLabel(etiquetas.get(i));
            YParser.Caso_bloqueContext caso = casos.get(i);
            visit(caso.bloque_interno_opcional());
            // Al final de un caso se va al final del elegir, salvo que el caso
            // termine en "romper", que ya salio y dejaria un salto de mas. El
            // "romper" puede quedarse en el cuerpo o recogerse la propia regla
            // del caso, segun como lo reparta el analizador.
            if (caso.ROMPER() == null && !terminaConRomper(caso.bloque_interno_opcional())) {
                c3d.emitGoto(endLabel);
            }
        }
        if (siempre != null) {
            c3d.emitLabel(defectoLabel);
            visit(siempre.bloque_interno_opcional());
        }
        c3d.emitLabel(endLabel);

        breakLabels.pop();
        continueLabels.pop();
        return null;
    }

    /** Dice si la ultima sentencia con codigo de un bloque es un "romper". */
    private boolean terminaConRomper(YParser.Bloque_interno_opcionalContext cuerpo) {
        List<YParser.SentenciaContext> sentencias = cuerpo.sentencia();
        // Los saltos de linea en blanco se cuelan como sentencias sin codigo, asi
        // que se mira hacia atras saltandoslos.
        for (int i = sentencias.size() - 1; i >= 0; i--) {
            YParser.SentenciaContext sentencia = sentencias.get(i);
            if (sentencia.getChildCount() == 1
                    && sentencia.getChild(0) instanceof TerminalNode) {
                continue;
            }
            return sentencia.romper_sentencia() != null;
        }
        return false;
    }

    @Override
    public String visitRetornar_sentencia(YParser.Retornar_sentenciaContext ctx) {
        if (ctx.expresion() != null) {
            String val = visit(ctx.expresion());
            // El valor de retorno se coloca convencionalmente en stack[P]
            c3d.emit(QuadrupleOp.STACK_SET, "P", val, null);
        }
        c3d.emit(QuadrupleOp.RETURN, null, null, null);
        return null;
    }

    @Override
    public String visitRomper_sentencia(YParser.Romper_sentenciaContext ctx) {
        if (!breakLabels.isEmpty()) {
            c3d.emitGoto(breakLabels.peek());
        }
        return null;
    }

    @Override
    public String visitContinuar_sentencia(YParser.Continuar_sentenciaContext ctx) {
        if (!continueLabels.isEmpty()) {
            c3d.emitGoto(continueLabels.peek());
        }
        return null;
    }

    @Override
    public String visitImprimir_sentencia(YParser.Imprimir_sentenciaContext ctx) {
        if (ctx.argumentos() != null) {
            for (YParser.ExpresionContext exprCtx : ctx.argumentos().expresion()) {
                String val = visit(exprCtx);
                if (esCadena(exprCtx)) {
                    // Una cadena se imprime desde el heap, asi que se pasa la
                    // direccion donde se escribieron sus caracteres.
                    c3d.emit(QuadrupleOp.PRINT_STR, val, null, null);
                } else {
                    c3d.emit(QuadrupleOp.PRINT_INT, val, null, null);
                }
            }
        }
        c3d.emit(QuadrupleOp.PRINTLN, null, null, null);
        return null;
    }

    /**
     * true si la expresion es una cadena.
     *
     * <p>Se mira primero lo escrito: un literal entre comillas es una cadena
     * aunque todavia no se haya escrito en el heap. Si no, se pregunta a la tabla
     * de simbolos, que es lo que permite imprimir tanto "x" de tipo cadena como
     * "leer()".</p>
     */
    private boolean esCadena(YParser.ExpresionContext ctx) {
        if (ctx.leer_funcion() != null) {
            return true;
        }
        if (ctx.termino().isEmpty()) {
            return false;
        }
        YParser.TerminoContext termino = ctx.termino(0);
        if (termino.CADENA_TEXTO() != null) {
            return true;
        }
        if (termino.ID() != null) {
            Symbol sym = symbolTable.resolve(termino.ID().getText());
            return sym != null && Type.STRING.equals(sym.getType());
        }
        if (termino.acceso_miembro() != null) {
            return Type.STRING.equals(tipoDeAcceso(termino.acceso_miembro()));
        }
        return false;
    }

    /**
     * Tipo que hay en la celda a la que lleva una cadena de acceso, sin generar
     * codigo: los indices solo se cuentan, no se evaluan.
     */
    private Type tipoDeAcceso(YParser.Acceso_miembroContext ctx) {
        Symbol base = symbolTable.resolve(ctx.ID(0).getText());
        Type tipo = base == null ? null : base.getType();
        for (Object paso : pasosDeAcceso(ctx)) {
            if (tipo == null) {
                return Type.UNKNOWN;
            }
            if (paso instanceof String nombreCampo) {
                Symbol campo = campoDe(tipo, nombreCampo);
                tipo = campo == null ? Type.UNKNOWN : campo.getType();
            } else if (tipo.isArray() && tipo.tieneTamanos()) {
                tipo = tipo.desindexar(1);
            } else {
                tipo = Type.UNKNOWN;
            }
        }
        return tipo;
    }

    @Override
    public String visitExpresion(YParser.ExpresionContext ctx) {
        // La gramatica reescribe la recursion izquierda, asi que una expresion
        // llega de dos formas: los operadores aritmeticos se quedan en el mismo
        // nivel ("a + b * c" es una sola cadena) mientras que los relacionales y
        // los logicos envuelven a sus operandos. Cada caso se recorre como toca.

        if (ctx.operador_negacion() != null) {
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.NOT, visit(ctx.expresion(0)), null, temp);
            return temp;
        }

        if (ctx.operador_logico() != null) {
            return logico(visit(ctx.expresion(0)), ctx.operador_logico().getText(),
                    ctx.expresion(1));
        }

        if (ctx.operador_relacional() != null) {
            String izquierda = visit(ctx.expresion(0));
            String derecha = visit(ctx.expresion(1));
            String temp = c3d.newTemp();
            String verdaderaLabel = c3d.newLabel();
            String endLabel = c3d.newLabel();

            String op = ctx.operador_relacional().getText();
            QuadrupleOp qOp = "==".equals(op) ? QuadrupleOp.IF_EQ :
                    "!=".equals(op) ? QuadrupleOp.IF_NE :
                    "<".equals(op) ? QuadrupleOp.IF_LT :
                    ">".equals(op) ? QuadrupleOp.IF_GT : QuadrupleOp.IF_GE;

            c3d.emit(qOp, izquierda, derecha, verdaderaLabel);
            c3d.emitAssign(temp, "0");
            c3d.emitGoto(endLabel);
            c3d.emitLabel(verdaderaLabel);
            c3d.emitAssign(temp, "1");
            c3d.emitLabel(endLabel);

            return temp;
        }

        if (!ctx.termino().isEmpty() || !ctx.operador_aritmetico().isEmpty()) {
            // Cadena aritmetica: los operadores van todos al mismo nivel, asi que
            // se aplican de izquierda a derecha sobre lo ya acumulado.
            String acumulado = null;
            String pendiente = null;
            for (int i = 0; i < ctx.getChildCount(); i++) {
                ParseTree hijo = ctx.getChild(i);
                if (hijo instanceof YParser.Operador_aritmeticoContext) {
                    pendiente = hijo.getText();
                    continue;
                }
                String valor = visit(hijo);
                if (acumulado == null) {
                    acumulado = valor;
                } else {
                    String temp = c3d.newTemp();
                    c3d.emit(operadorAritmetico(pendiente), acumulado, valor, temp);
                    acumulado = temp;
                }
            }
            return acumulado == null ? "0" : acumulado;
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        if (ctx.leer_funcion() != null) {
            return visit(ctx.leer_funcion());
        }

        return "0";
    }

    /** Traduce el signo del operador aritmetico a su quadruplo. */
    private QuadrupleOp operadorAritmetico(String op) {
        return switch (op == null ? "" : op) {
            case "+" -> QuadrupleOp.ADD;
            case "-" -> QuadrupleOp.SUB;
            case "*" -> QuadrupleOp.MUL;
            default -> QuadrupleOp.DIV;
        };
    }

    /**
     * Combina dos operandos con "&&" u "||" en corto circuito: el segundo solo
     * se mira cuando el primero no deja nada que decidir.
     *
     * @param derechaCtx el operando derecho, que se visita despues del salto para
     *                   que el salto lo pueda saltarse
     */
    private String logico(String izquierda, String operador,
                          YParser.ExpresionContext derechaCtx) {
        String temp = c3d.newTemp();
        String endLabel = c3d.newLabel();
        if ("&&".equals(operador)) {
            c3d.emitAssign(temp, "0");
            c3d.emit(QuadrupleOp.IF_FALSE, izquierda, null, endLabel);
        } else {
            c3d.emitAssign(temp, "1");
            c3d.emit(QuadrupleOp.IF_TRUE, izquierda, null, endLabel);
        }
        c3d.emitAssign(temp, visit(derechaCtx));
        c3d.emitLabel(endLabel);
        return temp;
    }

    @Override
    public String visitLlamada_funcion(YParser.Llamada_funcionContext ctx) {
        String funcName = ctx.ID().getText();
        if (ctx.argumentos() != null) {
            for (YParser.ExpresionContext argCtx : ctx.argumentos().expresion()) {
                String argVal = visit(argCtx);
                c3d.emit(QuadrupleOp.PARAM, argVal, null, null);
            }
        }
        String retTemp = c3d.newTemp();
        c3d.emit(QuadrupleOp.CALL, funcName, String.valueOf(ctx.argumentos() != null ? ctx.argumentos().expresion().size() : 0), retTemp);
        return retTemp;
    }

    /**
     * Escribe una cadena literal en el heap, terminada en cero, y devuelve la
     * direccion de su primer caracter.
     *
     * <p>Es la misma convencion que usa el runtime de impresion: {@code print_str}
     * recibe la direccion y va leyendo hasta el cero.</p>
     */
    private String cadenaEnHeap(String literal) {
        String contenido = literal.substring(1, literal.length() - 1);
        String inicio = c3d.newTemp();
        c3d.emitAssign(inicio, "H");
        for (int i = 0; i < contenido.length(); i++) {
            c3d.emit(QuadrupleOp.HEAP_SET, "H", String.valueOf((int) contenido.charAt(i)), null);
            c3d.emit(QuadrupleOp.ADD, "H", "1", "H");
        }
        c3d.emit(QuadrupleOp.HEAP_SET, "H", "0", null);
        c3d.emit(QuadrupleOp.ADD, "H", "1", "H");
        return inicio;
    }

    /** Valor numerico de un caracter literal, que es lo que se guarda en una celda. */
    private String caracterDe(String literal) {
        return String.valueOf((int) literal.charAt(1));
    }

    @Override
    public String visitTermino(YParser.TerminoContext ctx) {
        if (ctx.NUMERO_ENTERO() != null) return ctx.NUMERO_ENTERO().getText();
        if (ctx.NUMERO_DECIMAL() != null) return ctx.NUMERO_DECIMAL().getText();
        if (ctx.VERDADERO() != null) return "1";
        if (ctx.FALSO() != null) return "0";
        if (ctx.CARACTER() != null) return caracterDe(ctx.CARACTER().getText());
        if (ctx.CADENA_TEXTO() != null) return cadenaEnHeap(ctx.CADENA_TEXTO().getText());

        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Symbol sym = symbolTable.resolve(name);
            if (sym != null) {
                String temp = c3d.newTemp();
                String posTemp = c3d.newTemp();
                c3d.emit(QuadrupleOp.ADD, "P", String.valueOf(sym.getOffset()), posTemp);
                c3d.emit(QuadrupleOp.STACK_GET, posTemp, null, temp);
                return temp;
            }
            return name;
        }

        if (ctx.acceso_miembro() != null) {
            return visit(ctx.acceso_miembro());
        }

        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
        }

        // Menos unario: "-x" es una instruccion propia, no un 0 menos x.
        if (ctx.MENOS() != null) {
            String temp = c3d.newTemp();
            c3d.emit(QuadrupleOp.NEG, visit(ctx.termino()), null, temp);
            return temp;
        }

        return "0";
    }
}
