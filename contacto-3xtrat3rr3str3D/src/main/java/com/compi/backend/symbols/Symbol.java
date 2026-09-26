package com.compi.backend.symbols;

import java.util.ArrayList;
import java.util.List;

/**
 * Entrada de la tabla de simbolos.
 *
 * <p>Ademas de nombre, tipo, categoria y offset, guarda donde se declaro (linea y
 * columna), en que ambito vive y en que lenguaje se declaro. Sin eso la tabla de
 * simbolos no decia nada util: el offset solo tiene sentido conociendo el ambito,
 * y sin la posicion no se puede saltar a la declaracion.</p>
 */
public class Symbol {
    private final String name;
    private final Type type;
    private final SymbolCategory category;
    private int offset; // Offset en Stack o Heap
    private boolean isGlobal;
    private boolean isByReference; // Para arrays y structs en 'Y'

    // Metadatos para funciones / métodos / constructores
    private List<Symbol> parameters = new ArrayList<>();
    private Type returnType;

    // Metadatos para structs / clases
    private List<Symbol> members = new ArrayList<>();

    // Datos de procedencia. La linea y la columna van en base 1, como las
    // cuentan los editors; -1 significa que no se sabe.
    private int line = -1;
    private int column = -1;
    private String scope;
    private String language;
    private String sourceFile;

    /**
     * Copia de trabajo, no una declaracion real.
     *
     * <p>La generacion de codigo vuelve a declarar parametros, locales y
     * atributos en su propio ambito, con los offsets de su marco de pila. Son
     * los mismos simbolos que ya declaro el analizador semantico, asi que no
     * deben salir otra vez en la tabla.</p>
     */
    private boolean working;

    public Symbol(String name, Type type, SymbolCategory category) {
        this.name = name;
        this.type = type;
        this.category = category;
    }

    public Symbol(String name, Type type, SymbolCategory category, int offset, boolean isGlobal) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.offset = offset;
        this.isGlobal = isGlobal;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public SymbolCategory getCategory() {
        return category;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public boolean isByReference() {
        return isByReference;
    }

    public void setByReference(boolean byReference) {
        this.isByReference = byReference;
    }

    public List<Symbol> getParameters() {
        return parameters;
    }

    public void setParameters(List<Symbol> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Symbol param) {
        this.parameters.add(param);
        // Un parametro vive en el ambito de la funcion que lo declara.
        param.setScopeIfAbsent(name);
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public List<Symbol> getMembers() {
        return members;
    }

    public void setMembers(List<Symbol> members) {
        this.members = members;
    }

    public void addMember(Symbol member) {
        this.members.add(member);
        // Un miembro vive en el ambito de la clase o estructura que lo contiene.
        member.setScopeIfAbsent(name);
    }

    public Symbol getMember(String memberName) {
        for (Symbol s : members) {
            if (s.getName().equals(memberName)) return s;
        }
        return null;
    }

    // ====================== Procedencia ======================

    /** Linea donde se declaro, o -1 si no se sabe. */
    public int getLine() {
        return line;
    }

    /**
     * Columna donde se declaro, contando desde 1, o -1 si no se sabe.
     *
     * <p>Base 1 y no base 0 como ANTLR: el primer caracter de una linea es la
     * columna 1, que es como lo cuentan el editor y el numero de linea.</p>
     */
    public int getColumn() {
        return column;
    }

    /** Nombre del ambito donde vive, o null si no se sabe. */
    public String getScope() {
        return scope;
    }

    /** Identificador del lenguaje en el que se declaro, o null si no se sabe. */
    public String getLanguage() {
        return language;
    }

    /**
     * Archivo del proyecto en el que se declaro, o null si no se sabe.
     *
     * <p>Sobra cuando un archivo se compila solo, pero con los imports no: un
     * simbolo puede venir de otro archivo y su linea es la de ese otro, asi que
     * sin esto el salto desde la tabla acabaria en una linea que no es la
     * suya.</p>
     */
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * Anota el archivo en el que se declaro.
     *
     * @return este mismo simbolo, para encadenar la llamada
     */
    public Symbol inFile(String fileName) {
        this.sourceFile = fileName;
        return this;
    }

    /**
     * Anota la posicion de la declaracion.
     *
     * <p>Se pasa el contexto de la regla de la gramantica y se toma su primer
     * token, que es justo donde empieza la declaracion. ANTLR numera las
     * columnas desde 0, asi que se suma uno para dejar la columna en base 1
     * (ver {@link #getColumn()}).</p>
     *
     * @return este mismo simbolo, para encadenar con el constructor
     */
    public Symbol at(org.antlr.v4.runtime.ParserRuleContext ctx) {
        if (ctx != null && ctx.getStart() != null) {
            this.line = ctx.getStart().getLine();
            this.column = ctx.getStart().getCharPositionInLine() + 1;
        }
        return this;
    }

    /** Fija el ambito solo si todavia no tiene uno. */
    public void setScopeIfAbsent(String scopeName) {
        if (this.scope == null) {
            this.scope = scopeName;
        }
    }

    /**
     * Anota el lenguaje de origen.
     *
     * @return este mismo simbolo, para encadenar la llamada
     */
    public Symbol inLanguage(String languageId) {
        this.language = languageId;
        return this;
    }

    /**
     * Marca el simbolo como copia de trabajo de la generacion de codigo.
     *
     * @return este mismo simbolo, para encadenar con el constructor
     */
    public Symbol markWorking() {
        this.working = true;
        return this;
    }

    /** true si es una copia de trabajo y no una declaracion del fuente. */
    public boolean isWorking() {
        return working;
    }

    @Override
    public String toString() {
        return String.format("Symbol(%s, %s, %s, offset=%d, isGlobal=%s, ambito=%s, linea=%d)",
                name, type, category, offset, isGlobal, scope, line);
    }
}
