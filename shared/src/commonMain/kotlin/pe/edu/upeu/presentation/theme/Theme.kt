package pe.edu.upeu.presentation.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = TextLight,
    onPrimaryContainer = PrimaryContainer,
    secondary = AccentGold,
    onSecondary = TextDark,
    background = BackgroundLight,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}


