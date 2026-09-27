%funciones

// ===========================================================================
// Proyecto 6, parte de Y: ciclos, ciclos anidados, condicionales y saltos.
//
// En Y el bloque es la sangria, nunca unas llaves. Todas las formas que se
// prueban aqui salen del enunciado y de sus ejemplos:
//
//   - "si", "sino si", "sino" y "contrario", con y sin "entonces", con y sin
//     los dos puntos.
//   - "para", "mientras" y "hacer ... mientras", con y sin los dos puntos.
//   - ciclos anidados a dos y a tres niveles.
//   - "romper" y "continuar" en cada ciclo, y en el ciclo equivocado a
//     proposito, para comprobar a cual saltan.
//   - "elegir" con "caso" y "siempre", con "romper" explicito y sin el.
//
// Cada funcion devuelve un numero y "principal" lo imprime con su nombre, de
// modo que un resultado raro senala justo el caso que fallo.
// ===========================================================================


// ---------------------------------------------------------------------------
// 1. Formas del condicional
// ---------------------------------------------------------------------------

// "si" a secas: si no se cumple, se sigue con lo que venga despues.
definir ramaSimple(entero n) -> entero:
    si (n > 0) entonces
        retornar 1
    retornar 0

// "si" y "sino", sin "contrario".
definir ramaDoble(entero n) -> entero:
    si (n > 0) entonces
        retornar 1
    sino
        retornar 0

// La cadena entera: "si", "sino si" y "contrario".
definir ramaTriple(entero n) -> entero:
    si (n > 80) entonces
        retornar 3
    sino si (n > 50) entonces
        retornar 2
    sino si (n > 20) entonces
        retornar 1
    contrario
        retornar 0

// Los dos puntos en lugar de "entonces".
definir ramaConDosPuntos(entero n) -> entero:
    si (n > 0):
        retornar 1
    retornar 0

// Ni "entonces" ni dos puntos: el bloque empieza con la sangria.
definir sinEntonces(entero n) -> entero:
    si (n > 0)
        retornar 1
    retornar 0

// "si" anidado tres niveles, cada uno con su "contrario".
definir siAnidado(entero n) -> entero:
    si (n > 0) entonces
        si (n > 10) entonces
            si (n > 100) entonces
                retornar 1
            contrario
                retornar 2
        contrario
            retornar 3
    contrario
        retornar 4


// ---------------------------------------------------------------------------
// 2. Formas del "para"
// ---------------------------------------------------------------------------

definir paraBasico(entero n) -> entero:
    entero i
    entero total
    i = 0
    total = 0
    para (i = 0; i < n; i++)
        total = total + i
    retornar total

// El mismo "para" cerrando con los dos puntos.
definir paraConDosPuntos(entero n) -> entero:
    entero total
    total = 0
    para (entero i = 0; i < n; i++):
        total = total + i
    retornar total


// ---------------------------------------------------------------------------
// 3. Formas del "mientras" y del "hacer ... mientras"
// ---------------------------------------------------------------------------

definir mientrasHacer(entero n) -> entero:
    entero i
    entero total
    i = 0
    total = 0
    mientras (i < n) hacer
        total = total + 1
        i++
    retornar total

definir mientrasConDosPuntos(entero n) -> entero:
    entero i
    entero total
    i = 0
    total = 0
    mientras (i < n):
        total = total + 1
        i++
    retornar total

// Igual que el del enunciado, sin un solo espacio alrededor de la condicion.
definir mientrasSinEspacios(entero n) -> entero:
    entero i
    entero total
    i = 0
    total = 0
    mientras(i < n) hacer
        total = total + 1
        i++
    retornar total

// El cuerpo corre al menos una vez, aunque la condicion ya sea falsa.
definir hacerUnaVez(entero n) -> entero:
    entero vueltas
    entero total
    vueltas = 0
    total = 0
    hacer:
        vueltas = vueltas + 1
        total = total + 10
    mientras (vueltas < 3)
    retornar total

