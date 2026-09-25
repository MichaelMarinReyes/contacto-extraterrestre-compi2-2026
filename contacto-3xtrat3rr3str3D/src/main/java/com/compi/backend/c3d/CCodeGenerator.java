package com.compi.backend.c3d;

public class CCodeGenerator {

    private final Mode mode;
    private final IntermediateCodeManager manager;

    public CCodeGenerator(Mode mode, IntermediateCodeManager manager){
        this.mode = mode;
        this.manager = manager;
    }

    public String generateC(){
        StringBuilder sb = new StringBuilder();
        sb.append("#include <stdio.h>\n");
        sb.append("int main(){\n");
        if(mode == Mode.TRIPLETS){
            sb.append("/* Triplets */\n");
            for(Triplet t : manager.getTriplets()){
                sb.append("// ").append(t).append("\n");
            }
        }else{
            sb.append("/* Quadruples */\n");
            for(Quadruple q : manager.getQuadruples()){
                sb.append("// ").append(q).append("\n");
            }
        }
        sb.append("return 0;\n}\n");
        return sb.toString();
    }
}
