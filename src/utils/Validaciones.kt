package utils

// telefono: solo dígitos y formato seleccionado
fun validarTelefono(entrada: String): String {
    val telefono = entrada.trim()
    val formatoTel = Regex("9[0-9]{8}")

    return if (formatoTel.matches(telefono)) {
        "+56 9 ${telefono.substring(1, 5)} ${telefono.substring(5, 9)}"
    } else {
        println(
            "Teléfono inválido. Ingresa 9 dígitos comenzando con 9. " +
                    "Se usará el valor predeterminado: No informado."
        )
        "No informado"
    }
}

// email: debe tener @ y un punto después del @
fun validarEmail(entrada: String): String {
    val trimmed = entrada.trim()
    val valido = trimmed.contains("@") && trimmed.substringAfter("@").contains(".")
    return if (valido) {
        trimmed
    } else {
        println("Email inválido: '$entrada'. Se usará correo@invalido.com.")
        "correo@invalido.com"
    }
}

// cantidad de ingredientes positivos
fun leerCantidadPositiva(mensaje: String): Int? {
    while (true) {
        print(mensaje)
        val entrada = readlnOrNull() ?: return null

        try {
            val valor = entrada.trim().toInt()

            if (valor > 0) {
                return valor
            }

            println("Error: la cantidad debe ser mayor a 0.")
        } catch (e: NumberFormatException) {
            println("Error: debe ingresar un número entero.")
        }
    }
}

// validación de zona de reparto
fun validarZona(entrada: String): String? {
    return when (entrada.trim().lowercase()) {
        "centro" -> "Centro"
        "norte" -> "Norte"
        "sur" -> "Sur"
        else -> null
    }
}