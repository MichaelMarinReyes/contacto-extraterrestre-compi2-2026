# Manual Técnico — Contacto 3xtr4t3rr3str3D

**Proyecto 2 — St4ck Over-Pig**

Universidad de San Carlos de Guatemala · Centro Universitario de Occidente ·
Organización de Lenguajes y Compiladores 2 · Segundo semestre 2026

## Índice

1. Descripción
2. Stack tecnológico
3. Archivo de configuración
4. Requisitos y entorno de desarrollo
5. Compilación y empaquetado
6. Arquitectura general del sistema
7. Gramáticas de los tres lenguajes
8. Semántica y código intermedio
9. Back-end de ensamblador RISC-V
10. Integración con el IDE
11. Compatibilidad de tipos
12. Diagrama de clases
13. Verificación y pruebas

---

## 1. Descripción

Contacto 3xtr4t3rr3str3D es un entorno de escritorio para editar y compilar
programas en **PigLatin** (`.pig`), **Y** (`.y`) y **Zetariano** (`.z`). Integra
editor con pestañas y resaltado, árbol de proyecto, tabla de símbolos, árbol de
sintaxis abstracta, traza de parser y salidas intermedias.

El **Proyecto 2** añade sobre el Proyecto 1: la semántica de Zetariano
(encapsulamiento, herencia, polimorfismo con `@Override` y `this`), objetos de
PigLatin con tipo explícito y `null`, y la **traducción de las
cuartetas/tripletas a ensamblador RISC-V funcional** (módulo nuevo
`com.compi.backend.riscv`, independiente de los emisores de C). La emisión de C
del Proyecto 1 se mantiene operativa.

El procesamiento común lo coordina `Compiler`: análisis léxico y sintáctico
ANTLR, carga de imports, semántica sobre AST propio, generación de C3D,
derivación de cuartetas/tripletas, **emisión de C** y **emisión de RISC-V**, y
datos para la visualización de pila.

---

## 2. Stack tecnológico

| Componente | Tecnología | Uso |
|---|---|---|
| Lenguaje | Java 22 (JDK 22) | Aplicación completa |
| Léxico/sintáctico | ANTLR 4 (4.13.2) | Gramáticas `.g4`, generación de Lexer/Parser/Visitor |
| Interfaz | Java Swing | Editor, consolas, paneles de herramientas |
| Apariencia | FlatLaf 3.6 (+ temas IntelliJ) | Temas modernos claro/oscuro |
| Build | Maven | `generate-sources` con `antlr4-maven-plugin` |
| Procesamiento | Lombok 1.18.34 (`provided`) | Reducción de código repetitivo |
| Simulador interno | RARS 1.6 (jar `lib/rars1_6.jar`, MIT) | Ejecución del ensamblador RV32IMF/D dentro del IDE |
| Simulador externo (opcional) | RARS de escritorio | Comprobación manual del `.s` generado |

---

## 3. Archivo de configuración

`contacto-3xtrat3rr3str3D/pom.xml` declara:

- Dependencias: `AbsoluteLayout RELEASE260`, `flatlaf 3.6`,
  `flatlaf-intellij-themes 3.6`, `antlr4 4.13.2`, `lombok 1.18.34` (provided) y
  `rars 1.6` con alcance `system` apuntando a `lib/rars1_6.jar` (RARS no está en
  Maven Central; el jar se versiona en el repositorio con su licencia MIT).
- Propiedades: `maven.compiler.source/target = 22`, UTF-8.
- `maven-compiler-plugin 3.13.0` con `release 22`.
- `antlr4-maven-plugin 4.13.2` en `generate-sources` (goal `antlr4`, con
  `visitor` y `listener` habilitados): genera los analizadores desde las tres
  gramáticas antes de compilar.

Las clases generadas por ANTLR (en `target`) no se editan a mano; cambie el
`.g4` y vuelva a compilar: `mvn antlr4:antlr4` regenera, y `mvn clean package`
reconstruye todo.

---

