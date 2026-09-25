// Fixture con errores a proposito: no debe compilar.
VARIABILES >
    esto x: numerus 5;
    esto y: numerus 10;
MAIOR >
    z = x + y;
    si (z > 10) {
        >> z
    } finis;
    per (esto i: numerus 0; i < 3; i++) {
        >> "falta punto y coma"
    }
FINIS;
