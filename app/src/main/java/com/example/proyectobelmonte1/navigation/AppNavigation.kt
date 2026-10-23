
package com.example.proyectobelmonte1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectobelmonte1.view.AdministradorScreen
import com.example.proyectobelmonte1.view.ConductorScreen
import com.example.proyectobelmonte1.view.LoginScreen
import com.example.proyectobelmonte1.view.SolicitanteScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(route = Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(route = Routes.ADMINISTRADOR) {
            AdministradorScreen()
        }

        composable(route = Routes.SOLICITANTE) {
            SolicitanteScreen()
        }

        composable(route = Routes.CONDUCTOR) {
            ConductorScreen()
        }
    }
}
