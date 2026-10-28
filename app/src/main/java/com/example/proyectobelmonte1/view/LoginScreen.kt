
package com.example.proyectobelmonte1.view
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.example.proyectobelmonte1.viewmodel.LoginViewModel
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.OutlinedTextFieldDefaults
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.text.input.PasswordVisualTransformation
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import android.util.Log
    import androidx.navigation.NavController
    import com.example.proyectobelmonte1.navigation.Routes
    import com.example.proyectobelmonte1.model.RolUsuario
    import androidx.compose.runtime.LaunchedEffect



@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {

    val correo = loginViewModel.correo
    val contrasena = loginViewModel.contrasena

    LaunchedEffect(Unit) {
        loginViewModel.recuperarSesion { usuario ->
            when (usuario?.rol) {
                RolUsuario.ADMINISTRADOR -> {
                    navController.navigate(Routes.ADMINISTRADOR) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                RolUsuario.SOLICITANTE -> {
                    navController.navigate(Routes.SOLICITANTE) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                RolUsuario.CONDUCTOR -> {
                    navController.navigate(Routes.CONDUCTOR) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                null -> Unit
            }
        }
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 26.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "B",
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "BELMONTE",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Transporte corporativo",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(34.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                    .padding(22.dp)
            ) {
                Text(
                    text = "Bienvenido",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ingresa a tu cuenta para continuar.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Correo electrónico",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { nuevoCorreo ->
                        loginViewModel.actualizarCorreo(nuevoCorreo)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("nombre@empresa.cl") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = Color(0xFF444658),
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.secondary
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Contraseña",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { nuevaContrasena ->
                        loginViewModel.actualizarContrasena(nuevaContrasena)
                    },


                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ingresa tu contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = Color(0xFF444658),
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.secondary
                    )
                )
                if (loginViewModel.textoVisible) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Credenciales inválidas",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))

                Button(

                    onClick = {
                        val accesoPermitido = loginViewModel.validarCredenciales()

                        Log.d(
                            "LoginScreen",
                            "¿Credenciales válidas?: $accesoPermitido"
                        )

                        if (accesoPermitido) {
                            when (loginViewModel.usuarioAutenticado?.rol) {
                                RolUsuario.ADMINISTRADOR -> {
                                    navController.navigate(Routes.ADMINISTRADOR)
                                }
                            loginViewModel.guardarSesion {
                                when (loginViewModel.usuarioAutenticado?.rol) {
                                    RolUsuario.ADMINISTRADOR -> {
                                        navController.navigate(Routes.ADMINISTRADOR) {
                                            popUpTo(Routes.LOGIN) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }

                                RolUsuario.SOLICITANTE -> {
                                    navController.navigate(Routes.SOLICITANTE)
                                }
                                    RolUsuario.SOLICITANTE -> {
                                        navController.navigate(Routes.SOLICITANTE) {
                                            popUpTo(Routes.LOGIN) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }

                                RolUsuario.CONDUCTOR -> {
                                    navController.navigate(Routes.CONDUCTOR)
                                }
                                    RolUsuario.CONDUCTOR -> {
                                        navController.navigate(Routes.CONDUCTOR) {
                                            popUpTo(Routes.LOGIN) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }

                                    null -> Unit
                                }
                            }
                        }
                    },




                            modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "Regístrate",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BELMONTE · TRANSPORTE SEGURO",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        }
    }

