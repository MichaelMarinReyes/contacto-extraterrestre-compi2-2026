# Proyecto 2 — Fase 2, incremento 3: módulo de ensamblador RISC-V

Este documento registra el tercer incremento de la fase 2: la traducción de la IR
(cuartetas/tripletas) a **ensamblador RISC-V** como módulo nuevo, completamente
independiente de los emisores de C del Proyecto 1. El Proyecto 1 no se toca: la
gramática, el AST, la semántica y la emisión de C siguen funcionando y todas las
regresiones previas siguen pasando.

## Objetivo y decisiones de diseño

El enunciado del Proyecto 2 no fija un simulador concreto, así que la elección es
del equipo. Se eligió **RARS (RISC-V Assembler and Runtime Simulator)**, que
soporta las extensiones `RV32IMF/D` (enteros con multiplicación/división y dobles
IEEE-754) y su convención de `ecall`:

| Servicio | Número (`a7`) | Argumentos |
|---|---|---|
| Imprimir entero | 1 | `a0` |
| Imprimir doble | 3 | `fa0` |
| Imprimir cadena | 4 | `a0` |
| Leer entero | 5 | `a0` (resultado) |
| Leer doble | 7 | `fa0` (resultado) |
| Leer cadena | 8 | `a0` (buffer), `a1` (longitud) |
| Imprimir carácter | 11 | `a0` |
| Leer carácter | 12 | `a0` (resultado) |
| Salir | 10 | — |

El ensamblador generado comienza en `main:` dentro de `.text`; RARS inicia la
ejecución en `main` por defecto, sin `libc` ni llamadas externas.

El módulo **no reutiliza los emisores de C**: es una traducción desde cero de las
mismas cuartetas que consume `FlatEmitter`. Por esa razón hereda el modelo plano
de la IR (un único `main` con `P` como puntero de frame y despacho por retorno),
que es la semántica que garantiza los `archivos_calificacion`.

## Arquitectura del módulo (`com.compi.backend.riscv`)

| Clase | Líneas | Responsabilidad |
|---|---|---|
| `RiscvAssembler` | 24 | Fachada: `generateAssembly(cuartetas, tabla)` → texto del `.s` |
| `RiscvTranslator` | 122 | Orquesta: `main`, cuerpo del programa, funciones, secciones |
| `RiscvLayout` | 178 | Clasifica la IR: cuerpos por función, slots, frames, globales |
| `RiscvConstants` | 37 | Piscina de constantes numéricas en `.rodata` (`.double`) |
| `RiscvContext` | 29 | Estado por compilación: escritor, layout, constantes, pila de expresión |
| `RiscvAccess` | 133 | Helpers de acceso: variables, frames, constantes, heap, concat |
| `RiscvFlow` | 157 | Saltos, `CALL`/`RETURN`, despacho por `RA`, fin de programa |
| `RiscvArithmetic` | 152 | Asignaciones, operaciones aritméticas y concatenación |
| `RiscvMemory` | 118 | `HEAP_GET/SET` y el subconjunto `ARRAY_GET/SET` por completitud |
| `RiscvRuntime` | 221 | Rutinas de runtime inline: imprimir, leer, convertir número→texto |
| `RiscvWriter` | 26 | Acumula las líneas del ensamblador |

## Modelo de datos (espejo del plano del C)

Se replica fielmente la convención de la IR para que el comportamiento sea
idéntico al del C generado:

- `stack` / `heap` / `otype`: arreglos de 800 000 celdas de 8 bytes (`double`).
- Escalares `H` (puntero de heap), `P` (puntero de frame), `RET` (valor de retorno),
  `RA` (marca de retorno): celdas `double` en `.data`.
- Frames: cada invocación reserva `frame(funcion)` celdas; los parámetros y
  locales van en `P + slot`, los argumentos de llamada en `P + 2 + i` del frame
  actual, la marca de retorno en `P + 0` y el `P` anterior en `P + 1`.
- Despacho: `CALL` guarda `P`, el id de llamada y los argumentos; `j etiqueta` al
  cuerpo; al terminar, `RETURN` restaura `P` y salta a `L_dispatch`, que compara
  `(int)RA` con cada id y salta al `L_ret_N` del punto de retorno.
- `null` es `-1.0`; las cadenas viven en `heap` como códigos de carácter en
  celdas `double`, terminadas en `0`; los índices se convierten con `fcvt.w.d` y
  se multiplican por 8 sobre la base.
- El frontend no emite `ARRAY_GET/SET` (usa `HEAP_GET/SET` con direcciones
  calculadas, p. ej. `heap[(int)stack[(int)P + 3]]`); se implementan igualmente
  por completitud.

## Convención de registros

| Registros | Uso |
|---|---|
| `t0`/`t1`/`t2`, `ft0`/`ft1` | Scratch de los helpers de acceso (direcciones, valores temporales) |
| `s0` | Índice de concatenación en `concat` (no clobberear con helpers) |
| `s1`–`s6`, `ft2`–`ft7`, `fa1`/`fa2` | Estado interno de las rutinas de runtime inline |
| `fa0` | Registro de valor (resultado de operaciones y retornos, `loadConstant` de dobles) |
| `a0`/`a7` | Argumento y servicio de `ecall` |
| `ra` | Libre (todas las rutinas son inline, no hay `jal`) |

Los helpers de acceso usan solo `t0..t2` y `ft0/ft1`, por lo que los `s*` y `f*`
de estado sobreviven entre segmentos de una misma rutina.

## Rutinas de runtime

