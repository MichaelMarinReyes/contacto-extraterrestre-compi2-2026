grammar PigLatin;

// GRAMÁTICA
init: codex_latinus;

codex_latinus: (structura_def | variables | munera)* maior;

structura_def: STRUCTURA VARIABLE LLAVE_IZQ miembro_structura* LLAVE_DER FINIS PUNTO_COMA;

miembro_structura: ESTO VARIABLE DOS_PUNTOS (tipo_dato | VARIABLE) (COMA | PUNTO_COMA)?
                 | SERIES VARIABLE DOS_PUNTOS (tipo_dato | VARIABLE) (COMA | PUNTO_COMA)?
                 | SERIES VARIABLE CORCHETE_IZQ NUMERO_ENTERO CORCHETE_DER DOS_PUNTOS (tipo_dato | VARIABLE) (COMA | PUNTO_COMA)?;

variables: VARIABILES MAYOR_QUE (declaracion | arreglo_declaracion | structura_def)+;

declaracion: ESTO VARIABLE DOS_PUNTOS? tipo_dato expresion PUNTO_COMA
           | ESTO VARIABLE DOS_PUNTOS? TEXTUM CADENA_TEXTO PUNTO_COMA
           | ESTO VARIABLE DOS_PUNTOS? LITTERA CARACTER PUNTO_COMA
           | ESTO VARIABLE DOS_PUNTOS? VARIABLE structura_instanciacion PUNTO_COMA
           | ESTO VARIABLE DOS_PUNTOS? expresion PUNTO_COMA
           | arreglo_declaracion;

arreglo_declaracion: SERIES VARIABLE CORCHETE_IZQ NUMERO_ENTERO CORCHETE_DER DOS_PUNTOS? tipo_dato (LLAVE_IZQ elemento_arreglo? LLAVE_DER)? PUNTO_COMA
                   | SERIES VARIABLE CORCHETE_IZQ NUMERO_ENTERO CORCHETE_DER DOS_PUNTOS? VARIABLE (LLAVE_IZQ elemento_arreglo_struct? LLAVE_DER)? PUNTO_COMA;

elemento_arreglo: expresion (COMA expresion)*;

elemento_arreglo_struct: structura_instanciacion (COMA structura_instanciacion)*;

structura_instanciacion: (VARIABLE)? LLAVE_IZQ atributo_asignacion (COMA atributo_asignacion)* LLAVE_DER
                        | VARIABLE CORCHETE_IZQ NUMERO_ENTERO CORCHETE_DER;

atributo_asignacion: VARIABLE DOS_PUNTOS? (expresion | structura_instanciacion | arreglo_literal);

arreglo_literal: LLAVE_IZQ elemento_arreglo? LLAVE_DER;

munera: MUNERA MAYOR_QUE funcion+;

funcion: ratio_funcion
       | actio_funcion;

ratio_funcion: RATIO tipo_dato VARIABLE PARENTESIS_IZQ parametros? PARENTESIS_DER LLAVE_IZQ variables_locales? sentencia* reddere_sentencia LLAVE_DER FINIS PUNTO_COMA;

actio_funcion: ACTIO VARIABLE PARENTESIS_IZQ parametros? PARENTESIS_DER LLAVE_IZQ variables_locales? sentencia* LLAVE_DER FINIS PUNTO_COMA;

tipo_dato: NUMERUS
         | TEXTUM
         | DECIMALIS
         | LITTERA
         | VERUM
         | FALSUS;

parametros: parametro (COMA parametro)*;

parametro: ESTO VARIABLE DOS_PUNTOS? (tipo_dato | VARIABLE);

variables_locales: VARIABILES CORCHETE_IZQ declaracion_local+ CORCHETE_DER;

declaracion_local: ESTO VARIABLE DOS_PUNTOS? tipo_dato expresion PUNTO_COMA
                 | ESTO VARIABLE DOS_PUNTOS? TEXTUM CADENA_TEXTO PUNTO_COMA
                 | ESTO VARIABLE DOS_PUNTOS? LITTERA CARACTER PUNTO_COMA
                 | ESTO VARIABLE DOS_PUNTOS? VARIABLE structura_instanciacion PUNTO_COMA
                 | ESTO VARIABLE DOS_PUNTOS? expresion PUNTO_COMA;

expresion: termino (operacion_aritmetica termino)*;

termino: VARIABLE
       | acceso_miembro
       | NUMERO_ENTERO
       | NUMERO_DECIMAL
       | CADENA_TEXTO
       | CARACTER
       | VERUM
       | FALSUS
       | llamada_funcion;

acceso_miembro: VARIABLE (PUNTO VARIABLE | CORCHETE_IZQ expresion CORCHETE_DER)+;

arreglo_acceso: VARIABLE CORCHETE_IZQ expresion CORCHETE_DER (PUNTO VARIABLE)*;

operacion_aritmetica: MAS | MENOS | MULTIPLICACION | DIVISION;

reddere_sentencia: REDDERE expresion PUNTO_COMA;

maior: MAIOR MAYOR_QUE sentencia* FINIS PUNTO_COMA;

sentencia: imprimir_sentencia
         | leer_sentencia
         | asignacion_sentencia
         | si_sentencia
         | ciclo_dum
         | ciclo_facere
         | ciclo_per
         | salto_sentencia
         | llamada_funcion PUNTO_COMA?
         | (VARIABLE | acceso_miembro) (SUMA_ABREVIADA | RESTA_ABREVIADA) PUNTO_COMA;

