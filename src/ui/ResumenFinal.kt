package ui

import model.CatalogoProductos
import model.Cliente
import model.Pedido
import model.Producto
import service.obtenerProductosPromocionables
import utils.TASA_DESCUENTO
import utils.formatearCLP
import utils.inspeccionarClase

fun imprimirResumenFinal(
    pedidosDia: Array<Pedido>,
    clientesRegistrados: Set<Cliente>
) {
    println("\n=== RESUMEN FINAL ===")

    imprimirPedidoCombinado(pedidosDia)
    imprimirClienteDesestructurado(pedidosDia, clientesRegistrados)
    imprimirProductosPromocionables()
    imprimirValidacionIgualdad()
    imprimirInspeccionReflection()
}

private fun imprimirPedidoCombinado(pedidosDia: Array<Pedido>) {
    println("\n--- Combinación de pedidos (operator +) ---")

    val pedidosCompatibles = buscarPedidosCompatibles(pedidosDia)

    if (pedidosCompatibles == null) {
        println(
            "No se encontraron dos pedidos compatibles para combinar. " +
                    "Para probarlo, se deben ingresar dos pedidos para el mismo cliente, " +
                    "en la misma fecha y con la misma promoción."
        )
        return
    }

    // desestructuración del Pair que contiene los dos pedidos compatibles
    val (primerPedido, segundoPedido) = pedidosCompatibles
    val pedidoCombinado = primerPedido + segundoPedido

    // desestructuración del Pedido usando sus component1, component2 y component3
    val (clienteCombinado, productosCombinados, totalFinalCombinado) =
        pedidoCombinado

    val total = pedidoCombinado.calcularTotal()

    println("Pedidos combinados: #${primerPedido.id} + #${segundoPedido.id}")
    println("Cliente extraído del pedido: ${clienteCombinado.nombre}")
    println(
        "Productos extraídos del pedido: " +
                productosCombinados.joinToString { it.nombre }
    )

    println("Detalle de productos y cantidades:")
    pedidoCombinado.items.forEach { item ->
        println("- ${item.producto.nombre} x${item.cantidad}")
    }

    val motivoDescuento = when {
        total.descuentoPorCantidad && total.descuentoPorPromocion ->
            "por cantidad y fecha de promoción vigente; se aplica una sola vez"

        total.descuentoPorCantidad ->
            "por cantidad"

        total.descuentoPorPromocion ->
            "por fecha de promoción vigente"

        else ->
            "no aplica"
    }

    val porcentajeDescuento = (TASA_DESCUENTO * 100).toInt()

    println("Subtotal combinado: ${formatearCLP(total.subtotal)}")
    println(
        "Descuento combinado ($porcentajeDescuento%, $motivoDescuento): " +
                formatearCLP(total.descuento)
    )
    println("IVA: ${formatearCLP(total.impuestos)}")
    println(
        "Total combinado (extraído por desestructuración): " +
                formatearCLP(totalFinalCombinado)
    )
}

private fun buscarPedidosCompatibles(
    pedidosDia: Array<Pedido>
): Pair<Pedido, Pedido>? {
    for (i in pedidosDia.indices) {
        for (j in i + 1 until pedidosDia.size) {
            val primero = pedidosDia[i]
            val segundo = pedidosDia[j]

            val sonCompatibles =
                primero.cliente == segundo.cliente &&
                        primero.fechaPedido == segundo.fechaPedido &&
                        primero.promocion == segundo.promocion

            if (sonCompatibles) {
                return primero to segundo
            }
        }
    }

    return null
}

private fun imprimirClienteDesestructurado(
    pedidosDia: Array<Pedido>,
    clientesRegistrados: Set<Cliente>
) {
    println("\n--- Cliente desestructurado ---")

    val cliente = pedidosDia.firstOrNull()?.cliente
        ?: clientesRegistrados.firstOrNull()

    if (cliente == null) {
        println("No hay clientes registrados para mostrar.")
        return
    }

    // desestructuración: component1 = nombre, component2 = email, component3 = teléfono.
    val (nombre, correo, telefono) = cliente

    println("Nombre: $nombre")
    println("Correo electrónico: $correo")
    println("Teléfono: $telefono")
}

private fun imprimirProductosPromocionables() {
    println("\n--- Productos promocionables (anotaciones + reflection) ---")

    val promocionables = obtenerProductosPromocionables()

    if (promocionables.isEmpty()) {
        println("No hay productos promocionables en el catálogo.")
    } else {
        promocionables.forEach { println("- ${it.nombre}") }
    }
}

private fun imprimirValidacionIgualdad() {
    println("\n--- Validación de igualdad de objetos ---")

    // productos con el mismo nombre y categoría se consideran iguales
    val p1 = Producto("Margarita", 5000.0, emptyMap())
    val p2 = Producto("Margarita", 9999.0, emptyMap())
    println("¿Productos iguales (Margarita, misma categoría)? ${p1 == p2}")

    // clientes con el mismo nombre y correo se consideran duplicados
    val c1 = Cliente("Juan", "912345678", "juan@mail.com", "Calle 1")
    val c2 = Cliente("Juan", "998877665", "juan@mail.com", "Calle 2")
    val setDemo = mutableSetOf(c1)
    val agregado = setDemo.add(c2)
    println("¿Cliente duplicado evitado (mismo nombre y correo)? ${!agregado}")

    // el catálogo rechaza un producto igual a uno que ya contiene
    val duplicadoHawaiana = Producto("Hawaiana", 1111.0, emptyMap())
    val registrado = CatalogoProductos.registrarProducto(duplicadoHawaiana)
    println("¿Producto duplicado rechazado en catálogo? ${!registrado}")
    println("Tamaño actual del catálogo: ${CatalogoProductos.productos.size}")
}

private fun imprimirInspeccionReflection() {
    println("\n--- Inspección de clases (Reflection) ---")

    listOf(Pedido::class, Producto::class, Cliente::class).forEach { clase ->
        inspeccionarClase(clase).forEach { println(it) }
        println()
    }
}