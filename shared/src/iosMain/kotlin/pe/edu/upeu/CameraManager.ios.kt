package pe.edu.upeu
import androidx.compose.runtime.Composable

@Composable
actual fun rememberCameraLauncher(onResult: (ByteArray?) -> Unit): CameraLauncher {
    return object : CameraLauncher {
        override fun launch() {
            // Se deja vacío por ahora (Windows no puede compilar esto)
        }
    }
}
