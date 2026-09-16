grammar Zetariano;

programa: claseDef+;

claseDef: PUBLIC? CLASS ID LBRACE miembroClase* RBRACE;

miembroClase: atributoDef
                | constructorDef
                | metodoDef
                ;

atributoDef: tipoDato ID (ASSIGN expresion)? SEMICOLON;

constructorDef: PUBLIC? ID LPAREN parametros? RPAREN bloque;

metodoDef: PUBLIC? tipoDato ID LPAREN parametros? RPAREN bloque;

parametros: parametro (COMMA parametro)*;
parametro: tipoDato ID;

bloque: LBRACE sentencia* RBRACE
          | sentencia
          ;

sentencia: declaracionVariable
         | asignacion
         | sentenciaIf
         | sentenciaSwitch
         | sentenciaFor
         | sentenciaWhile
         | sentenciaDoWhile
         | retorno
         | romper
         | continuar
         | llamadaFuncionSemilla SEMICOLON?
         | incrementoDecremento SEMICOLON
         | SEMICOLON
         ;

declaracionVariable: tipoDato ID (LBRACK RBRACK)* (ASSIGN expresion)? SEMICOLON
                    | tipoDato LBRACK RBRACK (LBRACK RBRACK)* ID (ASSIGN expresion)? SEMICOLON
                    ;

asignacion: (ID | accesoMiembro) (ASSIGN | PLUS_ASSIGN | MINUS_ASSIGN | MUL_ASSIGN) expresion SEMICOLON
          | ID LBRACK expresion RBRACK (LBRACK expresion RBRACK)* ASSIGN expresion SEMICOLON
          ;

incrementoDecremento: (ID | accesoMiembro) (INCREMENT | DECREMENT);

sentenciaIf: IF LPAREN expresion RPAREN bloque (ELSE IF LPAREN expresion RPAREN bloque)* (ELSE bloque)?;

sentenciaSwitch: SWITCH LPAREN expresion RPAREN LBRACE casoSwitch* defectoSwitch? RBRACE;

casoSwitch: CASE expresion COLON sentencia* (BREAK SEMICOLON)?;
defectoSwitch: DEFAULT COLON sentencia* (BREAK SEMICOLON)?;

sentenciaFor: FOR LPAREN forInit? SEMICOLON expresion? SEMICOLON forUpdate? RPAREN bloque;
forInit: declaracionVariable | asignacionExpresion;
asignacionExpresion: (ID | accesoMiembro) (ASSIGN | PLUS_ASSIGN | MINUS_ASSIGN | MUL_ASSIGN) expresion;
forUpdate: incrementoDecremento | asignacionExpresion;

sentenciaWhile: WHILE LPAREN expresion RPAREN bloque;

sentenciaDoWhile: DO bloque WHILE LPAREN expresion RPAREN SEMICOLON;

retorno: RETURN expresion? SEMICOLON;
romper: BREAK SEMICOLON;
continuar: CONTINUE SEMICOLON;

llamadaFuncionSemilla: PRINTLN LPAREN argumentos? RPAREN
                     | PRINT LPAREN argumentos? RPAREN
                     | READLN LPAREN RPAREN
                     | ID LPAREN argumentos? RPAREN
                     | accesoMiembro LPAREN argumentos? RPAREN
                     ;

argumentos: expresion (COMMA expresion)*;

expresion: expresion TernaryOp expresion COLON expresion # TernaryExpr
         | expresion (MUL | DIV | MOD) expresion # MulDivModExpr
         | expresion (PLUS | MINUS) expresion # AddSubExpr
         | expresion (LT | GT | LE | GE) expresion # RelationalExpr
         | expresion (EQUALS | NOTEQUALS) expresion # EqualityExpr
         | expresion AND expresion # AndExpr
         | expresion OR expresion # OrExpr
         | NOT expresion # NotExpr
         | INCREMENTO_PRE=(INCREMENT | DECREMENT) (ID | accesoMiembro) # PreIncDecExpr
         | NEW ID LPAREN argumentos? RPAREN # NewObjectExpr
         | NEW tipoDato LBRACK expresion RBRACK (LBRACK expresion RBRACK)* # NewArrayExpr
         | LBRACE expresion (COMMA expresion)* RBRACE # ArrayLiteralExpr
         | NULL # NullExpr
         | ID # IdExpr
         | accesoMiembro # MemberAccessExpr
         | LITERAL_ENTERO # IntLiteralExpr
         | LITERAL_DECIMAL # DoubleLiteralExpr
         | CADENA_TEXTO # StringLiteralExpr
         | CARACTER # CharLiteralExpr
         | TRUE # TrueExpr
         | FALSE # FalseExpr
         | LPAREN expresion RPAREN # ParenExpr
         | llamadaFuncionSemilla # FunctionCallExpr
         ;

accesoMiembro: ID miembroAcceso+;

miembroAcceso: DOT ID (LBRACK expresion RBRACK)*
             | LBRACK expresion RBRACK (LBRACK expresion RBRACK)*
             ;

tipoDato: INT_TYPE
        | DOUBLE_TYPE
        | CHAR_TYPE
        | BOOLEAN_TYPE
        | STRING_TYPE
        | VOID
        | ID
        | tipoDato LBRACK RBRACK
        ;

// LEXER
PUBLIC: 'public';
CLASS: 'class';
VOID: 'void';
NEW: 'new';
NULL: 'null';
IF: 'if';
ELSE: 'else';
SWITCH: 'switch';
CASE: 'case';
DEFAULT: 'default';
BREAK: 'break';
CONTINUE: 'continue';
FOR: 'for';
WHILE: 'while';
DO: 'do';
RETURN: 'return';

PRINTLN: 'println';
PRINT: 'print';
READLN: 'readln';

INT_TYPE: 'int';
DOUBLE_TYPE: 'double';
CHAR_TYPE: 'char';
BOOLEAN_TYPE: 'boolean';
STRING_TYPE: 'String';

TRUE: 'true';
FALSE: 'false';

TernaryOp: '?';
COLON: ':';
SEMICOLON: ';';
COMMA: ',';
ASSIGN: '=';
PLUS_ASSIGN: '+=';
MINUS_ASSIGN: '-=';
MUL_ASSIGN: '*=';
DOT: '.';

LPAREN: '(';
RPAREN: ')';
LBRACK: '[';
RBRACK: ']';
LBRACE: '{';
RBRACE: '}';

PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
MOD: '%';

INCREMENT: '++';
DECREMENT: '--';

EQUALS: '==';
NOTEQUALS: '!=';
LE: '<=';
GE: '>=';
LT: '<';
GT: '>';

AND: '&&';
OR: '||';
NOT: '!';

ID: [a-zA-Z_] [a-zA-Z0-9_]*;
LITERAL_ENTERO: [0-9]+;
LITERAL_DECIMAL: [0-9]+ '.' [0-9]+;
CADENA_TEXTO: '"' (~["\r\n])* '"';
CARACTER: '\'' . '\'';

WS: [ \t\r\n]+ -> skip;
COMENTARIO_LINEA: '//' ~[\r\n]* -> skip;
COMENTARIO_BLOQUE: '/*' .*? '*/' -> channel(HIDDEN);