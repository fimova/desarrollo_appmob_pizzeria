package model

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
        return "Producto: $nombre | Precio: $$precio | Ingredientes: $ingredientesTexto"
    }
}