# Proyecto 2 — Incremento 4: ejecución interna del ensamblador

## Indicación del curso (12/10/2026)

Se indicó hoy que el programa debe **compilar y ejecutar su ensamblador de
forma interna**, sin que el usuario tenga que hacerlo en la terminal ni con
herramientas externas. Disponer de un simulador preparado (Venus u otro) queda
como **comprobación externa opcional** de que el ensamblador generado sí
ejecuta; no es el mecanismo de ejecución del IDE.

## Estado actual (lo que ya se tiene)

- Módulo `com.compi.backend.riscv` completo (11 clases): consume las mismas
  cuartetas que `FlatEmitter` y emite un `.s` autocontenido para **RARS**
  (`RV32IMF/D`).
- El `.s` ya es compatible con RARS: comienza en `main`, convención `ecall`
  con servicio en `a7` (1, 3, 4, 5, 7, 8, 10, 11, 12), rutinas de runtime
  inline (`print_string`, `print_char`, `read_text`, `read_char`,
  `number_to_text`), constantes en `.rodata` con `.double` y
  `stack`/`heap`/`otype` en `.data`.
- El IDE ya publica el ensamblador: casilla **RISC-V** en la barra superior,
  pestaña **Ensamblador** en `ConsoleDock` y `CompilationActions` escribe
  `Nombre.s` en la raíz del proyecto junto al `.c`.
- `RiscvAssemblyTest` verifica estructura, determinismo y conmutador de
  habilitación.

Falta exactamente lo pedido hoy: **ejecutar ese ensamblador dentro del IDE**.

## Objetivo

Añadir un submódulo de ejecución que ensamble y ejecute el RISC-V generado sin
salir del IDE: botón **Ejecutar**, consola de salida y de entrada integrada,
con las mismas garantías del resto (clases ≤ 600 líneas, métodos ≤ 50, no tocar
`initComponents`, explicaciones en español).

## Flujo del usuario (equivalente a «Run» de IntelliJ/NetBeans)

La experiencia buscada es la del botón **Run** de los IDEs de propósito general,
pero dentro de nuestra aplicación:

1. **Compilar** (ya existe): la casilla **RISC-V** publica el `.s` en la
   pestaña **Ensamblador** y lo escribe en la raíz del proyecto. Es la *vista*
   del código, igual que hoy la pestaña **Código C** muestra el `.c`.
2. **Ejecutar** (nuevo, este incremento): al pulsar **Ejecutar**, el IDE
   ensambla el `.s` **automáticamente con el RARS embebido** (equivalente
   interno al `gcc` del sistema; no hay paso manual en la terminal) y lo
   ejecuta en ese simulador embebido. La salida del programa aparece en una
   **consola integrada** del IDE, y las lecturas (`<<`, `leer`, `readln`,
   `ReadInt`, `ReadString`…) se escriben en esa misma consola.

Diferencia con la ruta de C actual: para C el IDE solo *genera* el `.c` y quien
quiere ejecutarlo usa `gcc` en la terminal del sistema operativo. Para RISC-V
ese paso externo **desaparece**: compilar y ejecutar ocurren dentro del IDE al
pulsar Ejecutar. Abrir el `.s` en RARS o Venus de forma manual queda solo como
comprobación externa opcional, no como mecanismo de ejecución.

## Opciones analizadas

### A. Empotrar RARS como librería (recomendada)

RARS está escrito en Java y expone la API necesaria. Verificado con el jar
oficial **v1.6** (última release, repositorio `TheThirdOne/rars`):

- `rars.api.Program`:
  - `assemble(String)` (archivo) y `assembleString(String)` (texto) → `ErrorList`.
  - `setup(ArrayList<String> args, String stdin)` — inyecta la entrada del
    programa; los `ecall` de lectura la consumen.
  - `simulate()` → `rars.simulator.Simulator.Reason` (`NORMAL_TERMINATION`,
    `MAX_STEPS`, `BREAKPOINT`, `CLIFF_TERMINATION`).
  - `getSTDOUT()`, `getSTDERR()`, `getExitCode()`, `getRegisterValue(String)`,
    `getMemory()`.
- `rars.api.Options`: `startAtMain` (entrar en `main`, tal como ya genera el
  `.s`), `maxSteps` (límite contra lazos infinitos), `pseudo`,
  `selfModifyingCode`, `warningsAreErrors`.
