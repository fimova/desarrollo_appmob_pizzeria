package utils

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

fun inspeccionarClase(clase: KClass<*>): List<String> {
    val lineas = mutableListOf<String>()
    val nombreClase = clase.simpleName ?: clase.qualifiedName ?: "sin nombre"
    lineas += "Clase: $nombreClase"

    val propiedadesDeclaradas = clase.declaredMemberProperties.sortedBy { it.name }
    val funcionesDeclaradas = clase.declaredMemberFunctions.sortedBy { it.name }

    lineas += "Propiedades declaradas:"
    if (propiedadesDeclaradas.isEmpty()) {
        lineas += "  (ninguna)"
    } else {
        propiedadesDeclaradas.forEach { propiedad ->
            lineas += "  propiedad ${propiedad.name}: ${propiedad.returnType}"
        }
    }

    lineas += "Funciones declaradas:"
    if (funcionesDeclaradas.isEmpty()) {
        lineas += "  (ninguna)"
    } else {
        funcionesDeclaradas.forEach { funcion ->
            lineas += "  funcion ${funcion.name}: ${funcion.returnType}"
        }
    }

    val nombresPropiedadesDeclaradas = propiedadesDeclaradas.map { it.name }.toSet()
    val propiedadesHeredadas = clase.memberProperties
        .filterNot { it.name in nombresPropiedadesDeclaradas }
        .sortedBy { it.name }

    lineas += "Propiedades heredadas:"
    if (propiedadesHeredadas.isEmpty()) {
        lineas += "  (ninguna)"
    } else {
        propiedadesHeredadas.forEach { propiedad ->
            lineas += "  propiedad ${propiedad.name}: ${propiedad.returnType}"
        }
    }

    val nombresFuncionesDeclaradas = funcionesDeclaradas.map { it.name }.toSet()
    val funcionesHeredadas = clase.memberFunctions
        .filterNot { it.name in nombresFuncionesDeclaradas }
        .sortedBy { it.name }

    lineas += "Funciones heredadas:"
    if (funcionesHeredadas.isEmpty()) {
        lineas += "  (ninguna)"
    } else {
        funcionesHeredadas.forEach { funcion ->
            lineas += "  funcion ${funcion.name}: ${funcion.returnType}"
        }
    }

    return lineas
}