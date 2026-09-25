
grammar PigLatin;

@header {
    package igriega.piglatin.antlr4;
}

// principal
program : stmt* EOF
    ;

stmt : if_stmt
    | assignation
    | declaration
    | print
    ;

print : PRINT expression SEMICOLON
    ;

if_stmt : SI LPAREN expression RPAREN LBRACE stmt* RBRACE FINIS SEMICOLON
    ;

declaration : ESTO ID COLON type expression SEMICOLON
    ;

assignation : ID '=' expression SEMICOLON
    ;
    
//TODO: investigar como mejorar la expresion
expression
    : ID
    | INT
    | LPAREN expression RPAREN
    | expression '*' expression
    | expression '/' expression
    | expression '+' expression
    | expression '-' expression
    | expression '>' expression
    | expression '<' expression
    | expression '>=' expression
    | expression '<=' expression
    | expression '==' expression
    | expression '!=' expression
    | expression '||' expression
    | expression '&&' expression
    ;

type : NUMERUS
    | DECIMALIS
    ;

NUMERUS : 'numerus';
DECIMALIS: 'decimalis';

SI : 'si';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
FINIS: 'finis';
COLON: ':';
ESTO: 'esto';
SEMICOLON: ';';
ID: [a-zA-Z_][a-zA-Z0-9_]*;
INT: [0-9]+;
PRINT: '>>';


// Ignorar espacios
WS       : [ \t\r\n]+ -> skip;