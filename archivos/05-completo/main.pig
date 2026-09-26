// Proyecto 5: todo junto.
// Importa una clase de Zetariano, un modulo de datos y otro que
// importa a su vez un tercero. Usa estructuras, arreglos de estructuras,
// ciclos, llamadas a metodos y los cuatro tipos de dato.

import modelos.Empleado;
import datos.Globales;

VARIABILES >
    esto jefe: Empleado {nombre: "Maria", cargo: "Gerente", salario: 3200.5, activo: verum};
    series equipo[2]: Empleado {
        {nombre: "Ana", cargo: "Analista", salario: 1800.0, activo: verum},
        {nombre: "Luis", cargo: "Programador", salario: 2100.0, activo: falsus}
    };
    esto activos: numerus 0;
    esto nomina: decimalis 0.0;
MAIOR >
    jefe.saludar();
    >> "El jefe es";
    >> jefe.nombre;

    per (esto i: numerus 0; i < 2; i++) {
        equipo[i].saludar();
        si (equipo[i].activo == verum) {
            activos = activos + 1;
        } finis;
        nomina = nomina + equipo[i].salario;
    }

    >> "Empleados activos";
    >> activos;
    >> "Nomina";
    >> nomina;

    si (jefe.activo == verum) {
        >> "La empresa sigue activa";
    } aliter (jefe.activo == falsus) {
        >> "La empresa esta cerrada";
    } finis;

    dum (activos > 0) {
        >> "Quedan empleados";
        activos = activos - 1;
    } finis;

    >> "Empresa";
    >> nombreEmpresa;
FINIS;
