package ui

// cantidad de ingredientes positivos
fun leerCantidadPositiva(mensaje: String): Int? {
    while (true) {
        print(mensaje)
        val entrada = readlnOrNull() ?: return null

        try {
            val valor = entrada.trim().toInt()

            if (valor <= 0) {
                println("Error: la cantidad debe ser mayor a 0.")
            } else {
                return valor
            }
        } catch (error: NumberFormatException) {
            println("Error: debe ingresar un número entero.")
        }
    }
}
