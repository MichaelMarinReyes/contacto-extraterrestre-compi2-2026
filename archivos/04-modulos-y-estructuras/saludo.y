%estructuras
estructura Contacto:
    cadena nombre
    entero telefono

estructura Agenda:
    Contacto dueno
    entero cantidad

%funciones

definir mostrarContacto(cadena nombre, entero telefono) -> entero:
    imprimir(nombre)
    imprimir(telefono)
    retornar telefono

definir principal() -> entero:
    Contacto ana
    cadena saludo
    entero total
    saludo = "Bienvenido"
    ana.nombre = "Ana"
    ana.telefono = 3001234
    total = mostrarContacto(ana.nombre, ana.telefono)
    imprimir(saludo)
    imprimir(total)
    retornar total