## 4. Requisitos y entorno de desarrollo

- JDK 22 con `JAVA_HOME` configurado (`java -version` lo valida).
- IntelliJ IDEA (soporte Maven y plugin ANTLR) o Apache NetBeans.
- Maven (resolución de dependencias).

---

## 5. Compilación y empaquetado

**Opción A — Maven:**

```
cd contacto-3xtrat3rr3str3D
mvn clean verify
```

`clean` elimina salidas previas, `generate-sources` genera los analizadores,
`verify` compila, ejecuta pruebas y empaqueta. El JAR del `pom` actual es un
artefacto Maven normal (no autocontenido): el `pom` no configura Shade,
Assembly ni JAR ejecutable con dependencias.

**Opción B — IntelliJ IDEA:** `File → Project Structure → Artifacts` → JAR →
*From modules with dependencies* → clase principal `Main` → `Build → Build
Artifacts`. El archivo queda en `out/artifacts/...` y se ejecuta con:

```
java -jar contacto_3xtr4t3rr3str3D.jar
```

Para un ejecutable autocontenido haría falta configuración adicional (p. ej.
jpackage); ese límite está documentado para no presentar el JAR delgado como
aplicación de doble clic.

---

## 6. Arquitectura general del sistema

Tres capas: gramáticas ANTLR, frontend (Swing) y backend (motor de análisis y
generación).

### 6.1 Presentación — `com.compi.frontend`

- `MainWindow` compone la ventana, cablea acciones y pide compilaciones;
  `TopToolbar` agrupa acciones y controles (incluida la casilla **RISC-V**);
  `FileTreePanel` gestiona la raíz, filtro y árbol; `EditorTabs` administra
  `EditorPanel` por pestaña; `ConsoleDock` presenta las ocho áreas de salida
  (Consola, Tripletes, Cuartetas, Código 3D, Código C, **Ensamblador**,
  **Simulador**, Resumen); `ToolWindow`/`ToolWindowDock` agrupan AST, símbolos,
  pila y errores;
  `StatusBar` refleja posición, lenguaje, resultado y contadores.
  `SyntaxHighlighter` y `LineNumberComponent` apoyan la edición.

### 6.2 Fachada y selección de lenguaje

`Compiler` es la fachada central. `LanguageCompilerFactory` asocia
extensiones con `PigLatinCompiler`, `YCompiler` y `ZetarianoCompiler`, que
implementan `LanguageCompiler` vía `AbstractLanguageCompiler`. Esta frontera
encapsula parseo, imports y builders por lenguaje.

### 6.3 Análisis, semántica y generación

ANTLR produce el parse tree; cada `*AstBuilder` construye el **AST propio**
(`com.compi.backend.ast`). La semántica (`AstSemanticAnalyzer`,
`AstExpressionAnalyzer`, `AstDefinitionAnalyzer`, `AstDeclarationAnalyzer` y
`TypeCompatibility`) alimenta `SymbolTable`/`TypeTable` y produce
`CompilationError`. `AstCodeGenerator` emite operaciones a
`C3DGenerator`/`IntermediateCodeManager`; las cuartetas sirven de base para
tripletas, **C** (`CCodeEmitter` → `FlatEmitter`) y **RISC-V**
(`RiscvAssembler`). `ParseTrace` produce pasos para el panel Pila y
`ASTDotGenerator` el DOT de la vista AST.

### 6.4 Errores y runtime de análisis

`AntlrErrorCollector` adapta errores de lexer/parser a `CompilationError` con
categoría y posición. `Stack`, `StackSimulator` y `StackState` representan la
pila de procesos mostrada por el frontend (simulación del compilador, no una
máquina virtual de ejecución).

### 6.5 Flujo de datos

```
fuente / texto del editor
  → LanguageCompilerFactory → *AstBuilder → AST propio
  → AstSemanticAnalyzer (tabla de símbolos, tipos, errores)
  → AstCodeGenerator → cuartetas (C3D) → tripletas
  → CCodeEmitter/FlatEmitter → C
  → RiscvAssembler → ensamblador RISC-V (RARS)
  → vistas (AST, símbolos, pila, consolas, resumen)
```

