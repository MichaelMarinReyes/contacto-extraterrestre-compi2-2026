# Proyecto 1 - Organización de Lenguajes y Compiladores 2
## Contacto-3xtrat3rr3str3D

### Estructura del proyecto

```
com.compi.backend
  c3d
    C3DGenerator
    Quadruple / QuadrupleOp
    CCodeEmitter
  languages
    base.ast
      NodeAST
      Expression
      Statement
      ArithmeticOperation
      RelationalOperation
      LogicalOperation
      FunctionCall
      MemberAccess
      AccessArray
      Literal
      Identifier
      Assignment
    piglatin
      ast
        NodeASTPig
        PigLatinVisitorCustom
        expressions
          ArithmeticOperationPig extends ArithmeticOperationBase
          RelationalOperationPig extends RelationalOperationBase
          LogicalOperationPig extends LogicalOperationBase
          FunctionCallPig extends FunctionCallBase
          MemberAccessPig extends MemberAccessBase
          AccessArrayPig extends AccessArrayBase
          LiteralPig extends LiteralBase
          IdentifierPig extends IdentifierBase
        sentence
          ...
      PigLatinSemanticVisitor
      PigLatinC3DVisitor
    y
      ast
        NodeASTY
        YVisitorCustom
        expressions
          ArithmeticOperationY extends ArithmeticOperationBase
          RelationalOperationY extends RelationalOperationBase
          LogicalOperationY extends LogicalOperationBase
          FunctionCallY extends FunctionCallBase
          LiteralY extends LiteralBase
          IdentifierY extends IdentifierBase
        statements
          ...
      YSemanticVisitor
      YC3DVisitor
    zetariano
      ast
        NodeASTZet
        ZetarianVisitorCustom
        expressions
          ArithmeticOperationZet extends ArithmeticOperationBase
          LiteralZet extends LiteralBase
        statements
          ...
      ZetarianoSemanticVisitor
      ZetarianoC3DVisitor
  symbols
  errors
```

### Flujo correcto de compilación

1. **Análisis Léxico**
   Lexer de ANTLR genera tokens a partir del código fuente `.pig`, `.y` o `.z`.

2. **Análisis Sintáctico**
   Parser de ANTLR genera el árbol de análisis `ParseTree`.

3. **Construcción del AST**
   `*ASTBuilder` recorre el `ParseTree` y construye el Árbol de Sintaxis Abstracta usando las clases de `languages.base.ast` y las especializaciones por lenguaje.

4. **Análisis Semántico**
   Visitor semántico sobre el AST valida tipos, declaración/uso de símbolos, alcances, compatibilidad de tipos. Se llena `SymbolTable`.

5. **Generación de Código Intermedio**
   Visitor de C3D sobre el AST recorre nodos y emite cuartetas mediante `C3DGenerator.emit(op, arg1, arg2, result)`.
   Las cuartetas son `Quadruple(op, arg1, arg2, result)` que representan código de tres direcciones.

6. **Emisión de C**
   `CCodeEmitter` convierte las cuartetas acumuladas en código C ejecutable, gestionando stack y heap.

### Relación AST ↔ Cuartetas

* El AST **no** genera cuartetas directamente.
* El AST es la representación intermedia independiente del parser.
* Un visitor de generación de código intermedio recorre el AST y, por cada nodo, emite una o varias cuartetas al `C3DGenerator`.

### Reutilización

Las clases base en `languages.base.ast` evitan la repetición de nodos comunes:
`ArithmeticOperation`, `Literal`, `Identifier`, `RelationalOperation`, `LogicalOperation`, `FunctionCall`, `MemberAccess`, `AccessArray`, `Assignment`.
Cada lenguaje mantiene solo los nodos específicos de su sintaxis.
