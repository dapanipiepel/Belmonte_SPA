
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
import com.example.proyectobelmonte1.model.Vehiculo
import com.example.proyectobelmonte1.model.Viaje
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ViajeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ViajeRepository

    private val _notificaciones = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val notificaciones = _notificaciones.asSharedFlow()

    val conductoresDisponibles = listOf("Carlos Soto", "Ana Torres")
    val vehiculosDisponibles = listOf(
        Vehiculo(1, "Toyota", "Corolla", "ABCD-12", 4),
        Vehiculo(2, "Hyundai", "Staria", "EFGH-34", 8),
        Vehiculo(3, "Mercedes-Benz", "Sprinter", "IJKL-56", 16)
    )

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
            _notificaciones.emit("Solicitud de viaje registrada")
        }
    }

    fun actualizarEstado(viaje: Viaje, nuevoEstado: EstadoViaje) {
        viewModelScope.launch {
            val viajeActualizado = viaje.copy(estado = nuevoEstado)
            repository.actualizarViaje(viajeActualizado)
            viajes = repository.obtenerViajes()
            val notificacion = when (nuevoEstado) {
                EstadoViaje.EN_CURSO -> "Viaje iniciado"
                EstadoViaje.FINALIZADO -> "Viaje finalizado"
                else -> "Estado del viaje actualizado"
            }
            _notificaciones.emit(notificacion)
        }
    }


    fun asignarConductor(
        viaje: Viaje,
        conductor: String,
        vehiculo: Vehiculo
    ) {
        viewModelScope.launch {
            if (!vehiculo.activo || vehiculo.capacidadPasajeros < viaje.cantidadPasajeros) {
                _notificaciones.emit(
                    "El vehículo no tiene capacidad suficiente para los pasajeros"
                )
                return@launch
            }

            val viajeActualizado = viaje.copy(
                conductor = conductor,
                vehiculoId = vehiculo.id,
                estado = EstadoViaje.ASIGNADO
            )

            repository.actualizarViaje(viajeActualizado)
            viajes = repository.obtenerViajes()
            mensaje = "Viaje asignado a $conductor"
            _notificaciones.emit(
                "Viaje asignado a $conductor con ${vehiculo.descripcion}"
            )
        }
    }

    fun obtenerVehiculo(id: Long?): Vehiculo? {
        return vehiculosDisponibles.find { it.id == id }
    }

    fun ordenarCronologicamente(viajes: List<Viaje>): List<Viaje> {
        val formatoFechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return viajes.sortedBy { viaje ->
            try {
                LocalDateTime.parse(
                    "${viaje.fecha} ${viaje.hora}",
                    formatoFechaHora
                )
            } catch (e: DateTimeParseException) {
                LocalDateTime.MAX
            }
        }
    }

}
