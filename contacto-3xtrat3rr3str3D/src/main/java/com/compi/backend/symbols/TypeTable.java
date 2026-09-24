package com.compi.backend.symbols;

import java.util.HashMap;
import java.util.Map;

public class TypeTable {
    private final Map<String, Type> types = new HashMap<>();

    public TypeTable(){
        // Tipos primitivos base
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

    private void addPrimitive(String name, DataType dt){
        types.put(name, new Type(dt, name));
    }

    public void define(String name, Type type){
        types.put(name, type);
    }

    public Type resolve(String name){
        return types.get(name);
    }

    public boolean exists(String name){
        return types.containsKey(name);
    }
}
