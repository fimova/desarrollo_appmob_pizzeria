package service

import model.CatalogoProductos
import model.Pedido
import model.Producto

// revisa con reflection y devuelve los productos que tengan la anotación
fun obtenerProductosPromocionables(): List<Producto> {
    return CatalogoProductos.productos.filter { it.esPromocionable() }
}

// revisar cuáles productos promocionables aparecen en un pedido
fun productosPromocionablesDelPedido(pedido: Pedido): List<Producto> {
    return pedido.productos
        .filter { it.esPromocionable() }
        .distinct()
}