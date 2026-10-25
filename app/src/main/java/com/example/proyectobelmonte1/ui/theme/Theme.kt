
package com.example.proyectobelmonte1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BelmonteDarkColorScheme = darkColorScheme(
    primary = BelmonteVioleta,
    onPrimary = BelmonteTexto,
    secondary = BelmonteTurquesa,
    onSecondary = BelmonteFondo,
    tertiary = BelmonteTurquesa,
    background = BelmonteFondo,
    onBackground = BelmonteTexto,
    surface = BelmontePanel,
    onSurface = BelmonteTexto,
    onSurfaceVariant = BelmonteTextoSecundario
)

private val BelmonteLightColorScheme = lightColorScheme(
    primary = BelmonteVioleta,
    onPrimary = BelmonteTexto,
    secondary = BelmonteTurquesa,
    onSecondary = BelmonteFondo,
    tertiary = BelmonteVioleta,
    background = BelmonteFondo,
    onBackground = BelmonteTexto,
    surface = BelmontePanel,
    onSurface = BelmonteTexto,
    onSurfaceVariant = BelmonteTextoSecundario
)

@Composable
fun ProyectoBelmonte1Theme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        BelmonteDarkColorScheme
    } else {
        BelmonteLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
