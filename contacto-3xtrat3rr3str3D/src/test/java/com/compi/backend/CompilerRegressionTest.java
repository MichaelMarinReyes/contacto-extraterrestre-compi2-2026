package com.compi.backend;

import com.compi.backend.c3d.Mode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

/** Regresiones de compilación y ejecución del C, con entradas reproducibles. */
class CompilerRegressionTest {
    @TempDir Path temporaryDirectory;
    private final Path gradingDirectory = Path.of("..", "archivos", "archivos_calificacion").toAbsolutePath().normalize();

    @ParameterizedTest
    @EnumSource(Mode.class)
    void helloWorldRunsAcrossThreeLanguages(Mode mode) throws Exception {
        Compiler compiler = compileFile(gradingDirectory.resolve("00_hello_world/HolaMundo.pig"), mode);
        assertEquals("""
                Hola Mundo desde Pig Latin
                Hola Mundo desde Y?
                Hola Cadete
                Hola Mundo desde Zetariano
                2 + 3 = 5
                El doble de 21 es 42
                """, execute(compiler, ""));
        assertEquals(2, compiler.getImportedFiles().size());
    }

    @Test
    void minimalHelloWorldCompilesAndRuns() throws Exception {
        Compiler compiler = compileFile(gradingDirectory.resolve("00_hello_world/Minimo.pig"), Mode.QUADRUPLES);
        assertEquals("Hola Mundo\n", execute(compiler, ""));
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void calculatorPreservesRecursionReferencesAndDecimalInput(Mode mode) throws Exception {
        Compiler compiler = compileFile(gradingDirectory.resolve("01_calculadora/Calculadora.pig"), mode);
        String output = execute(compiler, "Ana\n1\n1.5\n2.25\n6\n5\n7\n8\n10\n1\n2\n0\n11\n12\n1234\n0\n");
        assertTrue(output.contains("Resultado: 3.750000"), output);
        assertTrue(output.contains("5! = 120"), output);
        assertTrue(output.contains("F(7) = 13"), output);
        assertTrue(output.contains("Promedio: 1.500000"), output);
        assertTrue(output.contains("--- Historial (2 resultados) ---"), output);
        assertTrue(output.contains("#0: 3.75"), output);
        assertTrue(output.contains("#1: 120"), output);
        assertTrue(output.contains("Digitos: 4"), output);
        assertTrue(output.contains("Hasta luego, Ana"), output);
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void mazeUsesDynamicMatricesChainedObjectsAndIndependentInstances(Mode mode) throws Exception {
        Compiler compiler = compileFile(gradingDirectory.resolve("03_laberinto/Laberinto.pig"), mode);
        String output = execute(compiler, "Ana\n4\n0\nLuis\n0\n");
        assertTrue(output.contains("Celdas accesibles desde el inicio: 29"), output);
        assertTrue(output.contains("Tesoros en el tablero: 4"), output);
        assertTrue(output.contains("Jugador: Ana (P)"), output);
        assertTrue(output.contains("Jugador: Luis (P) | Puntos: 0 | Vidas: 3 | Movimientos: 0"), output);
        assertTrue(output.contains("Veredicto: Mision fallida"), output);
        assertTrue(output.contains("Partidas jugadas: 2"), output);
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void hangmanReadsCharactersAndFinishesACompleteGame(Mode mode) throws Exception {
        Compiler compiler = compileFile(gradingDirectory.resolve("04_ahorcado/JuegoAhorcado.pig"), mode);
        String output = execute(compiler, "Ana\n0\nx\nz\nw\nq\nv\nb\n0\n");
        assertTrue(output.contains("Fallaste! La letra x no esta"), output);
        assertTrue(output.contains("PERDISTE. La palabra era:"), output);
        assertTrue(output.contains("p y t h o n"), output);
        assertTrue(output.contains("Partidas: 1 | Ganadas: 0 | Perdidas: 1"), output);
        assertTrue(output.contains("Gracias por jugar, Ana"), output);
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void gradingListCompilesWithItsUtilityModuleAndRunsItsDemo(Mode mode) throws Exception {
        Path source = gradingDirectory.resolve("02_lista_doble/ListaDobleMain.pig");
        Compiler compiler = compileFile(source, mode);
        assertEquals(3, compiler.getImportedFiles().size(), compiler.getImportedFiles().toString());
        String output = execute(compiler, "0\n");
        assertTrue(output.contains("=== LISTA DOBLEMENTE ENLAZADA ==="), output);
        assertTrue(output.contains("[10 <-> 20 <-> 30 <-> 40 <-> 50]"), output);
        assertTrue(output.contains("[50 <-> 40 <-> 30 <-> 20 <-> 10]"), output);
        assertTrue(output.contains("[7 <-> 8 <-> 9]"), output);
        assertTrue(output.contains("Saliendo..."), output);
        assertTrue(output.contains("REGISTRO DE OPERACIONES"), output);
        assertTrue(output.contains("Operaciones totales: 0"), output);
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void listClassesSupportOverloadsRecursionAndHeapLinks(Mode mode) throws Exception {
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(gradingDirectory.resolve("02_lista_doble").toFile());
        compiler.setCMode(mode);
        String source = """
                import clases.Nodo.z
                import clases.ListaDoble.z
                VARIABILES>
                esto lista : novus ListaDoble();
                esto otra : novus ListaDoble(99);
                MAIOR>
                lista.insertarFinal(3, 1);
                lista.insertarInicio(2);
                >> lista.suma();
                lista.ordenar();
                >> lista.obtener(0) >> "," >> lista.obtener(1) >> "," >> lista.obtener(2);
                lista.invertir();
                >> lista.obtener(0) >> "," >> lista.obtener(2);
                >> otra.suma();
                FINIS;
                """;
        assertTrue(compiler.compile(source, "pig", "ListDriver.pig"), compiler.getErrors().toString());
        assertEquals("6\n1,2,3\n3,1\n99\n", execute(compiler, ""));
    }

    @Test
    void importsUseEachModulesDirectoryAndAvoidDuplicateLoads() throws Exception {
        Path module = temporaryDirectory.resolve("lib");
        Files.createDirectories(module);
        Files.writeString(module.resolve("Extra.y"), "%funciones\ndefinir valor() -> entero:\n    retornar 7\n");
        Files.writeString(module.resolve("Modulo.pig"), "import Extra.y\nMAIOR> FINIS;\n");
        Path main = temporaryDirectory.resolve("Main.pig");
        Files.writeString(main, "import lib.Modulo.pig\nimport lib.Modulo.pig\nMAIOR> >> valor(); FINIS;\n");
        Compiler compiler = compileFile(main, Mode.QUADRUPLES);
        assertEquals("7\n", execute(compiler, ""));
        assertEquals(2, compiler.getImportedFiles().size());
    }

    @Test
    void syntaxAndSemanticFailuresDoNotEmitExecutableCode() throws Exception {
        Compiler compiler = new Compiler();
        assertFalse(compiler.compile("MAIOR> >> ; FINIS;", "pig"));
        assertEquals("", compiler.getCCode());
        assertFalse(compiler.compile("MAIOR> funcionInexistente(); FINIS;", "pig"));
        assertEquals("", compiler.getCCode());
        assertTrue(compiler.getErrors().toString().contains("funcionInexistente"));
        assertTrue(compiler.compile("MAIOR> >> \"Recuperado\"; FINIS;", "pig"), compiler.getErrors().toString());
        assertEquals("Recuperado\n", execute(compiler, ""));
    }

    @Test
    void referenceSignaturesAndStructureFieldsAreTypeChecked() throws Exception {
        Files.writeString(temporaryDirectory.resolve("Tipos.y"), """
                %estructuras
                estructura Datos:
                    entero cantidad
                    flotante valores[2]
                %funciones
                definir cambiar([] flotante valores):
                    valores[0] = 8.5
                """);
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(temporaryDirectory.toFile());
        assertFalse(compiler.compile("""
                import Tipos.y
                VARIABILES>
                esto datos : Datos {"incorrecto", 3};
                MAIOR> cambiar(1); FINIS;
                """, "pig"));
        assertTrue(compiler.getErrors().size() >= 3, compiler.getErrors().toString());
        assertEquals("", compiler.getCCode());
    }

    @Test
    void multidimensionalArraysAreSharedWithStructureReferences() throws Exception {
        Files.writeString(temporaryDirectory.resolve("Datos.y"), """
                %estructuras
                estructura Datos:
                    entero cantidad
                    flotante valores[2]
                %funciones
                definir cambiar({} Datos datos):
                    datos.valores[1] = 9.5
                    datos.cantidad++
                """);
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(temporaryDirectory.toFile());
        assertTrue(compiler.compile("""
                import Datos.y
                VARIABILES>
                series matriz[2][3] : numerus {{1,2,3},{4,5,6}};
                series valores[2] : decimalis {1.0,2.0};
                esto datos : Datos {0, valores};
                MAIOR>
                matriz[1][2] = 42;
                >> matriz[0][1] >> "," >> matriz[1][2];
                cambiar(datos);
                >> datos.cantidad >> "," >> valores[1];
                FINIS;
                """, "pig"), compiler.getErrors().toString());
        assertEquals("2,42\n1,9.500000\n", execute(compiler, ""));
    }

    @Test
    void dynamicThreeDimensionalArraysAndSwitchFallthroughWork() throws Exception {
        Files.writeString(temporaryDirectory.resolve("Cubo.z"), """
                public class Cubo {
                    int[][][] datos;
                    public Cubo(int n) { datos = new int[n][n][n]; }
                    public int probar(int valor) {
                        datos[1][1][1] = 42;
                        int suma = 0;
                        switch (valor) {
                            case 1: suma += 2;
                            case 2: suma += 3; break;
                            default: suma = 10;
                        }
                        for (int i = 0; i < 1; i++) { suma++; }
                        for (int i = 0; i < 1; i++) { suma++; }
                        return datos[1][1][1] + suma;
                    }
                }
                """);
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(temporaryDirectory.toFile());
        assertTrue(compiler.compile("import Cubo.z\nVARIABILES> esto cubo : novus Cubo(2);\nMAIOR> >> cubo.probar(1); FINIS;", "pig"), compiler.getErrors().toString());
        assertEquals("49\n", execute(compiler, ""));
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void booleanTypeIsInferredFromVerumFalsusAndBoolName(Mode mode) throws Exception {
        Compiler compiler = new Compiler();
        compiler.setCMode(mode);
        String source = """
                VARIABILES>
                esto mi_booleano : falsus;
                esto otro : verum;
                esto con_bool : bool falsus;
                esto con_bool_verum : bool verum;
                esto mi_flag = verum || 1 == 1;
                MAIOR>
                >> mi_booleano >> "," >> otro >> "," >> con_bool >> "," >> con_bool_verum;
                si (mi_flag) { >> "flag activo"; } finis;
                otro = falsus || 2 == 3;
                >> otro;
                FINIS;
                """;
        assertTrue(compiler.compile(source, "pig"), compiler.getErrors().toString());
        assertEquals("0,1,0,1\nflag activo\n0\n", execute(compiler, ""));
    }

    @ParameterizedTest
    @EnumSource(Mode.class)
    void boolIsAFirstClassTypeInScalarsAndArrays(Mode mode) throws Exception {
        Compiler compiler = new Compiler();
        compiler.setCMode(mode);
        String source = """
                VARIABILES>
                esto activo : bool verum;
                series banderas[3] : bool {verum, falsus, verum};
                MAIOR>
                >> activo >> "," >> banderas[0] >> "," >> banderas[1] >> "," >> banderas[2];
                FINIS;
                """;
        assertTrue(compiler.compile(source, "pig"), compiler.getErrors().toString());
        assertEquals("1,1,0,1\n", execute(compiler, ""));
    }

    private Compiler compileFile(Path source, Mode mode) throws Exception {
        Compiler compiler = new Compiler();
        compiler.setCMode(mode);
        assertTrue(compiler.compile(Files.readString(source), "pig", source.toString()), compiler.getErrors().toString());
        return compiler;
    }

    @Test
    void cyclicImportsLoadTheEntryFileOnlyOnce() throws Exception {
        Path main = temporaryDirectory.resolve("Main.pig");
        Files.writeString(main, "import Otro.pig\nVARIABILES> esto valor : numerus 3;\nMAIOR> >> valor; FINIS;");
        Files.writeString(temporaryDirectory.resolve("Otro.pig"), "import Main.pig\nMAIOR> FINIS;");
        Compiler compiler = compileFile(main, Mode.QUADRUPLES);
        assertEquals(1, compiler.getImportedFiles().size());
        assertEquals("3\n", execute(compiler, ""));
    }

    @Test
    void forwardCallsLoopScopesAndContinueTargetsRemainCorrect() throws Exception {
        Files.writeString(temporaryDirectory.resolve("Funciones.y"), """
                %funciones
                definir primero(entero n) -> entero:
                    retornar segundo(n)
                definir segundo(entero n) -> entero:
                    entero suma = 0
                    para(entero i = 0; i < n; i++):
                        elegir(i):
                            caso 1:
                                continuar
                            siempre:
                                suma = suma + 1
                    para(entero i = 0; i < 2; i++):
                        suma = suma + 1
                    retornar suma
                definir decimal() -> flotante:
                    retornar 1.5 * 2 / 2
                """);
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(temporaryDirectory.toFile());
        assertTrue(compiler.compile("""
                import Funciones.y
                VARIABILES> esto i : numerus 99;
                MAIOR>
                per (esto i : numerus 0; i < 2; i++) { >> i; }
                >> i;
                >> primero(3);
                >> decimal();
                FINIS;
                """, "pig"), compiler.getErrors().toString());
        assertEquals("0\n1\n99\n4\n1.500000\n", execute(compiler, ""));
    }

    @Test
    void invalidArrayElementsIndicesAndDimensionsAreRejected() {
        Compiler compiler = new Compiler();
        assertFalse(compiler.compile("VARIABILES> series a[2] : numerus {1,\"texto\"}; MAIOR> FINIS;", "pig"));
        assertTrue(compiler.getErrors().toString().contains("incompatibles"));
        assertFalse(compiler.compile("VARIABILES> series a[2] : numerus {1,2}; MAIOR> >> a[-1]; FINIS;", "pig"));
        assertFalse(compiler.compile("VARIABILES> series a[2] : numerus {1,2}; MAIOR> >> a[0.5]; FINIS;", "pig"));
        assertTrue(compiler.getErrors().toString().contains("entero"));
        assertFalse(compiler.compile("VARIABILES> series a[0] : numerus; MAIOR> FINIS;", "pig"));
        assertEquals("", compiler.getCCode());
    }

    @Test
    void arraysOfObjectsResolveTheIndexedReceiver() throws Exception {
        Path source = Path.of("..", "archivos", "05-completo", "main.pig").toAbsolutePath().normalize();
        Compiler compiler = compileFile(source, Mode.QUADRUPLES);
        String output = execute(compiler, "");
        assertTrue(output.contains("Maria"), output);
        assertTrue(output.contains("Ana"), output);
        assertTrue(output.contains("Luis"), output);
        assertTrue(output.contains("3900.000000"), output);
    }

    private String execute(Compiler compiler, String input) throws Exception {
        Path source = temporaryDirectory.resolve("program.c");
        Path executable = temporaryDirectory.resolve("program");
        Path output = temporaryDirectory.resolve("output.txt");
        Path inputFile = temporaryDirectory.resolve("input.txt");
        Files.writeString(source, compiler.getCCode());
        Files.writeString(inputFile, input);
        Process gcc = new ProcessBuilder("gcc", "-std=c11", "-Werror=implicit-function-declaration",
                source.toString(), "-lm", "-o", executable.toString()).redirectErrorStream(true).redirectOutput(output.toFile()).start();
        assertTrue(gcc.waitFor(10, TimeUnit.SECONDS), "GCC no terminó a tiempo");
        assertEquals(0, gcc.exitValue(), Files.readString(output));
        Process program = new ProcessBuilder(executable.toString()).redirectInput(inputFile.toFile())
                .redirectErrorStream(true).redirectOutput(output.toFile()).start();
        if (!program.waitFor(5, TimeUnit.SECONDS)) {
            program.destroyForcibly();
            fail("El programa generado no terminó a tiempo");
        }
        assertEquals(0, program.exitValue(), Files.readString(output));
        return Files.readString(output);
    }
}
