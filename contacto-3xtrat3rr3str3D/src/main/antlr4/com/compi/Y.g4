grammar Y;

tokens {
    INDENT,
    DEDENT
}

@lexer::members {
    private java.util.Stack<Integer> indents = new java.util.Stack<Integer>() {{
        push(0);
    }};
    private int dedentsCount = 0;
    private java.util.Queue<Token> tokenQueue = new java.util.LinkedList<>();

    @Override
    public Token nextToken() {
        if (!tokenQueue.isEmpty()) {
            return tokenQueue.poll();
        }
        Token next = super.nextToken();
        return next;
    }
}

// GRAMÁTICA
init: archivo EOF;

archivo: estructuras_seccion? funciones_seccion;

estructuras_seccion: ESTRUCTURAS_SEC NUEVA_LINEA* estructura_def+;

estructura_def: ESTRUCTURA ID DOS_PUNTOS NUEVA_LINEA INDENT campo_struct+ DEDENT
              | ESTRUCTURA ID DOS_PUNTOS campo_struct*
              | estructura_anidada_o_variable
              ;

estructura_anidada_o_variable: ID ID;

campo_struct: tipo_dato ID (CORCHETE_IZQ NUMERO_ENTERO CORCHETE_DER)* NUEVA_LINEA?
            | ID ID (CORCHETE_IZQ NUMERO_ENTERO CORCHETE_DER)* NUEVA_LINEA?
            | estructura_anidada_o_variable NUEVA_LINEA?
            ;

funciones_seccion: FUNCIONES_SEC NUEVA_LINEA* funcion_def+;

funcion_def: DEFINIR ID PARENTESIS_IZQ parametros? PARENTESIS_DER (FLECHA tipo_dato)? DOS_PUNTOS bloque;

parametros: parametro (COMA parametro)*;

parametro: CORCHETE_IZQ CORCHETE_DER tipo_dato ID
         | LLAVE_IZQ LLAVE_DER tipo_dato ID
         | tipo_dato ID
         ;

bloque: NUEVA_LINEA INDENT sentencia+ DEDENT
      | sentencia
      ;

sentencia: declaracion_variable PUNTO_COMA?
         | asignacion PUNTO_COMA?
         | si_sentencia
         | elegir_sentencia
         | para_sentencia
         | mientras_sentencia
         | hacer_mientras_sentencia
         | retornar_sentencia PUNTO_COMA?
         | romper_sentencia PUNTO_COMA?
         | continuar_sentencia PUNTO_COMA?
         | imprimir_sentencia PUNTO_COMA?
         | llamada_funcion PUNTO_COMA?
         | incremento_decremento PUNTO_COMA?
         | declaracion_estructura_local
         | NUEVA_LINEA
         ;

declaracion_estructura_local: ESTRUCTURA ID DOS_PUNTOS NUEVA_LINEA INDENT campo_struct+ DEDENT;

declaracion_variable: tipo_dato ID (CORCHETE_IZQ NUMERO_ENTERO CORCHETE_DER)* (ASIGNACION expresion_inicializacion)?
                    | ID ID (CORCHETE_IZQ NUMERO_ENTERO CORCHETE_DER)* (ASIGNACION expresion_inicializacion)?
                    | BOOL_TIPO ASIGNACION expresion_inicializacion
                    | BOOL_TIPO
                    ;

expresion_inicializacion: expresion
                        | LLAVE_IZQ elemento_lista (COMA elemento_lista)* LLAVE_DER
                        ;

elemento_lista: expresion | LLAVE_IZQ elemento_lista (COMA elemento_lista)* LLAVE_DER;

asignacion: acceso_miembro ASIGNACION expresion
          | ID ASIGNACION expresion
          | ID CORCHETE_IZQ expresion CORCHETE_DER ASIGNACION expresion
          ;

// CORREGIDO: Soporta tanto arreglos puros (misNumeros[i]) como llamadas a miembros (p1.promedio)
acceso_miembro: ID (CORCHETE_IZQ expresion CORCHETE_DER)* (PUNTO ID (CORCHETE_IZQ expresion CORCHETE_DER)*)+
              | ID (CORCHETE_IZQ expresion CORCHETE_DER)+
              ;

si_sentencia: SI PARENTESIS_IZQ condicion PARENTESIS_DER ENTONCES bloque (sino_si_bloque)* (sino_bloque)? (contrario_bloque)?;

sino_si_bloque: SINO PARENTESIS_IZQ condicion PARENTESIS_DER ENTONCES bloque;

sino_bloque: SINO PARENTESIS_IZQ condicion PARENTESIS_DER ENTONCES bloque
           | SINO ENTONCES bloque
           ;

contrario_bloque: CONTRARIO bloque;

elegir_sentencia: ELEGIR PARENTESIS_IZQ expresion PARENTESIS_DER LLAVE_IZQ NUEVA_LINEA* caso_bloque+ siempre_bloque? LLAVE_DER;

