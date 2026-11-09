package ui

import model.Cliente
import utils.validarEmail
import utils.validarTelefono

private fun leerTelefonoValido(): String? {
    while (true) {
        print("Teléfono (9 dígitos, comenzando con 9): ")
        val entrada = readlnOrNull() ?: return null

        val telefono = validarTelefono(entrada)

        if (telefono != null) {
            return telefono
        }

        println("Teléfono inválido. Ingresa los 9 dígitos nuevamente.")
    }
}

private fun leerEmailValido(): String? {
    while (true) {
        print("Correo electrónico: ")
        val entrada = readlnOrNull() ?: return null

        val email = validarEmail(entrada)

        if (email != null) {
            return email
        }

        println("Email inválido. Usa el formato nombre@dominio.com e inténtalo nuevamente.")
    }
}

// datos del cliente
fun registrarCliente(clientesRegistrados: MutableSet<Cliente>): Cliente? {
    print("Nombre cliente: ")
    val nombreEntrada = readlnOrNull() ?: return null
    val nombre = nombreEntrada.trim().ifBlank {
        println("Nombre vacío. Se usará 'Sin nombre'.")
        "Sin nombre"
    }

    val email = leerEmailValido() ?: return null

    // revisar si el cliente ya existe antes de pedir teléfono y dirección
    val clienteExistente = clientesRegistrados.firstOrNull { clienteRegistrado ->
        clienteRegistrado.nombre == nombre &&
                clienteRegistrado.email == email
    }

    if (clienteExistente != null) {
        println("El cliente ya está registrado. Se usarán sus datos guardados.")
        return clienteExistente
    }

    val telefono = leerTelefonoValido() ?: return null

    print("Dirección: ")
    val direccionEntrada = readlnOrNull() ?: return null
    val direccion = direccionEntrada.trim().ifBlank {
        println("Dirección vacía. Se usará 'Sin dirección'.")
        "Sin dirección"
    }

    val nuevoCliente = Cliente(nombre, telefono, email, direccion)

    // usa equals() y hashCode() al agregar al cliente
    clientesRegistrados.add(nuevoCliente)
    println("Cliente registrado.")
    return nuevoCliente
}