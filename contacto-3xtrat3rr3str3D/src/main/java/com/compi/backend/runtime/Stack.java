package com.compi.backend.runtime;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private final List<Object> data = new ArrayList<>();

    public void push(Object v){ data.add(v); }
    public Object pop(){ return data.isEmpty()? null : data.remove(data.size()-1); }
    public Object peek(){ return data.isEmpty()? null : data.get(data.size()-1); }
    public int size(){ return data.size(); }
    public void clear(){ data.clear(); }
}
