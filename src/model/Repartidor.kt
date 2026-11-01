package model

class Repartidor (
    nombre: String,
    telefono: String,
    email: String,
    val zona: String
) : Usuario(nombre, telefono, email) {

    // ==MÉTODOS==
    fun entregarPedido (pedido: Pedido) {
        println("Repartidor $nombre está entregando el pedido #${pedido.id}")
        pedido.cambiarEstado(EstadoPedido.EN_CAMINO)
    }
}