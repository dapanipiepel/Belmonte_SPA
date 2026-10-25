package com.example.proyectobelmonte1.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel
import kotlinx.coroutines.launch

@Composable
fun DetalleViajeScreen(
    viajeId: Long,
    viajeViewModel: ViajeViewModel,
    onVolver: () -> Unit
) {
    val viaje = viajeViewModel.viajes.find { it.id == viajeId }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val vehiculo = viaje?.let { viajeViewModel.obtenerVehiculo(it.vehiculoId) }

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
                text = "Detalle del viaje",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            if (viaje == null) {
                Text(
                    text = "No se encontró el viaje seleccionado.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Viaje #${viaje.id}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(text = "Pasajero: ${viaje.pasajero}")
                        Text(text = "Cantidad de pasajeros: ${viaje.cantidadPasajeros}")
                        Text(text = "Fecha: ${viaje.fecha}")
                        Text(text = "Hora: ${viaje.hora}")
                        Text(text = "Origen: ${viaje.origen}")
                        Text(text = "Destino: ${viaje.destino}")
                        Text(text = "Conductor: ${viaje.conductor ?: "Sin asignar"}")
                        Text(text = "Vehículo: ${vehiculo?.descripcion ?: "Sin asignar"}")
                        Text(
                            text = "Capacidad del vehículo: ${
                                vehiculo?.capacidadPasajeros?.let { "$it pasajeros" }
                                    ?: "No disponible"
                            }"
                        )
                        if (!viaje.numeroVuelo.isNullOrBlank()) {
                            Text(text = "Número de vuelo: ${viaje.numeroVuelo}")
                        }
                        Text(
                            text = "Estado: ${viaje.estado}",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (viaje.estado != EstadoViaje.FINALIZADO) {
                    OutlinedButton(
                        onClick = {
                            val ruta = Uri.parse("https://www.google.com/maps/dir/")
                                .buildUpon()
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
                            abrirNavegacion(context, ruta, snackbarHostState, scope)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Abrir ruta en Google Maps")
                    }

                    OutlinedButton(
                        onClick = {
                            val ruta = Uri.parse("waze://")
                                .buildUpon()
                                .appendQueryParameter(
                                    "ll",
                                    "${viaje.latitudDestino},${viaje.longitudDestino}"
                                )
                                .appendQueryParameter("navigate", "yes")
                                .build()
                            abrirNavegacion(context, ruta, snackbarHostState, scope)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Abrir ruta en Waze")
                    }
                }

                when (viaje.estado) {
                    EstadoViaje.ASIGNADO -> Button(
                        onClick = {
                            viajeViewModel.actualizarEstado(viaje, EstadoViaje.EN_CURSO)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar viaje")
                    }

                    EstadoViaje.EN_CURSO -> Button(
                        onClick = {
                            viajeViewModel.actualizarEstado(viaje, EstadoViaje.FINALIZADO)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Finalizar viaje")
                    }

                    else -> Unit
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a mis viajes")
            }
        }
    }
}

private fun abrirNavegacion(
    context: android.content.Context,
    ruta: Uri,
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope
) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, ruta))
        scope.launch {
            snackbarHostState.showSnackbar("Abriendo app de navegación")
        }
    } catch (e: ActivityNotFoundException) {
        scope.launch {
            snackbarHostState.showSnackbar("No se encontró la app de navegación seleccionada")
        }
    }
}
