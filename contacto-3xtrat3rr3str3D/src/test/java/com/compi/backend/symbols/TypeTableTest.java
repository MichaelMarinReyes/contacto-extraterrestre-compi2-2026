package com.compi.backend.symbols;

import com.compi.backend.Compiler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

/** Verifica referencias, límites, aislamiento léxico y renovación entre compilaciones. */
class TypeTableTest {
    @TempDir Path directory;

    @Test
    void primitiveAliasesResolveToCompatibleBooleanTypes() {
        TypeTable table = new TypeTable();
        for (String name : List.of("bool", "boolean", "verum", "falsus")) {
            assertEquals(Type.BOOLEAN, table.resolve(name));
        }
        TypeEntry entry = table.getEntries().stream().filter(type -> type.name().equals("bool")).findFirst().orElseThrow();
        assertNull(entry.baseTypeId());
        assertNull(entry.parentId());
        assertEquals(0, entry.dimension());
        assertEquals(0, entry.scopeId());
        assertTrue(entry.maximum().isEmpty());
    }

    @Test
    void matrixBoundsPreserveDifferentSizesAndUseBaseTypeReferences() {
        SymbolTable symbols = new SymbolTable();
        TypeTable table = symbols.getTypeTable();
        Scope scope = symbols.getCurrentScope();
        TypeEntry matrix = table.register(Type.arrayOf(Type.INT, new int[]{2, 3}), scope);
        TypeEntry other = table.register(Type.arrayOf(Type.INT, new int[]{4, 3}), scope);
        assertEquals(2, matrix.dimension());
        assertEquals(List.of(0, 0), matrix.minimum());
        assertEquals(List.of(1, 2), matrix.maximum());
        assertEquals(Type.INT, table.entryById(matrix.baseTypeId()).type());
        assertNotEquals(matrix.id(), other.id());
        assertEquals(matrix.id(), table.register(Type.arrayOf(Type.INT, new int[]{2, 3}), scope).id());
        assertThrows(UnsupportedOperationException.class, () -> matrix.maximum().add(99));
    }

    @Test
    void scopesShadowTypesAndKeepDistinctIdsEvenWithRepeatedNames() {
        Scope global = new Scope("global", null);
        Scope first = new Scope("bloque", global);
        Scope second = new Scope("bloque", global);
        TypeTable table = new TypeTable(global);
        TypeEntry root = table.define("Dato", Type.structType("Dato"), global);
        TypeEntry local = table.define("Dato", Type.classType("Dato"), first, root.id());
        assertEquals(local, table.resolveEntry("Dato", first));
        assertEquals(root, table.resolveEntry("Dato", second));
        assertEquals(root.id(), local.parentId());
        assertNotEquals(table.scopeId(first), table.scopeId(second));
        assertThrows(IllegalArgumentException.class,
                () -> table.define("Inválido", Type.INT, first, Integer.MAX_VALUE));
    }

    @Test
    void parameterArraysKeepUnknownUpperBoundsRatherThanInventingSizes() {
        SymbolTable symbols = new SymbolTable();
        symbols.enterScope("func_demo", 0);
        symbols.define(new Symbol("values", Type.array(Type.DOUBLE, 2), SymbolCategory.PARAMETER));
        symbols.exitScope();
        TypeEntry array = symbols.getTypeEntries().stream().filter(type -> type.dimension() == 2).findFirst().orElseThrow();
        assertEquals(List.of(0, 0), array.minimum());
        assertEquals(List.of(Type.UNKNOWN_SIZE, Type.UNKNOWN_SIZE), array.maximum());
        assertEquals("func_demo", array.scopeName());
        assertNotEquals(0, array.scopeId());
    }

    @Test
    void compiledImportsRegisterStructuresClassesAndMemberArrayTypes() throws Exception {
        Files.writeString(directory.resolve("Tipos.y"), """
                %estructuras
                estructura Datos:
                    entero cantidad
                    flotante valores[2]
                %funciones
                definir cambiar([] flotante valores):
                    valores[0] = 8.5
                """);
        Path classFile = Path.of("..", "archivos", "archivos_calificacion", "00_hello_world", "Hola.z");
        Files.copy(classFile, directory.resolve("Hola.z"));
        Compiler compiler = new Compiler();
        compiler.setWorkingDirectory(directory.toFile());
        assertTrue(compiler.compile("import Tipos.y\nimport Hola.z\nMAIOR> FINIS;", "pig"), compiler.getErrors().toString());
        List<TypeEntry> types = compiler.getTypes();
        assertTrue(types.stream().anyMatch(type -> type.name().equals("Datos") && type.type().isStruct()));
        assertTrue(types.stream().anyMatch(type -> type.type().isClass()));
        assertTrue(types.stream().anyMatch(type -> type.type().isArray() && type.maximum().equals(List.of(1))));
        for (TypeEntry type : types) {
            if (type.baseTypeId() != null) assertTrue(types.stream().anyMatch(base -> base.id() == type.baseTypeId()));
        }
    }

    @Test
    void compilerResetRemovesOldTypesButKeepsPreviousSnapshots() {
        Compiler compiler = new Compiler();
        assertTrue(compiler.compile("VARIABILES> series matriz[2][3]: numerus {{1,2,3},{4,5,6}}; MAIOR> FINIS;", "pig"),
                compiler.getErrors().toString());
        List<TypeEntry> previous = compiler.getTypes();
        assertTrue(previous.stream().anyMatch(type -> type.dimension() == 2));
        assertTrue(compiler.compile("MAIOR> FINIS;", "pig"), compiler.getErrors().toString());
        assertFalse(compiler.getTypes().stream().anyMatch(type -> type.type().isArray()));
        assertTrue(previous.stream().anyMatch(type -> type.dimension() == 2));
        assertEquals(Type.INT, compiler.getSymbolTable().resolveType("int"));
    }
}
