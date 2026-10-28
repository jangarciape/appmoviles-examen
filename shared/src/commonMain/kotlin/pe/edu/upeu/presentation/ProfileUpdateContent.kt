package pe.edu.upeu.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import pe.edu.upeu.presentation.components.BookingButton
import pe.edu.upeu.presentation.components.BookingTextField
import pe.edu.upeu.rememberCameraLauncher  //<---- Importante es el puente de comunicacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUpdateContent(
    onBack: () -> Unit,
    onSave: (ByteArray?) -> Unit
){
    var name by remember { mutableStateOf("Juan") }
    var lastname by remember { mutableStateOf("Perez") }
    var phone by remember { mutableStateOf("999999999") }

    //estados de imagen y dialog
    var selectedImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var showOptions by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    //3. lanzar libreria de galeria(Peekaboo)
    val galleryLauncher = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = {
            byteArrays -> selectedImageBytes = byteArrays.firstOrNull()
        }
    )
    //4. lanzamos camara
    val cameraLauncher = rememberCameraLauncher {
        bytes -> if (bytes !=null) selectedImageBytes = bytes
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack){
                        Icon(Icons.Default.ArrowBack,null)
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    ){ padding ->
        Column (modifier = Modifier.padding(padding).
              fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp)
                    .clickable {showOptions=true} //abrir un dialog
            ){
                if (selectedImageBytes !=null){
                    Image(
                        bitmap = selectedImageBytes!!.toImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }else{
                    Surface (shape =CircleShape, color = Color.LightGray,
                        modifier = Modifier.fillMaxSize() ) {
                        Icon(Icons.Default.Person,null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                            )
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.BottomEnd).size(40.dp),
                    shadowElevation = 4.dp
                ) {
                    Icon(Icons.Default.Add, null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(8.dp))
                }
            }
            Spacer(Modifier.height(32.dp))
            BookingTextField(value = name, onValueChange = { name = it },
                label = "Nombre", icono = Icons.Default.Person)
            Spacer(Modifier.height(16.dp))
            BookingTextField(value = lastname, onValueChange = { lastname = it },
                label = "Apellido", icono = Icons.Default.AccountBox)
            Spacer(Modifier.height(16.dp))
            BookingTextField(value = phone, onValueChange = { phone = it },
                label = "Teléfono", icono = Icons.Default.Phone)

            Spacer(Modifier.weight(1f))

            BookingButton(text = "Actualizar mis datos", onClick = { onSave(selectedImageBytes) })

        }
    }

    // 5. DIÁLOGO DE SELECCIÓN (Cámara o Galería)
    if (showOptions) {
        AlertDialog(
            onDismissRequest = { showOptions = false },
            title = { Text("Cambiar foto") },
            text = { Text("Selecciona una opción para actualizar tu foto de perfil.") },
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        cameraLauncher.launch() // <--- ACTIVA CÁMARA
                        showOptions = false
                    }
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Tomar Foto")
                }
            },
            dismissButton = {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        galleryLauncher.launch() // <--- ABRE GALERÍA
                        showOptions = false
                    }
                ) {
                    Icon(Icons.Default.List, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Elegir de Galería")
                }
            }
        )
    }


}