package com.example.proyectobelmonte1.view

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel
import kotlinx.coroutines.launch

@Composable
fun ObservarNotificaciones(
    viajeViewModel: ViajeViewModel,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(viajeViewModel, snackbarHostState) {
        viajeViewModel.notificaciones.collect { mensaje ->
            this@LaunchedEffect.launch {
                snackbarHostState.showSnackbar(mensaje)
            }
        }
    }
}
