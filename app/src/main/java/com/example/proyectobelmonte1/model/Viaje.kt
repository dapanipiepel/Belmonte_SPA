
package com.example.proyectobelmonte1.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viajes")
data class Viaje(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pasajero: String,
    val fecha: String,
    val hora: String,
    val origen: String,
    val latitudOrigen: Double,
    val longitudOrigen: Double,
    val destino: String,
    val latitudDestino: Double,
    val longitudDestino: Double,
    val numeroVuelo: String? = null,
    val estado: EstadoViaje = EstadoViaje.PENDIENTE,
    val conductor: String? = null,
    val cantidadPasajeros: Int = 1,
    val vehiculoId: Long? = null
    val conductor: String? = null
)

