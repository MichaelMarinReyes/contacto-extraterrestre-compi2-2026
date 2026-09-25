package com.compi.backend;

import com.compi.backend.errors.CompilationError;
import com.compi.backend.errors.ErrorType;
import com.compi.backend.runtime.Stack;
import com.compi.backend.symbols.SymbolTable;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
    private String source;
    private String language;
    private final SymbolTable symbolTable = new SymbolTable();
    private final Stack processStack = new Stack();
    private final List<CompilationError> errors = new ArrayList<>();
    private String astDot;
    private String translatedCode;
    private String c3dCode;
    private String tripletsCode;
    private String quadruplesCode;
    private final com.compi.backend.c3d.IntermediateCodeManager icm = new com.compi.backend.c3d.IntermediateCodeManager();

    /**
     * Recibe el texto fuente desde el frontend y el lenguaje a compilar.
     * @param source Texto completo del archivo fuente.
     * @param language Identificador del lenguaje: pig, y o zetariano.
     * @return true si el proceso se inició sin errores de entrada, false en caso contrario.
     */
    public boolean compile(String source, String language){
        this.source = source;
        this.language = language;
        errors.clear();
        processStack.clear();
        icm.clear();
        try{
            // 1. Análisis léxico y sintáctico -> AST
            // 2. Generación de AST en Graphviz
            astDot = generateAstDot();
            // 3. Análisis semántico -> Tabla de símbolos
            // 4. Generación de pila de procesos
            // 5. Generación de tripletas y cuartetas
            generateIntermediateFromSource();
            tripletsCode = icm.getTripletsString();
            quadruplesCode = icm.getQuadruplesString();
            c3dCode = generateC3D();
            translatedCode = generateTranslatedCode();
        }catch(Exception e){
            errors.add(new CompilationError(ErrorType.SEMANTICO,"Error interno de compilación: "+e.getMessage(),0,0));
        }
        return errors.isEmpty();
    }

    /**
     * Genera la representación DOT del AST para visualización en el frontend.
     * @return String con el contenido DOT de Graphviz.
     */
    public String getAstDot(){
        return astDot;
    }

    /**
     * Devuelve la pila de procesos usada durante la ejecución simulada.
     * @return Stack con el estado actual de la pila.
     */
    public Stack getProcessStack(){
        return processStack;
    }

    /**
     * Devuelve el código traducido a un pseudo-código intermedio legible.
     * @return String con el código traducido.
     */
    public String getTranslatedCode(){
        return translatedCode;
    }

    /**
     * Devuelve el código de tres direcciones generado.
     * @return String con el C3D.
     */
    public String getC3DCode(){
        return c3dCode;
    }

    /**
     * Devuelve las tripletas generadas a partir del AST.
     * @return String con la lista de tripletas.
     */
    public String getTriplets(){
        return tripletsCode;
    }

    /**
     * Devuelve las cuartetas generadas a partir del AST.
     * @return String con la lista de cuartetas.
     */
    public String getQuadruples(){
        return quadruplesCode;
    }

    /**
     * Devuelve la tabla de símbolos construida durante el análisis semántico.
     * @return SymbolTable con símbolos y scopes.
     */
    public SymbolTable getSymbolTable(){
        return symbolTable;
    }

    /**
     * Devuelve la lista de errores léxicos, sintácticos o semánticos capturados.
     * @return Lista de CompilationError.
     */
    public List<CompilationError> getErrors(){
        return errors;
    }

    private String generateAstDot(){
        return "digraph AST { /* DOT generado desde AST */ }";
    }
    private void generateIntermediateFromSource(){
        if(source == null) return;
        String[] lines = source.split("\\r?\\n");
        for(String line : lines){
            line = line.trim();
            if(line.isEmpty() || line.startsWith("//") || line.startsWith("/*")) continue;
            // Simple heuristic: detect assignment pattern var = expr
            if(line.matches(".*[=].*")){
                String[] parts = line.split("=",2);
                String left = parts[0].trim();
                String right = parts[1].trim().replaceAll(";", "");
                String temp = icm.newTemp();
                icm.emitTriplet(com.compi.backend.c3d.QuadrupleOp.ASSIGN, right, left);
                icm.emitQuadruple(com.compi.backend.c3d.QuadrupleOp.ASSIGN, right, null, left);
            }
        }
    }
    private String generateTriplets(){ return icm.getTripletsString(); }
    private String generateQuadruples(){ return icm.getQuadruplesString(); }
    private String generateC3D(){ return "/* C3D */"; }
    private String generateTranslatedCode(){ return "/* código traducido */"; }
}
