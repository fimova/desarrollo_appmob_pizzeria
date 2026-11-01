import model.Cliente
import model.EstadoPedido
import model.ItemPedido
import model.Pedido
import model.Producto
import model.Repartidor
import utils.imprimirInventario
import utils.leerCantidadPositiva
import utils.validarEmail
import utils.validarTelefono
import utils.validarZona
import utils.verificarYActualizarInventario

// ============================================
// Asignatura: Desarrollo App Móviles I (PMY2201)
// Semana 2 - Formativa: Aplicando utilidades de Kotlin
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
        ingredientes = mapOf("masa" to 1, "queso" to 1, "salsa" to 1, "pepperoni" to 2)
    )

    val hawaiana = Producto(
        "Hawaiana",
        6000.0,
        ingredientes = mapOf("masa" to 1, "queso" to 1, "salsa" to 1, "piña" to 2)
    )

    val menu = listOf(margarita, pepperoni, hawaiana)

    // REPARTIDORES (listOf + find)
    val repartidores = listOf(
        Repartidor("Carlos", "+569 11111111", "carlos@pizza.com", "Centro"),
        Repartidor("Ana", "+569 22222222", "ana@pizza.com", "Norte"),
        Repartidor("Luis", "+569 33333333", "luis@pizza.com", "Sur")
    )

    val pedidosDia = mutableListOf<Pedido>() // se convertirá en array
    var siguienteId = 1001L
    var seguir = true

    while (seguir) {
        println("\n=== Nuevo Pedido ===")

        // datos del cliente
        print("Nombre cliente: ")
        val nombreEntrada = readlnOrNull() ?: return
        val nombre = nombreEntrada.trim().ifBlank {
            println("Nombre vacío. Se usará 'Sin nombre'.")
            "Sin nombre"
        }

        print("Teléfono (9 dígitos, comenzando con 9): ")
        val telefonoEntrada = readlnOrNull() ?: return
        val telefono = validarTelefono(telefonoEntrada)

        print("Correo electrónico: ")
        val emailEntrada = readlnOrNull() ?: return
        val email = validarEmail(emailEntrada)

        print("Dirección: ")
        val direccionEntrada = readlnOrNull() ?: return
        val direccion = direccionEntrada.trim().ifBlank {
            println("Dirección vacía. Se usará 'Sin dirección'.")
            "Sin dirección"
        }

        print("Zona (Centro/Norte/Sur) o 'Sucursal central' para retirar: ")

        var zona: String? = null
        var zonaIngresadaCorrectamente = false

        while (!zonaIngresadaCorrectamente) {
            val zonaEntrada = readlnOrNull() ?: return
            val zonaValidada = validarZona(zonaEntrada)

            when {
                zonaValidada != null -> {
                    zona = zonaValidada
                    zonaIngresadaCorrectamente = true
                }

                zonaEntrada.trim().equals("sucursal", ignoreCase = true) ||
                        zonaEntrada.trim().equals("sucursal central", ignoreCase = true) -> {
                    zona = null
                    zonaIngresadaCorrectamente = true
                    println("El pedido será para retirar en sucursal central.")
                }

                else -> {
                    println("Zona inválida. Ingresa Centro, Norte, Sur o Sucursal central.")
                    print("Zona: ")
                }
            }
        }

        val cliente = Cliente(nombre, telefono, email, direccion)

        // mostrar menú
        println("\n--- Menú ---")
        menu.forEachIndexed { idx, producto ->
            println("${idx + 1}. ${producto.detallarProducto()}")
        }

        // elegir pizza válida
        var productoElegido: Producto? = null

        while (productoElegido == null) {
            print("Elija una pizza (1-${menu.size}): ")
            val opcionEntrada = readlnOrNull() ?: return
            val opcion = opcionEntrada.trim().toIntOrNull()

            if (opcion == null || opcion !in 1..menu.size) {
                println("Opción inválida. Ingrese un número del 1 al ${menu.size}.")
            } else {
                productoElegido = menu[opcion - 1]
            }
        }

        // comprobó que no es null
        val productoSeleccionado = productoElegido ?: return

        val cantidad = leerCantidadPositiva(
            "¿Cuántas pizzas ${productoSeleccionado.nombre} desea? "
        ) ?: return

        val items = listOf(ItemPedido(productoSeleccionado, cantidad))

        val pedido = Pedido(
            id = siguienteId,
            cliente = cliente,
            items = items,
            estado = EstadoPedido.EN_PREPARACION
        )

        siguienteId++

        // verificar inventario con when y map
        val inventarioOk = verificarYActualizarInventario(pedido, inventario)

        if (!inventarioOk) {
            println("Pedido rechazado por falta de ingredientes.")
            println("Inventario actual:")
            imprimirInventario(inventario)
        } else {
            // 1. asignar repartidor
            // 2. transición estado: preparación -> listo
            pedido.cambiarEstado(EstadoPedido.LISTO)
            println("Pedido cocinado y empacado.")

            val zonaEntrega = zona

            if (zonaEntrega == null) {
                pedido.repartidor = null // usuario decide retirar en sucursal
                println("Pedido listo para retirar en sucursal central.")
            } else {
                val repartidor = repartidores.find {
                    it.zona.equals(zonaEntrega, ignoreCase = true)
                }

                // este if solo ocurre si no hubiera un repartidor para esa zona
                // validación si por ejemplo se eliminara un repartidor
                if (repartidor == null) {
                    pedido.repartidor = null
                    println("No hay repartidor disponible para la zona $zonaEntrega.")
                    println("El pedido quedará listo para retirar en sucursal central.")
                } else {
                    pedido.repartidor = repartidor // caso normal

                    // 3. transición estado: listo -> en camino (repartidor toma el pedido)
                    repartidor.entregarPedido(pedido) // dentro cambia a EN_CAMINO
                    println("${repartidor.nombre} ha salido hacia la dirección del cliente.")
                }
            }

            pedido.mostrarResumen()
            pedidosDia.add(pedido)

            println("\nInventario actualizado:")
            imprimirInventario(inventario)
        }

        // preguntar si desea otro pedido
        print("\n¿Desea ingresar otro pedido? (S/N): ")
        val respuesta = readlnOrNull()
        seguir = respuesta?.trim()?.equals("s", ignoreCase = true) == true
    }

    // ====== RESUMEN (array + for + filter) ======
    println("\n=== Reporte de pedidos del día ===")

    val pedidosArray: Array<Pedido> = pedidosDia.toTypedArray()

    for (pedido in pedidosArray) {
        val totales = pedido.calcularTotal()

        var cantidadTotalPizzas = 0
        for (item in pedido.items) {
            cantidadTotalPizzas += item.cantidad
        }

        println(
            "Cliente: ${pedido.cliente.nombre} | " +
                    "Pizza(s): $cantidadTotalPizzas | " +
                    "Total: $${totales.totalFinal} | " +
                    "Estado: ${pedido.estado}"
        )
    }

    val completados = pedidosDia.filter {
        it.estado == EstadoPedido.LISTO || it.estado == EstadoPedido.EN_CAMINO
    }

    val pendientes = pedidosDia.filter {
        it.estado == EstadoPedido.EN_PREPARACION
    }

    println("Pedidos completados: ${completados.size}")
    println("Pedidos pendientes:  ${pendientes.size}")

    println()
    println("=== Gracias por usar el sistema de La Pizzería ===")
}