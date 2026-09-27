%funciones

// Proyecto 2, parte de Y: condicionales y ciclos.
// En Y el bloque es la sangria: nada se cierra con llaves. Los dos puntos y la
// palabra "entonces" son opcionales, y "continuar" y "romper" se escriben sin
// punto y coma.

// ---------------------------------------------------------------- condicionales

// Cadena completa: si, sino si y contrario.
definir clasificar(entero nota) -> entero:
    si (nota > 69) entonces
        retornar 3
    sino si (nota > 49) entonces
        retornar 2
    sino si (nota > 29) entonces
        retornar 1
    contrario
        retornar 0

// Si y sino a secas, sin contrario.
definir esPositivo(entero n) -> entero:
    si (n > 0) entonces
        retornar 1
    sino
        retornar 0

// Si solo, con dos puntos en vez de "entonces".
definir esCero(entero n) -> entero:
    si (n == 0):
        retornar 1
    retornar 0

// ------------------------------------------------------------------- el para

// Continuar y romper en el mismo ciclo. "continuar" salta al incremento.
definir acumular(entero hasta) -> entero:
    entero i
    entero total
    i = 0
    total = 0
    para (i = 0; i < hasta; i++)
        si (i == 2) entonces
            continuar
        si (i == 5) entonces
            romper
        total = total + i
    retornar total

// El mismo ciclo escrito con los dos puntos.
definir acumularConDosPuntos(entero hasta) -> entero:
    entero total
    total = 0
    para (entero i = 0; i < hasta; i++):
        total = total + i
    retornar total

// ------------------------------------------------------------------ mientras

// Continuar dentro del mientras.
definir contarSaltando(entero limite) -> entero:
    entero i
    entero cuenta
    i = 0
    cuenta = 0
    mientras (i < limite) hacer
        i++
        si (i == 2) entonces
            continuar
        cuenta = cuenta + i
    retornar cuenta

// Romper dentro del mientras.
definir buscar(entero objetivo) -> entero:
    entero n
    entero encontrado
    n = 0
    encontrado = 0
    hacer:
        n++
        si (n == objetivo) entonces
            encontrado = n
            romper
    mientras (n < 20)
    retornar encontrado

// --------------------------------------------------- hacer ... mientras

// El cuerpo se ejecuta al menos una vez, aunque la condicion ya sea falsa.
definir minimoUnaVez() -> entero:
    entero vueltas
    entero total
    vueltas = 0
    total = 0
    hacer:
        vueltas = vueltas + 1
        total = total + vueltas
    mientras (vueltas < 3)
    retornar total

// ------------------------------------------------------------------ principal

definir principal() -> entero:
    entero total

    imprimir("Clasificacion")
    imprimir(clasificar(80))
    imprimir(clasificar(60))
    imprimir(clasificar(40))
    imprimir(clasificar(10))

    imprimir("Positivo")
    imprimir(esPositivo(5))
    imprimir(esPositivo(0))
    imprimir(esPositivo(-5))

    imprimir("Es cero")
    imprimir(esCero(0))
    imprimir(esCero(7))

    imprimir("Acumular")
    imprimir(acumular(10))
    imprimir("Acumular con dos puntos")
    imprimir(acumularConDosPuntos(5))

    imprimir("Contar saltando")
    imprimir(contarSaltando(6))

    imprimir("Buscar")
    imprimir(buscar(7))
    imprimir(buscar(99))

    imprimir("Hacer mientras")
    imprimir(minimoUnaVez())

    total = 0
    retornar total
