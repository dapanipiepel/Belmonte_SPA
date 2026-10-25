
package com.example.proyectobelmonte1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyectobelmonte1.navigation.AppNavigation
import com.example.proyectobelmonte1.ui.theme.ProyectoBelmonte1Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ProyectoBelmonte1Theme {
                AppNavigation()
            }
        }
    }
}
