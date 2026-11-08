package model

import utils.TASA_DESCUENTO
import utils.TASA_IVA
import utils.UMBRAL_PIZZAS_DESCUENTO
import java.time.LocalDate

class Pedido(
    val id: Long,
    val cliente: Cliente,
    val items: List<ItemPedido>,
    val notas: String? = null,
    val fechaPedido: LocalDate = LocalDate.now(),
    val promocion: Promocion? = null
) {

    var estado: EstadoPedido = EstadoPedido.EN_PREPARACION
        private set

    var repartidor: Repartidor? = null
        private set

    // productos del pedido; las cantidades se conservan en items
    val productos: List<Producto>
        get() = items.map { it.producto }

    // == DESESTRUCTURACIÓN ==

    // permite: val (cliente, productos, total) = pedido
    operator fun component1(): Cliente = cliente

    operator fun component2(): List<Producto> = productos

    operator fun component3(): Double = calcularTotal().totalFinal

    // == MÉTODOS ==

    // cáculo del total, junto con iva y también descuento si hay
    fun calcularTotal(): TotalPedido {
        var subtotal = 0.0
        var cantidadTotal = 0

        for (item in items) {
            subtotal += item.producto.precio * item.cantidad
            cantidadTotal += item.cantidad
        }

        // el descuento se aplica si se supera el umbral de cantidad o si hay
        // una promoción vigente para al menos un producto promocionable
        // si se cumplen ambas condiciones, se aplica una sola vez
        val descuentoPorCantidad =
            cantidadTotal > UMBRAL_PIZZAS_DESCUENTO

        val incluyeProductoPromocionable =
            items.any { item -> item.producto.esPromocionable() }

        val descuentoPorPromocion =
            incluyeProductoPromocionable &&
                    (promocion?.estaVigente(fechaPedido) == true)

        val descuentoAplica =
            descuentoPorCantidad || descuentoPorPromocion

        val descuento = if (descuentoAplica) {
            subtotal * TASA_DESCUENTO
        } else {
            0.0
        }

        val neto = subtotal - descuento
        val impuestos = neto * TASA_IVA
        val totalFinal = neto + impuestos

        return TotalPedido(
            subtotal = subtotal,
            descuento = descuento,
            impuestos = impuestos,
            totalFinal = totalFinal,
            descuentoPorCantidad = descuentoPorCantidad,
            descuentoPorPromocion = descuentoPorPromocion
        )
    }

    // combinar dos pedidos
    operator fun plus(otro: Pedido): Pedido {
        require(cliente == otro.cliente) {
            "Solo se pueden combinar pedidos del mismo cliente."
        }
        require(fechaPedido == otro.fechaPedido) {
            "Solo se pueden combinar pedidos de la misma fecha."
        }
        require(promocion == otro.promocion) {
            "Solo se pueden combinar pedidos con la misma promoción."
        }

        // agrupar los productos iguales y sumar sus cantidades
        val cantidadesPorProducto = linkedMapOf<Producto, Int>()

        for (item in items + otro.items) {
            val cantidadActual = cantidadesPorProducto[item.producto] ?: 0
            cantidadesPorProducto[item.producto] =
                cantidadActual + item.cantidad
        }

        val itemsCombinados = cantidadesPorProducto.map { (producto, cantidad) ->
            ItemPedido(producto, cantidad)
        }

        val notasCombinadas = when {
            notas.isNullOrBlank() -> otro.notas
            otro.notas.isNullOrBlank() -> notas
            else -> "$notas | ${otro.notas}"
        }

        return Pedido(
            id = id,
            cliente = cliente,
            items = itemsCombinados,
            notas = notasCombinadas,
            fechaPedido = fechaPedido,
            promocion = promocion
        )
    }

    // cambiar estado del pedido
    fun cambiarEstado(nuevoEstado: EstadoPedido): Boolean {
        val transicionPermitida = when (estado) {
            EstadoPedido.EN_PREPARACION ->
                nuevoEstado == EstadoPedido.LISTO

            EstadoPedido.LISTO ->
                nuevoEstado == EstadoPedido.EN_CAMINO

            EstadoPedido.EN_CAMINO -> false
        }

        if (transicionPermitida) {
            estado = nuevoEstado
        }

        return transicionPermitida
    }

    // asignar repartidor al pedido
    fun asignarRepartidor(nuevoRepartidor: Repartidor): Boolean {
        if (estado != EstadoPedido.LISTO || repartidor != null) {
            return false
        }

        repartidor = nuevoRepartidor
        return true
    }
}