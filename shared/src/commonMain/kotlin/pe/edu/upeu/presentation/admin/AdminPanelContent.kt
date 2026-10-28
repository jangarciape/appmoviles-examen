package pe.edu.upeu.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import pe.edu.upeu.domain.model.User
import pe.edu.upeu.presentation.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelContent(
    viewModel: AdminViewModel,
    user: User?,
    onBack: () -> Unit,
    onNavigateToCreateRoomType: () -> Unit,
    onNavigateToCreateRoom: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val profile = state.adminProfile ?: user

    LaunchedEffect(Unit) {
        viewModel.loadAdminData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil Administrador") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Tarjeta de Información de Usuario Admin (Gradiente Azul Elegante y Alta Legibilidad)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.White,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF0D47A1),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                text = profile?.name ?: "Administrador",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = profile?.email ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "Rol: ${profile?.role?.uppercase() ?: "ADMINISTRADOR"}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Resumen del sistema",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            val metrics = state.dashboard
            if (state.isDashboardLoading && metrics == null) {
                CircularProgressIndicator()
            } else if (metrics != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminMetricCard("Usuarios", metrics.totalUsers.toString(), Modifier.weight(1f))
                    AdminMetricCard("Tipos", metrics.totalRoomTypes.toString(), Modifier.weight(1f))
                    AdminMetricCard("Cuartos", metrics.totalRooms.toString(), Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AdminMetricCard("Disponibles", metrics.availableRooms.toString(), Modifier.weight(1f))
                    AdminMetricCard("Reservas", metrics.totalBookings.toString(), Modifier.weight(1f))
                }
            }
            state.dashboardError?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            state.profileError?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Acciones de Gestión",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            // Botón 1: Crear Tipo de Habitación (RoomType)
            Button(
                onClick = onNavigateToCreateRoomType,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Crear Tipo de Habitación (RoomType)")
            }

            Spacer(Modifier.height(12.dp))

            // Botón 2: Crear Cuarto (Room)
            OutlinedButton(
                onClick = onNavigateToCreateRoom,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.MeetingRoom, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Crear Cuarto (Habitación Específica)")
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Tipos de Habitación Existentes (${state.roomTypes.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            if (state.isRoomTypesLoading && state.roomTypes.isEmpty()) {
                CircularProgressIndicator()
            }
            state.errorMessage?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.roomTypes) { roomType ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = roomType.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = roomType.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2
                                )
                                Text(
                                    text = "Capacidad: ${roomType.capacity} personas",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Text(
                                text = "S/ ${roomType.price}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun AdminMetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}
