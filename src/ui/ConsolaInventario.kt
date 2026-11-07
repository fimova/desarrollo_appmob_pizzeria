package ui

import service.ResultadoInventario

fun mostrarResultadoInventario(resultado: ResultadoInventario) {
    if (resultado.inventarioActualizado) {
        println("Ingredientes suficientes. Inventario actualizado.")
    } else {
        println("No hay suficientes ingredientes para este pedido.")
        println("Ingredientes insuficientes:")

        for (faltante in resultado.faltantes) {
            val cantidadFaltante =
                faltante.cantidadRequerida - faltante.cantidadDisponible

            println(
                "- ${faltante.nombre}: se necesitan ${faltante.cantidadRequerida}, " +
                        "hay ${faltante.cantidadDisponible}, faltan $cantidadFaltante"
            )
        }

        println("Pasos para reabastecer:")
        println("1. Revisar el inventario físico.")
        println("2. Contactar al proveedor.")
        println("3. Actualizar la cantidad disponible en el sistema.")
    }
}

fun imprimirInventario(inventario: Map<String, Int>) {
    println("=== Inventario de ingredientes ===")

    for ((ingrediente, cantidad) in inventario) {
        println("- $ingrediente: $cantidad")
    }
}