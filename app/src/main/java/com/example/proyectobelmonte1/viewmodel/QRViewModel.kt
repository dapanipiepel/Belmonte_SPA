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

object ConductorRepository {
    private val perfiles = mapOf(
        "ana" to ConductorUiState(
            nombre = "Ana Torres",
            rut = "17.456.789-1",
            patente = "EFGH-34",
            vehiculo = "Hyundai Staria 2023",
            telefono = "+56 9 8765 4321"
        ),
        "carlos.soto" to ConductorUiState(
            nombre = "Carlos Soto",
            rut = "15.890.123-4",
            patente = "ABCD-12",
            vehiculo = "Toyota Corolla",
            telefono = "+56 9 1234 5678"
        ),
        "carlos.torres" to ConductorUiState(
            nombre = "Carlos Torres",
            rut = "14.321.654-K",
            patente = "IJKL-56",
            vehiculo = "Mercedes-Benz Sprinter",
            telefono = "+56 9 9876 5432"
        )
    )

    fun obtenerPerfil(identificador: String): ConductorUiState {
        val key = identificador.lowercase()
        return when {
            key.contains("ana") -> perfiles["ana"]!!
            key.contains("soto") -> perfiles["carlos.soto"]!!
            key.contains("torres") -> perfiles["carlos.torres"]!!
            key.contains("conductor") -> perfiles["carlos.soto"]!!
            else -> {
                val nombreLimpio = if (identificador.contains("@")) {
                    identificador.substringBefore("@").replace(".", " ").uppercase()
                } else {
                    identificador.ifBlank { "Conductor Belmonte" }
                }
                ConductorUiState(
                    nombre = nombreLimpio,
                    rut = "12.345.678-K",
                    patente = "BM-2026",
                    vehiculo = "Vehículo Corporativo",
                    telefono = "+56 9 5555 4444"
                )
            }
        }
    }
}

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
                    // Mapeo dinámico según el contenido escaneado en el emulador
                    _scannedConductor.value = ConductorRepository.obtenerPerfil(data.trim())
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