caso_bloque: CASO expresion DOS_PUNTOS bloque_interno_opcional ROMPER PUNTO_COMA? NUEVA_LINEA*;

siempre_bloque: SIEMPRE DOS_PUNTOS bloque_interno_opcional ROMPER PUNTO_COMA? NUEVA_LINEA*;

bloque_interno_opcional: NUEVA_LINEA INDENT sentencia+ DEDENT | sentencia*;

para_sentencia: PARA PARENTESIS_IZQ (declaracion_variable | asignacion) PUNTO_COMA condicion PUNTO_COMA incremento_decremento PARENTESIS_DER DOS_PUNTOS bloque;

mientras_sentencia: MIENTRAS PARENTESIS_IZQ condicion PARENTESIS_DER HACER bloque;

hacer_mientras_sentencia: HACER DOS_PUNTOS bloque MIENTRAS PARENTESIS_IZQ condicion PARENTESIS_DER;

retornar_sentencia: RETORNAR expresion?;

romper_sentencia: ROMPER;

continuar_sentencia: CONTINUAR;

imprimir_sentencia: IMPRIMIR PARENTESIS_IZQ argumentos? PARENTESIS_DER;

llamada_funcion: ID PARENTESIS_IZQ argumentos? PARENTESIS_DER;

argumentos: expresion (COMA expresion)*;

incremento_decremento: (ID | acceso_miembro) (SUMA_ABREVIADA | RESTA_ABREVIADA)
                     | (SUMA_ABREVIADA | RESTA_ABREVIADA) (ID | acceso_miembro)
                     ;

expresion: termino (operador_aritmetico termino)*
         | expresion operador_relacional expresion
         | expresion operador_logico expresion
         | operador_negacion expresion
         | llamada_funcion
         | leer_funcion
         ;

termino: ID
       | acceso_miembro
       | NUMERO_ENTERO
       | NUMERO_DECIMAL
       | CADENA_TEXTO
       | CARACTER
       | VERDADERO
       | FALSO
       | PARENTESIS_IZQ expresion PARENTESIS_DER
       | LLAVE_IZQ elemento_lista (COMA elemento_lista)* LLAVE_DER
       ;

leer_funcion: LEER PARENTESIS_IZQ PARENTESIS_DER;

condicion: expresion;

tipo_dato: ENTERO_TIPO
         | CADENA_TIPO
         | FLOTANTE_TIPO
         | CARACTER_TIPO
         | BOOL_TIPO
         | ID
         ;

operador_aritmetico: MAS | MENOS | MULTIPLICACION | DIVISION;
operador_relacional: IGUAL | NO_IGUAL | MENOR_QUE | MAYOR_QUE;
operador_logico: AND | OR;
operador_negacion: NEGACION;

// LEXER
ESTRUCTURAS_SEC: '%estructuras';
FUNCIONES_SEC: '%funciones';
ESTRUCTURA: 'estructura';
DEFINIR: 'definir';
SI: 'si';
ENTONCES: 'entonces';
SINO: 'sino';
CONTRARIO: 'contrario';
ELEGIR: 'elegir';
CASO: 'caso';
SIEMPRE: 'siempre';
ROMPER: 'romper';
CONTINUAR: 'continuar';
RETORNAR: 'retornar';
PARA: 'para';
MIENTRAS: 'mientras';
HACER: 'hacer';
IMPRIMIR: 'imprimir';
LEER: 'leer';

ENTERO_TIPO: 'entero';
CADENA_TIPO: 'cadena';
FLOTANTE_TIPO: 'flotante';
CARACTER_TIPO: 'caracter';
BOOL_TIPO: 'bool';

VERDADERO: 'verdadero';
FALSO: 'falso';

FLECHA: '->';
DOS_PUNTOS: ':';
PUNTO_COMA: ';';
COMA: ',';
ASIGNACION: '=';
PUNTO: '.';

PARENTESIS_IZQ: '(';
PARENTESIS_DER: ')';
CORCHETE_IZQ: '[';
CORCHETE_DER: ']';
LLAVE_IZQ: '{';
LLAVE_DER: '}';

MAS: '+';
MENOS: '-';
MULTIPLICACION: '*';
DIVISION: '/';

IGUAL: '==';
NO_IGUAL: '!=';
MENOR_QUE: '<';
MAYOR_QUE: '>';

AND: '&&';
OR: '||';
NEGACION: '!';
SUMA_ABREVIADA: '++';
RESTA_ABREVIADA: '--';

ID: [a-zA-Z_] [a-zA-Z0-9_]*;
NUMERO_ENTERO: [0-9]+;
NUMERO_DECIMAL: [0-9]+ '.' [0-9]+;
CADENA_TEXTO: '"' (~["\r\n])* '"';
CARACTER: '\'' . '\'';

NUEVA_LINEA: [\r\n]+ ('\t' | ' ')*;

WS : [ \t]+ -> skip;
COMENTARIO_LINEA : '//' ~[\r\n]* -> skip;
COMENTARIO_BLOQUE: '/*' .*? '*/' -> channel(HIDDEN);