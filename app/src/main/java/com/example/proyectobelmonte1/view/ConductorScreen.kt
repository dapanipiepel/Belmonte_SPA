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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectobelmonte1.model.EstadoViaje
import com.example.proyectobelmonte1.model.Viaje
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.proyectobelmonte1.navigation.Routes
import com.example.proyectobelmonte1.viewmodel.LoginViewModel

@Composable
fun ConductorScreen(
    viajeViewModel: ViajeViewModel,
    onAbrirDetalle: (Long) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val viajesOrdenados = viajeViewModel.ordenarCronologicamente(viajeViewModel.viajes)
    val viajesAsignados = viajesOrdenados.filter {
fun ConductorScreen(navController: NavController){

    val viajeViewModel: ViajeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    val viajesAsignados = viajeViewModel.viajes.filter {
        it.estado == EstadoViaje.ASIGNADO
    }

    val viajesEnCurso = viajeViewModel.viajes.filter {
    val viajesEnCurso = viajesOrdenados.filter {
        it.estado == EstadoViaje.EN_CURSO
    }

    val viajesFinalizados = viajeViewModel.viajes.filter {
    val viajesFinalizados = viajesOrdenados.filter {
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

        Button(
            onClick = {
                loginViewModel.cerrarSesion {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.CONDUCTOR) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        ) {
            Text("Cerrar sesión")
        }

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
            text = "Viajes en curso (${viajesEnCurso.size})",
            text = titulo,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (viajesEnCurso.isEmpty()) {
        if (viajes.isEmpty()) {
            Text(
                text = "No tienes viajes en curso.",
                text = mensajeVacio,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            viajesEnCurso.forEach { viaje ->
            viajes.forEach { viaje ->
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
    textoBoton: String?,
    vehiculo: String?,
    onClick: () -> Unit

) { val context = LocalContext.current
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

            if (textoBoton != null) {
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(textoBoton)
                }
            }


            if (!viaje.numeroVuelo.isNullOrBlank()) {
                Text(text = "Vuelo: ${viaje.numeroVuelo}")
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (viaje.estado != EstadoViaje.FINALIZADO) {

                val coordenadasDestino = if (viaje.estado == EstadoViaje.ASIGNADO) {
                    "${viaje.latitudOrigen},${viaje.longitudOrigen}"
                } else {
                    "${viaje.latitudDestino},${viaje.longitudDestino}"
                }

                val textoBoton = if (viaje.estado == EstadoViaje.ASIGNADO) {
                    "Navegar al punto de recogida"
                } else {
                    "Navegar al destino"
                }

                Button(
                    onClick = {

                        val rutaWaze = Uri.parse("https://waze.com/ul").buildUpon()
                            .appendQueryParameter("ll", coordenadasDestino)
                            .appendQueryParameter("navigate", "yes")
                            .build()

                        val intentWaze = Intent(
                            Intent.ACTION_VIEW,
                            rutaWaze
                        ).apply {
                            setPackage("com.waze")
                        }

                        try {
                            context.startActivity(intentWaze)
                        } catch (e: android.content.ActivityNotFoundException) {
                            val rutaMaps = Uri.parse(
                                "https://www.google.com/maps/dir/"
                            ).buildUpon()
                                .appendQueryParameter("api", "1")
                                .appendQueryParameter("destination", coordenadasDestino)
                                .appendQueryParameter("travelmode", "driving")
                                .build()

                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, rutaMaps)
                            )
                        }

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(textoBoton)
                }
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver detalle del viaje")
            }



        }
    }
}
