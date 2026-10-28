package com.compi.backend.ast;

import com.compi.backend.Compiler;
import com.compi.backend.ast.declarations.*;
import com.compi.backend.ast.exprressions.*;
import com.compi.backend.ast.statements.*;
import com.compi.backend.ast.semantic.*;
import com.compi.backend.errors.CompilationError;
import com.compi.backend.languages.LanguageCompilerFactory;
import com.compi.backend.symbols.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/** Contratos semánticos sobre AST, ámbitos y diagnósticos de los tres lenguajes. */
class AstSemanticTest {
    @TempDir Path directory;

    private ProgramNodeAST parse(String language, String source) {
        var compiler = LanguageCompilerFactory.getCompiler(language);
        List<CompilationError> errors = new ArrayList<>();
        var tree = compiler.parse(source, errors);
        assertTrue(errors.isEmpty(), errors.toString());
        return compiler.buildAst(tree, "prueba." + language);
    }

    private List<AbstractNodeAST> flatten(AbstractNodeAST root) {
        List<AbstractNodeAST> nodes = new ArrayList<>();
        nodes.add(root);
        for (ASTNode child : root.children()) if (child instanceof AbstractNodeAST node) nodes.addAll(flatten(node));
        return nodes;
    }

    @Test
    void handBuiltAstIsAnalyzedWithoutAParserOrSyntaxBindings() {
        LiteralNodeAST value = new LiteralNodeAST(1, 12, true, LiteralKind.BOOLEAN);
        DeclarationNodeAST declaration = new DeclarationNodeAST(1, 0, Type.UNKNOWN, "flag", List.of(), value);
        IdentifierNodeAST use = new IdentifierNodeAST(2, 3, "flag", null);
        ProgramNodeAST ast = new ProgramNodeAST(1, 0, List.of(), List.of(), List.of(declaration),
                List.of(new PrintNodeAST(2, 0, List.of(use))));
        ast.attachSource("manual.pig");
        SymbolTable symbols = new SymbolTable();
        List<CompilationError> errors = new ArrayList<>();
        SemanticModel model = new AstSemanticAnalyzer(symbols, "pig", errors).analyze(ast);
        assertTrue(errors.isEmpty(), errors.toString());
        assertEquals(Type.BOOLEAN, model.typeOf(use));
        assertSame(model.symbolOf(declaration), model.symbolOf(use));
        assertSame(symbols.getGlobalScope(), model.scopeOf(declaration));
        assertSame(symbols.scopeOf(ast), model.scopeOf(use));
        assertEquals("manual.pig", model.symbolOf(use).getSourceFile());
        assertEquals(1, model.symbolOf(use).getColumn());
        assertSame(symbols.scopeOf(ast), model.scopeOf(ast));
    }

    @Test
    void repeatedLoopsKeepDistinctSymbolsAndReuseScopesForCodeGeneration() {
        ProgramNodeAST ast = parse("pig", """
                VARIABILES> esto i: numerus 99;
                MAIOR>
                per (esto i: numerus 0; i < 2; i++) { >> i; }
                per (esto i: numerus 0; i < 2; i++) { >> i; }
                >> i;
                FINIS;
                """);
        List<CompilationError> errors = new ArrayList<>();
        SymbolTable table = new SymbolTable();
        SemanticModel model = new AstSemanticAnalyzer(table, "pig", errors).analyze(ast);
        assertTrue(errors.isEmpty(), errors.toString());
        List<ForNodeAST> loops = flatten(ast).stream().filter(ForNodeAST.class::isInstance).map(ForNodeAST.class::cast).toList();
        Scope first = table.scopeOf(loops.get(0)), second = table.scopeOf(loops.get(1));
        assertNotSame(first, second);
        assertNotSame(first.resolve("i"), second.resolve("i"));
        assertNotSame(first.resolve("i"), table.getGlobalScope().resolve("i"));
        assertSame(first, model.scopeOf(loops.get(0)));
        table.beginCodeGeneration();
        table.enterAstScope("ignorado", 0, loops.get(1));
        assertSame(second, table.getCurrentScope());
        table.endCodeGeneration();
        assertSame(table.getGlobalScope(), table.getCurrentScope());
    }

