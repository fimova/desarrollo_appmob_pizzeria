package ui

// cantidad de ingredientes positivos
fun leerCantidadPositiva(mensaje: String): Int? {
    while (true) {
        print(mensaje)
        val entrada = readlnOrNull() ?: return null

        try {
            val cantidad = entrada.trim().toInt()

            if (cantidad in 1..100) { // uso de rango
                return cantidad
            }

            println("Error: ingresa una cantidad entre 1 y 100.")
        } catch (error: NumberFormatException) {
            println("Error: debes ingresar un número entero.")
        }
    }
}
