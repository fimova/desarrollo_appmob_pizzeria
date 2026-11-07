package model

import utils.formatearCLP

class Producto (
    val nombre: String,
    val precio: Double,
    val ingredientes: Map<String, Int>
) {

    // == MÉTODOS ==

    fun detallarProducto(): String {
        val ingredientesTexto = ingredientes.entries.joinToString { (nombre, cant) ->
            "$nombre x $cant"
        }
        return "Producto: $nombre | Precio: $${formatearCLP(precio)} | Ingredientes: $ingredientesTexto"
    }
}