    @Test
    void forwardFunctionsAndReferenceParametersHaveResolvedSignatures() {
        ProgramNodeAST ast = parse("y", """
                %funciones
                definir primero(entero n) -> entero:
                    retornar segundo(n)
                definir segundo(entero n) -> entero:
                    retornar n
                definir cambiar([] flotante valores):
                    valores[0] = 1.5
                """);
        SymbolTable table = new SymbolTable();
        List<CompilationError> errors = new ArrayList<>();
        SemanticModel model = new AstSemanticAnalyzer(table, "y", errors).analyze(ast);
        assertTrue(errors.isEmpty(), errors.toString());
        CallNodeAST call = flatten(ast).stream().filter(CallNodeAST.class::isInstance).map(CallNodeAST.class::cast).findFirst().orElseThrow();
        assertSame(table.getFunction("segundo"), model.symbolOf(call));
        assertEquals(Type.INT, model.typeOf(call));
        Symbol parameter = table.getFunction("cambiar").getParameters().getFirst();
        assertTrue(parameter.isByReference());
        assertEquals(0, parameter.getOffset());
        assertTrue(parameter.getType().isArray());
    }

    @ParameterizedTest
    @CsvSource({
            "zet, 'class C { void f() { if (1) { print(1); } } }', booleana",
            "pig, 'VARIABILES> esto x: bool 1; MAIOR> FINIS;', incompatibles",
            "pig, 'VARIABILES> series x[2]: numerus {1}; MAIOR> FINIS;', Tamaño",
            "pig, 'VARIABILES> series x[2]: numerus; MAIOR> >> x[2]; FINIS;', rango",
            "pig, 'VARIABILES> series x[2]: numerus; MAIOR> >> x[1.5]; FINIS;', entero",
            "pig, 'MAIOR> perge; FINIS;', ciclo",
            "zet, 'class C { void f() { break; } }', ciclo",
            "zet, 'class C { int f() { return true; } }', incompatibles",
            "zet, 'class C { void f() { boolean x = true + false; } }', Operand",
            "zet, 'class C { C() {} void f() { C x = new C(1); } }', constructor",
            "zet, 'class C { Otra() {} }', constructor",
            "zet, 'class C { int f(int n) { return n; } int f(int n) { return n; } }', Firma",
            "zet, 'class A { int f() { return 1; } } class B extends A { int f() { return 2; } }', @Override",
            "zet, 'class A { } class B extends A { public @Override int g() { return 1; } }', @Override",
            "zet, 'class B extends Inexistente { }', superclase",
            "zet, 'class A extends A { }', heredar de sí misma",
            "zet, 'class A { int x; } class B extends A { int x; }', oculta",
            "zet, 'class A { private int x; } class B { void f(A a) { int y = a.x; } }', privado",
            "zet, 'class A { private int g() { return 1; } } class B { void f(A a) { int y = a.g(); } }', privado"
    })
    void semanticFailuresProduceLocatedDiagnosticsWithoutExecutableC(String language, String source, String message) {
        Compiler compiler = new Compiler();
        assertFalse(compiler.compile(source, language));
        assertNotNull(compiler.getAst());
        assertNotNull(compiler.getSemanticModel());
        assertTrue(compiler.getErrors().stream().anyMatch(error -> error.getMessage().contains(message)), compiler.getErrors().toString());
        assertTrue(compiler.getErrors().stream().allMatch(error -> error.getLine() > 0 && error.getColumn() > 0));
        assertFalse(compiler.getErrors().toString().contains("Error interno"), compiler.getErrors().toString());
        assertTrue(compiler.getCCode().isEmpty());
    }

