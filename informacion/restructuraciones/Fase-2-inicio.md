# Proyecto 2 — Inicio de la fase 2

## Alcance

Aumentar el compilador del Proyecto 1 para cubrir el Proyecto 2 *St4ck Over-Pig*.
Todo el trabajo es aditivo: no se elimina ni se desactiva ninguno de los módulos
existentes de C3D y emisión de C.

El Proyecto 2 exige:

- Traducir las tripletas/cuadruplas a ensamblador RISC-V funcional.
- Zetariano con encapsulamiento, herencia, polimorfismo (`@Override`) y `this`.
- PigLatin con objetos de tipo explícito (`novus C(...) : Tipo`) y objetos `null : Tipo`.

## Incremento 1 — Gramática y AST

### Zetariano

- Nuevos tokens: `private`, `protected`, `extends`, `@Override`, `this`.
- `claseDef` admite superclase: `class Hija extends Padre { ... }`.
- Atributos, métodos y constructores admiten `public | private | protected`.
- Los métodos admiten la anotación obligatoria `@Override`.
- Nueva expresión `this` y acceso a miembros desde `this`.
- La regla `this.nombre` se reconoce como acceso a miembro, de modo que
  `this.nombre = valor;` sigue siendo una asignación válida.

### PigLatin

- Nuevo token `null`.
- Declaración con tipo explícito de objeto: `esto o : novus C(...) : Tipo;`
- Declaración de objeto nulo: `esto o : null : Tipo;`
- `null` como término de expresión.

### AST

- `AccessModifier`: `PUBLIC`, `PRIVATE`, `PROTECTED`, `DEFAULT` con
  `from(String)`.
- `ClassDefinitionNodeAST` guarda `parentName` (superclase) y conserva los
  constructores anteriores.
- `MethodDefinitionNodeAST` guarda `access` y `override`.
- `ConstructorDefinitionNodeAST` guarda `access`.
- `AttributeDefinitionNodeAST` guarda `access`.

Los constructores previos siguen existiendo y delegan en los nuevos con valores
por defecto, por lo que no cambia el comportamiento del código existente.

### Interfaces

- `ASTNode`, `ASTVisitor` y `LanguageCompiler` declaran `public` explícito en
  todos sus métodos, incluidos los `default`.

## Verificación de este incremento

- `mvn -q -DskipTests compile` correcto.
- `mvn -q -Dtest=CompilerRegressionTest test` correcto; los 27 casos siguen
  pasando, incluido `Empleado.z`, que usa `this.nombre`.
- `python3 tools/check_java_size.py`: 137 fuentes dentro de límites.

## Pendiente (siguientes incrementos)

Estado al cierre del incremento 3: los seis pendientes originales están
completos (cada uno en su registro: `Fase-2-semantica-y-despacho.md` para los
incrementos 1–2 y `Fase-2-riscv.md` para el 3).

Por indicación del curso (12/10/2026), el cronograma continúa con un
**incremento 4**:

7. **Ejecución interna del ensamblador**: el programa debe compilar y ejecutar
   su ensamblador RISC-V dentro del IDE, sin terminal ni herramientas externas.
   El simulador externo (RARS/Venus) queda solo como comprobación opcional.

La planificación detallada (opciones analizadas, decisión de empotrar RARS 1.6,
pasos y criterios de aceptación) está en
`informacion/restructuraciones/Fase-2-ejecucion-interna.md`.

La emisión de C del Proyecto 1 se mantiene operativa durante el incremento 4.
