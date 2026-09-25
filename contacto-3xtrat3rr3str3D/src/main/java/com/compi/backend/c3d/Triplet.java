package com.compi.backend.c3d;

/**
 * Triplete en codigo de tres direcciones: una instruccion {@code op arg1 arg2}.
 *
 * <p>A diferencia de la cuarteta, el triplete no muestra el destino: ese campo
 * queda aqui solo para que quien lo consume (por ejemplo, la traduccion a C)
 * sepa donde escribir el resultado. El listado que se muestra al usuario sigue
 * siendo la forma clasica de tres campos.</p>
 */
public class Triplet {
    private final QuadrupleOp op;
    private final String arg1;
    private final String arg2;
    private final String result;

    public Triplet(QuadrupleOp op, String arg1, String arg2){
        this(op, arg1, arg2, null);
    }

    public Triplet(QuadrupleOp op, String arg1, String arg2, String result){
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public QuadrupleOp getOp(){ return op; }
    public String getArg1(){ return arg1; }
    public String getArg2(){ return arg2; }
    public String getResult(){ return result; }

    @Override public String toString(){
        return String.format("%s %s %s", op, arg1 == null ? "" : arg1, arg2 == null ? "" : arg2);
    }
}
