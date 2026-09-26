// Proyecto 3: arreglos y matrices.
// Declaracion con tamano, inicializador literal, acceso por indice
// y recorrido con per.

VARIABILES >
    series numeros[5]: numerus {1, 2, 3, 4, 5};
    series matriz[3][3]: numerus {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    esto suma: numerus 0;
MAIOR >
    ## Comentario de bloque, en una sola linea ##
    numeros[0] = 10;
    numeros[4] = numeros[0] + numeros[1];
    >> numeros[0];
    >> numeros[4];

    per (esto i: numerus 0; i < 5; i++) {
        >> numeros[i];
    }

    >> "Centro de la matriz";
    >> matriz[1][1];

    per (esto f: numerus 0; f < 3; f++) {
        suma = suma + matriz[f][0];
    }
    >> "Suma de la primera columna";
    >> suma;
FINIS;
