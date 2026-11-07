package ui

import model.Producto

// elegir pizza válida
fun seleccionarProducto(menu: List<Producto>): Producto? {
    // mostrar menú
    println("\n--- Menú ---")
    menu.forEachIndexed { idx, producto ->
        println("${idx + 1}. ${producto.detallarProducto()}")
    }

    while (true) {
        print("Elija una pizza (1-${menu.size}): ")
        val opcionEntrada = readlnOrNull() ?: return null
        val opcion = opcionEntrada.trim().toIntOrNull()

        if (opcion != null && opcion in 1..menu.size) {
            return menu[opcion - 1]
        }

        println("Opción inválida. Ingrese un número del 1 al ${menu.size}.")
    }
}