package ui

// lee cantidad de productos entre 1 y 100
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
            println("Error: ingresa un número entero entre 1 y 100.")
        }
    }
}
