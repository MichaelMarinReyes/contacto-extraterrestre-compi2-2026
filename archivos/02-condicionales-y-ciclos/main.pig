// Proyecto 2: decisiones y repeticiones.
// si / aliter, dum, facere y per, con perge e interrumpe dentro del ciclo.

VARIABILES >
    esto edad: numerus 22;
    esto total: numerus 0;
    esto i: numerus 0;
    esto j: numerus 0;
MAIOR >
    si (edad >= 18) {
        >> "Es mayor de edad";
    } aliter (edad < 18) {
        >> "Es menor de edad";
    } finis;

    dum (i < 5) {
        total = total + i;
        i++;
    } finis;

    // Los tres ciclos de una vez: facere, dum y per.

    facere {
        total = total + 100;
    } dum (total > 200);

    per (j = 0; j < 10; j++) {
        si (j == 3) {
            perge;
        } finis;
        si (j == 7) {
            interrumpe;
        } finis;
        >> j;
    }

    >> "Total";
    >> total;
FINIS;
