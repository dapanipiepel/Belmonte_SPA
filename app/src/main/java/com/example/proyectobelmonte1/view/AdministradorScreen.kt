package com.example.proyectobelmonte1.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.model.Vehiculo
import com.example.proyectobelmonte1.model.Viaje
import com.example.proyectobelmonte1.navigation.Routes
import com.example.proyectobelmonte1.viewmodel.LoginViewModel
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel

@Composable
fun AdministradorScreen(
    viajeViewModel: ViajeViewModel = viewModel(),
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val viajesOrdenados = viajeViewModel.ordenarCronologicamente(viajeViewModel.viajes)
    val solicitudesPendientes = viajesOrdenados.filter {
        it.estado == EstadoViaje.PENDIENTE
    }
    val viajesAsignados = viajesOrdenados.filter {
        it.estado == EstadoViaje.ASIGNADO
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
                text = "Panel del administrador",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Gestiona las solicitudes y asigna conductores y vehículos.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        conductores = viajeViewModel.conductoresDisponibles,
                        vehiculos = viajeViewModel.vehiculosDisponibles.filter { it.activo },
                        onAsignar = { conductor, vehiculo ->
                            viajeViewModel.asignarConductor(viaje, conductor, vehiculo)
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
                            Text(
                                text = "Vehículo: ${
                                    viajeViewModel.obtenerVehiculo(viaje.vehiculoId)?.descripcion
                                        ?: "Sin asignar"
                                }"
                            )
                            Text(text = "Estado: ${viaje.estado}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaSolicitud(
    viaje: Viaje,
    conductores: List<String>,
    vehiculos: List<Vehiculo>,
    onAsignar: (String, Vehiculo) -> Unit
) {
    var conductorSeleccionado by rememberSaveable(viaje.id) {
        mutableStateOf("")
    }
    var vehiculoSeleccionadoId by rememberSaveable(viaje.id) {
        mutableStateOf<Long?>(null)
    }
    var menuConductoresAbierto by remember { mutableStateOf(false) }
    var menuVehiculosAbierto by remember { mutableStateOf(false) }
    val vehiculoSeleccionado = vehiculos.find { it.id == vehiculoSeleccionadoId }
    val capacidadInsuficiente =
        vehiculoSeleccionado != null &&
                vehiculoSeleccionado.capacidadPasajeros < viaje.cantidadPasajeros

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

            if (!viaje.numeroVuelo.isNullOrBlank()) {
                Text(text = "Vuelo: ${viaje.numeroVuelo}")
            }

            Text(
                text = "Estado: ${viaje.estado}",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Asignar conductor y vehículo",
                fontWeight = FontWeight.SemiBold
            )

            Box {
                OutlinedButton(
                    onClick = { menuConductoresAbierto = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = conductorSeleccionado.ifBlank { "Seleccionar conductor" },
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = menuConductoresAbierto,
                    onDismissRequest = { menuConductoresAbierto = false }
                ) {
                    conductores.forEach { conductor ->
                        DropdownMenuItem(
                            text = { Text(conductor) },
                            onClick = {
                                conductorSeleccionado = conductor
                                menuConductoresAbierto = false
                            }
                        )
                    }
                }
            }

            Box {
                OutlinedButton(
                    onClick = { menuVehiculosAbierto = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = vehiculoSeleccionado?.let {
                            "${it.descripcion} · ${it.capacidadPasajeros} pasajeros"
                        } ?: "Seleccionar vehículo",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = menuVehiculosAbierto,
                    onDismissRequest = { menuVehiculosAbierto = false }
                ) {
                    vehiculos.forEach { vehiculo ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "${vehiculo.descripcion} · " +
                                            "${vehiculo.capacidadPasajeros} pasajeros"
                                )
                            },
                            onClick = {
                                vehiculoSeleccionadoId = vehiculo.id
                                menuVehiculosAbierto = false
                            }
                        )
                    }
                }
            }

            if (capacidadInsuficiente) {
                Text(
                    text = "El vehículo no tiene capacidad suficiente para este grupo.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    val conductor = conductorSeleccionado
                    val vehiculo = vehiculoSeleccionado
                    if (conductor.isNotBlank() && vehiculo != null) {
                        onAsignar(conductor, vehiculo)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = conductorSeleccionado.isNotBlank() &&
                        vehiculoSeleccionado != null &&
                        !capacidadInsuficiente
            ) {
                Text("Confirmar asignación")
            }
        }
    }
}