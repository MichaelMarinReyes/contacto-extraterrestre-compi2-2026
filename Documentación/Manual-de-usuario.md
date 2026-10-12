# Manual de Usuario — Contacto 3xtr4t3rr3str3D

**Proyecto 2 — St4ck Over-Pig**

Universidad de San Carlos de Guatemala · Centro Universitario de Occidente ·
Organización de Lenguajes y Compiladores 2 · Segundo semestre 2026

Este manual corresponde a la versión 2 del entorno, que añade la **traducción a
ensamblador RISC-V** sobre los productos del Proyecto 1 (AST, C3D y C).

> Los textos entre corchetes indican el lugar recomendado para insertar una
> captura propia. Sustituya cada marcador `[CAPTURA: ...]` por una imagen de la
> aplicación en el estado descrito y, si lo desea, elimine el texto del marcador.

## Índice

1. Descripción
2. Instalación y requisitos del sistema
3. Conocer la ventana principal
4. Abrir un proyecto
5. Abrir y editar archivos
6. Crear un archivo
7. Guardar el trabajo
8. Compilar y consultar resultados
9. Ejecutar el ensamblador RISC-V en RARS
10. Paneles de herramientas
11. Atajos de teclado
12. Situaciones frecuentes
13. Flujo de trabajo recomendado

---

## 1. Descripción

Contacto 3xtr4t3rr3str3D es una aplicación de escritorio para la creación,
análisis y compilación de programas en tres lenguajes: **PigLatin** (`.pig`),
**Y** (`.y`) y **Zetariano** (`.z`). Permite organizar archivos en un árbol de
proyecto, abrir varias pestañas de edición, guardar cambios, compilar y
consultar representaciones en **AST**, **C3D**, **C** y, desde esta versión,
**ensamblador RISC-V**.

La aplicación determina el lenguaje por la extensión del archivo; no hay un
selector global de lenguaje. Una compilación correcta produce, además de C, un
archivo `.s` (ensamblador RISC-V) listo para ejecutarse en el simulador RARS.

[CAPTURA: ventana principal del IDE con un archivo abierto]

---

## 2. Instalación y requisitos del sistema

Requisitos previos:

- **Software**: Java 22 (JDK 22). Verifíquelo en la terminal:

  ```
  java -version
  ```

- **Simulador RISC-V (opcional, para ejecutar el ensamblador)**: RARS,
  disponible como archivo `.jar` en el sitio oficial del proyecto RARS.

Instalación y ejecución:

1. Descargue el paquete de distribución (el archivo `.jar` ejecutable).
2. Ejecute desde su terminal:

   ```
   java -jar contacto_3xtr4t3rr3str3D.jar
   ```

Se abrirá la interfaz gráfica de la aplicación.

---

## 3. Conocer la ventana principal

[CAPTURA: anotaciones de la ventana principal]

- **Barra superior**: botones de nuevo, abrir y guardar; accesos a AST,
  símbolos, pila y errores; el selector **Generar C** (Cuartetas o Tripletes);
  la casilla **RISC-V** (genera también la traducción a ensamblador); el botón
  Herramientas y el botón **Compilar**.
- **Árbol de proyecto** (izquierda): carpeta raíz y archivos reconocidos.
  Incluye filtro de búsqueda, botón Actualizar y botón para abrir una carpeta
  como proyecto. Doble clic abre un archivo en el editor.
- **Editor central**: una pestaña por archivo, números de línea, resaltado
  visual y posición de cursor. El lenguaje se infiere de la extensión.
- **Consolas inferiores**: pestañas Consola, Tripletes, Cuartetas, Código 3D,
  **Código C**, **Ensamblador** y Resumen. Áreas de solo lectura.
- **Ventana Herramientas**: flotante, con las pestañas Árbol AST, Símbolos,
  Pila y Errores. Se abre desde Herramientas o desde los botones/atajos.
- **Barra de estado**: línea y columna, archivo activo, lenguaje, resultado de
  compilación, conteos de cuartetas/tripletes y cantidad de errores.

