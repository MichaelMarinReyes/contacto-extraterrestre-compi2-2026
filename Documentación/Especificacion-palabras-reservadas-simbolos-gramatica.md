# Especificación de palabras reservadas, símbolos y gramática

Documento de entregable del Proyecto 2 (*St4ck Over-Pig*). Resume palabras
reservadas, símbolos y la estructura gramatical de los tres lenguajes del
compilador. La fuente normativa son las gramáticas ANTLR4 del repositorio:

| Lenguaje | Gramática | Extensiones |
|---|---|---|
| PigLatin | `contacto-3xtrat3rr3str3D/src/main/antlr4/com/compi/PigLatin.g4` | `.pig` |
| Y | `contacto-3xtrat3rr3str3D/src/main/antlr4/com/compi/Y.g4` | `.y` |
| Zetariano | `contacto-3xtrat3rr3str3D/src/main/antlr4/com/compi/Zetariano.g4` | `.z` |

Tras modificar una gramática se regeneran los analizadores con
`mvn antlr4:antlr4` (el plugin corre en `generate-sources`).

---

## 1. PigLatin

### 1.1 Palabras reservadas

`import` · `esto` · `series` · `structura` · `finis`/`FINIS` · `si` · `aliter` ·
`dum` · `facere` · `per` · `perge` · `interrumpe` · `novus` · `null` ·
`numerus` · `textum` · `decimalis` · `littera` · `bool` · `verum` · `falsus` ·
`non` · `VARIABILES` · `MAIOR`

(`MUNERA`, `actio`, `ratio`, `reddere` están reservados como tokens; la sección
de funciones del Proyecto 1 se apoya en módulos Y importados.)

### 1.2 Símbolos y operadores

| Símbolo | Significado |
|---|---|
| `:` | separador de tipo/valor en declaraciones |
| `;` | fin de sentencia (a veces opcional) |
| `,` | separador de argumentos/elementos |
| `=` | asignación |
| `.` | acceso a miembro / separador de ruta de import |
| `<<` | leer entrada |
| `>>` | imprimir |
| `(` `)` `[` `]` `{` `}` | agrupación, índice y bloques |
| `+` `-` `*` `/` | aritméticos |
| `>=` `<=` `==` `!=` `<` `>` | relacionales/igualdad |
| `&&` `\|\|` `non` | lógicos |
| `++` `--` | incremento/decremento abreviado |

### 1.3 Estructura de programa y reglas principales

```
import ruta.con.puntos;
VARIABILES >
    esto x : numerus 10;
    series a [4] : numerus {1, 2, 3, 4};
    estructura punto { ... };
MAIOR >
    sentencias...
FINIS;
```

- `pig_latin := import_declaracion* variables? maior`
- `variables := VARIABILES '>' (declaracion | arreglo_declaracion)+`
- `declaracion` admite: valor tipado con expresión, `textum`/`littera` con
  literal, construcción `novus Clase(args)`, instancia de estructura, `null`
  con tipo explícito, asignación y expresión.
- `arreglo_declaracion := series var `[tam]`+ : (tipo | tipo_struct) {lit}?`
- `maior := MAIOR '>' sentencia* FINIS ';'`
- `sentencia`: imprimir (`>>` expr)*, leer (`<<` destino?), asignación,
  `si`/`aliter`, ciclos `dum` (while), `facere ... dum` (do-while),
  `per (init; cond; inc)` (for), `perge`/`interrumpe`, llamada a función
  e incremento/decremento.
- `expresion` con precedencia por niveles: suma/resta sobre producto,
  producto sobre término; `termino` incluye literales, `novus`,
  `llamada_funcion` y signo menos unario.
- `condicion` con `||`, `&&`, `non` y primarias relacionales.
- Literales: `NUMERO_ENTERO`, `NUMERO_DECIMAL`, `CADENA_TEXTO` (`"..."`),
  `CARACTER` (`'c'`), `VERUM`, `FALSUS`, `NULL`.
- Comentarios: `// línea` y bloque `## ... ##`.

---

## 2. Y

### 2.1 Palabras reservadas

Secciones `%estructuras` · `%funciones`; palabras: `estructura` · `definir` ·
`si` · `entonces` · `sino` · `contrario` · `elegir` · `caso` · `siempre` ·
`romper` · `continuar` · `retornar` · `para` · `mientras` · `hacer` ·
`imprimir` · `leer`; tipos `entero` · `cadena` · `flotante` · `caracter` ·
`bool`; literales `verdadero` · `falso`.

### 2.2 Símbolos y operadores

