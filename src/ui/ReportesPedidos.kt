package ui

import model.EstadoPedido
import model.Pedido
import utils.TASA_DESCUENTO
import utils.TASA_IVA
import utils.formatearCLP

// resumen total del pedido
fun imprimirResumenPedido(pedido: Pedido) {
    val total = pedido.calcularTotal()
    val porcentajeDescuento = (TASA_DESCUENTO * 100).toInt()
    val porcentajeIva = (TASA_IVA * 100).toInt()

    println("----- RESUMEN DEL PEDIDO -----")
    println("Cliente: ${pedido.cliente.nombre}")
    println("Teléfono: ${pedido.cliente.telefono}")
    println("Dirección: ${pedido.cliente.direccion}")
    println("Correo: ${pedido.cliente.email}")
    println("-------------------------------")
    println("Productos:")

    for (item in pedido.items) {
        println(
            "- ${item.producto.nombre} x${item.cantidad} " +
                    "(${formatearCLP(item.producto.precio)} c/u)"
        )
    }

    println("-------------------------------")
    println("Subtotal: ${formatearCLP(total.subtotal)}")
    println(
        "Descuento ($porcentajeDescuento%): " +
                formatearCLP(total.descuento)
    )
    println("IVA ($porcentajeIva%): ${formatearCLP(total.impuestos)}")
    println("Total: ${formatearCLP(total.totalFinal)}")
    println("Notas: ${pedido.notas ?: "Sin notas"}")
    println("-------------------------------")
    println("Estado: ${pedido.estado.etiqueta}")

    val asignacion = pedido.repartidor?.let {
        "Repartidor: ${it.nombre}"
    } ?: "Entrega: retiro en sucursal central"

    println(asignacion)
}

// ====== RESUMEN (array + for + filter) ======
fun imprimirReportePedidos(pedidosDia: Array<Pedido>) {
    println("\n=== Reporte de pedidos del día ===")

    if (pedidosDia.isEmpty()) {
        println("No se registraron pedidos.")
    }

    for (pedido in pedidosDia) {
        val totales = pedido.calcularTotal()
        val pizzasPedidas = pedido.items.joinToString(", ") {
            "${it.producto.nombre} x${it.cantidad}"
        }

        println(
            "Cliente: ${pedido.cliente.nombre} | " +
                    "Pizzas: $pizzasPedidas | " +
                    "Total: ${formatearCLP(totales.totalFinal)} | " +
                    "Estado: ${pedido.estado.etiqueta}"
        )
    }

    val completados = pedidosDia.filter {
        it.estado == EstadoPedido.LISTO ||
                it.estado == EstadoPedido.EN_CAMINO
    }

    val pendientes = pedidosDia.filter {
        it.estado == EstadoPedido.EN_PREPARACION
    }

    println("Pedidos listos o en camino: ${completados.size}")
    println("Pedidos pendientes: ${pendientes.size}")
}