Los divisores entre árbol, editor y consolas se pueden arrastrar; algunos tienen
control para contraer o expandir el panel.

---

## 4. Abrir un proyecto

Un proyecto se abre seleccionando una carpeta con los archivos del trabajo:
use **Abrir proyecto** en la pantalla de bienvenida o el botón de carpeta de la
cabecera del árbol. En el selector navegue hasta la carpeta y confirme **Abrir
proyecto**; su nombre aparece en la interfaz y su contenido se carga en el árbol.

En el árbol solo se muestran archivos con extensión reconocida (`.pig`, `.y`,
`.z`); otros tipos se omiten y la aplicación puede informar cuáles no incluyó.
Use **Buscar archivo** para filtrar y **Actualizar** para releer la carpeta.
Doble clic abre o activa la pestaña de un archivo; un solo clic solo selecciona.

---

## 5. Abrir y editar archivos

Seleccione **Abrir archivo** (`Ctrl+O`), localice el archivo y ábralo; el
selector filtra extensiones aceptadas. Si el archivo está fuera del proyecto, la
aplicación puede pedir cómo incorporarlo a la carpeta contenedora.

Cada archivo abierto aparece en una pestaña. Cambie entre archivos con sus
pestañas; cierre la activa con `Ctrl+W`. Al editar se activa el resaltado del
lenguaje y la pestaña se marca como modificada. La barra inferior del editor
muestra línea y columna, nombre del archivo y lenguaje detectado.

---

## 6. Crear un archivo

Pulse **Archivo nuevo** (`Ctrl+N`). Si no hay proyecto abierto, primero
seleccione o cree una carpeta contenedora. Complete los campos del diálogo:

- **Nombre**: sin necesidad de añadir la extensión.
- **Lenguaje**: `.pig`, `.y` o `.z` (define el lenguaje y la extensión).
- **Carpeta**: una carpeta del proyecto u otra ubicación.

La vista previa muestra el nombre final. Pulse **Crear** para abrirlo en una
pestaña nueva; **Cancelar** o `Escape` para salir sin crear.

---

## 7. Guardar el trabajo

Use **Guardar** (`Ctrl+S`) para la pestaña activa y `Ctrl+Mayús+S` para todas.
La compilación puede usar el texto actual del editor aunque no se haya guardado.
Después de guardar se quita el indicador de pestaña modificada.

---

## 8. Compilar y consultar resultados

Pulse **Compilar** (`Ctrl+B`). Con proyecto abierto se compilan los archivos
reconocidos del proyecto (si existe `main.pig` se toma como punto de partida y
los importados se cargan durante el proceso). Sin proyecto, se compilan las
pestañas abiertas con archivo asociado.

Antes de compilar se limpian las vistas de resultados anteriores. La barra de
estado cambia a *Compilación correcta* o *Compilación con errores*. Si hay
errores, la ventana Herramientas se abre en **Errores** y el editor puede saltar
a la línea reportada.

Para consultar las salidas:

- **Consola**: resumen del proceso y mensajes.
- **Tripletes** y **Cuartetas**: representaciones intermedias del archivo de
  enfoque.
- **Código 3D**: código de tres direcciones.
- **Código C**: salida en C según el modo elegido.
- **Ensamblador**: traducción RISC-V del archivo de enfoque (RARS).
- **Resumen**: texto traducido/resumen de la compilación.

La casilla **RISC-V** de la barra superior habilita o deshabilita la generación
de ensamblador: si está marcada, cada compilación correcta rellena la pestaña
**Ensamblador** y escribe `NombreDelArchivo.s` en la raíz del proyecto (junto al
`.c` ya existente). Si la desmarca, la generación vuelve a activarse en la
siguiente compilación cuando se marque otra vez.

[CAPTURA: consolas inferiores con la pestaña Ensamblador seleccionada]

---

## 9. Ejecutar el ensamblador RISC-V en RARS

El archivo `.s` generado es ensamblador RISC-V RV32IMF/D autocontenido, pensado
para **RARS** (RISC-V Assembler and Runtime Simulator):

