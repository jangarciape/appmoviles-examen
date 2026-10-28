package pe.edu.upeu.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.presentation.components.RoomCard
import pe.edu.upeu.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    viewModel: HomeViewModel,
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onLogout: () -> Unit,
    onReserveClick: (RoomType) -> Unit,
    onEditProfile: () -> Unit,
    onCartClick: () -> Unit,
    cartSize: Int,
    onAdminClick: () -> Unit = {}
) {
    // controlar los estados del home
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectTab by remember { mutableStateOf(0) }

    val isAdmin = uiState.currentUser?.role?.equals("ADMIN", ignoreCase = true) == true

    val roleLabel = if (isAdmin) "Rol: ADMINISTRADOR 🛠️" else "Rol: CLIENTE 👤"

    // estructura del drawer (menú lateral)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoggedIn) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                // componente 1: icono o foto de perfil
                                val userImage = uiState.currentUser?.image
                                if (!userImage.isNullOrBlank()) {
                                    KamelImage(
                                        resource = asyncPainterResource(data = userImage),
                                        contentDescription = "Imagen de perfil",
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(CircleShape)
                                            .clickable { onEditProfile() },
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clickable { onEditProfile() },
                                        tint = Color.White
                                    )
                                }

                                // componente 2: lápiz de edición
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .offset(x = (-5).dp, y = (-4).dp)
                                ) {
                                    IconButton(onClick = onEditProfile) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }
                            }
                            Text(
                                uiState.currentUser?.name ?: "Nombre de Usuario",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                uiState.currentUser?.email ?: "Email de Usuario",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(top = 6.dp)
                            ) {
                                Text(
                                    text = roleLabel,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = Color.White.copy(alpha = 0.5f)
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    onLoginClick()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(
                                    "Iniciar Sesión",
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                if (isLoggedIn) {
                    NavigationDrawerItem(
                        label = { Text("Mi Perfil") },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) }
                    )
                    if (isAdmin) {
                        NavigationDrawerItem(
                            label = { Text("Perfil Admin / Gestión") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                onAdminClick()
                            },
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) }
                        )
                    }
                    NavigationDrawerItem(
                        label = { Text("Cerrar Sesión") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onLogout()
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Reservas App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = onCartClick) {
                            BadgedBox(
                                badge = {
                                    if (cartSize > 0) {
                                        Badge { Text("$cartSize") }
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectTab == 0,
                        onClick = { selectTab = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = selectTab == 1,
                        onClick = { selectTab = 1 },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                        label = { Text("Reservas") }
                    )
                }
            }
        ) { padding ->
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(uiState.successMessage) {
                uiState.successMessage?.let { message ->
                    snackbarHostState.showSnackbar(message)
                    viewModel.clearMessages()
                }
            }
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (selectTab == 0) {
                    PullToRefreshBox(
                        isRefreshing = uiState.isLoading,
                        onRefresh = { viewModel.loadRooms() }
                    ) {
                        val error = uiState.errorMessage
                        if (uiState.rooms.isEmpty() && error != null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(error, color = MaterialTheme.colorScheme.error)
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                item {
                                    Text(
                                        "Habitaciones disponibles",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    Spacer(Modifier.height(16.dp))
                                }
                                items(uiState.rooms) { room ->
                                    RoomCard(
                                        room = room,
                                        onViewDetail = { onReserveClick(room) }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    if (uiState.reservations.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Historial de Reservas Vacío",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "Realiza un pago para ver tus habitaciones ocupadas aquí.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    "Mis Reservas Confirmadas (${uiState.reservations.size})",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(Modifier.height(8.dp))
                            }
                            items(uiState.reservations) { res ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(3.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = res.roomName,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = MaterialTheme.colorScheme.error
                                            ) {
                                                Text(
                                                    text = res.status, // "OCUPADO"
                                                    color = MaterialTheme.colorScheme.onError,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            text = "Entrada: ${res.checkIn} | Salida: ${res.checkOut}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "Monto Pagado: S/ ${res.price}",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Código de Reserva: ${res.id}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
