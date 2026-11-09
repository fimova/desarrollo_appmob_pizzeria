package model

class Cliente(
    nombre: String,
    telefono: String,
    email: String,
    val direccion: String,
) : Usuario(nombre, telefono, email) {

    // == DESESTRUCTURACIÓN ==

    operator fun component1(): String = nombre
    operator fun component2(): String = email
    operator fun component3(): String = telefono

    // == IGUALDAD ==
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cliente) return false

        return nombre == other.nombre &&
                email == other.email
    }

    override fun hashCode(): Int {
        return 31 * nombre.hashCode() + email.hashCode()
    }
}
