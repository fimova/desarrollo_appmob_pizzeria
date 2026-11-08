package ui

import model.Pedido
import service.productosPromocionablesDelPedido

fun imprimirPromocionablesDelPedido(pedido: Pedido) {
    val productos = productosPromocionablesDelPedido(pedido)

    if (productos.isEmpty()) {
        println("Este pedido no contiene productos promocionables.")
        return
    }

    println("Productos promocionables incluidos en este pedido:")
    productos.forEach { producto ->
        println(" - ${producto.nombre}")
    }
}