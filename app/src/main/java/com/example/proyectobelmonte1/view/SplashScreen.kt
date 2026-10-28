package com.example.proyectobelmonte1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectobelmonte1.model.RolUsuario
import com.example.proyectobelmonte1.navigation.Routes
import com.example.proyectobelmonte1.viewmodel.AuthViewModel
import com.example.proyectobelmonte1.viewmodel.LoginViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        loginViewModel.recuperarSesion { usuario ->
            if (usuario != null) {
                authViewModel.iniciarSesion(usuario.correo, usuario.rol.name)
                val destino = when (usuario.rol) {
                    RolUsuario.ADMINISTRADOR -> Routes.ADMINISTRADOR
                    RolUsuario.SOLICITANTE -> Routes.SOLICITANTE
                    RolUsuario.CONDUCTOR -> Routes.CONDUCTOR
                }
                navController.navigate(destino) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "B",
                    color = Color.White,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BELMONTE",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
        }
    }
}