package com.compi.backend;

import com.compi.backend.c3d.Mode;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/** Los seis proyectos de ejemplo del curso deben compilar sin errores. */
class ExamplesCompilationTest {
    private final Path base = Path.of("..", "archivos").toAbsolutePath().normalize();

    @ParameterizedTest
    @ValueSource(strings = {
            "01-hola-mundo", "02-condicionales-y-ciclos", "03-arreglos",
            "04-modulos-y-estructuras", "05-completo", "06-ciclos-y-saltos"
    })
    void exampleProjectsCompile(String project) throws Exception {
        Path main = base.resolve(project).resolve("main.pig");
        assertTrue(Files.exists(main), "Falta el archivo " + main);
        for (Mode mode : Mode.values()) {
            Compiler compiler = new Compiler();
            compiler.setCMode(mode);
            assertTrue(compiler.compile(Files.readString(main), "pig", main.toString()),
                    project + " (" + mode + ") -> " + compiler.getErrors());
            assertFalse(compiler.getCCode().isBlank(), project + " (" + mode + ") no generó código C");
        }
    }
}
