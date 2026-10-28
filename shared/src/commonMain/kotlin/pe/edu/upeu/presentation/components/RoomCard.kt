package pe.edu.upeu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import pe.edu.upeu.domain.model.RoomType

@Composable
fun RoomCard(room: RoomType, onViewDetail: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // IMAGEN DE LA HABITACIÓN
            Box(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                if (room.images.isNotEmpty()) {
                    println("🖼️ URL IMAGEN: ${room.images.first().url}")
                    KamelImage(
                        resource = asyncPainterResource(room.images.first().url),
                        contentDescription = room.images.first().altText,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text("Imagen no disponible", modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(room.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Text("Desde S/ ${room.price}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)

                Spacer(Modifier.height(8.dp))

                // BOTÓN "VER"
                Button(
                    onClick = onViewDetail,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Ver Detalles")
                }
            }
        }
    }
}