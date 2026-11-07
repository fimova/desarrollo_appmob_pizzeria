package utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun formatearCLP(monto: Double): String {
    val formato = NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("es-CL")
    )

    formato.currency = Currency.getInstance("CLP")
    formato.minimumFractionDigits = 0
    formato.maximumFractionDigits = 0

    return formato.format(monto)
}