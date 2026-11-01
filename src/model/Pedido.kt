package model

class Pedido(
    val id: Long,
    val cliente: Cliente,
    val items: List<ItemPedido>,
    var estado: EstadoPedido = EstadoPedido.EN_PREPARACION,
    var repartidor: Repartidor? = null
) {

    // == MÉTODOS ==

    // cáculo del total, junto con iva y también descuento si hay
    fun calcularTotal(iva: Double = 0.19): TotalPedido {
        var subtotal = 0.0
        var cantidadTotal = 0

        for (item in items) {
            subtotal += item.producto.precio * item.cantidad
            cantidadTotal += item.cantidad
        }

        val descuento = if (cantidadTotal > 3) {
            subtotal * 0.10 // descuento del 10%
        } else {
            0.0
        }

        val base = subtotal - descuento
        val impuestos = base * iva
        val totalFinal = base + impuestos

        return TotalPedido(subtotal, descuento, impuestos, totalFinal)
    }

    // cambiar estado del pedido
    fun cambiarEstado(nuevoEstado: EstadoPedido) {
        println("Estado del pedido #$id: $estado → $nuevoEstado")
        estado = nuevoEstado
    }

    // resumen total del pedido
    fun mostrarResumen() {
        val total = calcularTotal()

        println("----- RESUMEN DEL PEDIDO -----")
        println("Cliente: ${cliente.nombre}")
        println("Teléfono: ${cliente.telefono}")
        println("Dirección: ${cliente.direccion}")
        println("Correo: ${cliente.email}")
        println("-------------------------------")
        println("Productos: ")
        for (item in items) {
            println("- ${item.producto.nombre} x${item.cantidad} (\$${item.producto.precio} c/u)")
        }
        println("-------------------------------")
        println("Subtotal: $${total.subtotal}")
        println("Descuento (10%): $${total.descuento}")
        println("IVA: $${total.impuestos}")
        println("Total: $${total.totalFinal}")
        println("-------------------------------")
        println("Estado: $estado")
        val asignacion = repartidor?.let {
            "Repartidor: ${it.nombre}"
        } ?: "Entrega: retiro en sucursal central"
        println(asignacion)
    }
}