    @Test
    void importedSymbolsAndErrorsKeepTheirActualSourceFile() throws Exception {
        Path module = directory.resolve("Datos.y");
        Files.writeString(module, """
                %estructuras
                estructura Datos:
                    entero cantidad
                %funciones
                definir valor() -> entero:
                    retornar 7
                """);
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(directory.toFile());
        assertTrue(compiler.compile("import Datos.y\nVARIABILES> esto dato: Datos {1};\nMAIOR> >> valor(); FINIS;", "pig", "main.pig"), compiler.getErrors().toString());
        String key = module.toAbsolutePath().normalize().toString();
        assertTrue(compiler.getImportedSemanticModels().containsKey(key));
        for (Symbol symbol : compiler.getSymbols()) {
            if ("Y".equals(symbol.getLanguage())) assertEquals(key, symbol.getSourceFile(), symbol.toString());
        }
        Files.writeString(module, "%funciones\ndefinir valor() -> entero:\n    retornar inexistente\n");
        assertFalse(compiler.compile("import Datos.y\nMAIOR> FINIS;", "pig", "main.pig"));
        assertTrue(compiler.getErrors().stream().anyMatch(error -> error.getFileName().equals(module.toString()) && error.getLine() == 3));
        assertTrue(compiler.getCCode().isEmpty());
    }

    @Test
    void semanticModelIsReplacedBetweenCompilationsAndClearedOnSyntaxFailure() {
        Compiler compiler = new Compiler();
        assertTrue(compiler.compile("VARIABILES> esto x: numerus 1; MAIOR> >> x; FINIS;", "pig"));
        SemanticModel first = compiler.getSemanticModel();
        assertTrue(compiler.compile("MAIOR> FINIS;", "pig"));
        assertNotSame(first, compiler.getSemanticModel());
        assertTrue(compiler.getImportedSemanticModels().isEmpty());
        assertFalse(compiler.compile("MAIOR> >> ; FINIS;", "pig"));
        assertNull(compiler.getSemanticModel());
        assertSame(compiler.getSymbolTable().getGlobalScope(), compiler.getSymbolTable().getCurrentScope());
    }

    @Test
    void localStructuresAreIsolatedByTheirFunctionScope() {
        Compiler compiler = new Compiler();
        assertTrue(compiler.compile("""
                %funciones
                definir uno() -> entero:
                    estructura Local:
                        entero a
                    Local dato = {1}
                    retornar dato.a
                definir dos() -> entero:
                    estructura Local:
                        entero b
                    Local dato = {2}
                    retornar dato.b
                """, "y"), compiler.getErrors().toString());
        List<StructDefinitionNodeAST> definitions = flatten(compiler.getAst()).stream()
                .filter(StructDefinitionNodeAST.class::isInstance).map(StructDefinitionNodeAST.class::cast).toList();
        assertEquals(2, definitions.size());
        SemanticModel model = compiler.getSemanticModel();
        assertNotSame(model.symbolOf(definitions.get(0)), model.symbolOf(definitions.get(1)));
        assertNotSame(model.scopeOf(definitions.get(0)), model.scopeOf(definitions.get(1)));
        assertNull(compiler.getSymbolTable().getStruct("Local"));
        assertEquals(2, compiler.getTypes().stream().filter(type -> type.name().equals("Local")).count());
    }

    @Test
    void classSignaturesAndFieldsAreAvailableBeforeAnyMethodBody() {
        ProgramNodeAST ast = parse("zet", """
                class Primera {
                    int f() { Segunda otra = new Segunda(); return otra.valor + otra.f(); }
                }
                class Segunda {
                    int valor;
                    Segunda() {}
                    int f() { return 2; }
                }
                """);
        List<CompilationError> errors = new ArrayList<>();
        SymbolTable table = new SymbolTable();
        SemanticModel model = new AstSemanticAnalyzer(table, "zet", errors).analyze(ast);
        assertTrue(errors.isEmpty(), errors.toString());
        NewObjectNodeAST allocation = flatten(ast).stream().filter(NewObjectNodeAST.class::isInstance)
                .map(NewObjectNodeAST.class::cast).findFirst().orElseThrow();
        assertEquals(SymbolCategory.CONSTRUCTOR, model.symbolOf(allocation).getCategory());
    }

