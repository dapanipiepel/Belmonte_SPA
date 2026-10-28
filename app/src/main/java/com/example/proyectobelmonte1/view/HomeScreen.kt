package com.example.proyectobelmonte1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobelmonte1.navigation.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    userRole: String = "SOLICITANTE"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "BELMONTE",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Bienvenido!",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Has iniciado sesión correctamente.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Opción para Solicitante / Pasajero
        if (userRole == "SOLICITANTE" || userRole == "ADMINISTRADOR") {
            Button(
                onClick = { navController.navigate(Routes.QR_SCANNER) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escanear QR del Conductor")
            }
        }

        // Opción para Conductor
        if (userRole == "CONDUCTOR" || userRole == "ADMINISTRADOR") {
            if (userRole == "ADMINISTRADOR") {
                Spacer(modifier = Modifier.height(12.dp))
            }
            OutlinedButton(
                onClick = { navController.navigate(Routes.CONDUCTOR_QR) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mostrar Mi Código QR")
            }
        }
    }
}