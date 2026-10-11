package com.compi.backend.ast;

import com.compi.backend.Compiler;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.languages.LanguageCompiler;
import com.compi.backend.languages.LanguageCompilerFactory;
import com.compi.backend.ast.decl.*;
import com.compi.backend.ast.expr.*;
import com.compi.backend.ast.stmt.*;
import com.compi.backend.symbols.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/** Contratos estructurales del AST y su publicación, independientes del C3D. */
class AstBuilderTest {
    private ProgramNodeAST parse(String language, String source) {
        LanguageCompiler compiler = LanguageCompilerFactory.getCompiler(language);
        List<CompilationError> errors = new ArrayList<>();
        var tree = compiler.parse(source, errors);
        assertTrue(errors.isEmpty(), errors.toString());
        ProgramNodeAST ast = compiler.buildAst(tree, "prueba." + language);
        for (AbstractNodeAST node : flatten(ast)) {
            assertEquals("prueba." + language, node.sourceFile());
            assertTrue(node.line() > 0);
            assertTrue(node.column() >= 0);
        }
        return ast;
    }

    private List<AbstractNodeAST> flatten(AbstractNodeAST root) {
        List<AbstractNodeAST> result = new ArrayList<>();
        result.add(root);
        for (ASTNode child : root.children()) if (child != null) result.addAll(flatten((AbstractNodeAST) child));
        return result;
    }

    private <T> List<T> nodes(ProgramNodeAST root, Class<T> type) {
        return flatten(root).stream().filter(type::isInstance).map(type::cast).toList();
    }

    @Test
    void pigLatinArithmeticPreservesPrecedenceAndLeftAssociativity() {
        ProgramNodeAST ast = parse("pig", "MAIOR> >> 10 - 3 - 2 * 4; FINIS;");
        PrintNodeAST print = (PrintNodeAST) ast.statements().getFirst();
        BinaryOperationNodeAST subtract = (BinaryOperationNodeAST) print.expressions().getFirst();
        assertEquals("-", subtract.operator());
        assertEquals("-", ((BinaryOperationNodeAST) subtract.left()).operator());
        assertEquals("*", ((BinaryOperationNodeAST) subtract.right()).operator());
        assertEquals("10", ((LiteralNodeAST) ((BinaryOperationNodeAST) subtract.left()).left()).value().toString());
    }

    @Test
    void pigLatinKeepsArrayShapesNamedInitializersAndMethodReceivers() {
        ProgramNodeAST ast = parse("pig", """
                import lib.Datos.y
                VARIABILES>
                series matriz[2][2]: numerus {{1,2},{3,4}};
                esto dato: Datos {cantidad: 3, valores: {1,2}};
                esto objeto: novus Caja(4);
                MAIOR>
                dato.valores[1] = objeto.calcular(matriz[0][1]);
                objeto.mostrar();
                FINIS;
                """);
        assertEquals("lib.Datos.y", ast.imports().getFirst().path());
        DeclarationNodeAST matrix = (DeclarationNodeAST) ast.globals().getFirst();
        assertEquals(2, matrix.dimensions().size());
        assertEquals(2, ((ArrayLiteralNodeAST) matrix.initializer()).elements().size());
        InstanceInitNodeAST init = nodes(ast, InstanceInitNodeAST.class).getFirst();
        assertEquals("Datos", init.typeName());
        assertEquals(List.of("cantidad", "valores"), new ArrayList<>(init.named().keySet()));
        CallNodeAST call = nodes(ast, CallNodeAST.class).getFirst();
        assertEquals("calcular", call.name());
        assertEquals("objeto", ((IdentifierNodeAST) ((MemberAccessNodeAST) call.callee()).target()).name());
        assertInstanceOf(IndexNodeAST.class, ((AssignmentNodeAST) ast.statements().getFirst()).target());
    }

