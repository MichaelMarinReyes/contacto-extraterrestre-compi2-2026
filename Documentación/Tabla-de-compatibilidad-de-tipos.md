# Tabla de compatibilidad de tipos

Documento de entregable del Proyecto 2 (*St4ck Over-Pig*). Describe, para los tres
lenguajes del compilador (PigLatin, Y y Zetariano), cómo se convierte un valor al
operar con otro: responde, por ejemplo, a qué se convierte la suma de un `textum`
con un `numerus`.

La política descrita es la que implementa el compilador (paquetes
`com.compi.backend.symbols.TypeCompatibility` y `Type.isAssignableTo`, con la
regla de subtipos `assignable` de `AstSemanticAnalyzer`), y se aplica de forma
compartida a los tres lenguajes porque los tres construyen el mismo AST.

## 1. Tipos de cada lenguaje

| Tipo lógico | PigLatin | Y | Zetariano |
|---|---|---|---|
| entero | `numerus` | `entero` | `int` |
| decimal | `decimalis` | `flotante` | `double` |
| cadena | `textum` | `cadena` | `String` |
| carácter | `littera` | `caracter` | `char` |
| booleano | `bool` | `bool` | `boolean` |
| clase | `novus C(...) : Tipo` / `null : Tipo` | — | `class C` + `new C()` |
| estructura | `structura` | `estructura` | — |
| arreglo | `series T [tam]` | `T [tam]` | `T[]` |
| vacío | — | — | `void` |
| nulo | `null : Tipo` | — | `null` |

Dos tipos son **numéricos**: `INT` y `DOUBLE`. `CHAR` no es numérico.

## 2. Operadores aritméticos (`+`, `-`, `*`, `/`)

Regla general (`checkArithmetic`):

| Operandos | Resultado |
|---|---|
| ambos numéricos y no hay `DOUBLE` | `INT` |
| al menos uno `DOUBLE` | `DOUBLE` |
| **al menos uno `STRING`** (solo `+`) | `STRING` (concatenación) |
| otro caso | incompatibles (error) |

- Solo `+` concatena cadenas. `-`, `*` y `/` con una cadena son incompatibles.
- Si uno de los sumandos es cadena y el otro numérico, el número se convierte a
  su representación textual: `"edad: " + 21` → `"edad: 21"`.
- Ejemplo del enunciado: `textum a = "total: " + 5;` convierte el `numerus` a
  `textum`. En Zetariano: `String s = "total: " + 5;`.

## 3. Operadores relacionales (`<`, `>`, `<=`, `>=`)

Regla (`checkRelational`):

| Operandos | Resultado |
|---|---|
| ambos numéricos | `BOOLEAN` |
| ambos carácter (`CHAR`) | `BOOLEAN` |
| otro caso | incompatibles |

## 4. Igualdad y desigualdad (`==`, `!=`)

Regla (`checkEquality`):

| Operandos | Resultado |
|---|---|
| mismo tipo exacto | `BOOLEAN` |
| ambos numéricos (p. ej. `int` vs `double`) | `BOOLEAN` |
| `null` contra clase, arreglo o estructura | `BOOLEAN` |
| otro caso | incompatibles |

## 5. Operadores lógicos (`&&`, `||`, `!`)

Regla (`checkLogical` y negación unaria):

| Operandos | Resultado |
|---|---|
| ambos `BOOLEAN` | `BOOLEAN` |
| otro caso | incompatibles |

`!` exige un booleano y devuelve `BOOLEAN`. El signo menos unario exige un
numérico y conserva su tipo.

## 6. Asignación y compatibilidad de tipos

Regla `assignable(expected, actual)` (asignación, retornos, argumentos,
inicializadores):

| Valor (actual) | Destino (esperado) | ¿Válido? |
|---|---|---|
| igual al destino | mismo tipo | ✅ |
| `int` | `double` | ✅ promoción implícita |
| `double` | `int` | ❌ (no hay degradación implícita) |
| `null` | clase, arreglo o estructura | ✅ |
| arreglo | arreglo | ✅ solo si misma forma (mismo tipo de elemento y dimensiones) |
| subclase | superclase (clase base o ancestro) | ✅ (polimorfismo, Zetariano/PigLatin) |
| superclase | subclase | ❌ |
| otro caso | otro caso | ❌ |

En PigLatin, `esto otro : null : Persona;` declara un objeto nulo; en Zetariano,
`Persona p = null;` es válido y `p.equals(...)`/`p == null` comprobable. Una
clase hija asignada a una variable de su clase base conserva el despacho virtual
del objeto real.

## 7. Asignaciones compuestas (`+=`, `-=` …)

Se aplica la misma tabla aritmética del punto 2 al operador base y el resultado
debe ser asignable al destino (`a.compatible`). Por ejemplo `x += 2` con `x`
numérico produce el tipo aritmético común; `s += "!"` con `s` cadena es
concatenación.

## 8. Índices de arreglos

- El índice debe ser `INT`.
- Con índices constantes se comprueba el rango `[0..tamaño-1]` cuando el tamaño
  es conocido.
- `objeto.campo[i]` y accesos encadenados degradan el arreglo por niveles
  (`desindexar`).

## 9. Ternario

Las dos ramas deben ser compatibles entre sí; si ambas son numéricas el
resultado es `DOUBLE`; en otro caso debe existir un tipo común asignable.

## Referencias en el código

- `com.compi.backend.symbols.TypeCompatibility` — aritmética, relacional,
  igualdad y lógica.
- `com.compi.backend.symbols.Type.isAssignableTo` — asignación básica,
  `null`, `int→double` y arreglos de misma forma.
- `com.compi.backend.ast.semantic.AstSemanticAnalyzer.assignable` — añade
  `subclase → superclase`.
- `com.compi.backend.ast.semantic.AstExpressionAnalyzer` — aplica las reglas a
  operandos, unarios, índices, asignaciones compuestas y ternarios.