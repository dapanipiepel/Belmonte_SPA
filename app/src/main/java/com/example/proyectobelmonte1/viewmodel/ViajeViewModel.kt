
package com.example.proyectobelmonte1.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobelmonte1.data.ViajeRepository
import com.example.proyectobelmonte1.data.local.AppDatabase
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.model.Viaje
import kotlinx.coroutines.launch

class ViajeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ViajeRepository

    var viajes by mutableStateOf<List<Viaje>>(emptyList())
        private set

    var mensaje by mutableStateOf("")
        private set

    init {
        val viajeDao = AppDatabase.getDatabase(application).viajeDao()
        repository = ViajeRepository(viajeDao)
        cargarViajes()
    }

    fun cargarViajes() {
        viewModelScope.launch {
            viajes = repository.obtenerViajes()
        }
    }

    fun insertarViaje(viaje: Viaje) {
        viewModelScope.launch {
            repository.insertarViaje(viaje)
            viajes = repository.obtenerViajes()
            mensaje = "Viaje guardado correctamente"
        }
    }

    fun actualizarEstado(viaje: Viaje, nuevoEstado: EstadoViaje) {
        viewModelScope.launch {
            val viajeActualizado = viaje.copy(estado = nuevoEstado)
            repository.actualizarViaje(viajeActualizado)
            viajes = repository.obtenerViajes()
        }
    }


    fun asignarConductor(viaje: Viaje, conductor: String) {
        viewModelScope.launch {
            val viajeActualizado = viaje.copy(
                conductor = conductor,
                estado = EstadoViaje.ASIGNADO
            )

            repository.actualizarViaje(viajeActualizado)
            viajes = repository.obtenerViajes()
            mensaje = "Viaje asignado a $conductor"
        }
    }

}
