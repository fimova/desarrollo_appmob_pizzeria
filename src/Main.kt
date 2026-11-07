import model.EstadoPedido
import model.ItemPedido
import model.Pedido
import model.Producto
import model.Repartidor
import service.buscarRepartidorDisponible
import service.verificarYActualizarInventario
import ui.imprimirInventario
import ui.imprimirReportePedidos
import ui.imprimirResumenPedido
import ui.leerCantidadPositiva
import ui.mostrarResultadoInventario
import ui.registrarCliente
import ui.seleccionarProducto
import utils.validarZona

// ============================================
// Asignatura: Desarrollo App Móviles I (PMY2201)
// Semana 3 - Sumativa 1
// ============================================

fun main() {

    println("=== Bienvenido a La Pizzería ===")
    println("-------------------------------------------------")

    // INVENTARIO (Map)
    val inventario = mutableMapOf(
        "masa" to 50,
        "queso" to 50,
        "salsa" to 40,
        "pepperoni" to 30,
        "piña" to 20
    )

    println("Inventario inicial:")
    imprimirInventario(inventario)

    // PRODUCTOS (pizzas) como Producto
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

    val hawaiana = Producto(
        "Hawaiana",
        6000.0,
        ingredientes = mapOf(
            "masa" to 1,
            "queso" to 1,
            "salsa" to 1,
            "piña" to 2
        )
    )

    val menu = listOf(margarita, pepperoni, hawaiana)

    // REPARTIDORES (listOf + búsqueda por zona y cercanía)
    // un número menor en prioridadCercania representa mayor cercanía
    val repartidores = listOf(
        Repartidor("Carlos", "+56 9 1111 1111", "carlos@pizza.com", "Centro", 2),
        Repartidor("Camila", "+56 9 2222 2222", "camila@pizza.com", "Centro", 1),
        Repartidor("Ana", "+56 9 3333 3333", "ana@pizza.com", "Norte", 1),
        Repartidor("Luis", "+56 9 4444 4444", "luis@pizza.com", "Sur", 1)
    )

    // array de pedidos que se actualiza al confirmar cada pedido
    var pedidosDia: Array<Pedido> = emptyArray()

    var siguienteId = 1001L
    var seguir = true

    // se le da un nombre al loop (pedidoLoop) con una etiqueta
    pedidoLoop@ while (seguir) {
        println("\n=== Nuevo Pedido ===")

        // datos del cliente
        val cliente = registrarCliente()
        if (cliente == null) {
            break@pedidoLoop
        }

        print("Notas del pedido (opcional): ")
        val notasEntrada = readlnOrNull()
        if (notasEntrada == null) {
            break@pedidoLoop
        }

        val notas = if (notasEntrada.trim().isBlank()) {
            null
        } else {
            notasEntrada.trim()
        }

        print("Zona (Centro/Norte/Sur) o 'Sucursal central' para retirar: ")

        var zona: String? = null
        var zonaIngresadaCorrectamente = false

        while (!zonaIngresadaCorrectamente) {
            val zonaEntrada = readlnOrNull()
            if (zonaEntrada == null) {
                break@pedidoLoop
            }

            val zonaValidada = validarZona(zonaEntrada)

            when {
                zonaValidada != null -> {
                    zona = zonaValidada
                    zonaIngresadaCorrectamente = true
                }

                zonaEntrada.trim().equals("sucursal", ignoreCase = true) ||
                        zonaEntrada.trim().equals(
                            "sucursal central",
                            ignoreCase = true
                        ) -> {
                    zona = null
                    zonaIngresadaCorrectamente = true
                    println("El pedido será para retirar en sucursal central.")
                }

                else -> {
                    println(
                        "Zona inválida. Ingresa Centro, Norte, Sur " +
                                "o Sucursal central."
                    )
                    print("Zona: ")
                }
            }
        }

        // elegir pizza válida
        val productoSeleccionado = seleccionarProducto(menu)
        if (productoSeleccionado == null) {
            break@pedidoLoop
        }

        val cantidadLeida = leerCantidadPositiva(
            "¿Cuántas pizzas ${productoSeleccionado.nombre} desea? "
        )
        if (cantidadLeida == null) {
            break@pedidoLoop
        }

        val cantidad = cantidadLeida
        val items = listOf(ItemPedido(productoSeleccionado, cantidad))

        val pedido = Pedido(
            id = siguienteId,
            cliente = cliente,
            items = items,
            notas = notas
        )

        // verificar inventario con when y map
        // esta función se llama una sola vez porque también puede descontar el stock
        val resultadoInventario =
            verificarYActualizarInventario(pedido, inventario)

        mostrarResultadoInventario(resultadoInventario)

        if (!resultadoInventario.inventarioActualizado) {
            println("Pedido rechazado por falta de ingredientes.")
            println("Inventario actual:")
            imprimirInventario(inventario)
        } else {
            // 1. asignar repartidor
            // 2. transición estado: preparación -> listo
            val cambioAListo = pedido.cambiarEstado(EstadoPedido.LISTO)

            if (cambioAListo) {
                println(
                    "Estado del pedido #${pedido.id}: " +
                            "${EstadoPedido.EN_PREPARACION.etiqueta} → " +
                            "${pedido.estado.etiqueta}"
                )
                println("Pedido cocinado y empacado.")

                val zonaEntrega = zona

                if (zonaEntrega == null) {
                    println("Pedido listo para retirar en sucursal central.")
                } else {
                    val repartidor =
                        buscarRepartidorDisponible(zonaEntrega, repartidores)

                    // este caso ocurre si no hay un repartidor disponible para esa zona
                    if (repartidor == null) {
                        println(
                            "No hay repartidor disponible para la zona $zonaEntrega."
                        )
                        println(
                            "El pedido quedará listo para retirar en sucursal central."
                        )
                    } else {
                        val asignacionExitosa = repartidor.asignarPedido(pedido)

                        if (!asignacionExitosa) {
                            println(
                                "No se pudo asignar a ${repartidor.nombre}, " +
                                        "no está disponible."
                            )
                            println(
                                "El pedido quedará listo para retirar en sucursal central."
                            )
                        } else {
                            // 3. transición estado: listo -> en camino
                            // el repartidor toma el pedido
                            val estadoAnterior = pedido.estado
                            val entregaIniciada =
                                repartidor.entregarPedido(pedido)

                            if (entregaIniciada) {
                                println(
                                    "Estado del pedido #${pedido.id}: " +
                                            "${estadoAnterior.etiqueta} → " +
                                            "${pedido.estado.etiqueta}"
                                )
                                println(
                                    "${repartidor.nombre} ha salido hacia " +
                                            "la dirección del cliente."
                                )
                            } else {
                                println("No se pudo iniciar el despacho del pedido.")
                            }
                        }
                    }
                }
            } else {
                println("No se pudo cambiar el estado del pedido a Listo.")
            }

            imprimirResumenPedido(pedido)

            // agrega el pedido al array y avanza el id
            pedidosDia = pedidosDia + pedido
            siguienteId++

            println("\nInventario actualizado:")
            imprimirInventario(inventario)
        }

        // preguntar si desea otro pedido
        print("\n¿Desea ingresar otro pedido? (S/N): ")
        val respuesta = readlnOrNull()
        seguir = respuesta?.trim()?.equals("s", ignoreCase = true) == true
    }

    // resumen (array + for + filter)
    imprimirReportePedidos(pedidosDia)

    println()
    println("=== Gracias por usar el sistema de La Pizzería ===")
}