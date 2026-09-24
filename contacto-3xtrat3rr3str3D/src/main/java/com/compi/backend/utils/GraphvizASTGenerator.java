package com.compi.backend.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

public class GraphvizASTGenerator {
    private final StringBuilder sb = new StringBuilder();
    private int idCounter = 0;
    private final Map<Object,Integer> nodeIds = new IdentityHashMap<>();

    public GraphvizASTGenerator(){
        sb.append("digraph AST {\n");
        sb.append("node [shape=box];\n");
    }

    public int getId(Object node){
        return nodeIds.computeIfAbsent(node, k -> idCounter++);
    }

    public void addNode(Object node, String label){
        int id = getId(node);
        sb.append(id).append(" [label=\"").append(label.replace("\"","\\\"")).append("\"];\n");
    }

    public void addEdge(Object parent, Object child){
        int p = getId(parent);
        int c = getId(child);
        sb.append(p).append(" -> ").append(c).append(";\n");
    }

    public String getDot(){ return sb.append("}\n").toString(); }

    public void writeToFile(String path) throws IOException{
        try(FileWriter fw = new FileWriter(path)){
            fw.write(getDot());
        }
    }
}
