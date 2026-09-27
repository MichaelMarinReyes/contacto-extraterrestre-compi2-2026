// Proyecto 6: ciclos, ciclos anidados y saltos, en PigLatin.
// Es el equivalente de la parte de Y del mismo proyecto, para comprobar que
// los tres lenguajes manejan el control de flujo con la misma semantica.
//
// Comprueba: per, dum y facere por separado; los tres anidados a dos y a tres
// niveles; perge e interrumpe en cada uno; y un si/aliter/finis dentro del
// ciclo, con el condicional aussi anidado.

VARIABILES >
    esto total: numerus 0;
    esto i: numerus 0;
    esto j: numerus 0;
    esto k: numerus 0;
MAIOR >
    // ------------------------------------------------ per, con perge e interrumpe
    total = 0;
    per (i = 0; i < 10; i++) {
        si (i == 2) {
            perge;
        } finis;
        si (i == 7) {
            interrumpe;
        } finis;
        total = total + i;
    }
    >> "per con perge e interrumpe";
    >> total;

    // ------------------------------------------------------- dum y facere juntos
    // Ojo: en un "dum" el "perge" salta a la prueba, no al incremento, asi que
    // el i++ tiene que ir antes del "perge" o el ciclo nunca termina.
    total = 0;
    i = 0;
    dum (i < 4) {
        i++;
        si (i == 3) {
            perge;
        } finis;
        total = total + i;
    } finis;
    >> "dum con perge";
    >> total;

    total = 0;
    facere {
        total = total + 7;
    } dum (total < 20);
    >> "facere con interrumpe implicito";
    >> total;

    // -------------------------------------------------------------- dos niveles
    total = 0;
    per (i = 0; i < 3; i++) {
        per (j = 0; j < 3; j++) {
            total = total + 1;
        }
    }
    >> "per dentro de per";
    >> total;

    total = 0;
    per (i = 0; i < 3; i++) {
        j = 0;
        dum (j < 2) {
            total = total + 1;
            j++;
        } finis;
    }
    >> "dum dentro de per";
    >> total;

    // -------------------------------------------------------------- tres niveles
    total = 0;
    per (i = 0; i < 2; i++) {
        per (j = 0; j < 2; j++) {
            per (k = 0; k < 2; k++) {
                total = total + 1;
            }
        }
    }
    >> "tres niveles";
    >> total;

    // ------------------------------- interrumpe solo corta el ciclo mas interno
    total = 0;
    per (i = 0; i < 3; i++) {
        per (j = 0; j < 3; j++) {
            si (j == 1) {
                interrumpe;
            } finis;
            total = total + 1;
        }
    }
    >> "interrumpe en el interior";
    >> total;

    // ------------------------------------- condicional dentro y fuera del ciclo
    total = 0;
    per (i = 0; i < 7; i++) {
        si (i > 5) {
            total = total + 100;
        } aliter (i > 2) {
            total = total + 10;
        } finis;
        total = total + 1;
    }
    >> "si dentro del per";
    >> total;

    si (total > 50) {
        per (i = 0; i < 2; i++) {
            total = total + 1;
        }
    } aliter (total > 10) {
        total = total + 1000;
    } finis;
    >> "per dentro del si";
    >> total;

    // ------------------------------------- declarando la variable en el per
    total = 0;
    per (esto n: numerus 0; n < 5; n++) {
        total = total + n;
    }
    >> "per con declaracion";
    >> total;
FINIS;
