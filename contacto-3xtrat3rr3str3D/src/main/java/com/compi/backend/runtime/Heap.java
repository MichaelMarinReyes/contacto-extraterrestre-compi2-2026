package com.compi.backend.runtime;

import java.util.ArrayList;
import java.util.List;

public class Heap {
    private final List<Object> memory = new ArrayList<>();
    private int pointer = 0;

    public int allocate(int size){
        int start = pointer;
        for(int i=0;i<size;i++) memory.add(null);
        pointer += size;
        return start;
    }

    public void set(int addr, Object value){
        ensure(addr);
        memory.set(addr, value);
    }

    public Object get(int addr){
        ensure(addr);
        return memory.get(addr);
    }

    private void ensure(int addr){
        while(memory.size() <= addr) memory.add(null);
    }
}
