package com.example.proyectobelmonte1.model

data class Vehiculo(
    val id: Long,
    val marca: String,
    val modelo: String,
    val patente: String,
    val capacidadPasajeros: Int,
    val activo: Boolean = true
) {
    val descripcion: String
        get() = "$marca $modelo · $patente"
}
