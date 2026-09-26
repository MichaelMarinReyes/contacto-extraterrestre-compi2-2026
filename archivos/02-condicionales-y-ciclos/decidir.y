%funciones

definir clasificar(entero nota) -> entero:
    si (nota > 69) entonces
        retornar 1
    sino si (nota > 49) entonces
        retornar 2
    contrario
        retornar 0

definir contarDigitos(entero numero) -> entero:
    entero digitos
    digitos = 0
    mientras (numero > 0) hacer
        digitos = digitos + 1
        numero = numero / 10
    hacer:
        digitos = digitos - 1
    mientras (digitos > 0)
    retornar digitos

definir principal() -> entero:
    entero total
    entero i
    total = 0
    para (i = 0; i < 5; i++)
        si (i == 2) entonces
            continuar
        si (i == 4) entonces
            romper
        total = total + i
    imprimir("Total")
    imprimir(total)
    imprimir("Clasificacion")
    imprimir(clasificar(total))
    imprimir("Digitos")
    imprimir(contarDigitos(4821))
    retornar total