---

## 7. Gramáticas de los tres lenguajes

Definidas en `contacto-3xtrat3rr3str3D/src/main/antlr4/com/compi/`:

- **`PigLatin.g4`**: `import` con rutas de puntos; sección `VARIABILES >`
  (declaraciones `esto`, arreglos `series`); bloque `MAIOR > ... FINIS;`.
  Expresiones con precedencia por niveles (`expresion` → `producto` → `termino`),
  condiciones con `&&`, `||` y `non`, sentencias de impresión `>>`, lectura
  `<<`, `si`/`aliter`, ciclos `dum`, `facere ... dum`, `per`, saltos
  `perge`/`interrumpe` y llamadas a funciones. **Novedades Proyecto 2**: token
  `null`; declaración de objeto `esto o : novus C(args) : Tipo;` y
  `esto o : null : Tipo;`.
- **`Y.g4`**: sintaxis por indentación (tokens `INDENT`/`DEDENT`); secciones
  `%estructuras` y `%funciones`; `estructura` y `definir nombre(pars) -> tipo :`
  con `retornar`, `si`/`entonces`/`sino`, `elegir`/`caso`/`siempre`,
  ciclos `para`, `mientras`, `hacer`, `romper`/`continuar`, `imprimir`,
  `leer`.
- **`Zetariano.g4`**: clases `(public)? class Nombre (extends Padre)? { ... }`
  con atributos, constructores y métodos; **novedades Proyecto 2**: tokens
  `public`/`private`/`protected`, `extends`, `@Override` (anotación), `this`,
  `new`, `null`, sentencias `switch`/`case`/`default`, `do`, y E/S
  `print`/`println`/`readln`.

La lista completa de palabras reservadas y símbolos por lenguaje está en
`Documentación/Especificacion-palabras-reservadas-simbolos-gramatica.md`.

---

## 8. Semántica y código intermedio

La semántica es compartida por los tres lenguajes (mismo AST):

- **Encapsulamiento** (Zetariano): `private` solo desde la clase que declara;
  `protected` desde esa clase o subclases; `default`/`public` según el caso.
- **Herencia**: `parentClass` y cadena de superclases; `getMember` desciende por
  ancestros; `@Override` obligatorio para sobrescribir; campos del ancestro
  primero y la subclase después (`inheritedFieldCount`).
- **Despacho virtual**: cada clase recibe una marca de tipo única guardada en
  `otype[base]` (`TYPE_SET`/`TYPE_GET`); al llamar un método polimórfico se
  bifurca por marca a la etiqueta concreta. La llamada estática se conserva
  cuando todas las clases resuelven el mismo método.
- **`this`**: variable `this` del tipo de la clase; identificadores no resueltos
  caen a campos heredados sin calificar.
- **PigLatin**: objetos con tipo explícito (`novus C(...) : Tipo`), objetos
  `null : Tipo`, y compatibilidad subclase→superclase en asignación.
- **Generación C3D**: el AST → cuartetas (`C3DGenerator`) con el modelo plano:
  `stack`/`heap`/`otype` en celdas `double`, escalares `P`/`H`/`RET`/`RA`,
  frames por invocación y despachador por retorno.

---

## 9. Back-end de ensamblador RISC-V

Módulo nuevo (`com.compi.backend.riscv`, 12 clases) que consume las mismas
cuartetas que `FlatEmitter` y produce un `.s` funcional para **RARS**
(`RV32IMF/D`). No reutiliza los emisores de C. Además de generarlo, el módulo
lo **ensambla y ejecuta internamente** (sección 9.6) sin salir del IDE.

### 9.1 Clases

