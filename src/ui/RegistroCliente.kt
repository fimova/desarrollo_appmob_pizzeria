package ui

import model.Cliente
import utils.validarEmail
import utils.validarTelefono

fun registrarCliente(): Cliente? {
    print("Nombre cliente: ")
    val nombreEntrada = readlnOrNull() ?: return null
    val nombre = nombreEntrada.trim().ifBlank {
        println("Nombre vacío. Se usará 'Sin nombre'.")
        "Sin nombre"
    }

    print("Teléfono (9 dígitos, comenzando con 9): ")
    val telefonoEntrada = readlnOrNull() ?: return null
    val telefonoValidado = validarTelefono(telefonoEntrada)
    val telefono = if (telefonoValidado == null) {
        println("Teléfono inválido. Se usará 'No informado'.")
        "No informado"
    } else {
        telefonoValidado
    }

    print("Correo electrónico: ")
    val emailEntrada = readlnOrNull() ?: return null
    val emailValidado = validarEmail(emailEntrada)
    val email = if (emailValidado == null) {
        println("Email inválido. Se usará 'correo@invalido.com'.")
        "correo@invalido.com"
    } else {
        emailValidado
    }

    print("Dirección: ")
    val direccionEntrada = readlnOrNull() ?: return null
    val direccion = direccionEntrada.trim().ifBlank {
        println("Dirección vacía. Se usará 'Sin dirección'.")
        "Sin dirección"
    }

    return Cliente(nombre, telefono, email, direccion)
}