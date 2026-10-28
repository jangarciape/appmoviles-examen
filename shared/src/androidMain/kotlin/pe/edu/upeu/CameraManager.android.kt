package pe.edu.upeu


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberCameraLauncher(onResult: (ByteArray?) -> Unit): CameraLauncher {
    val context = LocalContext.current

    // 1. Lanzador para tomar la foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            onResult(stream.toByteArray())
        }
    }

    // 2. Lanzador para pedir el permiso
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch() // Si aceptó, abrimos la cámara
        } else {
            onResult(null) // Si rechazó, no hacemos nada
        }
    }

    return remember {
        object : CameraLauncher {
            override fun launch() {
                // 3. Lógica de verificación
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    cameraLauncher.launch() // Ya tiene permiso, abrir directo
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA) // No tiene, pedirlo
                }
            }
        }
    }
}