- `rars.util.SystemIO.Data(InputStream, OutputStream, OutputStream)` +
  `SystemIO.swapData` permiten controlar entrada/salida; `Program.setup` ya
  captura ambas por defecto (patrón documentado: assemble → setup → simulate →
  recoger salida → repetir si no terminó).

Ventajas: fidelidad total (es el mismo simulador de referencia de la
calificación), cero código de interpretación, entrada programable, límite de
pasos y lectura de registros/memoria para depurar desde el IDE.

Inconvenientes y mitigaciones:

- RARS **no está en Maven Central** → versionar `lib/rars1_6.jar` en el
  repositorio y declararlo con dependencia `system` scope (`systemPath`),
  documentando origen y licencia **MIT** (confirmada en `License.txt` de RARS).
- Solo una instancia `Program` en `setup` está activa a la vez → crear una
  instancia por ejecución, sin paralelismo (el patrón de la API así lo dice).
- El jar pesa ~1,86 MB; ninguna clase de la GUI se invoca usando `rars.api`.

### B. Venus (descartada para embebido)

Venus es un emulador en TypeScript (web). No hay puerto Java oficial; integrarlo
al Swing exigiría un puente o reescritura. Su lugar es la **verificación
externa opcional** (abrir el `.s` en venus.cs61c.org o con Node), nunca el
mecanismo interno del IDE.

### C. Simulador propio mínimo (alternativa sin dependencias)

Interpretar solo el subconjunto RV32IMF/D emitido (accesos a `.data`,
aritmética de dobles con `fadd.d`/`fdiv.d`, `ecall`). Sin dependencias, pero
con costo alto y riesgo de divergencia con RARS, que es el simulador de
referencia. Se desestima mientras se pueda empotrar RARS.

## Decisión

**Empotrar RARS 1.6** en el repositorio y construir un submódulo de ejecución
pequeño. RARS/Venus externos quedan documentados como comprobación opcional.

## Pasos planificados

1. **Dependencia**: copiar `rars1_6.jar` a `contacto-3xtrat3rr3str3D/lib/`,
   declararlo en el `pom.xml` con `system` scope y `systemPath`, agregando un
   comentario con origen y licencia MIT. (Ya descargado y verificado en
   `/tmp/opencode/rars.jar`.)
2. **`RiscvRunner`** (backend, una clase corta): dado el texto del `.s`,
   crear `Program` con `Options` (`startAtMain=true`, `maxSteps` alto),
   `assembleString`, `setup` con la entrada del usuario, `simulate` en un hilo
   con timeout, y devolver salida/entrada/`Reason` o el primer error de
   ensamblaje. Sin tocar `initComponents` (métodos nuevos llamados desde el
   constructor).
3. **Prueba unitaria** `RiscvExecutionTest`: ejecutar los `.s` de
   `archivos_calificacion` (y `Minimo.s`) y comparar la salida contra la
   esperada, con el mismo oráculo que las pruebas de C; confirmar que el
   programa termina con el servicio 10 de `ecall`.
4. **Integración IDE**: botón **Ejecutar** en `TopToolbar` y una consola de
   ejecución en `ConsoleDock` que muestre la salida y capture la entrada de los
   programas interactivos (lecturas `<<`, `leer`, `readln`, `ReadInt`,
   `ReadString`…).
5. **Regresión completa**: `mvn clean verify` verde y `check_java_size.py` OK.
6. **Documentación**: actualizar el Manual de usuario (sección Ejecutar y
   atajos), el Manual técnico (submódulo de ejecución y dependencia `lib/`),
   `Fase-2-riscv.md` y el diagrama de clases si la nueva clase lo amerita;
   nota de licencia del jar.

## Criterios de aceptación

- Compilar y ejecutar el ensamblador **sin salir del IDE** (sin terminal ni
  herramienta externa), incluidos programas con lectura de entrada.
- Salida idéntica a la del programa C equivalente para los casos de
  `archivos_calificacion`.
- Lazo infinito acotado por `maxSteps`/timeout con mensaje claro.
- `mvn clean verify` y `check_java_size.py` OK; `initComponents` intacto.
- Verificación externa opcional documentada (RARS abierto manualmente o Venus).