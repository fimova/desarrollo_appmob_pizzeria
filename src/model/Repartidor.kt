package model

class Repartidor(
    nombre: String,
    telefono: String,
    email: String,
    val zona: String,
    val prioridadCercania: Int
) : Usuario(nombre, telefono, email) {

    var disponible: Boolean = true
        private set

    // ==MÉTODOS==

    // asignar un pedido al repartidor si está disponible
    fun asignarPedido(pedido: Pedido): Boolean {
        if (!disponible) {
            return false
        }

        val asignacionCorrecta = pedido.asignarRepartidor(this)

        if (asignacionCorrecta) {
            disponible = false
        }

        return asignacionCorrecta
    }

    // entregar el pedido, cambiando estado a EN_CAMINO
    fun entregarPedido(pedido: Pedido): Boolean {
        if (pedido.repartidor !== this) {
            return false
        }

        return pedido.cambiarEstado(EstadoPedido.EN_CAMINO)
    }
}