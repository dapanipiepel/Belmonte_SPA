
package com.example.proyectobelmonte1.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobelmonte1.data.local.AppDatabase
import com.example.proyectobelmonte1.model.RolUsuario
import com.example.proyectobelmonte1.model.Sesion
import com.example.proyectobelmonte1.model.Usuario
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val sesionDao = AppDatabase
        .getDatabase(application)
        .sesionDao()

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
            usuario.correo == correo.trim() &&
                    usuario.contrasena == contrasena
        }

        usuarioAutenticado = usuarioEncontrado

        val credencialesValidas = usuarioEncontrado != null
        textoVisible = !credencialesValidas

        return credencialesValidas
    }

    fun guardarSesion(onGuardada: () -> Unit) {
        val usuario = usuarioAutenticado ?: return

        viewModelScope.launch {
            sesionDao.guardarSesion(
                Sesion(
                    correo = usuario.correo,
                    rol = usuario.rol.name
                )
            )

            onGuardada()
        }
    }

    fun recuperarSesion(onRecuperada: (Usuario?) -> Unit) {
        viewModelScope.launch {
            val sesionGuardada = sesionDao.obtenerSesion()

            val usuario = try {
                sesionGuardada?.let { sesion ->
                    Usuario(
                        correo = sesion.correo,
                        contrasena = "",
                        rol = RolUsuario.valueOf(sesion.rol)
                    )
                }
            } catch (e: IllegalArgumentException) {
                null
            }

            usuarioAutenticado = usuario
            onRecuperada(usuario)
        }
    }

    fun cerrarSesion(onCerrada: () -> Unit) {
        viewModelScope.launch {
            sesionDao.cerrarSesion()

            usuarioAutenticado = null
            correo = ""
            contrasena = ""
            textoVisible = false

            onCerrada()
        }
    }
}
