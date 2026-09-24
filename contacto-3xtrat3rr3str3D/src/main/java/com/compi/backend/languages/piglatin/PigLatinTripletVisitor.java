package com.compi.backend.languages.piglatin;

import com.compi.PigLatinBaseVisitor;
import com.compi.PigLatinParser;
import com.compi.backend.c3d.QuadrupleOp;
import com.compi.backend.c3d.TripletGenerator;
import com.compi.backend.symbols.Symbol;
import com.compi.backend.symbols.SymbolTable;

public class PigLatinTripletVisitor extends PigLatinBaseVisitor<String> {
    private final SymbolTable symbolTable;
    private final TripletGenerator tg;

    public PigLatinTripletVisitor(SymbolTable symbolTable, TripletGenerator tg){
        this.symbolTable = symbolTable;
        this.tg = tg;
    }

    @Override
    public String visitMaior(PigLatinParser.MaiorContext ctx){
        tg.emit(QuadrupleOp.FUNCTION_START, null, "main");
        symbolTable.enterScope("maior",0);
        ctx.sentencia().forEach(this::visit);
        tg.emit(QuadrupleOp.FUNCTION_END, null, "main");
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitAsignacion_sentencia(PigLatinParser.Asignacion_sentenciaContext ctx){
        String left = ctx.VARIABLE()!=null ? ctx.VARIABLE(0).getText() : ctx.acceso_miembro().getText();
        String val = visit(ctx.expresion()!=null ? ctx.expresion() : ctx.condicion());
        Symbol s = symbolTable.resolve(left);
        if(s!=null){
            tg.emit(QuadrupleOp.STACK_SET, String.valueOf(s.getOffset()), val);
        } else {
            tg.emit(QuadrupleOp.ASSIGN, val, left);
        }
        return null;
    }

    @Override
    public String visitImprimir_sentencia(PigLatinParser.Imprimir_sentenciaContext ctx){
        // Simplificado: imprime cada operando
        for(int i=0;i<ctx.getChildCount();i++){
            String txt = ctx.getChild(i).getText();
            if(txt.equals(">>")||txt.equals(";")) continue;
            if(txt.startsWith("\"")){
                String content = txt.substring(1,txt.length()-1);
                String start = tg.newTemp();
                tg.emitAssign(start, "H");
                // Heap set simplificado
                tg.emit(QuadrupleOp.HEAP_SET, "H", String.valueOf((int)content.charAt(0)));
                tg.emit(QuadrupleOp.PRINT_STR, start, null);
            } else {
                tg.emit(QuadrupleOp.PRINT_INT, txt, null);
            }
        }
        tg.emit(QuadrupleOp.PRINTLN, null, null);
        return null;
    }

    @Override
    public String visitSi_sentencia(PigLatinParser.Si_sentenciaContext ctx){
        String cond = visit(ctx.condicion());
        String t = tg.newLabel();
        String f = tg.newLabel();
        String e = tg.newLabel();
        tg.emit(QuadrupleOp.IF_TRUE, cond, t);
        tg.emitGoto(f);
        tg.emitLabel(t);
        ctx.sentencia().forEach(this::visit);
        tg.emitGoto(e);
        tg.emitLabel(f);
        tg.emitLabel(e);
        return null;
    }

    @Override
    public String visitExpresion(PigLatinParser.ExpresionContext ctx){
        if(ctx.termino().size()==1) return visit(ctx.termino(0));
        String cur = visit(ctx.termino(0));
        for(int i=1;i<ctx.termino().size();i++){
            String nxt = visit(ctx.termino(i));
            String op = ctx.operacion_aritmetica(i-1).getText();
            String tmp = tg.newTemp();
            QuadrupleOp q = "+".equals(op)?QuadrupleOp.ADD:
                           "-".equals(op)?QuadrupleOp.SUB:
                           "*".equals(op)?QuadrupleOp.MUL:QuadrupleOp.DIV;
            tg.emit(q, cur, nxt);
            tg.emitAssign(tmp, null); // resultado en tmp por convención
            cur = tmp;
        }
        return cur;
    }

    @Override
    public String visitTermino(PigLatinParser.TerminoContext ctx){
        if(ctx.NUMERO_ENTERO()!=null) return ctx.NUMERO_ENTERO().getText();
        if(ctx.NUMERO_DECIMAL()!=null) return ctx.NUMERO_DECIMAL().getText();
        if(ctx.VERUM()!=null) return "1";
        if(ctx.FALSUS()!=null) return "0";
        if(ctx.VARIABLE()!=null){
            String name = ctx.VARIABLE().getText();
            Symbol s = symbolTable.resolve(name);
            if(s!=null){
                String tmp = tg.newTemp();
                tg.emit(QuadrupleOp.STACK_GET, String.valueOf(s.getOffset()), tmp);
                return tmp;
            }
            return name;
        }
        return "0";
    }

    public TripletGenerator getGenerator(){ return tg; }
}