    @Test
    void pigLatinKeepsBranchesLoopsReadAndBooleanOperators() {
        ProgramNodeAST ast = parse("pig", """
                MAIOR>
                si (non falsus && verum || 1 < 2) { >> 1; }
                aliter (falsus) { >> 2; } aliter { >> 3; } FINIS;
                dum (verum) { interrumpe; } FINIS;
                facere { perge; } dum (falsus);
                per (esto i: numerus 0; i < 3; i++) { i << }
                FINIS;
                """);
        IfNodeAST conditional = nodes(ast, IfNodeAST.class).getFirst();
        assertEquals(1, conditional.elseIfs().size());
        assertEquals(1, ((BlockNodeAST) conditional.elseBody()).statements().size());
        assertEquals("||", ((BinaryOperationNodeAST) conditional.condition()).operator());
        assertEquals(1, nodes(ast, WhileNodeAST.class).size());
        assertEquals(1, nodes(ast, DoWhileNodeAST.class).size());
        assertInstanceOf(DeclarationNodeAST.class, nodes(ast, ForNodeAST.class).getFirst().initializer());
        assertEquals(1, nodes(ast, ReadStatementNodeAST.class).size());
        assertEquals(1, nodes(ast, BreakNodeAST.class).size());
        assertEquals(1, nodes(ast, ContinueNodeAST.class).size());
    }

    @Test
    void yKeepsSignaturesLocalDefinitionsAndReferenceParameters() {
        ProgramNodeAST ast = parse("y", """
                %estructuras
                estructura Datos:
                    entero cantidad
                    flotante valores[2]
                %funciones
                definir cambiar([] flotante valores, {} Datos dato, bool activo) -> entero:
                    estructura Local:
                        entero valor
                    bool listo = verdadero
                    valores[0] = leer()
                    ++dato.cantidad
                    retornar 1
                """);
        FunctionDefinitionNodeAST function = nodes(ast, FunctionDefinitionNodeAST.class).getFirst();
        assertEquals(Type.INT, function.returnType());
        assertTrue(function.parameters().get(0).byReference());
        assertTrue(function.parameters().get(0).type().isArray());
        assertTrue(function.parameters().get(1).byReference());
        assertTrue(function.parameters().get(1).type().isStruct());
        assertFalse(function.parameters().get(2).byReference());
        assertEquals(1, nodes(ast, DefinitionStatementNodeAST.class).size());
        assertTrue(nodes(ast, IncrementNodeAST.class).getFirst().prefix());
        assertEquals(ReadKind.INPUT, nodes(ast, ReadExpressionNodeAST.class).getFirst().kind());
        assertEquals(Type.BOOLEAN, nodes(ast, DeclarationNodeAST.class).getFirst().type());
    }

    @Test
    void yKeepsControlFlowSwitchBreaksAndNestedArrays() {
        ProgramNodeAST ast = parse("y", """
                %funciones
                definir prueba() -> entero:
                    entero datos[2][2] = {{1,2},{3,4}}
                    si (verdadero):
                        imprimir(1)
                    sino (falso):
                        imprimir(2)
                    contrario:
                        imprimir(3)
                    para (entero i = 0; i < 2; i++):
                        continuar
                    mientras (falso):
                        romper
                    hacer:
                        imprimir(4)
                    mientras (falso)
                    elegir (1):
                        caso 1:
                            imprimir(5)
                            romper
                        siempre:
                            imprimir(6)
                    retornar 0
                """);
        assertEquals(1, nodes(ast, IfNodeAST.class).getFirst().elseIfs().size());
        assertNotNull(nodes(ast, IfNodeAST.class).getFirst().elseBody());
        assertEquals(1, nodes(ast, ForNodeAST.class).size());
        assertEquals(1, nodes(ast, WhileNodeAST.class).size());
        assertEquals(1, nodes(ast, DoWhileNodeAST.class).size());
        SwitchNodeAST selection = nodes(ast, SwitchNodeAST.class).getFirst();
        assertEquals(1, selection.cases().size());
        assertNotNull(selection.defaultBody());
        assertEquals(3, nodes(ast, ArrayLiteralNodeAST.class).size());
    }

