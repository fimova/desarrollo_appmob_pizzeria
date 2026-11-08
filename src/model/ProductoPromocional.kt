package model

@Promocionable
class ProductoPromocional(
    nombre: String,
    precio: Double,
    ingredientes: Map<String, Int>,
    categoria: CategoriaProducto = CategoriaProducto.PIZZA
) : Producto(nombre, precio, ingredientes, categoria)