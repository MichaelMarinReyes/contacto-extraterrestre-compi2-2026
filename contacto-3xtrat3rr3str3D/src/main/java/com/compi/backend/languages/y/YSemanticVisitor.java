package com.compi.backend.languages.y;

import com.compi.YBaseVisitor;
import com.compi.YParser;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.Diagnostico;
import com.compi.backend.symbols.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class YSemanticVisitor extends YBaseVisitor<Type> {
    private final SymbolTable symbolTable;
    private final List<CompilationError> errors = new ArrayList<>();
    private Type currentFunctionReturnType = Type.VOID;

    public YSemanticVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<CompilationError> getErrors() {
        return errors;
    }

    @Override
    public Type visitEstructuras_seccion(YParser.Estructuras_seccionContext ctx) {
        // Registrar estructuras primero para permitir tipos referenciados
        for (YParser.Estructura_defContext structCtx : ctx.estructura_def()) {
            if (structCtx.ID() != null) {
                registrarEstructura(structCtx.ID().getText(), structCtx.campo_struct(), structCtx);
            }
        }
        return Type.VOID;
    }

    @Override
    public Type visitDeclaracion_estructura_local(YParser.Declaracion_estructura_localContext ctx) {
        // "Se pueden declarar estructuras dentro de las funciones" (enunciado).
        // Se registran igual que las globales para que despues se puedan leer y
        // escribir sus campos.
        registrarEstructura(ctx.ID().getText(), ctx.campo_struct(), ctx);
        return Type.VOID;
    }

    /**
     * Registra una estructura con sus campos.
     *
     * <p>Los campos se guardan en el orden de la declaracion, que es el orden en
     * que tambien ocuparan celdas en el heap.</p>
     */
    private void registrarEstructura(String nombre, List<YParser.Campo_structContext> campos,
                                     ParserRuleContext ctx) {
        if (symbolTable.getStruct(nombre) != null) {
            return;
        }
        Symbol structSymbol = new Symbol(nombre, Type.structType(nombre),
                SymbolCategory.STRUCT).at(ctx);
        symbolTable.addStruct(structSymbol);

        int offset = 0;
        for (YParser.Campo_structContext fieldCtx : campos) {
            if (fieldCtx.ID().size() >= 1) {
                String fieldTypeStr = fieldCtx.tipo_dato() != null ? fieldCtx.tipo_dato().getText() : fieldCtx.ID(0).getText();
                String fieldName = fieldCtx.tipo_dato() != null ? fieldCtx.ID(0).getText() : (fieldCtx.ID().size() > 1 ? fieldCtx.ID(1).getText() : "campo");
                Type fieldType = resolveType(fieldTypeStr);

                if (!fieldCtx.CORCHETE_IZQ().isEmpty()) {
                    // El enunciado pide que el tamaño sea constante, asi que se
                    // puede guardar la forma completa y luego validar los indices
                    // de "p.miArray[3]".
                    int[] sizes = new int[fieldCtx.CORCHETE_IZQ().size()];
                    List<TerminalNode> numeros = fieldCtx.NUMERO_ENTERO();
                    for (int i = 0; i < sizes.length && i < numeros.size(); i++) {
                        sizes[i] = Integer.parseInt(numeros.get(i).getText());
                    }
                    fieldType = Type.arrayOf(fieldType, sizes);
                }

                Symbol fieldSymbol = new Symbol(fieldName, fieldType, SymbolCategory.FIELD,
                        offset++, false).at(fieldCtx);
                structSymbol.addMember(fieldSymbol);
            }
        }
    }

    @Override
    public Type visitFuncion_def(YParser.Funcion_defContext ctx) {
        String funcName = ctx.ID().getText();
        Type returnType = Type.VOID;
        if (ctx.tipo_dato() != null) {
            returnType = resolveType(ctx.tipo_dato().getText());
        }
        currentFunctionReturnType = returnType;

        Symbol funcSymbol = new Symbol(funcName, returnType, SymbolCategory.FUNCTION).at(ctx);
        funcSymbol.setReturnType(returnType);

        symbolTable.enterScope("func_" + funcName, 0);

        if (ctx.parametros() != null) {
            for (YParser.ParametroContext paramCtx : ctx.parametros().parametro()) {
                String paramName = paramCtx.ID().getText();
                Type paramType = resolveType(paramCtx.tipo_dato().getText());
                boolean isByRef = false;

                // [] entero miArray -> Arreglos pasan exclusivamente por referencia (PDF pág. 287)
                if (paramCtx.CORCHETE_IZQ() != null) {
                    paramType = Type.array(paramType, 1);
                    isByRef = true;
                }
                // {} MiEstructura miEstructura -> Estructuras pasan exclusivamente por referencia (PDF pág. 295)
                if (paramCtx.LLAVE_IZQ() != null) {
                    paramType = Type.structType(paramCtx.tipo_dato().getText());
                    isByRef = true;
                }

                int offset = symbolTable.getCurrentScope().allocateOffset(1);
                Symbol paramSymbol = new Symbol(paramName, paramType, SymbolCategory.PARAMETER,
                        offset, false).at(paramCtx);
                paramSymbol.setByReference(isByRef);
                funcSymbol.addParameter(paramSymbol);

                if (!symbolTable.define(paramSymbol)) {
                    errors.add(Diagnostico.parametroDuplicado(paramName, paramCtx));
                }
            }
        }

        symbolTable.addFunction(funcSymbol);

        // Analizar cuerpo
        visit(ctx.bloque());

        symbolTable.exitScope();
        return Type.VOID;
    }

    @Override
    public Type visitDeclaracion_variable(YParser.Declaracion_variableContext ctx) {
        String typeName = ctx.tipo_dato() != null ? ctx.tipo_dato().getText() : "bool";
        String varName = !ctx.ID().isEmpty() ? ctx.ID(ctx.ID().size() - 1).getText() : "boolVar";
        if (ctx.tipo_dato() == null && ctx.ID().size() > 1) {
            typeName = ctx.ID(0).getText();
        }
        Type varType = resolveType(typeName);

        if (!ctx.CORCHETE_IZQ().isEmpty()) {
            // Un corchete por dimension, con su tamano: "matriz[3][2]" son seis
            // celdas, la primera de tres filas.
            varType = Type.arrayOf(varType, tamanosDe(ctx));
        }

        int offset = symbolTable.getCurrentScope().allocateOffset(1);
        Symbol varSymbol = new Symbol(varName, varType, SymbolCategory.VARIABLE, offset, false).at(ctx);

        if (!symbolTable.define(varSymbol)) {
            errors.add(Diagnostico.variableYaDeclarada(varName, ctx));
        }

        if (ctx.expresion_inicializacion() != null) {
            if (ctx.expresion_inicializacion().expresion() != null) {
                // Validar compatibilidad de tipo si hay expresión
                Type initType = visit(ctx.expresion_inicializacion().expresion());
                if (esConocido(initType) && esConocido(varType)
                        && !initType.isAssignableTo(varType)) {
                    errors.add(Diagnostico.inicializacionIncompatible(
                            varName, varType, initType, ctx));
                }
            } else if (varType.isArray()) {
                // "{1, 2}" o "{ {1, 2}, {3, 4} }": la forma tiene que encajar con
                // los corchetes de la declaracion.
                Type obtenido = tipoDeLista(varName, ctx.expresion_inicializacion().elemento_lista(),
                        ctx.expresion_inicializacion());
                CompilationError error = LiteralDeArreglo.validar(varName, varType, obtenido, ctx);
                if (error != null) {
                    errors.add(error);
                }
            }
        }

        return varType;
    }

    /** Los tamanos escritos en los corchetes, en orden. */
    private static int[] tamanosDe(YParser.Declaracion_variableContext ctx) {
        int[] sizes = new int[ctx.CORCHETE_IZQ().size()];
        List<TerminalNode> numeros = ctx.NUMERO_ENTERO();
        for (int i = 0; i < sizes.length && i < numeros.size(); i++) {
            sizes[i] = Integer.parseInt(numeros.get(i).getText());
        }
        return sizes;
    }

    /**
     * Tipo de una lista de valores entre llaves, deducido de las llaves.
     *
     * <p>Es la misma idea que en los otros lenguajes: una lista de valores es una
     * dimension, y cada nivel de llaves anidadas es una dimension mas. Todas las
     * filas tienen que tener la misma forma.</p>
     */
    private Type tipoDeLista(String nombre, List<YParser.Elemento_listaContext> elementos,
                             ParserRuleContext donde) {
        int filas = elementos.size();
        Type tipoFila = Type.UNKNOWN;
        int[] formaFila = new int[0];
        for (int f = 0; f < filas; f++) {
            YParser.Elemento_listaContext elemento = elementos.get(f);
            Type tipo = elemento.LLAVE_IZQ() == null
                    ? visit(elemento.expresion())
                    : tipoDeLista(nombre, elemento.elemento_lista(), elemento);
            int[] forma = tipo.isArray() ? tipo.getSizes() : new int[0];
            if (f == 0) {
                tipoFila = tipo.isArray() ? tipo.getElementType() : tipo;
                formaFila = forma;
            } else if (!java.util.Arrays.equals(forma, formaFila)) {
                errors.add(Diagnostico.filasDesiguales(nombre, f + 1,
                        anchoDe(formaFila, elementos.get(0)),
                        anchoDe(forma, elemento), elemento));
            }
        }
        int[] sizes = new int[formaFila.length + 1];
        sizes[0] = filas;
        System.arraycopy(formaFila, 0, sizes, 1, formaFila.length);
        return Type.arrayOf(tipoFila, sizes);
    }

    /** Cuantos elementos se ven en la primera dimension de una fila. */
    private static int anchoDe(int[] forma, YParser.Elemento_listaContext fila) {
        if (forma.length > 0) {
            return forma[0];
        }
        return fila.LLAVE_IZQ() == null ? 1 : fila.elemento_lista().size();
    }

    @Override
    public Type visitAsignacion(YParser.AsignacionContext ctx) {
        Type targetType = Type.UNKNOWN;
        String name = "";

        if (ctx.ID() != null) {
            name = ctx.ID().getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostico.variableNoDeclarada(name, ctx));
            } else {
                targetType = s.getType();
                if (!ctx.CORCHETE_IZQ().isEmpty() && targetType != null) {
                    // "matriz[1][0] = 7" desindexa dos dimensiones, no una.
                    targetType = targetType.desindexar(ctx.CORCHETE_IZQ().size());
                }
            }
        } else if (ctx.acceso_miembro() != null) {
            targetType = visit(ctx.acceso_miembro());
        }

        Type exprType = ctx.expresion().isEmpty() ? Type.UNKNOWN : visit(ctx.expresion(ctx.expresion().size() - 1));
        if (esConocido(targetType) && esConocido(exprType)
                && !exprType.isAssignableTo(targetType)) {
            errors.add(Diagnostico.asignacionIncompatible(targetType, exprType, ctx));
        }

        return targetType;
    }

    @Override
    public Type visitRetornar_sentencia(YParser.Retornar_sentenciaContext ctx) {
        Type returnValType = Type.VOID;
        if (ctx.expresion() != null) {
            returnValType = visit(ctx.expresion());
        }

        if (esConocido(currentFunctionReturnType) && esConocido(returnValType)
                && !returnValType.isAssignableTo(currentFunctionReturnType)) {
            errors.add(Diagnostico.tipoDeRetornoErroneo(
                    currentFunctionReturnType, returnValType, ctx));
        }

        return returnValType;
    }

    @Override
    public Type visitExpresion(YParser.ExpresionContext ctx) {
        // La gramatica reescribe la recursion izquierda, asi que una expresion
        // llega de dos formas: los operadores aritmeticos se quedan en el mismo
        // nivel ("a + b * c" es una sola cadena) mientras que los relacionales y
        // los logicos envuelven a sus operandos. Cada caso se recorre entero, para
        // que un simbolo que no exista se vea este donde este escrito.

        if (ctx.operador_negacion() != null) {
            Type t = visit(ctx.expresion(0));
            // Con una expresion que ya fallo el tipo puede venir nulo: entonces el
            // error ya esta reportado y aqui solo habria un NullPointerException.
            if (esConocido(t) && t.getDataType() != DataType.BOOLEAN) {
                errors.add(Diagnostico.operadorNegacionNoBooleano(ctx));
            }
            return Type.BOOLEAN;
        }

        if (ctx.operador_logico() != null) {
            Type t1 = visit(ctx.expresion(0));
            Type t2 = visit(ctx.expresion(1));
            return TypeCompatibility.checkLogical(t1, t2);
        }

        if (ctx.operador_relacional() != null) {
            Type t1 = visit(ctx.expresion(0));
            Type t2 = visit(ctx.expresion(1));
            return TypeCompatibility.checkRelational(t1, t2, ctx.operador_relacional().getText());
        }

        if (!ctx.termino().isEmpty() || !ctx.operador_aritmetico().isEmpty()) {
            // Cadena aritmetica: se recorren los dos operandos de cada operador,
            // de izquierda a derecha, y el tipo que sale es el del lado izquierdo.
            Type acumulado = null;
            String operador = null;
            for (int i = 0; i < ctx.getChildCount(); i++) {
                ParseTree hijo = ctx.getChild(i);
                if (hijo instanceof YParser.Operador_aritmeticoContext) {
                    operador = hijo.getText();
                    continue;
                }
                Type valor = visit(hijo);
                if (acumulado == null) {
                    acumulado = valor;
                } else {
                    acumulado = TypeCompatibility.checkArithmetic(acumulado, valor, operador);
                }
            }
            return acumulado == null ? Type.UNKNOWN : acumulado;
        }

        if (ctx.llamada_funcion() != null) {
            return visit(ctx.llamada_funcion());
        }

        if (ctx.leer_funcion() != null) {
            return Type.STRING;
        }

        return Type.UNKNOWN;
    }

    @Override
    public Type visitTermino(YParser.TerminoContext ctx) {
        if (ctx.NUMERO_ENTERO() != null) return Type.INT;
        if (ctx.NUMERO_DECIMAL() != null) return Type.DOUBLE;
        if (ctx.CADENA_TEXTO() != null) return Type.STRING;
        if (ctx.CARACTER() != null) return Type.CHAR;
        if (ctx.VERDADERO() != null || ctx.FALSO() != null) return Type.BOOLEAN;

        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Symbol s = symbolTable.resolve(name);
            if (s == null) {
                errors.add(Diagnostico.simboloNoResuelto(name, ctx));
                return Type.UNKNOWN;
            }
            return s.getType();
        }

        if (ctx.acceso_miembro() != null) {
            return visit(ctx.acceso_miembro());
        }

        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
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

    @Override
    public Type visitLlamada_funcion(YParser.Llamada_funcionContext ctx) {
        String funcName = ctx.ID().getText();
        Symbol func = symbolTable.getFunction(funcName);
        if (func == null) {
            errors.add(Diagnostico.funcionNoDefinida(funcName, ctx));
            return Type.UNKNOWN;
        }
        return func.getReturnType() != null ? func.getReturnType() : Type.VOID;
    }

    @Override
    public Type visitAcceso_miembro(YParser.Acceso_miembroContext ctx) {
        String baseName = ctx.ID(0).getText();
        Symbol baseSymbol = symbolTable.resolve(baseName);
        if (baseSymbol == null) {
            errors.add(Diagnostico.variableNoDeclarada(baseName, ctx));
            return Type.UNKNOWN;
        }

        Type currentType = baseSymbol.getType();
        if (currentType == null) {
            return Type.UNKNOWN;
        }

        int indices = contarIndices(ctx);
        if (indices > 0 && !tienePuntos(ctx) && currentType.isArray()
                && indices > currentType.getDimensions()) {
            errors.add(Diagnostico.dimensionesDeIndice(baseName, indices,
                    currentType.getDimensions(), ctx));
            return Type.UNKNOWN;
        }

        // Se recorre en orden: cada punto baja a un miembro y cada corchete quita
        // una dimension del arreglo.
        for (int i = 1; i < ctx.getChildCount() && currentType != null; i++) {
            ParseTree hijo = ctx.getChild(i);
            if (hijo.getText().equals(".")) {
                String memberName = ctx.getChild(++i).getText();
                if (!currentType.isStruct()) {
                    return Type.UNKNOWN;
                }
                Symbol structDef = symbolTable.getStruct(currentType.getCustomTypeName());
                if (structDef == null) {
                    continue;
                }
                Symbol member = structDef.getMember(memberName);
                if (member == null) {
                    errors.add(Diagnostico.miembroNoExiste(memberName,
                            currentType.getCustomTypeName(), ctx));
                    return Type.UNKNOWN;
                }
                currentType = member.getType();
            } else if (hijo instanceof YParser.ExpresionContext) {
                visit(hijo);
                if (!currentType.isArray()) {
                    errors.add(Diagnostico.noEsArreglo(baseName, currentType, ctx));
                    return Type.UNKNOWN;
                }
                comprobarIndice(hijo, currentType, baseName);
                currentType = currentType.desindexar(1);
            }
        }
        return currentType;
    }

    /** Cuantos corchetes lleva la cadena. */
    private static int contarIndices(YParser.Acceso_miembroContext ctx) {
        return ctx.CORCHETE_IZQ().size();
    }

    /** true si la cadena baja a algun miembro, en cuyo caso los indices son de
     *  varios arreglos distintos y no se pueden contar juntos. */
    private static boolean tienePuntos(YParser.Acceso_miembroContext ctx) {
        return !ctx.PUNTO().isEmpty();
    }

    /**
     * Avisa si el indice se sale del arreglo.
     *
     * <p>Solo se comprueba cuando el indice es un numero escrito en el fuente;
     * con una variable el valor no se conoce al compilar.</p>
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
                    (YParser.ExpresionContext) indice));
        }
    }

    private Type resolveType(String name) {
        if (name == null) return Type.UNKNOWN;
        switch (name.toLowerCase()) {
            case "entero": return Type.INT;
            case "flotante": return Type.DOUBLE;
            case "cadena": return Type.STRING;
            case "caracter": return Type.CHAR;
            case "bool": return Type.BOOLEAN;
            default:
                if (symbolTable.getStruct(name) != null) {
                    return Type.structType(name);
                }
                if (symbolTable.getClass(name) != null) {
                    return Type.classType(name);
                }
                return new Type(DataType.STRUCT, name);
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
