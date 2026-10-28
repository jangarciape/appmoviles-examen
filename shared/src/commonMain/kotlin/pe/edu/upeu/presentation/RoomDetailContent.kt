package pe.edu.upeu.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.presentation.components.BookingButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailContent(
    room: RoomType,
    onBack: () -> Unit,
    onAddToCart: (RoomType) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Habitación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Galería de Imágenes (Real o Placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                if (room.images.isNotEmpty()) {
                    KamelImage(
                        resource = asyncPainterResource(data = room.images.first().url),
                        contentDescription = room.images.first().altText,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Galería de Imágenes", color = Color.White)
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "S/ ${room.price} por noche",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Capacidad: ${room.capacity} personas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(16.dp))
                Text("Descripción", style = MaterialTheme.typography.titleMedium)
                Text(room.description, style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(24.dp))

                // SECCIÓN DE AMENITIES / SERVICIOS
                Text("Servicios incluidos", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (room.amenities.isNotEmpty()) {
                        room.amenities.forEach { amenity ->
                            AmenityIcon(Icons.Default.Check, amenity.name)
                        }
                    } else {
                        AmenityIcon(Icons.Default.Check, "WiFi")
                        AmenityIcon(Icons.Default.Check, "TV")
                        AmenityIcon(Icons.Default.Check, "A/C")
                    }
                }

                Spacer(Modifier.height(32.dp))

                // BOTÓN FINAL DE RESERVA
                BookingButton(
                    text = "Agregar a mi lista",
                    onClick = {
                        onAddToCart(room)
                    }
                )
            }
        }
    }
}

@Composable
fun AmenityIcon(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
