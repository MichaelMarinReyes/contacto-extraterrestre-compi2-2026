package com.compi.backend;

public class BackendTest {

    private static void assertTrue(boolean condition, String msg) {
        if (!condition) throw new RuntimeException("Assertion failed: " + msg);
    }

    private static void assertTrue(boolean condition) {
        assertTrue(condition, "Condición no cumplida");
    }

    private static void assertNotNull(Object obj) {
        if (obj == null) throw new RuntimeException("Assertion failed: Object is null");
    }

    public void testYCompilation() {
        CompilerFacade facade = new CompilerFacade();
        String yCode =
                "%estructuras\n" +
                "estructura Punto:\n" +
                "    entero x\n" +
                "    entero y\n" +
                "%funciones\n" +
                "definir sumar(entero a, entero b) -> entero:\n" +
                "    entero resultado = a + b\n" +
                "    retornar resultado\n";

        boolean success = facade.compileY(yCode);
        if (!success) {
            System.err.println("Errores en Y: " + facade.getErrors());
        }
        assertTrue(success, "La compilación de Y debería ser exitosa");
        assertNotNull(facade.getC3DCode());
        assertTrue(facade.getC3DCode().contains("void sumar()"));
    }

    public void testZetarianoCompilation() {
        CompilerFacade facade = new CompilerFacade();
        String zCode =
                "public class Persona {\n" +
                "    String nombre;\n" +
                "    int edad;\n" +
                "    public Persona(String n, int e) {\n" +
                "        nombre = n;\n" +
                "        edad = e;\n" +
                "    }\n" +
                "    public int getEdad() {\n" +
                "        return edad;\n" +
                "    }\n" +
                "}\n";

        boolean success = facade.compileZetariano(zCode);
        if (!success) {
            System.err.println("Errores en Zetariano: " + facade.getErrors());
        }
        assertTrue(success, "La compilación de Zetariano debería ser exitosa");
        assertNotNull(facade.getC3DCode());
        assertTrue(facade.getC3DCode().contains("Persona_Persona"));
    }

    public void testPigLatinCompilation() {
        CompilerFacade facade = new CompilerFacade();
        String pigCode =
                "VARIABILES>\n" +
                "esto edad : numerus 20;\n" +
                "esto fuerza : numerus 10;\n" +
                "MAIOR>\n" +
                ">> \"Iniciando contacto\";\n" +
                "si (edad >= 18) {\n" +
                "    fuerza = 15;\n" +
                "} finis ;\n" +
                ">> fuerza;\n" +
                "FINIS;\n";

        boolean success = facade.compilePigLatin(pigCode);
        if (!success) {
            System.err.println("Errores en PigLatin: " + facade.getErrors());
        }
        assertTrue(success, "La compilación de PigLatin debería ser exitosa");
        String cCode = facade.emitCCode();
        assertNotNull(cCode);
        assertTrue(cCode.contains("int main()"));
        assertTrue(cCode.contains("double stack[100000];"));
    }

    public static void main(String[] args) {
        BackendTest test = new BackendTest();
        System.out.println("=== Probando compilador de Y ===");
        test.testYCompilation();
        System.out.println("OK: Y compilado correctamente.");

        System.out.println("=== Probando compilador de Zetariano ===");
        test.testZetarianoCompilation();
        System.out.println("OK: Zetariano compilado correctamente.");

        System.out.println("=== Probando compilador de PigLatin ===");
        test.testPigLatinCompilation();
        System.out.println("OK: PigLatin compilado correctamente.");

        System.out.println("=== TODOS LOS TESTS PASARON EXITOSAMENTE ===");
    }
}
