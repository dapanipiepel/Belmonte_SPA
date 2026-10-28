package com.example.proyectobelmonte1.view

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectobelmonte1.model.Viaje
import com.example.proyectobelmonte1.navigation.Routes
import com.example.proyectobelmonte1.viewmodel.AuthViewModel
import com.example.proyectobelmonte1.viewmodel.LoginViewModel
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.coroutines.resume

@Composable
fun SolicitanteScreen(
    viajeViewModel: ViajeViewModel = viewModel(),
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var pasajero by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var hora by rememberSaveable { mutableStateOf("") }
    var cantidadPasajeros by rememberSaveable { mutableStateOf("1") }
    var origen by rememberSaveable { mutableStateOf("") }
    var destino by rememberSaveable { mutableStateOf("") }
    var numeroVuelo by rememberSaveable { mutableStateOf("") }
    var mensajeLocal by rememberSaveable { mutableStateOf("") }
    var guardando by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

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
                text = "Solicitar un traslado",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { navController.navigate(Routes.QR_SCANNER) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Escanear QR del Conductor",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = {
                    loginViewModel.cerrarSesion {
                        authViewModel.cerrarSesion()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.LOGIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar sesión")
            }

            Text(
                text = "Completa los datos para registrar tu viaje.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Datos del pasajero",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = pasajero,
                        onValueChange = { pasajero = it },
                        label = { Text("Nombre del pasajero") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = cantidadPasajeros,
                        onValueChange = { cantidadPasajeros = it.filter(Char::isDigit) },
                        label = { Text("Cantidad de pasajeros") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text("Fecha (DD/MM/AAAA)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = hora,
                        onValueChange = { hora = it },
                        label = { Text("Hora (HH:MM)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Origen",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = origen,
                        onValueChange = { origen = it },
                        label = { Text("Dirección de origen") },
                        placeholder = { Text("Ej.: Plaza de Armas, Santiago, Chile") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Destino",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = destino,
                        onValueChange = { destino = it },
                        label = { Text("Dirección de destino") },
                        placeholder = { Text("Incluye comuna y ciudad") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Información adicional",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = numeroVuelo,
                        onValueChange = { numeroVuelo = it },
                        label = { Text("Número de vuelo (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            val pasajeros = cantidadPasajeros.toIntOrNull()
                            if (
                                pasajero.isBlank() ||
                                fecha.isBlank() ||
                                hora.isBlank() ||
                                origen.isBlank() ||
                                destino.isBlank() ||
                                pasajeros == null ||
                                pasajeros < 1
                            ) {
                                mensajeLocal =
                                    "Completa los campos obligatorios y define una cantidad válida de pasajeros."
                            } else if (!esFechaHoraFutura(fecha.trim(), hora.trim())) {
                                mensajeLocal =
                                    "La fecha y hora del viaje debe ser presente o futura."
                            } else {
                                scope.launch {
                                    guardando = true
                                    mensajeLocal = "Buscando las direcciones..."

                                    try {
                                        val defaultSantiago = Pair(-33.4489, -70.6693)
                                        val coordenadasOrigen =
                                            buscarCoordenadas(context, origen.trim()) ?: defaultSantiago

                                        val coordenadasDestino =
                                            buscarCoordenadas(context, destino.trim()) ?: defaultSantiago

                                        mensajeLocal = ""

                                        viajeViewModel.insertarViaje(
                                            Viaje(
                                                pasajero = pasajero.trim(),
                                                cantidadPasajeros = pasajeros,
                                                fecha = fecha.trim(),
                                                hora = hora.trim(),
                                                origen = origen.trim(),
                                                latitudOrigen = coordenadasOrigen.first,
                                                longitudOrigen = coordenadasOrigen.second,
                                                destino = destino.trim(),
                                                latitudDestino = coordenadasDestino.first,
                                                longitudDestino = coordenadasDestino.second,
                                                numeroVuelo = numeroVuelo.trim().ifBlank { null }
                                            )
                                        )
                                    } catch (_: Exception) {
                                        mensajeLocal =
                                            "No pudimos buscar las direcciones. " +
                                                    "Revisa los datos e inténtalo nuevamente."
                                    } finally {
                                        guardando = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !guardando
                    ) {
                        Text(
                            if (guardando) {
                                "Buscando ubicaciones..."
                            } else {
                                "Guardar solicitud"
                            }
                        )
                    }

                    val mensajeMostrar = mensajeLocal.ifBlank { viajeViewModel.mensaje }

                    if (mensajeMostrar.isNotBlank()) {
                        Text(
                            text = mensajeMostrar,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Text(
                text = "Mis solicitudes",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (viajeViewModel.viajes.isEmpty()) {
                Text(
                    text = "Todavía no tienes solicitudes guardadas.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                viajeViewModel.viajes.forEach { viaje ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${viaje.origen} → ${viaje.destino}",
                                fontWeight = FontWeight.Bold
                            )

                            Text(text = "Pasajero: ${viaje.pasajero}")
                            Text(text = "Cantidad de pasajeros: ${viaje.cantidadPasajeros}")
                            Text(text = "Fecha: ${viaje.fecha}")
                            Text(text = "Hora: ${viaje.hora}")
                            Text(text = "Estado: ${viaje.estado}")
                        }
                    }
                }
            }
        }
    }
}

private fun esFechaHoraFutura(fechaStr: String, horaStr: String): Boolean {
    val formatters = listOf(
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy H:m"),
        DateTimeFormatter.ofPattern("d/M/yyyy H:m"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    )
    val fechaHoraCombined = "$fechaStr $horaStr"
    for (formatter in formatters) {
        try {
            val parsedDateTime = LocalDateTime.parse(fechaHoraCombined, formatter)
            return !parsedDateTime.isBefore(LocalDateTime.now().minusMinutes(2))
        } catch (_: Exception) {
        }
    }
    return false
}

private suspend fun buscarCoordenadas(
    context: Context,
    direccion: String
): Pair<Double, Double>? = withContext(Dispatchers.IO) {
    if (direccion.isBlank() || !Geocoder.isPresent()) {
        return@withContext null
    }

    try {
        val geocoder = Geocoder(context, Locale.forLanguageTag("es-CL"))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                try {
                    geocoder.getFromLocationName(
                        direccion,
                        1,
                        object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                val direccionEncontrada = addresses.firstOrNull()
                                if (continuation.isActive) {
                                    val coordenadas = direccionEncontrada?.let {
                                        Pair(it.latitude, it.longitude)
                                    }
                                    continuation.resume(coordenadas)
                                }
                            }

                            override fun onError(errorMessage: String?) {
                                if (continuation.isActive) {
                                    continuation.resume(null)
                                }
                            }
                        }
                    )
                } catch (_: Throwable) {
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(direccion, 1)
            val direccionEncontrada = addresses?.firstOrNull()
            direccionEncontrada?.let { Pair(it.latitude, it.longitude) }
        }
    } catch (_: Throwable) {
        null
    }
}