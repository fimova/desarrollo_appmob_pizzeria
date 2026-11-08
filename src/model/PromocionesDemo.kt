package model

import java.time.LocalDate

// rango de demostración de siete días antes y después de la fecha en que se inicia el programa
// fecha de los pedidos queda dentro del rango al probar el programa
object PromocionesDemo {

    private val fechaReferencia = LocalDate.now()

    val promocionActual = Promocion(
        nombre = "Promoción de demostración",
        fechaInicio = fechaReferencia.minusDays(7),
        fechaFin = fechaReferencia.plusDays(7)
    )
}