// El mismo "hacer" sin los dos puntos.
definir hacerSinDosPuntos(entero n) -> entero:
    entero vueltas
    entero total
    vueltas = 0
    total = 0
    hacer
        vueltas = vueltas + 1
        total = total + 10
    mientras (vueltas < 3)
    retornar total


// ---------------------------------------------------------------------------
// 4. Ciclos anidados
// ---------------------------------------------------------------------------

// Dos "para" uno dentro del otro.
definir paraEnPara(entero n) -> entero:
    entero i
    entero j
    entero total
    total = 0
    para (i = 0; i < n; i++)
        para (j = 0; j < n; j++)
            total = total + 1
    retornar total

// Tres niveles.
definir tresNiveles(entero n) -> entero:
    entero i
    entero j
    entero k
    entero total
    total = 0
    para (i = 0; i < n; i++)
        para (j = 0; j < n; j++)
            para (k = 0; k < n; k++)
                total = total + 1
    retornar total

// Un "mientras" dentro del "para".
definir paraConMientras(entero n) -> entero:
    entero i
    entero j
    entero total
    total = 0
    para (i = 0; i < n; i++)
        j = 0
        mientras (j < 2) hacer
            total = total + 1
            j++
    retornar total

// Un "para" dentro del "mientras".
definir mientrasConPara(entero n) -> entero:
    entero i
    entero j
    entero total
    total = 0
    i = 0
    mientras (i < n) hacer
        para (j = 0; j < 2; j++)
            total = total + 1
        i++
    retornar total

// Un "hacer ... mientras" dentro del "para".
definir hacerEnPara(entero n) -> entero:
    entero i
    entero j
    entero total
    total = 0
    para (i = 0; i < n; i++)
        j = 0
        hacer:
            j++
            total = total + 1
        mientras (j < 2)
    retornar total

// Un "para" dentro del "hacer ... mientras".
definir paraEnHacer(entero n) -> entero:
    entero vueltas
    entero i
    entero total
    total = 0
    vueltas = 0
    hacer:
        para (i = 0; i < 2; i++)
            total = total + 1
        vueltas = vueltas + 1
    mientras (vueltas < 2)
    retornar total


// ---------------------------------------------------------------------------
// 5. "romper": a cual ciclo sale
// ---------------------------------------------------------------------------

// El "romper" del "para" exterior.
definir romperElExterno(entero n) -> entero:
    entero i
    entero total
    total = 0
    para (i = 0; i < n; i++)
        total = total + 1
        si (total == 3) entonces
            romper
    retornar total

// El "romper" sale solo del "para" interior: el exterior sigue y suma 3 veces.
definir romperSoloElInterno(entero n) -> entero:
    entero i
    entero j
    entero total
    total = 0
    para (i = 0; i < n; i++)
        para (j = 0; j < n; j++)
            si (j == 1) entonces
                romper
            total = total + 1
    retornar total

// "romper" en el "mientras".
definir romperEnMientras(entero n) -> entero:
    entero i
    entero total
    i = 0
    total = 0
    mientras (i < n) hacer
        i++
        total = total + i
        si (total > 5) entonces
            romper
    retornar total

// "romper" en el "hacer ... mientras".
definir romperEnHacer(entero n) -> entero:
    entero vueltas
    entero total
    vueltas = 0
    total = 0
    hacer:
        vueltas = vueltas + 1
        total = total + vueltas
        si (vueltas == 3) entonces
            romper
    mientras (vueltas < 10)
    retornar total

// Un "romper" metido tres niveles dentro sigue cortando el ciclo mas interno,
// asi que el "para" de fuera se completa entero y el total queda en "n".
definir romperEnElMasInterno(entero n) -> entero:
    entero total
    entero i
    entero j
    total = 0
    para (total = 0; total < n; total++)
        para (i = 0; i < n; i++)
            para (j = 0; j < n; j++)
                si (total == 1) entonces
                    romper
    retornar total

