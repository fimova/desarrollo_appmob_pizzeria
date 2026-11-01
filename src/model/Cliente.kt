package model

class Cliente (
    nombre: String,
    telefono: String,
    email: String,
    val direccion: String,
) : Usuario(nombre, telefono, email)