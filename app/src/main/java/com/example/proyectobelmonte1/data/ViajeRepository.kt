
package com.example.proyectobelmonte1.data

import com.example.proyectobelmonte1.data.local.ViajeDao
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.model.Viaje

class ViajeRepository(
    private val viajeDao: ViajeDao
) {

    suspend fun insertarViaje(viaje: Viaje): Long {
        return viajeDao.insertarViaje(viaje)
    }

    suspend fun obtenerViajes(): List<Viaje> {
        return viajeDao.obtenerViajes()
    }

    suspend fun obtenerViajesPorEstado(
        estado: EstadoViaje
    ): List<Viaje> {
        return viajeDao.obtenerViajesPorEstado(estado)
    }

    suspend fun actualizarViaje(viaje: Viaje) {
        viajeDao.actualizarViaje(viaje)
    }

    suspend fun eliminarViaje(viaje: Viaje) {
        viajeDao.eliminarViaje(viaje)
    }
}
