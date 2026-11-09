package ui

import model.TotalPedido
import utils.TASA_DESCUENTO
import utils.formatearCLP

// centraliza el formato y el motivo del descuento para todos los resúmenes
internal fun formatearLineaDescuento(
    total: TotalPedido,
    etiqueta: String = "Descuento"
): String {
    val motivo = when {
        total.descuentoPorCantidad && total.descuentoPorPromocion ->
            "por cantidad y fecha de promoción vigente; se aplica una sola vez"

        total.descuentoPorCantidad ->
            "por cantidad"

        total.descuentoPorPromocion ->
            "por fecha de promoción vigente"

        else ->
            "no aplica"
    }

    val porcentaje = (TASA_DESCUENTO * 100).toInt()

    return "$etiqueta ($porcentaje%, $motivo): ${formatearCLP(total.descuento)}"
}