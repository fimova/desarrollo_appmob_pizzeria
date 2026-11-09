package utils

private val formatoTelefonoChile = Regex("9[0-9]{8}")
private val formatoEmail = Regex(
    "^[A-Za-z0-9_%+-]+(?:\\.[A-Za-z0-9_%+-]+)*@" +
            "[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?" +
            "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)*" +
            "\\.[A-Za-z]{2,}$"
)

// telefono: solo dígitos y formato seleccionado
fun validarTelefono(entrada: String): String? {
    val telefono = entrada.trim()

    return if (formatoTelefonoChile.matches(telefono)) {
        "+56 (9) ${telefono.substring(1, 5)}-${telefono.substring(5, 9)}"
    } else {
        null
    }
}

// email: debe tener @ y un punto después del @
fun validarEmail(entrada: String): String? {
    val email = entrada.trim()

    return if (formatoEmail.matches(email)) {
        email
    } else {
        null
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