    @Test
    void protectedAndPrivateMembersAreReachableFromTheirAllowedContexts() {
        Compiler compiler = new Compiler();
        assertTrue(compiler.compile("""
                class Base {
                    protected int valor;
                    private int secreto() { return 1; }
                    int propio() { return secreto(); }
                }
                class Hija extends Base {
                    int leer() { return valor + this.propio(); }
                }
                """, "zet"), compiler.getErrors().toString());
    }

    @Test
    void protectedIsVisibleToAClassInTheSamePackage() throws Exception {
        Path pkg = Files.createDirectories(directory.resolve("modelos"));
        Files.writeString(pkg.resolve("Base.z"), "public class Base { protected int valor; }");
        Files.writeString(pkg.resolve("Amiga.z"), """
                public class Amiga {
                    public int leer(Base base) { return base.valor; }
                }
                """);
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(directory.toFile());
        assertTrue(compiler.compile("""
                import modelos.Base.z
                import modelos.Amiga.z
                MAIOR> FINIS;
                """, "pig", directory.resolve("Main.pig").toString()), compiler.getErrors().toString());
    }

    @Test
    void defaultIsRejectedAcrossDifferentPackages() throws Exception {
        Path first = Files.createDirectories(directory.resolve("paquete1"));
        Path second = Files.createDirectories(directory.resolve("paquete2"));
        Files.writeString(first.resolve("Base.z"), "public class Base { int valor; }");
        Files.writeString(second.resolve("Ajena.z"), """
                public class Ajena {
                    public int leer(Base base) { return base.valor; }
                }
                """);
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(directory.toFile());
        assertFalse(compiler.compile("""
                import paquete1.Base.z
                import paquete2.Ajena.z
                MAIOR> FINIS;
                """, "pig", directory.resolve("Main.pig").toString()));
        assertTrue(compiler.getErrors().stream().anyMatch(error ->
                error.getMessage().contains("acceso default") && error.getFileName().endsWith("Ajena.z")),
                compiler.getErrors().toString());
    }

    @Test
    void defaultMembersArePackagePrivateAndReachableFromEveryClassInTheProgram() {
        Compiler compiler = new Compiler();
        assertTrue(compiler.compile("""
                class Base {
                    int valor;
                    Base() { }
                    int leer() { return valor; }
                }
                class Otra {
                    int leer(Base base) { return base.valor + base.leer(); }
                    Base crear() { return new Base(); }
                }
                class Hija extends Base {
                    int leerHeredado() { return valor; }
                }
                """, "zet"), compiler.getErrors().toString());
    }

    @Test
    void overrideAnnotationMayPrecedeOrFollowTheAccessModifier() {
        Compiler compiler = new Compiler();
        assertTrue(compiler.compile("""
                class Base {
                    public void primero() { }
                    public void segundo() { }
                }
                class Hija extends Base {
                    @Override
                    public void primero() { }
                    public @Override void segundo() { }
                }
                """, "zet"), compiler.getErrors().toString());
    }

    @Test
    void duplicateNamedFieldsAreNotLostWhenLeavingTheParseTree() throws Exception {
        Files.writeString(directory.resolve("Datos.y"), "%estructuras\nestructura Datos:\n    entero valor\n%funciones\ndefinir f():\n    imprimir(1)\n");
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(directory.toFile());
        assertFalse(compiler.compile("import Datos.y\nVARIABILES> esto dato: Datos {valor: 1, valor: 2}; MAIOR> FINIS;", "pig"));
        assertTrue(compiler.getErrors().stream().anyMatch(error -> error.getMessage().contains("Campo repetido")), compiler.getErrors().toString());
        assertTrue(compiler.getCCode().isEmpty());
    }
}
