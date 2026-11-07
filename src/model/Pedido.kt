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

    // == MÉTODOS ==

    // cáculo del total, junto con iva y también descuento si hay
    fun calcularTotal(): TotalPedido {
        var subtotal = 0.0
        var cantidadTotal = 0

        for (item in items) {
            subtotal += item.producto.precio * item.cantidad
            cantidadTotal += item.cantidad
        }

        // descuento del 10% se aplica si pide sobre 3 pizza o si está dentro de
        // la fecha de promoción
        // NO se duplica el descuento
        val promocionVigente =
            promocion?.estaVigente(fechaPedido) == true

        val descuentoAplica =
            cantidadTotal > UMBRAL_PIZZAS_DESCUENTO || promocionVigente

        val descuento = if (descuentoAplica) {
            subtotal * TASA_DESCUENTO
        } else {
            0.0
        }

        val neto = subtotal - descuento
        val impuestos = neto * TASA_IVA
        val totalFinal = neto + impuestos

        return TotalPedido(subtotal, descuento, impuestos, totalFinal)
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