// Al contrario: el "romper" puesto en el cuerpo del ciclo de fuera si lo corta,
// y los dos ciclos de dentro ya no cuentan nada mas.
definir romperElMasExterno(entero n) -> entero:
    entero total
    entero i
    entero j
    total = 0
    para (total = 0; total < n; total++)
        para (i = 0; i < n; i++)
            para (j = 0; j < n; j++)
                total = total + 1
        si (total > 4) entonces
            romper
    retornar total


// ---------------------------------------------------------------------------
// 6. "continuar": a cual ciclo vuelve
// ---------------------------------------------------------------------------

// En el "para" vuelve al incremento, no a la prueba.
definir continuarEnPara(entero n) -> entero:
    entero i
    entero total
    total = 0
    para (i = 0; i < n; i++)
        si (i == 2) entonces
            continuar
        total = total + i
    retornar total

definir continuarEnMientras(entero n) -> entero:
    entero i
    entero total
    i = 0
    total = 0
    mientras (i < n) hacer
        i++
        si (i == 2) entonces
            continuar
        total = total + i
    retornar total

// En el "hacer ... mientras" vuelve a la prueba.
definir continuarEnHacer(entero n) -> entero:
    entero vueltas
    entero total
    vueltas = 0
    total = 0
    hacer:
        vueltas = vueltas + 1
        si (vueltas == 2) entonces
            continuar
        total = total + vueltas
    mientras (vueltas < 4)
    retornar total

// El "continuar" del interior no salta el incremento del exterior.
definir continuarSoloElInterno(entero n) -> entero:
    entero i
    entero j
    entero total
    total = 0
    para (i = 0; i < n; i++)
        para (j = 0; j < n; j++)
            si (j == 0) entonces
                continuar
            total = total + 1
    retornar total


// ---------------------------------------------------------------------------
// 7. Condicionales dentro de ciclos y ciclos dentro de condicionales
// ---------------------------------------------------------------------------

definir clasificarEnCiclo(entero n) -> entero:
    entero i
    entero total
    total = 0
    para (i = 0; i < n; i++)
        si (i > 5) entonces
            total = total + 100
        sino si (i > 2) entonces
            total = total + 10
        sino
            total = total + 1
    retornar total

definir cicloDentroDeSi(entero n) -> entero:
    entero i
    entero total
    total = 0
    si (n > 0) entonces
        para (i = 0; i < n; i++)
            total = total + 2
    contrario
        total = 0
    retornar total

definir cicloDentroDeSiAnidado(entero n) -> entero:
    entero i
    entero total
    total = 0
    si (n > 10) entonces
        si (n > 20) entonces
            para (i = 0; i < 2; i++)
                total = total + 1
        contrario
            total = total + 100
    contrario
        total = total + 1000
    retornar total


// ---------------------------------------------------------------------------
// 8. El "elegir"
// ---------------------------------------------------------------------------

// Con "romper" explicito, tal cual sale en el enunciado.
definir elegirConRomper(entero v) -> entero:
    entero x
    x = 0
    elegir (v) :
        caso 1:
            x = 10
            romper
        caso 2:
            x = 20
            romper
        caso 3:
            x = 30
            romper
        siempre:
            x = 40
            romper
    retornar x

// Sin "romper": cada caso salta igual, y no se cae al siguiente.
definir elegirSinRomper(entero v) -> entero:
    entero x
    x = 0
    elegir (v) :
        caso 1:
            x = 1
        caso 2:
            x = 2
            x = 22
        siempre:
            x = 9
    retornar x

// Sin "siempre": si nada coincide no se hace nada.
definir elegirSinSiempre(entero v) -> entero:
    entero x
    x = -1
    elegir (v) :
        caso 1:
            x = 1
        caso 2:
            x = 2
    retornar x

// Condicionales y hasta otro ciclo dentro de un caso.
definir elegirConSi(entero v) -> entero:
    entero x
    x = 0
    elegir (v) :
        caso 1:
            si (v == 1) entonces
                x = 100
            contrario
                x = 200
        caso 2:
            x = 5
            mientras (x < 3) hacer
                x = x + 1
        siempre:
            x = x + 1000
    retornar x

