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

Estado al cierre del incremento 4: los siete pendientes están completos.
El incremento 4 (ejecución interna con RARS embebido: `RiscvRunner`, botón
**Ejecutar**, pestaña **Simulador** y `RiscvExecutionTest`) se documenta en
`Fase-2-ejecucion-interna.md` (decisión, pasos y dos arreglos de compatibilidad
con RARS: constantes en `.data` y ramas «invertida + `j`»). La emisión de C del
Proyecto 1 se mantiene operativa en todo momento.