Todas se emiten **inline** en cada punto de uso (sin llamadas), lo que deja libre
`ra` y evita el enlazado:

- `print_string`: recorre los códigos de carácter hasta `0` o `-1`, uno por uno
  con `PrintChar` (que escribe directo, sin romper caracteres UTF-8).
- `print_char`: un `PrintChar`.
- `read_text`: pide un buffer temporal, lee con `ReadString`, recorta el `\n`
  final y copia los caracteres al heap terminando en `0`.
- `read_char`: un `ReadChar` convertido a `double`.
- `number_to_text`: entero → dígitos decimales; si hay parte fraccionaria,
  emite `.` y hasta 12 fracciones, terminando en `0` cuando ya no queda resto.

## Integración al pipeline y al IDE

- `Compiler`: genera el ensamblador en `publishIntermediate` junto al C, lo
  limpia en `reset`, lo expone con `getRiscvCode()` y permite apagarlo con
  `setRiscvEnabled(boolean)` (la generación de RISC-V es independiente del modo
  Tripletes/Cuartetas).
- `ConsoleDock`: nueva vista **Ensamblador** (`View.ASSEMBLY`) con su área de
  texto, entre "Código C" y "Resumen"; nueva vista **Simulador** (`View.RUN`)
  con salida de ejecución y campo de entrada (`setRunOutput`,
  `appendRunOutput`, `getRunInput`).
- `CompilationActions`: publica el ensamblador en la vista y, si la compilación
  es exitosa, escribe `nombre.s` en la raíz del proyecto junto al `.c` ya
  existente (con el mismo manejo de contenedor actual). `runOrStopRiscv()`
  arranca `runRiscv()`, que lanza el simulador embebido en un `SwingWorker` y
  publica salida/razón/tiempo, o pide detenerlo mientras corre.
- `TopToolbar`/`MainWindow`: casilla **RISC-V** y botón **Ejecutar** (+ atajo
  `Ctrl+R`) en la barra superior; `RiscvRunner` ensambla y ejecuta el `.s` con
  RARS embebido (`rars.api`) y admite parada cooperativa (el botón pasa a
  **Detener** mientras corre, vía `MainWindow.setRunningState`); se cablean
  desde el constructor de `MainWindow` sin tocar `initComponents`.
- Test `RiscvAssemblyTest`: verifica estructura del `.s` (`.globl main`, `main:`,
  `stack/heap/otype`, `ecall`, `L_dispatch`, `L_ret_*`, `fadd.d`/`fdiv.d`,
  `feq.d`, rutinas inline), determinismo de la salida y el conmutador de
  habilitación.
- Test `RiscvExecutionTest`: oráculo de ejecución interna con el mismo RARS
  embebido (Minimo → `Hola Mundo`; lectura con `<<`; Calculadora completa con
  imports y funciones; errores de ensamblaje reportados).

## Uso (manual breve)

1. Abrir un `.pig` en el IDE, marcar la casilla **RISC-V** y compilar.
2. El ensamblador queda visible en la pestaña **Ensamblador** y como
   `NombreDelArchivo.s` en la raíz del proyecto.
3. Escribir la entrada (si la hay) en el campo de la pestaña **Simulador** y
   pulsar **Ejecutar** (o `Ctrl+R`); el IDE ensambla y corre el `.s` con el
   RARS embebido y muestra la salida en esa pestaña. Mientras corre, el mismo
   botón cambia a **Detener** para interrumpirlo conservando la salida parcial.
4. (Opcional) Abrir el `.s` en RARS de escritorio (`File › Open`), *Assemble*
   y *Run* como comprobación externa manual.

## Notas y limitaciones

- El tamaño del `.s` crece con la cantidad de cadenas y conversiones: cada
  cadena se construye carácter a carácter y cada `number_to_text` es inline.
  RARS ensambla programas de ese tamaño sin problema (mucho menores que el
  segmento de texto disponible).
- Para que RARS ensamble **cualquier** programa generado: las constantes se
  emiten en `.data` (RARS no reconoce `.rodata`) y las ramas condicionales se
  emiten como «condición invertida a etiqueta local + `j` incondicional»
  (RARS declara error fatal las `B_FORMAT` a más de ±4096 bytes y no las
  relaja; el `j` cubre ±1 MiB).
- La ejecución interna acota los lazos con `maxSteps = 20 000 000`; si el
  programa no termina se reporta la razón `MAX_STEPS` sin bloquear el IDE (todo
  corre en un `SwingWorker`, con una única instancia `Program` activa a la vez).
- El backend asume dobles en todas las celdas (patrón del Proyecto 1); por eso
  los índices requieren `fcvt.w.d` y los valores pequeños requieren
  `fcvt.d.w`/`fcvt.w.d` al cruzar con enteros.
- No hay jerarquías de clases ni polimorfismo que resolver en el ensamblador: el
  despacho virtual ya quedó resuelto en las cuartetas (etiquetas concretas por
  clase), por lo que el módulo solo traduce `CALL`/`RETURN` planos.

## Verificación

- `mvn -q clean verify` verde, incluyendo las regresiones de ejecución de C y
  las nuevas de ejecución interna del ensamblador.
- `tools/check_java_size.py`: OK, 150 fuentes, máximo 600 líneas por clase y 50
  por método/constructor.
- `RiscvExecutionTest` ejecuta dentro del RARS embebido el `.s` de Minimo, uno
  con lectura (`a <<`, `b <<` → suma) y la Calculadora completa (~31 mil líneas;
  antes fallaba en el paso 16 687 por el rango de rama, hoy ensambla y termina
  con `NORMAL_TERMINATION`).