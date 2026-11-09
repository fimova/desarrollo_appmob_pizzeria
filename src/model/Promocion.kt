package model

import java.time.LocalDate

data class Promocion(
    val nombre: String,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate
) {
    fun estaVigente(fecha: LocalDate): Boolean {
        return !fechaInicio.isAfter(fechaFin) &&
                fecha in fechaInicio..fechaFin // rango de fecha
    }
}