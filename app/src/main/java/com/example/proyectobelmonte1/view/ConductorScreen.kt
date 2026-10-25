package com.example.proyectobelmonte1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.model.Viaje
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel

@Composable
fun ConductorScreen(
    viajeViewModel: ViajeViewModel,
    onAbrirDetalle: (Long) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val viajesOrdenados = viajeViewModel.ordenarCronologicamente(viajeViewModel.viajes)
    val viajesAsignados = viajesOrdenados.filter {
        it.estado == EstadoViaje.ASIGNADO
    }
    val viajesEnCurso = viajesOrdenados.filter {
        it.estado == EstadoViaje.EN_CURSO
    }
    val viajesFinalizados = viajesOrdenados.filter {
        it.estado == EstadoViaje.FINALIZADO
    }

    ObservarNotificaciones(viajeViewModel, snackbarHostState)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "BELMONTE",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Panel del conductor",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Consulta y gestiona los viajes asignados.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SeccionViajes(
                titulo = "Viajes asignados (${viajesAsignados.size})",
                mensajeVacio = "No hay viajes pendientes de iniciar.",
                viajes = viajesAsignados,
                viajeViewModel = viajeViewModel,
                onAbrirDetalle = onAbrirDetalle
            )

            SeccionViajes(
                titulo = "Viajes en curso (${viajesEnCurso.size})",
                mensajeVacio = "No tienes viajes en curso.",
                viajes = viajesEnCurso,
                viajeViewModel = viajeViewModel,
                onAbrirDetalle = onAbrirDetalle
            )

            SeccionViajes(
                titulo = "Viajes finalizados (${viajesFinalizados.size})",
                mensajeVacio = "Todavía no hay viajes finalizados.",
                viajes = viajesFinalizados,
                viajeViewModel = viajeViewModel,
                onAbrirDetalle = onAbrirDetalle
            )
        }
    }
}

@Composable
private fun SeccionViajes(
    titulo: String,
    mensajeVacio: String,
    viajes: List<Viaje>,
    viajeViewModel: ViajeViewModel,
    onAbrirDetalle: (Long) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = titulo,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (viajes.isEmpty()) {
            Text(
                text = mensajeVacio,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            viajes.forEach { viaje ->
                TarjetaViajeConductor(
                    viaje = viaje,
                    vehiculo = viajeViewModel.obtenerVehiculo(viaje.vehiculoId)?.descripcion,
                    onClick = { onAbrirDetalle(viaje.id) }
                )
            }
        }
    }
}

@Composable
private fun TarjetaViajeConductor(
    viaje: Viaje,
    vehiculo: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${viaje.origen} → ${viaje.destino}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(text = "Pasajero: ${viaje.pasajero}")
            Text(text = "Cantidad de pasajeros: ${viaje.cantidadPasajeros}")
            Text(text = "Fecha: ${viaje.fecha}")
            Text(text = "Hora: ${viaje.hora}")
            Text(text = "Conductor: ${viaje.conductor ?: "Sin asignar"}")
            Text(text = "Vehículo: ${vehiculo ?: "Sin asignar"}")

            Text(
                text = "Estado: ${viaje.estado}",
                color = MaterialTheme.colorScheme.primary
            )

            if (!viaje.numeroVuelo.isNullOrBlank()) {
                Text(text = "Vuelo: ${viaje.numeroVuelo}")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver detalle del viaje")
            }
        }
    }
}
