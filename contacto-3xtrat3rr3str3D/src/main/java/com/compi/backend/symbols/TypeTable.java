package com.compi.backend.symbols;

import java.util.*;

/**
 * Tabla de tipos con referencias, dimensiones, límites y ámbitos léxicos.
 */
public class TypeTable {
    private final Scope globalScope;
    private final Map<Scope, Map<String, TypeEntry>> scopedTypes = new IdentityHashMap<>();
    private final Map<Scope, Integer> scopeIds = new IdentityHashMap<>();
    private final Map<Integer, TypeEntry> entries = new LinkedHashMap<>();
    private final Map<DataType, TypeEntry> primitives = new LinkedHashMap<>();
    private int nextId;

    public TypeTable() {
        this(new Scope("global", null));
    }

    public TypeTable(Scope globalScope) {
        this.globalScope = globalScope;
        clear();
    }

    public void clear() {
        scopedTypes.clear();
        scopeIds.clear();
        entries.clear();
        primitives.clear();
        nextId = 0;
        scopeId(globalScope);
        addPrimitives();
    }

    private void addPrimitives() {
        addPrimitive("entero", DataType.INT);
        addPrimitive("flotante", DataType.DOUBLE);
        addPrimitive("cadena", DataType.STRING);
        addPrimitive("caracter", DataType.CHAR);
        addPrimitive("bool", DataType.BOOLEAN);
        addPrimitive("numerus", DataType.INT);
        addPrimitive("textum", DataType.STRING);
        addPrimitive("decimalis", DataType.DOUBLE);
        addPrimitive("littera", DataType.CHAR);
        addPrimitive("verum", DataType.BOOLEAN);
        addPrimitive("falsus", DataType.BOOLEAN);
        addPrimitive("int", DataType.INT);
        addPrimitive("double", DataType.DOUBLE);
        addPrimitive("String", DataType.STRING);
        addPrimitive("char", DataType.CHAR);
        addPrimitive("boolean", DataType.BOOLEAN);
        addPrimitive("void", DataType.VOID);
    }

    private void addPrimitive(String name, DataType dataType) {
        TypeEntry entry = define(name, new Type(dataType), globalScope);
        primitives.putIfAbsent(dataType, entry);
    }

    public int scopeId(Scope scope) {
        return scopeIds.computeIfAbsent(scope, key -> scopeIds.size());
    }

    public void define(String name, Type type) {
        define(name, type, globalScope);
    }

    public TypeEntry define(String name, Type type, Scope scope) {
        return define(name, type, scope, null);
    }

    /**
     * Padre es opcional: los lenguajes actuales no declaran herencia de tipos.
     */
    public TypeEntry define(String name, Type type, Scope scope, Integer parentId) {
        if (parentId != null && !entries.containsKey(parentId)) {
            throw new IllegalArgumentException("El tipo padre no pertenece a esta tabla");
        }
        TypeEntry base = type.isArray() ? register(type.getElementType(), scope) : null;
        Map<String, TypeEntry> local = scopedTypes.computeIfAbsent(scope, key -> new LinkedHashMap<>());
        TypeEntry previous = local.get(name);
        int id = previous == null ? nextId++ : previous.id();
        int dimension = type.isArray() ? type.getDimensions() : 0;
        List<Integer> minimum = new ArrayList<>();
        List<Integer> maximum = new ArrayList<>();
        if (type.isArray()) {
            for (int size : type.getSizes()) {
                minimum.add(0);
                maximum.add(size > 0 ? size - 1 : Type.UNKNOWN_SIZE);
            }
        }
        TypeEntry entry = new TypeEntry(id, name, type, base == null ? null : base.id(),
                parentId, dimension, minimum, maximum, scopeId(scope), scope.getName());
        local.put(name, entry);
        entries.put(id, entry);
        return entry;
    }

    /**
     * Registra tipos usados por símbolos; conserva formas de tamaños diferentes.
     */
    public TypeEntry register(Type type, Scope scope) {
        if (type == null) return null;
        if (!type.isArray() && !type.isClass() && !type.isStruct()) {
            TypeEntry primitive = primitives.get(type.getDataType());
            if (primitive != null) return primitive;
        }
        String name = typeName(type);
        if (!type.isArray()) {
            TypeEntry resolved = resolveEntry(name, scope);
            if (resolved != null && resolved.type().equals(type)) return resolved;
        }
        Map<String, TypeEntry> local = scopedTypes.get(scope);
        TypeEntry existing = local == null ? null : local.get(name);
        return existing == null ? define(name, type, scope) : existing;
    }

    private String typeName(Type type) {
        if (!type.isArray()) return type.label();
        StringBuilder name = new StringBuilder(type.getElementType().label());
        for (int size : type.getSizes()) {
            name.append('[');
            if (size > 0) name.append(size);
            name.append(']');
        }
        return name.toString();
    }

    public Type resolve(String name) {
        return resolve(name, globalScope);
    }

    public Type resolve(String name, Scope scope) {
        TypeEntry entry = resolveEntry(name, scope);
        return entry == null ? null : entry.type();
    }

    public TypeEntry resolveEntry(String name, Scope scope) {
        for (Scope current = scope; current != null; current = current.getParent()) {
            Map<String, TypeEntry> local = scopedTypes.get(current);
            if (local != null && local.containsKey(name)) return local.get(name);
        }
        return null;
    }

    public TypeEntry entryById(int id) {
        return entries.get(id);
    }

    public List<TypeEntry> getEntries() {
        return List.copyOf(entries.values());
    }

    public boolean exists(String name) {
        return resolve(name) != null;
    }
}
