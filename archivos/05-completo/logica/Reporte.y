%estructuras
estructura Articulo:
    cadena nombre
    entero cantidad
    flotante precio

estructura Pedido:
    Articulo articulo
    entero total

%funciones

definir totalDe([]entero cantidades, entero largo) -> entero:
    entero suma
    entero i
    suma = 0
    para (i = 0; i < largo; i++)
        suma = suma + cantidades[i]
    retornar suma

definir principal() -> entero:
    estructura Venta:
        cadena cliente
        entero monto

    entero cantidades[4] = {5, 3, 8, 2}
    Venta venta
    Articulo articulo
    cadena saludo
    entero total
    entero i
    entero codigo
    caracter inicial
    bool listo

    saludo = "Bienvenido"
    venta.cliente = "Ana"
    venta.monto = 0
    total = totalDe(cantidades, 4)
    articulo.nombre = "cafe"
    articulo.cantidad = 2
    articulo.precio = 1.5
    codigo = 7
    inicial = 'a'
    listo = verdadero

    imprimir(saludo)
    imprimir(venta.cliente)
    imprimir(articulo.nombre)
    imprimir(total)
    imprimir(codigo)
    imprimir(inicial)
    imprimir(articulo.cantidad)

    si (listo && total > 10) entonces
        imprimir("todo listo")
    sino entonces
        imprimir("faltan datos")

    elegir (total)
        caso 18:
            imprimir("total completo")
        caso 9:
            imprimir("total parcial")
        siempre:
            imprimir("total raro")

    para (i = 0; i < 4; i++)
        si (cantidades[i] > 5) entonces
            imprimir("mucho")
        sino entonces
            imprimir("poco")

    mientras (i > 0) hacer
        i = i - 1

    venta.monto = total
    retornar venta.monto
