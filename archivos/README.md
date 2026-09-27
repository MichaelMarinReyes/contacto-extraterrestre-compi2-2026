# Proyectos de prueba

Seis proyectos para comprobar los tres parsers, de menos a mas. Cada carpeta es
un proyecto aparte: en la IDE se abre **la carpeta del proyecto** (no esta),
porque la compilacion es de proyecto entero y `main.pig` es siempre el archivo
principal: se lee primero y sus imports se cargan antes que nada. Por eso
`05-completo/main.pig` no compila si se abre solo, y si compila con el resto del
proyecto al lado.

| Proyecto | Carpeta | Que comprueba |
| --- | --- | --- |
| 1 | `01-hola-mundo` | PigLatin: variables, una operacion, `>>`. Y: una funcion y una llamada. Zetariano: clase, constructor y metodo. |
| 2 | `02-condicionales-y-ciclos` | `si`/`aliter`/`finis`, `dum`, `facere`, `per` con `perge` e `interrumpe`. Y: las tres ramas del condicional, los tres ciclos, `continuar` y `romper`. Zetariano: `if`/`else if`/`else`, `for`, `while`, `do while`, `switch`, `break`, `continue`, `+=`. |
| 3 | `03-arreglos` | Arreglos y matrices en los tres lenguajes: `series` con tamano, inicializador literal, acceso por indice, matriz de dos dimensiones, arreglo pasado a una funcion. |
| 4 | `04-modulos-y-estructuras` | Imports: una clase de Zetariano usada como estructura, un modulo de datos y otro que ya importa a un tercero. Llamadas a metodos desde el `.pig`. |
| 5 | `05-completo` | Todo junto: cadena de imports de tres niveles, arreglo de estructuras, ciclos, metodos que llaman a otros metodos, los cuatro tipos de dato y las dos secciones de Y (`%estructuras` y `%funciones`). |
| 6 | `06-ciclos-y-saltos` | Solo control de flujo, y exhaustivo: todas las formas del condicional y de los tres ciclos, ciclos anidados a dos y a tres niveles, `romper` y `continuar` en cada ciclo y a proposito en el equivocado, y el `elegir` con `caso` y `siempre`. Esta en los tres lenguajes. |

Todos compilan sin errores: al abrir cualquiera de las carpetas y pulsar
`Compilar` deben salir `OK` en los archivos y cero errores en *Herramientas*.

Ademas de no dar errores, el codigo de estos proyectos **se ejecuta bien**. Cada
funcion de `06-ciclos-y-saltos/ciclos.y` devuelve un valor y `principal` lo
imprime, asi que un numero raro dice justo que caso fallo.

## El bloque de Y es la sangria, y no hay llaves

Esto es lo primero que hay que tener claro al escribir un `.y`. El enunciado lo
dice textual: *"Las tabulaciones en este lenguaje son las mas importantes. Pues
son las que definen el orden del codigo."* Ningun bloque se abre ni se cierra con
llaves, y una `}` suelta es un error de sintaxis, no un cierre.

Las llaves de Y solo aparecen en dos sitios, y ninguno es un bloque:

- La lista de un inicializador: `entero numeros[5] = {10, 20, 30, 40, 50}`, o
  una matriz, con llaves anidadas: `entero matriz[2][3] = {{1,2,3}, {4,5,6}}`.
- El paso de una estructura por referencia: `{} MiEstructura miEstructura`.

Asi queda un bloque, con la sangria marcando donde empieza y donde acaba:

```
definir clasificar(entero nota) -> entero:
    si (nota > 69) entonces
        retornar 3
    sino si (nota > 49) entonces
        retornar 2
    contrario
        retornar 0
```

> **Errata del enunciado.** En la pagina del `para` hay dos `}` sueltas, una
> detras de cada `continuar` y `romper` del ejemplo. No compilan, y no deben
> compilar: los `si` de al lado no se abren con llave, asi que esas llaves no
> cierran nada. Son un error de maquetacion del enunciado, no sintaxis de Y.

## Reglas del enunciado que conviene recordar al escribir mas ejemplos

Los seis proyectos estan escritos justamente alrededor de estas limitaciones,
que se ven al compilar:

- **PigLatin**
  - El import se escribe con la extension del archivo, como en el enunciado:
    `import carpeta.Objeto1.z`. Tambien se admite sin ella
    (`import datos.Globales`), y entonces se prueban las extensiones en orden.
    El orden de los imports importa: lo que un archivo usa como tipo tiene que
    estar cargado antes.
  - Las variables se declaran en `VARIABILES >`; dentro de `MAIOR >` no se
    declara nada (salvo en el inicializador de un `per`).
  - Un arreglo se declara con `series`: `series numeros[5]: numerus {1,2,3,4,5};`
    y una matriz con varios corchetes: `series matriz[3][3]: numerus {{...}};`.
  - Cada ciclo cierra a su manera, y no todos con `finis`:
    - `si ... { ... } finis;` - el `finis` va siempre, y `aliter` va entre medias.
    - `dum (condicion) { ... } finis;` - con `finis`.
    - `facere { ... } dum (condicion);` - sin `finis`, la prueba va al final.
    - `per (i = 0; i < n; i++) { ... }` - **sin `finis`**. Si se le pone un
      `finis` de mas, el `MAIOR` se cierra ahi y todo lo que venia despues se
      pierde en silencio.
  - `perge` e `interrumpe` solo dentro de `dum`, `facere` o `per`.
  - En un `per`, `perge` salta al incremento, no a la prueba; en un `dum` salta a
    la prueba. Por eso, en un `dum` el incremento tiene que ir **antes** del
    `perge`, o el ciclo no termina nunca: es lo mismo que pasaria en Java o en C.
  - El `per` si se inicializa de verdad, asi que `per (i = 5; i < 8; i++)` cuenta
    desde el 5. Lo mismo el `for` de Zetariano.
  - `>>` imprime un termino suelto (variable, miembro, llamada o texto), no
    una expresion: para operar hay que dejar el resultado en una variable.
  - Los comentarios son `//` o `## ... ##` en una sola linea.
- **Y**
  - El bloque se delimita con la indentacion, sin llaves (ver arriba). Cada nivel
    son 4 espacios; el enunciado mezcla tabulador y espacios, pero lo que cuenta
    es la sangria relativa.
  - `entonces` solo aparece en `si`, `sino` y `sino si`, nunca en `para`,
    `mientras` ni `elegir`. Los dos puntos son opcionales en todos ellos.
  - `romper` y `continuar` se escriben sin punto y coma, y `continuar` sale de un
    `caso` del `elegir` igual que `romper`.
  - Solo hay `<`, `>`, `==` y `!=`: no existen `>=` ni `<=`.
  - Un parametro de arreglo se escribe con los corchetes delante del tipo:
    `[]entero lista`.
  - `%funciones` es obligatoria; `%estructuras` es opcional.
  - Los comentarios son `//` o `/* ... */`.
- **Zetariano**
  - El archivo tiene que llamarse como la clase que declara (`Contador.z`
    declara `class Contador`).
  - Los parametros no admiten `[]`, ni `int[]` ni `int[][]`.
  - No hay menos unario: `return -1;` no compila.
  - Los comentarios son `//` o `/* ... */`.
