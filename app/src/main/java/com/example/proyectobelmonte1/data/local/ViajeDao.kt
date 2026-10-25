package com.example.proyectobelmonte1.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.model.Viaje

@Dao
interface ViajeDao {

    @Insert
    suspend fun insertarViaje(viaje: Viaje): Long

    @Query("SELECT * FROM viajes ORDER BY id DESC")
    suspend fun obtenerViajes(): List<Viaje>

    @Query("SELECT * FROM viajes WHERE estado = :estado ORDER BY id DESC")
    suspend fun obtenerViajesPorEstado(
        estado: EstadoViaje
    ): List<Viaje>

    @Update
    suspend fun actualizarViaje(viaje: Viaje)

    @Delete
    suspend fun eliminarViaje(viaje: Viaje)
}
