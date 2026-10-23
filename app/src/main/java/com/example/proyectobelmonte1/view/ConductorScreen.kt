
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.model.Viaje
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@Composable
fun ConductorScreen() {

    val viajeViewModel: ViajeViewModel = viewModel()

    val viajesAsignados = viajeViewModel.viajes.filter {
        it.estado == EstadoViaje.ASIGNADO
    }

    val viajesEnCurso = viajeViewModel.viajes.filter {
        it.estado == EstadoViaje.EN_CURSO
    }

    val viajesFinalizados = viajeViewModel.viajes.filter {
        it.estado == EstadoViaje.FINALIZADO
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Viajes asignados (${viajesAsignados.size})",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (viajesAsignados.isEmpty()) {
            Text(
                text = "No hay viajes pendientes de iniciar.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            viajesAsignados.forEach { viaje ->
                TarjetaViajeConductor(
                    viaje = viaje,
                    textoBoton = "Iniciar viaje",
                    onClick = {
                        viajeViewModel.actualizarEstado(
                            viaje,
                            EstadoViaje.EN_CURSO
                        )
                    }
                )
            }
        }

        Text(
            text = "Viajes en curso (${viajesEnCurso.size})",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (viajesEnCurso.isEmpty()) {
            Text(
                text = "No tienes viajes en curso.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            viajesEnCurso.forEach { viaje ->
                TarjetaViajeConductor(
                    viaje = viaje,
                    textoBoton = "Finalizar viaje",
                    onClick = {
                        viajeViewModel.actualizarEstado(
                            viaje,
                            EstadoViaje.FINALIZADO
                        )
                    }
                )
            }
        }

        Text(
            text = "Viajes finalizados (${viajesFinalizados.size})",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (viajesFinalizados.isEmpty()) {
            Text(
                text = "Todavía no hay viajes finalizados.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            viajesFinalizados.forEach { viaje ->
                TarjetaViajeConductor(
                    viaje = viaje,
                    textoBoton = null,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun TarjetaViajeConductor(
    viaje: Viaje,
    textoBoton: String?,
    onClick: () -> Unit

) { val context = LocalContext.current
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
            Text(text = "Fecha: ${viaje.fecha}")
            Text(text = "Hora: ${viaje.hora}")
            Text(text = "Conductor: ${viaje.conductor ?: "Sin asignar"}")

            Text(
                text = "Estado: ${viaje.estado}",
                color = MaterialTheme.colorScheme.primary
            )

            if (!viaje.numeroVuelo.isNullOrBlank()) {
                Text(text = "Vuelo: ${viaje.numeroVuelo}")
            }

            if (viaje.estado != EstadoViaje.FINALIZADO) {
                Button(
                    onClick = {
                        val ruta = Uri.parse("https://www.google.com/maps/dir/").buildUpon()
                            .appendQueryParameter("api", "1")
                            .appendQueryParameter(
                                "origin",
                                "${viaje.latitudOrigen},${viaje.longitudOrigen}"
                            )
                            .appendQueryParameter(
                                "destination",
                                "${viaje.latitudDestino},${viaje.longitudDestino}"
                            )
                            .appendQueryParameter("travelmode", "driving")
                            .build()

                        val intentMaps = Intent(
                            Intent.ACTION_VIEW,
                            ruta
                        )

                        try {
                            context.startActivity(intentMaps)
                        } catch (e: android.content.ActivityNotFoundException) {
                            // Android intentará abrir la ruta con una aplicación compatible.
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Abrir ruta en Maps")
                }
            }


        }
    }
}