1. Compile con la casilla **RISC-V** marcada; confirme en la pestaña
   **Ensamblador** que el código se generó y anote la ruta del archivo `.s`
   indicada en la consola.
2. Abra RARS y cargue el archivo: `File → Open`, seleccione `NombreDelArchivo.s`.
3. Pulse **Assemble** (icono con engranaje o `F8`); sin errores, puede ejecutar.
4. Pulse **Run** (icono de ejecutar o `F5`). RARS inicia la ejecución en
   `main:` por defecto y muestra en su consola las salidas del programa
   (`>>` en PigLatin, `imprimir` en Y, `println`/`print` en Zetariano).

Los programas interactivos usan `ecall` de RARS para lecturas (`ReadInt`,
`ReadDouble`, `ReadString`, `ReadChar`); responda las peticiones en la caja de
entrada de RARS cuando el programa se detenga esperando datos.

[CAPTURA: RARS con el programa ensamblado y la consola de salida]

---

## 10. Paneles de herramientas

Abra la ventana flotante mediante **Herramientas** o los atajos `Ctrl+1` a
`Ctrl+4`. Las cuatro vistas son pestañas de esa ventana.

- **Árbol AST** (`Ctrl+1`): árbol de sintaxis abstracta del archivo de enfoque.
- **Símbolos** (`Ctrl+2`): tabla de símbolos del archivo de enfoque, con filtro;
  doble clic intenta llevar el editor a la declaración.
- **Pila** (`Ctrl+3`): pasos/estados del análisis del archivo enfocado.
- **Errores** (`Ctrl+4`): diagnósticos de la compilación del proyecto, con
  archivo y posición; doble clic navega al error.

---

## 11. Atajos de teclado

- `Ctrl+N` archivo nuevo · `Ctrl+O` abrir archivo
- `Ctrl+S` guardar pestaña · `Ctrl+Mayús+S` guardar todas
- `Ctrl+B` compilar · `Ctrl+W` cerrar pestaña
- `Ctrl+1` Árbol AST · `Ctrl+2` Símbolos · `Ctrl+3` Pila · `Ctrl+4` Errores
- `Escape` cancelar diálogo de archivo nuevo

---

## 12. Situaciones frecuentes

- **El árbol está vacío**: confirme que abrió la carpeta correcta, que contiene
  archivos `.pig`, `.y` o `.z` y que no hay filtro activo; pulse **Actualizar**.
- **No hay nada que compilar**: abra una carpeta con fuentes o abra archivos
  guardados en pestañas.
- **La compilación informa errores**: abra Errores, navegue al archivo/línea,
  edite y compile de nuevo.
- **No aparece la pestaña Ensamblador o el `.s` no se escribió**: verifique que
  la casilla **RISC-V** esté marcada, que la compilación fue correcta (sin
  errores) y que consulta la pestaña correcta.
- **RARS no arranca en el programa**: confirme que abrió el `.s` correcto y
  presionó Assemble antes de Run; RARS inicia en `main` por defecto.
- **RARS pide entrada y no avanza**: el programa usa lectura (`<<`, `leer`,
  `readln`); escriba el valor en el cuadro de entrada de RARS.
- **Una salida parece vacía**: ejecutó Compilar y la pestaña consultada depende
  del archivo enfocado; verifique el archivo activo.

---

## 13. Flujo de trabajo recomendado

1. Abra la carpeta del proyecto y revise el árbol.
2. Abra `main.pig` u otro archivo con doble clic; edite y guarde (`Ctrl+S`).
3. Marque **RISC-V** y seleccione el modo **Generar C** deseado.
4. Compile con `Ctrl+B`.
5. Revise Consola y Resumen; consulte Tripletes, Cuartetas, Código 3D, Código C
   o **Ensamblador** según lo que necesite.
6. Si hay errores, corríjalos y vuelva a compilar.
7. Para ver el programa correr, abra el `.s` en **RARS**, Assemble y Run.
8. Use AST, Símbolos y Pila para inspeccionar el análisis del archivo enfocado.