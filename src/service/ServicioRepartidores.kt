package service

import model.Repartidor

fun buscarRepartidorDisponible(
    zona: String,
    repartidores: List<Repartidor>
): Repartidor? {
    val candidatos = repartidores
        .filter {
            it.disponible &&
                    it.zona.equals(zona, ignoreCase = true)
        }
        .sortedBy { it.prioridadCercania }

    return candidatos.firstOrNull()
}