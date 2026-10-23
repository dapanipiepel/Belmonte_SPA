
package com.example.proyectobelmonte1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
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
    }
}
