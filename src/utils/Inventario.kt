package utils

import model.Pedido

fun verificarYActualizarInventario(
    pedido: Pedido,
    inventario: MutableMap<String, Int>): Boolean {
    val requeridos = mutableMapOf<String, Int>()

    // acumular ingredientes necesarios
    for (item in pedido.items) {
        for ((ing, cantPorUnidad) in item.producto.ingredientes) {
            val totalIng = cantPorUnidad * item.cantidad
            requeridos[ing] = (requeridos[ing] ?: 0) + totalIng
        }
    }

    val faltantes = mutableListOf<String>()

    for ((ing, cantReq) in requeridos) {
        val disponible = inventario[ing] ?: 0
        if (disponible < cantReq) {
            faltantes += "$ing (falta ${cantReq - disponible}, hay $disponible)"
        }
    }

    return when {
        faltantes.isEmpty() -> {
            // descontar del inventario
            for ((ing, cantReq) in requeridos) {
                inventario[ing] = (inventario[ing] ?: 0) - cantReq
            }
            println("Ingredientes suficientes. Inventario actualizado.")
            true
        }
        else -> {
            println("No hay suficientes ingredientes para este pedido.")
            println("Faltan:")
            for (f in faltantes) println(" - $f")
            println("Pasos para reabastecer:")
            println(" 1. Revisar inventario físico.")
            println(" 2. Contactar proveedor.")
            println(" 3. Actualizar cantidades en el sistema.")
            false
        }
    }
}

fun imprimirInventario(inventario: Map<String, Int>) {
    println("=== Inventario de ingredientes ===")
    inventario.forEach { (ingrediente, cantidad) ->
        println(" - $ingrediente: $cantidad")
    }
}