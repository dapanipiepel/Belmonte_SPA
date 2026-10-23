package com.example.proyectobelmonte1.data.local

import androidx.room.TypeConverter
import com.example.proyectobelmonte1.model.EstadoViaje

class EstadoViajeConverter {

    @TypeConverter
    fun fromEstadoViaje(estado: EstadoViaje): String {
        return estado.name
    }

    @TypeConverter
    fun toEstadoViaje(valor: String): EstadoViaje {
        return EstadoViaje.valueOf(valor)
    }
}
