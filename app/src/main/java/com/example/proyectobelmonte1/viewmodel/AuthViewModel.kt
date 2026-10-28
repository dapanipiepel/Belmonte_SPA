package com.example.proyectobelmonte1.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _userRole = MutableStateFlow("SOLICITANTE") // Rol por defecto
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    fun iniciarSesion(email: String, role: String) {
        _userEmail.value = email
        _userRole.value = role.uppercase() // Guarda "ADMINISTRADOR", "SOLICITANTE" o "CONDUCTOR"
    }

    fun cerrarSesion() {
        _userEmail.value = ""
        _userRole.value = "SOLICITANTE"
    }
}