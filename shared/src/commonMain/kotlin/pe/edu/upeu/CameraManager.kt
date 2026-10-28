package pe.edu.upeu

import androidx.compose.runtime.Composable

@Composable
expect fun rememberCameraLauncher
            (onResult: (ByteArray?) -> Unit): CameraLauncher

interface CameraLauncher {
    fun launch()
}