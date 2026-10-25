
package com.example.proyectobelmonte1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectobelmonte1.model.RolUsuario
import com.example.proyectobelmonte1.model.Usuario

class LoginViewModel : ViewModel() {

    var correo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var textoVisible by mutableStateOf(false)
        private set

    var usuarioAutenticado by mutableStateOf<Usuario?>(null)
        private set

    fun actualizarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
    }

    fun actualizarContrasena(nuevaContrasena: String) {
        contrasena = nuevaContrasena
    }

    fun validarCredenciales(): Boolean {
        val usuariosPrueba = listOf(
            Usuario(
                correo = "admin@belmonte.cl",
                contrasena = "123456",
                rol = RolUsuario.ADMINISTRADOR
            ),
            Usuario(
                correo = "solicitante@belmonte.cl",
                contrasena = "123456",
                rol = RolUsuario.SOLICITANTE
            ),
            Usuario(
                correo = "conductor@belmonte.cl",
                contrasena = "123456",
                rol = RolUsuario.CONDUCTOR
            )
        )

        val usuarioEncontrado = usuariosPrueba.find { usuario ->
            usuario.correo == correo &&
                    usuario.contrasena == contrasena
        }

        usuarioAutenticado = usuarioEncontrado

        val credencialesValidas = usuarioEncontrado != null
        textoVisible = !credencialesValidas

        return credencialesValidas
    }
}
