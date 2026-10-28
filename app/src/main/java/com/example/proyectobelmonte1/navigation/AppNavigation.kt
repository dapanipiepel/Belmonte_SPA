package com.example.proyectobelmonte1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectobelmonte1.view.AdministradorScreen
import com.example.proyectobelmonte1.view.ConductorQRScreen
import com.example.proyectobelmonte1.view.ConductorScreen
import com.example.proyectobelmonte1.view.DetalleViajeScreen
import com.example.proyectobelmonte1.view.HomeScreen
import com.example.proyectobelmonte1.view.LoginScreen
import com.example.proyectobelmonte1.view.QRScreen
import com.example.proyectobelmonte1.view.SolicitanteScreen
import com.example.proyectobelmonte1.view.SplashScreen
import com.example.proyectobelmonte1.viewmodel.AuthViewModel
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel(),
    viajeViewModel: ViajeViewModel = viewModel()
) {
    val navController = rememberNavController()
    val userRole by authViewModel.userRole.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(route = Routes.SPLASH) {
            SplashScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Routes.HOME) {
            HomeScreen(
                navController = navController,
                userRole = userRole
            )
        }

        composable(route = Routes.ADMINISTRADOR) {
            AdministradorScreen(
                viajeViewModel = viajeViewModel,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Routes.SOLICITANTE) {
            SolicitanteScreen(
                viajeViewModel = viajeViewModel,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Routes.CONDUCTOR) {
            ConductorScreen(
                viajeViewModel = viajeViewModel,
                navController = navController,
                authViewModel = authViewModel,
                onAbrirDetalle = { viajeId ->
                    navController.navigate("${Routes.DETALLE_VIAJE}/$viajeId")
                }
            )
        }

        composable(
            route = "${Routes.DETALLE_VIAJE}/{viajeId}",
            arguments = listOf(
                navArgument("viajeId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val viajeId = backStackEntry.arguments?.getLong("viajeId") ?: -1L
            DetalleViajeScreen(
                viajeId = viajeId,
                viajeViewModel = viajeViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(route = Routes.QR_SCANNER) {
            QRScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        composable(route = Routes.CONDUCTOR_QR) {
            ConductorQRScreen(
                onVolver = { navController.popBackStack() },
                authViewModel = authViewModel
            )
        }
    }
}