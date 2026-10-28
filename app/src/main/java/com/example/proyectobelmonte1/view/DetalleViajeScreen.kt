package com.example.proyectobelmonte1.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale

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
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                //BOTONES DE NAVEGACIÓN GPS
                if (viaje.estado != EstadoViaje.FINALIZADO) {
                    // Si el viaje aún no inicia, se navega al origen (punto de recogida); una vez en curso, al destino
                    val esPuntoRecogida = viaje.estado == EstadoViaje.PENDIENTE || viaje.estado == EstadoViaje.ASIGNADO
                    val targetLat = if (esPuntoRecogida) viaje.latitudOrigen else viaje.latitudDestino
                    val targetLng = if (esPuntoRecogida) viaje.longitudOrigen else viaje.longitudDestino
                    val targetNombre = if (esPuntoRecogida) "Punto de recogida (${viaje.origen})" else "Destino (${viaje.destino})"

                    Text(
                        text = "Navegar hacia: $targetNombre",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                abrirGoogleMapsNativo(
                                    context = context,
                                    latitud = targetLat,
                                    longitud = targetLng,
                                    snackbarHostState = snackbarHostState,
                                    scope = scope
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0288D1),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Google Maps")
                        }

                        Button(
                            onClick = {
                                abrirWazeNativo(
                                    context = context,
                                    latitud = targetLat,
                                    longitud = targetLng,
                                    snackbarHostState = snackbarHostState,
                                    scope = scope
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF33CCFF),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Waze")
                        }
                    }
                }

                //BOTONES DE CAMBIO DE ESTADO DEL SERVICIO
                when (viaje.estado) {
                    EstadoViaje.ASIGNADO -> Button(
                        onClick = {
                            viajeViewModel.actualizarEstado(viaje, EstadoViaje.EN_CURSO)
                            scope.launch {
                                snackbarHostState.showSnackbar("Viaje iniciado: Estado cambiado a EN CURSO")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("INICIAR VIAJE", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    EstadoViaje.EN_CURSO -> Button(
                        onClick = {
                            viajeViewModel.actualizarEstado(viaje, EstadoViaje.FINALIZADO)
                            scope.launch {
                                snackbarHostState.showSnackbar("Viaje finalizado exitosamente")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC62828),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("FINALIZAR VIAJE", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    else -> Unit
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a mis viajes")
            }
        }
    }
}

private fun abrirGoogleMapsNativo(
    context: Context,
    latitud: Double,
    longitud: Double,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val coordsFormatted = String.format(Locale.US, "%.6f,%.6f", latitud, longitud)
    val gmmIntentUri = Uri.parse("google.navigation:q=$coordsFormatted")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }

    try {
        context.startActivity(mapIntent)
        scope.launch {
            snackbarHostState.showSnackbar("Iniciando navegación en Google Maps...")
        }
    } catch (e: Exception) {
        val genericUri = Uri.parse("geo:$coordsFormatted?q=$coordsFormatted")
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, genericUri))
        } catch (ex: Exception) {
            scope.launch {
                snackbarHostState.showSnackbar("No se encontró ninguna app de mapas disponible")
            }
        }
    }
}

private fun abrirWazeNativo(
    context: Context,
    latitud: Double,
    longitud: Double,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val coordsFormatted = String.format(Locale.US, "%.6f,%.6f", latitud, longitud)
    val wazeUri = Uri.parse("https://waze.com/ul?ll=$coordsFormatted&navigate=yes")
    val wazeIntent = Intent(Intent.ACTION_VIEW, wazeUri).apply {
        setPackage("com.waze")
    }

    try {
        context.startActivity(wazeIntent)
        scope.launch {
            snackbarHostState.showSnackbar("Iniciando navegación en Waze...")
        }
    } catch (e: ActivityNotFoundException) {
        scope.launch {
            snackbarHostState.showSnackbar("Waze no está instalado. Redirigiendo a Google Maps...")
        }
        abrirGoogleMapsNativo(context, latitud, longitud, snackbarHostState, scope)
    }
}