leer_sentencia: (VARIABLE | acceso_miembro)? LEER;

asignacion_sentencia: (VARIABLE | acceso_miembro) ASIGNACION (expresion | condicion | structura_instanciacion | arreglo_literal) PUNTO_COMA?;

imprimir_sentencia: IMPRIMIR (CADENA_TEXTO | VARIABLE | acceso_miembro | llamada_funcion) (IMPRIMIR (CADENA_TEXTO | VARIABLE | acceso_miembro | llamada_funcion))* PUNTO_COMA?;

si_sentencia: SI PARENTESIS_IZQ condicion PARENTESIS_DER LLAVE_IZQ sentencia* LLAVE_DER aliter_bloque* ((ALITER) LLAVE_IZQ sentencia* LLAVE_DER)? FINIS PUNTO_COMA;

//aliter_bloque: ALITER SI PARENTESIS_IZQ condicion PARENTESIS_DER LLAVE_IZQ sentencia* LLAVE_DER;
aliter_bloque: ALITER PARENTESIS_IZQ condicion PARENTESIS_DER LLAVE_IZQ sentencia* LLAVE_DER;

ciclo_dum: DUM PARENTESIS_IZQ condicion PARENTESIS_DER LLAVE_IZQ sentencia* LLAVE_DER FINIS PUNTO_COMA;

ciclo_facere: FACERE LLAVE_IZQ sentencia* LLAVE_DER DUM PARENTESIS_IZQ condicion PARENTESIS_DER PUNTO_COMA;

ciclo_per: PER PARENTESIS_IZQ inicializacion_per condiciones_per PUNTO_COMA incremento_per PARENTESIS_DER LLAVE_IZQ sentencia* LLAVE_DER;

inicializacion_per: ESTO VARIABLE DOS_PUNTOS? tipo_dato (ASIGNACION)? expresion PUNTO_COMA
                  | (VARIABLE | acceso_miembro) ASIGNACION expresion PUNTO_COMA;

condiciones_per: condicion;

incremento_per: (VARIABLE | acceso_miembro) SUMA_ABREVIADA
              | (VARIABLE | acceso_miembro) RESTA_ABREVIADA
              | (VARIABLE | acceso_miembro) ASIGNACION expresion;

salto_sentencia: PERGE PUNTO_COMA
               | INTERRUMPE PUNTO_COMA;

condicion
    : condicion OR conjuncion
    | conjuncion;

conjuncion
    : conjuncion AND negacion_logica
    | negacion_logica;

negacion_logica
    : NEGACION negacion_logica
    | primaria_logica;

primaria_logica
    : PARENTESIS_IZQ condicion PARENTESIS_DER
    | expresion operador_relacional expresion
    | VERUM
    | FALSUS
    | VARIABLE
    | llamada_funcion;

operador_relacional: MAYOR_IGUAL | MENOR_IGUAL | IGUAL | NO_IGUAL | MENOR_QUE | MAYOR_QUE;

llamada_funcion: VARIABLE PARENTESIS_IZQ argumentos? PARENTESIS_DER;

argumentos: expresion (COMA expresion)*;

// LEXER
// Palabras clave
ESTO: 'esto';
SERIES: 'series';
STRUCTURA: 'structura';
FINIS: 'finis' | 'FINIS';
SI: 'si';
ALITER: 'aliter';
DUM: 'dum';
FACERE: 'facere';
PER: 'per';
PERGE: 'perge';
INTERRUMPE: 'interrumpe';
ACTIO: 'actio';
RATIO: 'ratio';
REDDERE: 'reddere';
VARIABILES: 'VARIABILES';
MUNERA: 'MUNERA';
MAIOR: 'MAIOR';

// Palabras clave para tipos de datos
NUMERUS: 'numerus';
TEXTUM: 'textum';
DECIMALIS: 'decimalis';
LITTERA: 'littera';

// Booleanos
VERUM: 'verum';
FALSUS: 'falsus';

// Símbolos
DOS_PUNTOS: ':';
PUNTO_COMA: ';';
COMA: ',';
ASIGNACION: '=';
PUNTO: '.';
LEER: '<<';
IMPRIMIR: '>>';

// Delimitadores de arreglos y  bloques
PARENTESIS_IZQ: '(';
PARENTESIS_DER: ')';
CORCHETE_IZQ: '[';
CORCHETE_DER: ']';
LLAVE_IZQ: '{';
LLAVE_DER: '}';

// Aritmeticos
MAS: '+';
MENOS: '-';
MULTIPLICACION: '*';
DIVISION: '/';

// Relacionales
MAYOR_IGUAL: '>=';
MENOR_IGUAL: '<=';
IGUAL: '==';
NO_IGUAL: '!=';
MENOR_QUE: '<';
MAYOR_QUE: '>';

// Logicos
AND: '&&';
OR: '||';
NEGACION: 'non';
SUMA_ABREVIADA: '++';
RESTA_ABREVIADA: '--';

// Variables
VARIABLE: [a-zA-Z_] [a-zA-Z0-9_]*;
NUMERO_ENTERO: [0-9]+;
NUMERO_DECIMAL: [0-9]+ '.' [0-9]+;
CADENA_TEXTO: '"' (~["\r\n])* '"';
CARACTER: '\'' . '\'';

// Espacios en blanco y comentarios
WS : [ \t\r\n]+ -> skip;
COMENTARIO_LINEA : '//' ~[\r\n]* -> skip;
COMENTARIO_BLOQUE: '##' .*? '##' -> channel(HIDDEN);