package pe.edu.upeu.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.presentation.components.BookingButton
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    cartItems: List<RoomType>,
    onBack: () -> Unit,
    onRemoveItem: (RoomType) -> Unit,
    onCheckout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (cartItems.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { room ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(room.name, style = MaterialTheme.typography.titleMedium)
                                    Text("S/ ${room.price}", color = MaterialTheme.colorScheme.secondary)
                                }
                                IconButton(onClick = { onRemoveItem(room) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                val total = cartItems.sumOf { it.price }
                Text("Total a pagar: S/ $total", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)

                Spacer(Modifier.height(16.dp))

                BookingButton(text = "Procesar Reserva", onClick = onCheckout)
            }
        }
    }
}