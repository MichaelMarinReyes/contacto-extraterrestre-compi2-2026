// Proyecto 4: imports y estructuras.
// main.pig importa una clase de Zetariano (como tipo de estructura),
// un modulo de datos y otro modulo que ya trae su propio import.

import modelos.Persona;
import datos.Preferencias;
import utilidades.Matematica;

VARIABILES >
    esto persona: Persona {nombre: "Ana", edad: 30, ciudad: "Quito"};
    esto total: numerus 0;
MAIOR >
    persona.saludar();
    >> "Edad";
    >> persona.edad;
    >> "Nacio en";
    >> persona.calcularAnioNacimiento(2026);
    >> "Doble de la edad";
    total = persona.edad + persona.edad;
    >> total;

    si (persona.esMayorDeEdad() == verum) {
        >> "Cumple mayoria de edad";
    } aliter (persona.esMayorDeEdad() == falsus) {
        >> "No cumple mayoria de edad";
    } finis;

    >> "Tema preferido";
    >> tema;
    >> "Idioma";
    >> idioma;
    >> "Pi";
    >> pi;
FINIS;
