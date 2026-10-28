package com.example.proyectobelmonte1.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectobelmonte1.view.AdministradorScreen
import com.example.proyectobelmonte1.view.ConductorScreen
import com.example.proyectobelmonte1.view.DetalleViajeScreen
import com.example.proyectobelmonte1.view.LoginScreen
import com.example.proyectobelmonte1.view.SolicitanteScreen
import com.example.proyectobelmonte1.viewmodel.ViajeViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viajeViewModel: ViajeViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(route = Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(route = Routes.ADMINISTRADOR) {
            AdministradorScreen(
                viajeViewModel = viajeViewModel,
                navController = navController
            )
        }

        composable(route = Routes.SOLICITANTE) {
            SolicitanteScreen(
                viajeViewModel = viajeViewModel,
                navController = navController
            )
        }

        composable(route = Routes.CONDUCTOR) {
            ConductorScreen(
                viajeViewModel = viajeViewModel,
                navController = navController,
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
    }
}