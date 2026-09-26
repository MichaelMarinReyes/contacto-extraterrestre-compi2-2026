# Proyectos de prueba

Cinco proyectos para comprobar los tresparsers, de menos a mas. Cada
carpeta es un proyecto aparte: en la IDE se abre **la carpeta del proyecto**
(no esta), porque la compilacion es de proyecto entero y `main.pig` es siempre
el archivo principal: se lee primero y sus imports se cargan antes que nada.

| Proyecto | Carpeta | Que comprueba |
| --- | --- | --- |
| 1 | `01-hola-mundo` | PigLatin: variables, una operacion, `>>`. Y: una funcion y una llamada. Zetariano: clase, constructor y metodo. |
| 2 | `02-condicionales-y-ciclos` | `si`/`aliter`/`finis`, `dum`, `facere`, `per` con `perge` e `interrumpe`. Y: `si`/`sino si`/`contrario`, `para`, `mientras`, `hacer...mientras`, `continuar`, `romper`. Zetariano: `if`/`else if`/`else`, `for`, `while`, `do while`, `switch`, `break`, `continue`, `+=`. |
| 3 | `03-arreglos` | Arreglos y matrices en los tres lenguajes: `series` con tamano, inicializador literal, acceso por indice, matriz de dos dimensiones, arreglo pasado a una funcion. |
| 4 | `04-modulos-y-estructuras` | Imports: una clase de Zetariano usada como estructura, un modulo de datos y otro que ya importa a un tercero. Llamadas a metodos desde el `.pig`. |
| 5 | `05-completo` | Todo junto: cadena de imports de tres niveles, arreglo de estructuras, ciclos, metodos que llaman a otros metodos, los cuatro tipos de dato y las dos secciones de Y (`%estructuras` y `%funciones`). |

Todos compilan sin errores: al abrir cualquiera de las carpetas y pulsar
`Compilar` deben salir `OK` en los archivos y cero errores en *Herramientas*.

## Reglas del enunciado que conviene recordar al escribir mas ejemplos

Los cinco proyectos estan escritos justamente alrededor de estas limitaciones,
que se ven al compilar:

- **PigLatin**
  - El import se escribe con la extension del archivo, como en el enunciado:
    `import carpeta.Objeto1.z`. También se admite sin ella
    (`import datos.Globales`), y entonces se prueban las extensiones en orden.
    El orden de los imports importa: lo que un archivo usa como tipo tiene que
    estar cargado antes.
  - Las variables se declaran en `VARIABILES >`; dentro de `MAIOR >` no se
    declara nada (salvo en el inicializador de un `per`).
  - Un arreglo se declara con `series`: `series numeros[5]: numerus {1,2,3,4,5};`
    y una matriz con varios corchetes: `series matriz[3][3]: numerus {{...}};`.
  - `si` se cierra con `} finis;` siempre, y `aliter` va entre medias.
  - `perge` e `interrumpe` solo dentro de `dum`, `facere` o `per`.
  - `>>` imprime un termino suelto (variable, miembro, llamada o texto), no
    una expresion: para operar hay que dejar el resultado en una variable.
  - Los comentarios son `//` o `## ... ##` en una sola linea.
- **Y**
  - El bloque se delimita con la indentacion; `entonces` solo aparece en `si`,
    `sino` y `sino si`, nunca en `para`, `mientras` ni `elegir`.
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