    @Test
    void zetarianoKeepsClassesConstructorsCallsAndArrayCreation() {
        ProgramNodeAST ast = parse("zet", """
                public class Caja {
                    int[][] datos = new int[2][3];
                    public Caja(int valor) { datos[0][0] = valor; }
                    public int calcular(int valor) {
                        Caja otra = new Caja(1);
                        int[] copia = {1,2,3};
                        otra.datos[0][1] += ++valor;
                        println(otra.calcular(valor));
                        print(readln());
                        return valor > 0 ? valor : -valor;
                    }
                }
                """);
        assertEquals("Caja", nodes(ast, ClassDefinitionNodeAST.class).getFirst().name());
        assertEquals(1, nodes(ast, ConstructorDefinitionNodeAST.class).size());
        assertEquals(2, nodes(ast, AttributeDefinitionNodeAST.class).getFirst().dimensions().size());
        assertEquals(2, nodes(ast, NewArrayNodeAST.class).getFirst().sizes().size());
        assertEquals(1, nodes(ast, NewObjectNodeAST.class).size());
        assertInstanceOf(MemberAccessNodeAST.class, nodes(ast, CallNodeAST.class).getFirst().callee());
        List<PrintNodeAST> prints = nodes(ast, PrintNodeAST.class);
        assertTrue(prints.get(0).newline());
        assertFalse(prints.get(1).newline());
        assertEquals(ReadKind.READLINE, nodes(ast, ReadExpressionNodeAST.class).getFirst().kind());
        assertEquals(1, nodes(ast, TernaryNodeAST.class).size());
        assertEquals("+=", nodes(ast, AssignmentNodeAST.class).getLast().operator());
    }

    @Test
    void zetarianoKeepsOptionalForPartsAndCompoundAssignments() {
        ProgramNodeAST ast = parse("zet", """
                class Control {
                    void ejecutar() {
                        for (;;) { break; }
                        for (int i = 0; i < 3; i += 1) { continue; }
                        if (true) { print(1); } else if (false) { print(2); } else { print(3); }
                        while (false) { break; }
                        do { print(4); } while (false);
                        switch (1) { case 1: print(5); break; default: print(6); break; }
                        return;
                    }
                }
                """);
        ForNodeAST empty = nodes(ast, ForNodeAST.class).getFirst();
        assertNull(empty.initializer());
        assertNull(empty.condition());
        assertNull(empty.update());
        assertEquals("+=", nodes(ast, AssignmentExpressionNodeAST.class).getFirst().operator());
        assertEquals(1, nodes(ast, IfNodeAST.class).getFirst().elseIfs().size());
        assertNotNull(nodes(ast, SwitchNodeAST.class).getFirst().defaultBody());
        assertNull(nodes(ast, ReturnNodeAST.class).getFirst().expression());
    }

    @ParameterizedTest
    @ValueSource(strings = {"01-hola-mundo", "02-condicionales-y-ciclos", "03-arreglos",
            "04-modulos-y-estructuras", "05-completo", "06-ciclos-y-saltos"})
    void examplesPublishOwnAstAndImportedSourcePositions(String project) throws Exception {
        Path file = Path.of("..", "archivos", project, "main.pig").toAbsolutePath().normalize();
        Compiler compiler = new Compiler();
        assertTrue(compiler.compile(Files.readString(file), "pig", file.toString()), compiler.getErrors().toString());
        assertNotNull(compiler.getAst());
        assertTrue(compiler.getAstDot().contains("Program @"));
        assertFalse(compiler.getAstDot().contains("pig_latin"));
        for (var entry : compiler.getImportedAsts().entrySet()) {
            assertTrue(Files.isRegularFile(Path.of(entry.getKey())));
            for (AbstractNodeAST node : flatten(entry.getValue())) assertEquals(entry.getKey(), node.sourceFile());
        }
    }

    @Test
    void syntaxErrorsAndSubsequentCompilationsDoNotExposeStaleTrees() {
        Compiler compiler = new Compiler();
        assertTrue(compiler.compile("MAIOR> >> 1; FINIS;", "pig"));
        assertNotNull(compiler.getAst());
        assertFalse(compiler.compile("MAIOR> >> ; FINIS;", "pig"));
        assertNull(compiler.getAst());
        assertTrue(compiler.getAstDot().isEmpty());
        assertTrue(compiler.getImportedAsts().isEmpty());
        assertTrue(compiler.compile("MAIOR> FINIS;", "pig"));
        assertTrue(compiler.getAst().statements().isEmpty());
    }

    @Test
    void repeatedDimensionSizesKeepTheirIndividualTokenColumns() {
        String declaration = "    series matriz[2][2]: numerus;";
        ProgramNodeAST ast = parse("pig", "VARIABILES>\n" + declaration + "\nMAIOR> FINIS;");
        DeclarationNodeAST matrix = (DeclarationNodeAST) ast.globals().getFirst();
        assertEquals(2, matrix.dimensions().get(0).line());
        assertEquals(declaration.indexOf('2'), matrix.dimensions().get(0).column());
        assertEquals(declaration.lastIndexOf('2'), matrix.dimensions().get(1).column());
    }
}
