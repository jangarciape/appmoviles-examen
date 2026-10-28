package pe.edu.upeu.presentation.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.presentation.components.BookingButton
import pe.edu.upeu.presentation.components.BookingTextField
import pe.edu.upeu.presentation.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomContent(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var roomNumber by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var selectedRoomType by remember { mutableStateOf<RoomType?>(null) }
    var status by remember { mutableStateOf("available") }
    var validationMessage by remember { mutableStateOf<String?>(null) }

    var expandedTypeDropdown by remember { mutableStateOf(false) }
    var expandedStatusDropdown by remember { mutableStateOf(false) }

    val statusOptions = listOf("available" to "Disponible", "occupied" to "Ocupado", "maintenance" to "Mantenimiento")
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuarto / Habitación") },
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
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registrar Cuarto Físico",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Asigna un número de cuarto a un tipo de habitación existente",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            BookingTextField(
                value = roomNumber,
                onValueChange = { roomNumber = it },
                label = "Número de Cuarto (Ej: 101, 202-B)",
                icono = Icons.Default.MeetingRoom
            )

            Spacer(Modifier.height(12.dp))

            BookingTextField(
                value = floor,
                onValueChange = { floor = it.filter { character -> character.isDigit() || character == '-' } },
                label = "Piso (opcional)",
                icono = Icons.Default.Home
            )

            Spacer(Modifier.height(16.dp))

            // Selector de Tipo de Habitación
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedRoomType?.name ?: "Seleccionar Tipo de Habitación",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Habitación") },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedTypeDropdown = true },
                    enabled = false
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { expandedTypeDropdown = true }
                )
                DropdownMenu(
                    expanded = expandedTypeDropdown,
                    onDismissRequest = { expandedTypeDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    state.roomTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text("${type.name} (Capacidad: ${type.capacity})") },
                            onClick = {
                                selectedRoomType = type
                                expandedTypeDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Selector de Estado del Cuarto
            Box(modifier = Modifier.fillMaxWidth()) {
                val currentStatusLabel = statusOptions.find { it.first == status }?.second ?: "Disponible"
                OutlinedTextField(
                    value = currentStatusLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado del Cuarto") },
                    leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedStatusDropdown = true },
                    enabled = false
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { expandedStatusDropdown = true }
                )
                DropdownMenu(
                    expanded = expandedStatusDropdown,
                    onDismissRequest = { expandedStatusDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    statusOptions.forEach { (key, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                status = key
                                expandedStatusDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                BookingButton(
                    text = "Guardar Cuarto",
                    onClick = {
                        val selectedType = selectedRoomType
                        val floorValue = if (floor.isBlank()) null else floor.toIntOrNull()
                        validationMessage = when {
                            roomNumber.isBlank() -> "Ingresa el número del cuarto."
                            roomNumber.length > 20 -> "El número no puede superar 20 caracteres."
                            selectedType == null -> "Selecciona un tipo de habitación."
                            floor.isNotBlank() && floorValue == null -> "El piso debe ser un número entero."
                            else -> null
                        }
                        if (validationMessage == null && selectedType != null) {
                            viewModel.createRoom(
                                roomNumber = roomNumber,
                                roomTypeId = selectedType.id,
                                status = status,
                                floor = floorValue,
                                onSuccess = onBack
                            )
                        }
                    }
                )
            }

            validationMessage?.let { message ->
                Spacer(Modifier.height(12.dp))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            state.errorMessage?.let { err ->
                Spacer(Modifier.height(12.dp))
                Text(
                    text = err,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
