package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = androidx.compose.ui.graphics.Color.Black,
    secondary = GreenGrey80,
    tertiary = Emerald80,
    background = androidx.compose.ui.graphics.Color(0xFF07120B),
    onBackground = androidx.compose.ui.graphics.Color(0xFF00FF66),
    surface = androidx.compose.ui.graphics.Color(0xFF0B1811),
    onSurface = androidx.compose.ui.graphics.Color(0xFF00FF66),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF11251B),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFBEEFD8)
)

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = GreenGrey40,
    tertiary = Emerald40,
    background = androidx.compose.ui.graphics.Color(0xFFF1FFF8),
    onBackground = androidx.compose.ui.graphics.Color(0xFF0F1D17),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onSurface = androidx.compose.ui.graphics.Color(0xFF0F1D17),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFE7F7F0),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF2D473C)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
