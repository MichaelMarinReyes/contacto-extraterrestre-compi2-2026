package com.compi.backend.ast;

import com.compi.backend.Compiler;
import com.compi.backend.ast.codegen.AstCodeGenerator;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.*;
import com.compi.backend.ast.semantic.AstSemanticAnalyzer;
import com.compi.backend.c3d.*;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.symbols.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

/** Ejecuta el C generado desde AST y comprueba los contratos del runtime plano. */
class AstCodeGenerationTest {
    @TempDir Path directory;

    private String execute(String code, String input) throws Exception {
        Path source = directory.resolve("programa.c"), binary = directory.resolve("programa");
        Files.writeString(source, code);
        Process gcc = new ProcessBuilder("gcc", source.toString(), "-o", binary.toString()).redirectErrorStream(true).start();
        assertTrue(gcc.waitFor(15, TimeUnit.SECONDS), "GCC no terminó");
        assertEquals(0, gcc.exitValue(), new String(gcc.getInputStream().readAllBytes()));
        Process process = new ProcessBuilder(binary.toString()).redirectErrorStream(true).start();
        process.getOutputStream().write(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        process.getOutputStream().close();
        assertTrue(process.waitFor(15, TimeUnit.SECONDS), "El programa no terminó");
        String output = new String(process.getInputStream().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        assertEquals(0, process.exitValue(), output);
        return output;
    }

    private Compiler compile(Mode mode, String source) {
        Compiler compiler = new Compiler();
        compiler.setCMode(mode); compiler.setWorkingDirectory(directory.toFile());
        assertTrue(compiler.compile(source, "pig", "main.pig"), compiler.getErrors().toString());
        assertEquals(1, compiler.getCCode().split("int main\\(void\\)", -1).length - 1);
        assertFalse(compiler.getCCode().contains("#include <math.h>"));
        assertTrue(compiler.getCCode().contains("switch"));
        return compiler;
    }

    @Test
    void handwrittenAstGeneratesExecutableCWithoutAnyParser() throws Exception {
        LiteralNodeAST initial = new LiteralNodeAST(1, 0, 5, LiteralKind.INTEGER);
        DeclarationNodeAST declaration = new DeclarationNodeAST(1, 0, Type.INT, "x", List.of(), initial);
        IdentifierNodeAST before = new IdentifierNodeAST(2, 0, "x", null);
        IdentifierNodeAST target = new IdentifierNodeAST(2, 4, "x", null);
        IncrementNodeAST increment = new IncrementNodeAST(2, 4, target, "++", true);
        BinaryOperationNodeAST sum = new BinaryOperationNodeAST(2, 0, before, "+", increment, OperationKind.ARITHMETIC);
        ProgramNodeAST ast = new ProgramNodeAST(1, 0, List.of(), List.of(), List.of(declaration),
                List.of(new PrintNodeAST(2, 0, List.of(sum))));
        SymbolTable symbols = new SymbolTable();
        List<CompilationError> errors = new ArrayList<>();
        var model = new AstSemanticAnalyzer(symbols, "pig", errors).analyze(ast);
        assertTrue(errors.isEmpty(), errors.toString());
        int offset = symbols.getGlobalScope().getCurrentOffset();
        List<Symbol> previous = symbols.getAllSymbols();
        C3DGenerator instructions = new C3DGenerator();
        new AstCodeGenerator(symbols, model, instructions, "pig").generate(ast);
        assertEquals(previous, symbols.getAllSymbols());
        assertEquals(offset, symbols.getGlobalScope().getCurrentOffset());
        assertSame(symbols.getGlobalScope(), symbols.getCurrentScope());
        assertEquals("11\n", execute(CCodeEmitter.generateCCode(instructions.getQuadruples(), instructions.getDeclaredTemps(), symbols), ""));
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void indexedDestinationsAndOperandsAreEvaluatedOnceInSourceOrder(Mode mode) throws Exception {
        Files.writeString(directory.resolve("Effects.z"), """
                class Effects {
                    int[] datos = {10,20,30};
                    Effects() {}
                    int sumar(int a, int b) { return a * 10 + b; }
                    void probar() {
                        int i = 0;
                        datos[++i] += ++i;
                        println(i);
                        println(datos[1]);
                        println(datos[2]);
                        i = 1;
                        println(i + ++i);
                        println(sumar(i, ++i));
                    }
                }
                """);
        Compiler compiler = compile(mode, "import Effects.z\nVARIABILES> esto objeto: novus Effects(); MAIOR> objeto.probar(); FINIS;");
        assertEquals("2\n22\n30\n3\n23\n", execute(compiler.getCCode(), ""));
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void shortCircuitAndTernaryOnlyEvaluateTheSelectedBranches(Mode mode) throws Exception {
        Files.writeString(directory.resolve("Branches.z"), """
                class Branches {
                    int contador;
                    Branches() {}
                    boolean tocar() { contador++; return true; }
                    void probar() {
                        boolean a = false && tocar();
                        boolean b = true || tocar();
                        boolean c = true && tocar();
                        int valor = true ? 7 : ++contador;
                        println(contador);
                        println(valor);
                        println(!a && b && c);
                        println(7 / 2);
                        println(7.0 / 2);
                        println(7 % 2);
                    }
                }
                """);
        Compiler compiler = compile(mode, "import Branches.z\nVARIABILES> esto objeto: novus Branches(); MAIOR> objeto.probar(); FINIS;");
        assertEquals("1\n7\n1\n3\n3.500000\n1\n", execute(compiler.getCCode(), ""));
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void dynamicMatricesKeepTheirHeaderAcrossGroupedIndices(Mode mode) throws Exception {
        Files.writeString(directory.resolve("Matrix.z"), """
                class Matrix {
                    int[][] datos;
                    Matrix(int n) { datos = new int[n][n]; }
                    int probar() {
                        datos[1][2] = 40;
                        datos[1][2] += 2;
                        int actualizado = ++datos[0][1];
                        return datos[1][2] + datos[0][1];
                    }
                }
                """);
        Compiler compiler = compile(mode, "import Matrix.z\nVARIABILES> esto matriz: novus Matrix(3); MAIOR> >> matriz.probar(); FINIS;");
        assertEquals("43\n", execute(compiler.getCCode(), ""));
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void readingAnArrayElementAndIncrementingAMemberUsesItsHeapCell(Mode mode) throws Exception {
        Files.writeString(directory.resolve("Datos.y"), """
                %estructuras
                estructura Datos:
                    entero cantidad
                    entero valores[2]
                %funciones
                definir siguiente({} Datos dato):
                    dato.cantidad++
                """);
        Compiler compiler = compile(mode, """
                import Datos.y
                VARIABILES>
                series valores[2]: numerus {1,2};
                esto dato: Datos {cantidad: 0, valores: valores};
                MAIOR>
                dato.valores[1] <<
                dato.cantidad++;
                siguiente(dato);
                >> dato.cantidad >> "," >> dato.valores[1];
                FINIS;
                """);
        assertEquals("2,9\n", execute(compiler.getCCode(), "9\n"));
    }
}