// "romper" y "continuar" dentro de un caso salen de la seleccion.
definir elegirConSalto(entero v) -> entero:
    entero x
    x = 0
    elegir (v) :
        caso 1:
            x = 1
            continuar
            x = 999
        caso 2:
            x = 2
            romper
            x = 999
        siempre:
            x = 7
    retornar x

// El "romper" de un caso sale de la seleccion, no del "para" de alrededor.
definir elegirEnCiclo(entero n) -> entero:
    entero i
    entero total
    total = 0
    para (i = 0; i < n; i++)
        elegir (i) :
            caso 0:
                total = total + 1
                romper
            caso 1:
                total = total + 10
                romper
            siempre:
                total = total + 100
    retornar total


// ---------------------------------------------------------------------------
// 9. principal: imprime el resultado de cada caso
// ---------------------------------------------------------------------------

definir principal() -> entero:
    imprimir("--- condicionales ---")
    imprimir(ramaSimple(5))
    imprimir(ramaSimple(-5))
    imprimir(ramaDoble(5))
    imprimir(ramaDoble(-5))
    imprimir(ramaTriple(90))
    imprimir(ramaTriple(60))
    imprimir(ramaTriple(30))
    imprimir(ramaTriple(5))
    imprimir(ramaConDosPuntos(5))
    imprimir(ramaConDosPuntos(-5))
    imprimir(sinEntonces(5))
    imprimir(sinEntonces(-5))
    imprimir(siAnidado(200))
    imprimir(siAnidado(50))
    imprimir(siAnidado(5))
    imprimir(siAnidado(-5))

    imprimir("--- para ---")
    imprimir(paraBasico(5))
    imprimir(paraConDosPuntos(5))

    imprimir("--- mientras ---")
    imprimir(mientrasHacer(4))
    imprimir(mientrasConDosPuntos(4))
    imprimir(mientrasSinEspacios(4))

    imprimir("--- hacer mientras ---")
    imprimir(hacerUnaVez(0))
    imprimir(hacerSinDosPuntos(0))

    imprimir("--- anidados ---")
    imprimir(paraEnPara(3))
    imprimir(tresNiveles(2))
    imprimir(paraConMientras(3))
    imprimir(mientrasConPara(3))
    imprimir(hacerEnPara(3))
    imprimir(paraEnHacer(0))

    imprimir("--- romper ---")
    imprimir(romperElExterno(10))
    imprimir(romperSoloElInterno(3))
    imprimir(romperEnMientras(10))
    imprimir(romperEnHacer(10))
    imprimir(romperEnElMasInterno(5))
    imprimir(romperElMasExterno(2))

    imprimir("--- continuar ---")
    imprimir(continuarEnPara(6))
    imprimir(continuarEnMientras(6))
    imprimir(continuarEnHacer(0))
    imprimir(continuarSoloElInterno(3))

    imprimir("--- condicional en ciclo ---")
    imprimir(clasificarEnCiclo(7))
    imprimir(cicloDentroDeSi(4))
    imprimir(cicloDentroDeSi(0))
    imprimir(cicloDentroDeSiAnidado(5))
    imprimir(cicloDentroDeSiAnidado(30))

    imprimir("--- elegir ---")
    imprimir(elegirConRomper(1))
    imprimir(elegirConRomper(2))
    imprimir(elegirConRomper(3))
    imprimir(elegirConRomper(9))
    imprimir(elegirSinRomper(1))
    imprimir(elegirSinRomper(2))
    imprimir(elegirSinRomper(5))
    imprimir(elegirSinSiempre(1))
    imprimir(elegirSinSiempre(7))
    imprimir(elegirConSi(1))
    imprimir(elegirConSi(2))
    imprimir(elegirConSi(9))
    imprimir(elegirConSalto(1))
    imprimir(elegirConSalto(2))
    imprimir(elegirConSalto(5))
    imprimir(elegirEnCiclo(4))

    retornar 0