| Clase | Responsabilidad |
|---|---|
| `RiscvAssembler` | Fachada `generateAssembly(cuartetas, tabla)` → texto `.s` |
| `RiscvTranslator` | Orquesta: `main`, cuerpo, funciones y secciones |
| `RiscvLayout` | Clasifica la IR: cuerpos, slots, frames, globales |
| `RiscvConstants` | Piscina de constantes `.double` inicializadas en `.data` |
| `RiscvContext` | Estado por compilación (escritor, layout, pila de expresión) |
| `RiscvAccess` | Helpers: variables, frames, constantes, heap, concat |
| `RiscvFlow` | Saltos, `CALL`/`RETURN`, despacho por `RA`, fin de programa |
| `RiscvArithmetic` | Asignaciones, operaciones y concatenación |
| `RiscvMemory` | `HEAP_GET/SET` y `ARRAY_GET/SET` (completitud) |
| `RiscvRuntime` | Rutinas inline: imprimir, leer, `number_to_text` |
| `RiscvWriter` | Acumula las líneas del ensamblador |
| `RiscvRunner` | Ensambla y ejecuta el `.s` con RARS embebido (`rars.api`) |

Nota de compatibilidad RARS: las constantes se emiten dentro de `.data` porque
RARS no reconoce `.rodata`, y las ramas condicionales del lenguaje se emiten
como «condición invertida hacia una etiqueta local + `j` incondicional» porque
RARS marca como error fatal las `B_FORMAT` con objetivo a más de ±4096 bytes y
no las relaja. Así, cualquier `.s` generado (aun con funciones o ciclos muy
grandes) ensambla en el simulador interno.

### 9.2 Modelo de datos (espejo del plano)

`stack`/`heap`/`otype` (800 000 celdas `double` cada uno), escalares
`H`/`P`/`RET`/`RA`, frames por invocación, argumentos en `P+2+i`, marca de
retorno en `P+0` y `P` previo en `P+1`; despacho `L_dispatch` por `(int)RA`;
cadenas en heap terminadas en `0`; `null = -1.0`.

### 9.3 Syscalls (RARS)

| Servicio | `a7` | Argumentos |
|---|---|---|
| Imprimir entero | 1 | `a0` |
| Imprimir doble | 3 | `fa0` |
| Imprimir cadena | 4 | `a0` |
| Leer entero | 5 | `a0` |
| Leer doble | 7 | `fa0` |
| Leer cadena | 8 | `a0`, `a1` |
| Imprimir carácter | 11 | `a0` |
| Leer carácter | 12 | `a0` |
| Salir | 10 | — |

### 9.4 Registros

Scratch `t0..t2`/`ft0..ft1` en los helpers; estado de rutinas en `s1..s6`,
`ft2..ft7`, `fa1`/`fa2`; `fa0` como registro de valor; `a0`/`a7` para `ecall`;
`ra` libre (rutinas inline, sin `jal`).

### 9.5 Runtime inline

`print_string` (carácter a carácter hasta `0`/`-1`), `print_char`, `read_text`
(buffer temporal + recorte de `\n`), `read_char`, `number_to_text` (dígitos
enteros, `.` y hasta 12 fracciones). Todo inline para dejar `ra` libre.

### 9.6 Ejecución interna (`RiscvRunner`)

`com.compi.backend.riscv.RiscvRunner` es el puente hacia el simulador RARS
embebido (`lib/rars1_6.jar`, paquete público `rars.api`):

1. Crea un `rars.api.Program` nuevo por ejecución con `Options`
   (`startAtMain = true` — arranca en `main:` —, `pseudo = true` y
   `maxSteps = 20 000 000` para acotar lazos infinitos).
2. `assembleString(ensamblador)` ensambla desde el texto; los errores se
   convierten en un diagnóstico conciso («Línea N: mensaje») en `Result`.
3. `setup(args, stdin)` inyecta la entrada del usuario (el campo **Entrada** de
   la vista Simulador) y `simulate()` corre hasta terminar.
4. `Result(ok, stdout, stderr, reason, exitCode, elapsedMillis, message)`
   captura la salida con `getSTDOUT()/getSTDERR()` y la razón de terminación
   (`NORMAL_TERMINATION`, `MAX_STEPS`...).

