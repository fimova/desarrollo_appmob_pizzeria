package service

import model.Pedido

data class IngredienteFaltante(
    val nombre: String,
    val cantidadRequerida: Int,
    val cantidadDisponible: Int
)

data class ResultadoInventario(
    val inventarioActualizado: Boolean,
    val faltantes: List<IngredienteFaltante>
)

fun verificarYActualizarInventario(
    pedido: Pedido,
    inventario: MutableMap<String, Int>
): ResultadoInventario {
    val requeridos = mutableMapOf<String, Int>()

    // acumular ingredientes necesarios
    for (item in pedido.items) {
        for ((ingrediente, cantidadPorPizza) in item.producto.ingredientes) {
            val cantidadNecesaria = cantidadPorPizza * item.cantidad
            requeridos[ingrediente] =
                (requeridos[ingrediente] ?: 0) + cantidadNecesaria
        }
    }

    val faltantes = mutableListOf<IngredienteFaltante>()

    for ((ingrediente, cantidadRequerida) in requeridos) {
        val cantidadDisponible = inventario[ingrediente] ?: 0

        if (cantidadDisponible < cantidadRequerida) {
            faltantes.add(
                IngredienteFaltante(
                    nombre = ingrediente,
                    cantidadRequerida = cantidadRequerida,
                    cantidadDisponible = cantidadDisponible
                )
            )
        }
    }

    return when {
        faltantes.isEmpty() -> {
            // descontar del inventario solo si alcanza para todos los ingredientes
            for ((ingrediente, cantidadRequerida) in requeridos) {
                inventario[ingrediente] =
                    (inventario[ingrediente] ?: 0) - cantidadRequerida
            }

            ResultadoInventario(
                inventarioActualizado = true,
                faltantes = emptyList()
            )
        }

        else -> {
            ResultadoInventario(
                inventarioActualizado = false,
                faltantes = faltantes
            )
        }
    }
}