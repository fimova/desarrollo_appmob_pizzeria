package model

import utils.formatearCLP
import kotlin.reflect.full.findAnnotation

open class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientes: Map<String, Int>,
    val categoria: CategoriaProducto = CategoriaProducto.PIZZA
) {

    // == MÉTODOS ==

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Producto) return false

        return nombre == other.nombre &&
                categoria == other.categoria
    }

    override fun hashCode(): Int {
        return 31 * nombre.hashCode() + categoria.hashCode()
    }

    fun detallarProducto(): String {
        val ingredientesTexto = ingredientes.entries.joinToString { (nombre, cant) ->
            "$nombre x $cant"
        }
        return "Producto: $nombre | Precio: ${formatearCLP(precio)} | Ingredientes: $ingredientesTexto"
    }

    fun esPromocionable(): Boolean {
        return this::class.findAnnotation<Promocionable>() != null
    }
}