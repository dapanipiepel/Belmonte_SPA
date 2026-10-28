
package com.example.proyectobelmonte1.view

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectobelmonte1.model.Viaje
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import androidx.navigation.NavController
import com.example.proyectobelmonte1.navigation.Routes
import com.example.proyectobelmonte1.viewmodel.LoginViewModel

@Composable
fun SolicitanteScreen(viajeViewModel: ViajeViewModel) {
fun SolicitanteScreen(navController: NavController) {

    val viajeViewModel: ViajeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
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
            text = "Solicitar un traslado",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )


        Button(
            onClick = {
                loginViewModel.cerrarSesion {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SOLICITANTE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
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
                    placeholder = {
                        Text("Ej.: Plaza de Armas, Santiago, Chile")
                    },
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
                    placeholder = {
                        Text("Incluye comuna y ciudad")
                    },
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
                            destino.isBlank()
                        ) {
                            mensajeLocal =
                                "Completa los campos obligatorios y define una cantidad válida de pasajeros."
                                "Completa todos los campos obligatorios."
                        } else {
                            scope.launch {
                                guardando = true
                                mensajeLocal = "Buscando las direcciones..."

                                try {
                                    val coordenadasOrigen =
                                        buscarCoordenadas(context, origen.trim())

                                    val coordenadasDestino =
                                        buscarCoordenadas(context, destino.trim())

                                    when {
                                        coordenadasOrigen == null -> {
                                            mensajeLocal =
                                                "No pudimos localizar el origen. " +
                                                        "Escribe una dirección más precisa."
                                        }

                                        coordenadasDestino == null -> {
                                            mensajeLocal =
                                                "No pudimos localizar el destino. " +
                                                        "Incluye comuna y ciudad."
                                        }

                                        else -> {
                                            mensajeLocal = ""

                                            viajeViewModel.insertarViaje(
                                                Viaje(
                                                    pasajero = pasajero.trim(),
                                                    cantidadPasajeros = pasajeros,
                                                    fecha = fecha.trim(),
                                                    hora = hora.trim(),
                                                    origen = origen.trim(),
                                                    latitudOrigen =
                                                        coordenadasOrigen.first,
                                                    longitudOrigen =
                                                        coordenadasOrigen.second,
                                                    destino = destino.trim(),
                                                    latitudDestino =
                                                        coordenadasDestino.first,
                                                    longitudDestino =
                                                        coordenadasDestino.second,
                                                    numeroVuelo = numeroVuelo
                                                        .trim()
                                                        .ifBlank { null }
                                                )
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
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

                val mensajeMostrar =
                    mensajeLocal.ifBlank { viajeViewModel.mensaje }

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
        }
    }
}

private suspend fun buscarCoordenadas(
    context: Context,
    direccion: String
): Pair<Double, Double>? {

    if (!Geocoder.isPresent()) {
        return null
    }

    return suspendCancellableCoroutine { continuation ->
        try {
            val geocoder = Geocoder(context, Locale.forLanguageTag("es-CL"))
            val geocoder = Geocoder(context, Locale("es", "CL"))

            geocoder.getFromLocationName(
                direccion,
                1,
                object : Geocoder.GeocodeListener {

                    override fun onGeocode(
                        addresses: MutableList<Address>
                    ) {
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
        } catch (e: Exception) {
            if (continuation.isActive) {
                continuation.resume(null)
            }
        }
    }
}
