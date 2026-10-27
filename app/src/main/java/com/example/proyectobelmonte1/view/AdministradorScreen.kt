
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
import androidx.navigation.NavController
import com.example.proyectobelmonte1.navigation.Routes
import com.example.proyectobelmonte1.viewmodel.LoginViewModel

@Composable
fun AdministradorScreen(navController: NavController) {

    val viajeViewModel: ViajeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    val solicitudesPendientes = viajeViewModel.viajes.filter {
        it.estado == EstadoViaje.PENDIENTE
    }

    val viajesAsignados = viajeViewModel.viajes.filter {
        it.estado == EstadoViaje.ASIGNADO
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
            text = "Panel del administrador",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )


        Button(
            onClick = {
                loginViewModel.cerrarSesion {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ADMINISTRADOR) {
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
            text = "Gestiona las solicitudes y asigna conductores.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Solicitudes pendientes (${solicitudesPendientes.size})",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (solicitudesPendientes.isEmpty()) {
            Text(
                text = "No hay solicitudes pendientes por asignar.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            solicitudesPendientes.forEach { viaje ->

                TarjetaSolicitud(
                    viaje = viaje,
                    onAsignar = { conductor ->
                        viajeViewModel.asignarConductor(viaje, conductor)
                    }
                )
            }
        }

        if (viajeViewModel.mensaje.isNotBlank()) {
            Text(
                text = viajeViewModel.mensaje,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Viajes asignados (${viajesAsignados.size})",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (viajesAsignados.isEmpty()) {
            Text(
                text = "Todavía no se han asignado viajes.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            viajesAsignados.forEach { viaje ->
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
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(text = "Pasajero: ${viaje.pasajero}")
                        Text(text = "Fecha: ${viaje.fecha}")
                        Text(text = "Hora: ${viaje.hora}")
                        Text(text = "Conductor: ${viaje.conductor ?: "Sin asignar"}")
                        Text(text = "Estado: ${viaje.estado}")
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaSolicitud(
    viaje: Viaje,
    onAsignar: (String) -> Unit
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
            Text(text = "Fecha: ${viaje.fecha}")
            Text(text = "Hora: ${viaje.hora}")

            if (!viaje.numeroVuelo.isNullOrBlank()) {
                Text(text = "Vuelo: ${viaje.numeroVuelo}")
            }

            Text(
                text = "Estado: ${viaje.estado}",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Asignar conductor",
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = { onAsignar("Carlos Soto") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Asignar a Carlos Soto")
            }

            Button(
                onClick = { onAsignar("Ana Torres") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Asignar a Ana Torres")
            }
        }
    }
}
