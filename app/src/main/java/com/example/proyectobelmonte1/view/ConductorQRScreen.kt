package com.example.proyectobelmonte1.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectobelmonte1.viewmodel.AuthViewModel
import com.example.proyectobelmonte1.viewmodel.ConductorRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConductorQRScreen(
    onVolver: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val userEmail by authViewModel.userEmail.collectAsState()
    val conductor = remember(userEmail) {
        ConductorRepository.obtenerPerfil(userEmail)
    }

    val qrJsonContent = remember(conductor, userEmail) {
        JSONObject().apply {
            put("nombre", conductor.nombre)
            put("rut", conductor.rut)
            put("patente", conductor.patente)
            put("vehiculo", conductor.vehiculo)
            put("telefono", conductor.telefono)
            if (userEmail.isNotBlank()) put("correo", userEmail)
        }.toString()
    }

    val qrBitmap = remember(qrJsonContent) {
        generarBitmapQR(qrJsonContent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Identificación QR") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Muestra este QR al Pasajero",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    qrBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Código QR del Conductor",
                            modifier = Modifier
                                .size(240.dp)
                                .padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = conductor.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "RUT: ${conductor.rut}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${conductor.vehiculo} · Patente: ${conductor.patente}", fontSize = 14.sp, color = Color.Gray)
                    Text(text = "Teléfono: ${conductor.telefono}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

fun generarBitmapQR(texto: String, tamano: Int = 512): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(texto, BarcodeFormat.QR_CODE, tamano, tamano)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}