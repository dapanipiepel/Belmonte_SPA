package com.example.proyectobelmonte1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

data class ConductorUiState(
    val nombre: String = "",
    val rut: String = "",
    val patente: String = "",
    val vehiculo: String = "",
    val telefono: String = "",
    val valoracion: String = "4.9 ★"
)

class QrViewModel : ViewModel() {

    private val _scannedConductor = MutableStateFlow<ConductorUiState?>(null)
    val scannedConductor: StateFlow<ConductorUiState?> = _scannedConductor.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun iniciarEscaneo(context: Context) {
        _errorMessage.value = null
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(context, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue = barcode.rawValue
                if (!rawValue.isNullOrEmpty()) {
                    procesarCodigoQR(rawValue)
                } else {
                    _errorMessage.value = "No se pudo leer el contenido del código QR."
                }
            }
            .addOnCanceledListener {
                _errorMessage.value = "Escaneo cancelado por el usuario."
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Error al escanear: ${e.localizedMessage}"
            }
    }

    private fun procesarCodigoQR(data: String) {
        try {
            // Intenta parsear si el QR viene en formato JSON
            if (data.trim().startsWith("{")) {
                val json = JSONObject(data)
                _scannedConductor.value = ConductorUiState(
                    nombre = json.optString("nombre", "Conductor Belmonte"),
                    rut = json.optString("rut", "12.345.678-K"),
                    patente = json.optString("patente", "ABCD-12"),
                    vehiculo = json.optString("vehiculo", "Toyota Corolla"),
                    telefono = json.optString("telefono", "+56 9 1234 5678")
                )
            } else {
                // Si viene separado por comas
                val partes = data.split(",")
                if (partes.size >= 4) {
                    _scannedConductor.value = ConductorUiState(
                        nombre = partes[0].trim(),
                        rut = partes.getOrElse(1) { "12.345.678-K" }.trim(),
                        vehiculo = partes.getOrElse(2) { "Hyundai Staria" }.trim(),
                        patente = partes.getOrElse(3) { "EFGH-34" }.trim(),
                        telefono = partes.getOrElse(4) { "+56 9 8765 4321" }.trim()
                    )
                } else {
                    // Si el QR contiene un ID simple o código corto de conductor
                    _scannedConductor.value = ConductorUiState(
                        nombre = "Carlos Mendoza ($data)",
                        rut = "15.890.123-4",
                        patente = "EFGH-34",
                        vehiculo = "Hyundai Staria 2023",
                        telefono = "+56 9 9876 5432"
                    )
                }
            }
        } catch (e: Exception) {
            _errorMessage.value = "El código QR escaneado no tiene un formato válido de conductor."
        }
    }

    fun limpiarEscaneo() {
        _scannedConductor.value = null
        _errorMessage.value = null
    }
}