Solo puede haber un `Program` en `setup` a la vez; por eso cada ejecución crea
uno nuevo y `CompilationActions` impide lanzamientos simultáneos (corre sobre
un `SwingWorker` para no bloquear la interfaz). RARS se distribuye con licencia
MIT y no está en Maven Central, de ahí el `systemPath` al jar versionado.

---

## 10. Integración con el IDE

- `Compiler`: genera el ensamblador en `publishIntermediate` (junto al C),
  `getRiscvCode()`, `setRiscvEnabled(boolean)`/`isRiscvEnabled()` y limpieza en
  `reset`. La generación es independiente del modo Tripletes/Cuartetas.
- `ConsoleDock`: vistas **Ensamblador** (`View.ASSEMBLY`) y **Simulador**
  (`View.RUN`, con salida de ejecución y campo de entrada) entre "Código C" y
  "Resumen"; `setRunOutput`/`appendRunOutput`/`getRunInput` para el ciclo de
  ejecución.
- `CompilationActions`: publica `riscvCode` en la vista y escribe
  `NombreDelArchivo.s` en la raíz del proyecto junto al `.c`; `runRiscv()`
  lanza `RiscvRunner` en un `SwingWorker`, selecciona la vista Simulador y
  publica la salida, la razón de terminación y el estado en la barra inferior.
- `TopToolbar`/`MainWindow`: casilla **RISC-V** y botón **Ejecutar** en la
  barra superior (activados por defecto, con atajo `Ctrl+R`), cableados desde
  el constructor de `MainWindow` (sin tocar `initComponents`).

---

## 11. Compatibilidad de tipos

La política de conversiones para los tres lenguajes está en
`com.compi.backend.symbols.TypeCompatibility` y `Type.isAssignableTo` (con la
regla de subtipos `assignable` en `AstSemanticAnalyzer`): `+` con una cadena
convierte a cadena (concatenación); numéricos producen `DOUBLE` si hay un
`double` y `INT` en otro caso; relacionales y lógicos devuelven `BOOLEAN`;
asignación admite `int→double`, `null→clase/arreglo/estructura`, arreglos de
misma forma y `subclase→superclase`. La tabla completa está en
`Documentación/Tabla-de-compatibilidad-de-tipos.md`.

---

## 12. Diagrama de clases

`Documentación/Diagrama-clases.wsd` (fuente PlantUML) y
`Documentación/Diagrama-clases/Diagrama-clases.svg` (render). Incluye los
paquetes `com.compi.frontend`, `com.compi.backend` (AST, `c3d`, `languages`,
`riscv`, `symbols`, errores y runtime) y las relaciones
`Compiler ..> RiscvAssembler` y la composición interna del módulo RISC-V.
Regenerar el SVG: `plantuml -tsvg Diagrama-clases.wsd`.

---

## 13. Verificación y pruebas

- Suite de pruebas con JUnit 5 (`mvn clean verify`), incluidas regresiones de
  ejecución de C desde los `archivos_calificacion`.
- `RiscvAssemblyTest`: estructura del `.s` (`.globl main`, `stack/heap/otype`,
  `ecall`, `L_dispatch`, `L_ret_*`, `fadd.d`/`fdiv.d`, rutinas inline),
  determinismo y conmutador de habilitación.
- `RiscvExecutionTest`: oráculo de ejecución interna — el `.s` de Minimo corre
  dentro del simulador embebido y produce `Hola Mundo`, un programa con lectura
  (`a << b <<`) consume la entrada y suma, la Calculadora completa (imports y
  funciones) ensambla, ejecuta y termina normalmente, y los casos de error
  (instrucción inválida, ensamblador vacío) se reportan con diagnóstico.
- Control de tamaño: `contacto-3xtrat3rr3str3D/tools/check_java_size.py`
  (máximo 600 líneas por clase y 50 por método/constructor).