Identación sensible (tokens `INDENT`/`DEDENT`): los bloques se delimitan por
indentación. Símbolos: `:` · `;` · `,` · `=` · `.` · `->` (tipo de retorno) ·
`(` `)` `[` `]` `{` `}` · `+` `-` `*` `/` · `==` `!=` `<=` `>=` `<` `>` ·
`&&` `||` `!` · `++` `--`.

### 2.3 Estructura de programa y reglas principales

```
%estructuras
estructura Punto:
    entero x
    entero y

%funciones
definir distancia(Punto p, Punto q) -> flotante:
    retornar ...
```

- `archivo := (%estructuras ... | %funciones ...)`
- `estructura_def := estructura ID ':' (bloque indentado | cabeza)`
- `definir ID (parámetros) '->' tipo ':' bloque` — declaración de función con
  tipo de retorno.
- Sentencias: `si ... entonces ... sino ... contrario`, `elegir ... caso ...
  siempre`, `romper`, `continuar`, `retornar`, `para`, `mientras ... hacer`,
  `imprimir`, `leer`.
- Tipos: `entero`, `cadena`, `flotante`, `caracter`, `bool`, estructuras y
  arreglos `tipo [tam]`.
- Literales: `NUMERO_ENTERO`, `NUMERO_DECIMAL`, `CADENA_TEXTO`, `CARACTER`,
  `verdadero`/`falso`.
- Comentarios: `// línea` y bloque `/* ... */`.

---

## 3. Zetariano

### 3.1 Palabras reservadas

Modificadores/delaraciones de clase: `public` · `private` · `protected` ·
`class` · `extends` · `@Override` · `this` · `new` · `void`;

control: `if` · `else` · `switch` · `case` · `default` · `break` · `continue` ·
`for` · `while` · `do` · `return`; I/O: `println` · `print` · `readln`;

tipos: `int` · `double` · `char` · `boolean` · `String`; literales: `true` ·
`false` · `null`.

### 3.2 Símbolos y operadores

| Símbolo | Significado |
|---|---|
| `:` | después de `case` |
| `;` | fin de sentencia |
| `,` | separador de parámetros/argumentos |
| `=` `+=` `-=` `*=` | asignaciones |
| `.` | acceso a miembro |
| `(` `)` `[` `]` `{` `}` | llamada, índice y bloques |
| `+` `-` `*` `/` `%` | aritméticos y módulo |
| `++` `--` | incremento/decremento |
| `==` `!=` `<=` `>=` `<` `>` | relacionales/igualdad |
| `&&` `\|\|` `!` | lógicos |

### 3.3 Estructura de programa y reglas principales

```
public class Animal {
    String nombre;
    public void comer() { println(nombre + " está comiendo."); }
}

public class Perro extends Animal {
    @Override
    public void comer() { println(nombre + " come mejor."); }
    public void ladrar() { ... }
}
```

- `programa := claseDef+`
- `claseDef := (public)? class ID (extends ID)? '{' miembroClase* '}'`
- `miembroClase := atributoDef | constructorDef | metodoDef`
- `atributoDef := modificadorAcceso? tipo ID ('[' ']')* ('=' expr)? ';'`
- `constructorDef := modificadorAcceso? ID '(' parámetros? ')' bloque`
- `metodoDef := modificadorAcceso? '@Override'? tipo ID '(' parámetros? ')' bloque`
  (`@Override` obligatorio para sobrescribir, validado en semántica)
- `modificadorAcceso := public | private | protected` (sin modificador =
  acceso de paquete)
- Encapsulamiento aplicado en semántica: `private` solo en la clase que
  declara, `protected` en esa clase o subclases.
- Sentencias: declaraciones, asignaciones, `if`/`else`, `switch`/`case`/
  `default`, `break`, `continue`, `for`, `while`, `do ... while`, `return`,
  `print`/`println`/`readln` y llamadas a métodos.
- Literales: `LITERAL_ENTERO`, `LITERAL_DECIMAL`, `CADENA_TEXTO`,
  `CARACTER`, `true`, `false`, `null`; `this` como referencia a la instancia.
- Comentarios: `// línea` y bloque `/* ... */`.

---

## 4. Correspondencia con el compilador

- Los tres lenguajes construyen el mismo AST
  (`com.compi.backend.ast`) con builders específicos
  (`PigLatinAstBuilder`, `YAstBuilder`, `ZetarianoAstBuilder`).
- La semántica (`AstSemanticAnalyzer` y colaboradores) y la generación C3D son
  compartidas; el tipo base declarado por una palabra clave se resuelve en
  `AbstractAstBuilder.type` (`numerus`/`entero`/`int` → `INT`, etc.).
- La emisión de C y el nuevo módulo RISC-V consumen las mismas cuartetas
  (`com.compi.backend.c3d` → `com.compi.backend.riscv`), por lo que el
  comportamiento sintáctico de los tres lenguajes es único en el backend.