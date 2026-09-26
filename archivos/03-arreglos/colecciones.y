%funciones

// El tipo de un arreglo va con los corchetes delante: []entero lista
definir sumarLista([]entero lista, entero largo) -> entero:
    entero suma
    entero i
    suma = 0
    para (i = 0; i < largo; i++)
        suma = suma + lista[i]
    retornar suma

definir principal() -> entero:
    entero numeros[5] = {1, 2, 3, 4, 5}
    entero matriz[2][3] = {
        {1, 2, 3},
        {4, 5, 6}
    }
    entero total
    total = sumarLista(numeros, 5)
    imprimir("Suma de la lista")
    imprimir(total)
    imprimir("Una celda de la matriz")
    imprimir(matriz[0][1])
    retornar total
