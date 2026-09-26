package com.compi.backend.symbols;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SymbolTable {
    private Scope currentScope;
    private final Scope globalScope;
    private final Map<String, Symbol> structs = new HashMap<>();
    private final Map<String, Symbol> classes = new HashMap<>();
    private final Map<String, Symbol> functions = new HashMap<>();
    private final TypeTable typeTable;

    /**
     * Ambitos abiertos a lo largo de la compilacion, incluidos los ya cerrados.
     *
     * <p>El ambito de una funcion se cierra al terminar de visitarla, asi que sus
     * variables locales dejan de ser alcanzables desde el ambito global. Se
     * guardan aqui para que la tabla de simbolos las pueda enseñar: un simbolo
     * local con su offset es justo lo que se quiere ver.</p>
     */
    private final List<Scope> closedScopes = new ArrayList<>();

    public SymbolTable() {
        this.globalScope = new Scope("global", null);
        this.currentScope = this.globalScope;
        this.typeTable = new TypeTable();
    }

    public TypeTable getTypeTable() {
        return typeTable;
    }

    public void enterScope(String name) {
        currentScope = new Scope(name, currentScope);
    }

    public void enterScope(String name, int initialOffset) {
        currentScope = new Scope(name, currentScope, initialOffset);
    }

    public void exitScope() {
        if (currentScope.getParent() != null) {
            closedScopes.add(currentScope);
            currentScope = currentScope.getParent();
        }
    }

    public Scope getCurrentScope() {
        return currentScope;
    }

    /**
     * Devuelve la tabla al estado inicial para empezar una compilacion limpia.
     *
     * <p>Sin esto la segunda compilacion de la misma sesion volveria a declarar
     * los simbolos de la primera y cada variable globals aparecia duplicada.</p>
     */
    public void clear() {
        currentScope = globalScope;
        globalScope.clear();
        structs.clear();
        classes.clear();
        functions.clear();
        closedScopes.clear();
    }

    public Scope getGlobalScope() {
        return globalScope;
    }

    public boolean define(Symbol symbol) {
        // Aqui se sabe en que ambito queda el simbolo, y no en el visitor: asi la
        // columna de ambito de la tabla de la interfaz sale sola.
        symbol.setScopeIfAbsent(currentScope.getName());
        return currentScope.define(symbol);
    }

    /**
     * Define un simbolo en el ambito global, etiquetando su ambito.
     *
     * <p>Hace falta para los simbolos que se declaran a proposito en el ambito
     * global aunque se escriban dentro de un bloque, como la variable de control
     * del ciclo {@code per} de PigLatin. Si se definieran en el ambito del bloque,
     * al cerrarlo se perderian: de la tabla solo se recoge el ambito global, y el
     * generador de codigo tambien los busca ahi.</p>
     */
    public boolean defineGlobal(Symbol symbol) {
        symbol.setScopeIfAbsent(globalScope.getName());
        return globalScope.define(symbol);
    }

    public boolean defineByTypeName(String name, String typeName, SymbolCategory category) {
        Type type = typeTable.resolve(typeName);
        if (type == null) {
            // crear tipo personalizado si no está en la tabla
            type = new Type(DataType.UNKNOWN, typeName);
            typeTable.define(typeName, type);
        }
        Symbol symbol = new Symbol(name, type, category);
        return define(symbol);
    }

    public Type resolveType(String typeName) {
        return typeTable.resolve(typeName);
    }

    public Symbol resolve(String name) {
        return currentScope.resolve(name);
    }

    public void addStruct(Symbol struct) {
        // Una estructura o una clase se registran aparte de los ambitos: su
        // ambito es ellas mismas.
        struct.setScopeIfAbsent(struct.getName());
        structs.put(struct.getName(), struct);
    }

    public Symbol getStruct(String name) {
        return structs.get(name);
    }

    public void addClass(Symbol clazz) {
        clazz.setScopeIfAbsent(clazz.getName());
        classes.put(clazz.getName(), clazz);
    }

    public Symbol getClass(String name) {
        return classes.get(name);
    }

    public void addFunction(Symbol function) {
        function.setScopeIfAbsent(function.getName());
        functions.put(function.getName(), function);
    }

    public Symbol getFunction(String name) {
        return functions.get(name);
    }

    /**
     * Todos los simbolos declarados en la compilacion.
     *
     * <p>Se recogen el ambito global, los ambitos que ya se cerraron (funciones,
     * metodos y constructores, donde quedan las variables locales) y los
     * miembros de las clases y estructuras registradas.</p>
     */
    public List<Symbol> getAllSymbols() {
        List<Symbol> all = new ArrayList<>();
        Set<Symbol> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        collectAllSymbols(globalScope, all, seen);
        for (Scope scope : closedScopes) {
            collectAllSymbols(scope, all, seen);
        }
        addNested(structs.values(), all, seen);
        addNested(classes.values(), all, seen);
        addNested(functions.values(), all, seen);
        return all;
    }

    /**
     * Anade un simbolo contenedor y todo lo que cuelga de el.
     *
     * <p>Se usa para las estructuras, clases y funciones, que no viven en ningun
     * ambito: se registran aparte. De ahi cuelgan sus miembros y sus
     * parametros.</p>
     */
    private void addNested(Collection<Symbol> owners, List<Symbol> result, Set<Symbol> seen) {
        for (Symbol owner : owners) {
            if (!owner.isWorking() && seen.add(owner)) {
                result.add(owner);
            }
            addNested(owner.getMembers(), result, seen);
            addNested(owner.getParameters(), result, seen);
        }
    }

    private void collectAllSymbols(Scope scope, List<Symbol> result, Set<Symbol> seen) {
        if (scope == null) {
            return;
        }
        for (Symbol s : scope.getSymbols()) {
            // Las copias de trabajo de la generacion de codigo son los mismos
            // simbolos que ya declaro el analizador semantico: no se repiten.
            if (!s.isWorking() && seen.add(s)) {
                result.add(s);
            }
        }
    }
}
