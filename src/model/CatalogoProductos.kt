package model

object CatalogoProductos {

    val margarita = Producto(
        "Margarita",
        5000.0,
        ingredientes = mapOf("masa" to 1, "queso" to 2, "salsa" to 1)
    )

    val pepperoni = Producto(
        "Pepperoni",
        6500.0,
        ingredientes = mapOf(
            "masa" to 1,
            "queso" to 1,
            "salsa" to 1,
            "pepperoni" to 2
        )
    )

    val hawaiana = ProductoPromocional(
        "Hawaiana",
        6000.0,
        ingredientes = mapOf(
            "masa" to 1,
            "queso" to 1,
            "salsa" to 1,
            "piña" to 2
        )
    )

    private val productosRegistrados: LinkedHashSet<Producto> =
        linkedSetOf(margarita, pepperoni, hawaiana)

    // se expone como List para que Main y seleccionarProducto puedan seguir usándola
    val productos: List<Producto>
        get() = productosRegistrados.toList()

    // devuelve false si ya existe un producto con el mismo nombre y categoría
    fun registrarProducto(producto: Producto): Boolean {
        return productosRegistrados.add(producto)
    }
}