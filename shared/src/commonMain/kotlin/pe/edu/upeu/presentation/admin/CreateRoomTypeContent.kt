package pe.edu.upeu.presentation.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import pe.edu.upeu.presentation.components.BookingButton
import pe.edu.upeu.presentation.components.BookingTextField
import pe.edu.upeu.presentation.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomTypeContent(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Tipo de Habitación") },
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
                text = "Nuevo Tipo de Habitación",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Ingresa los detalles para registrar un nuevo tipo (Suite, Doble, etc.)",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            BookingTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre del tipo (Ej: Suite Ejecutiva)",
                icono = Icons.Default.Home
            )

            Spacer(Modifier.height(12.dp))

            BookingTextField(
                value = description,
                onValueChange = { description = it },
                label = "Descripción",
                icono = Icons.Default.Description
            )

            Spacer(Modifier.height(12.dp))

            BookingTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = "Capacidad (Número de personas)",
                icono = Icons.Default.People
            )

            Spacer(Modifier.height(12.dp))

            BookingTextField(
                value = price,
                onValueChange = { price = it },
                label = "Precio por Noche (S/)",
                icono = Icons.Default.AttachMoney
            )

            Spacer(Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                BookingButton(
                    text = "Guardar Tipo de Habitación",
                    onClick = {
                        val capInt = capacity.toIntOrNull()
                        val priceDbl = price.toDoubleOrNull()
                        validationMessage = when {
                            name.isBlank() -> "Ingresa el nombre del tipo."
                            description.isBlank() -> "Ingresa una descripción."
                            capInt == null || capInt <= 0 -> "La capacidad debe ser un entero mayor que cero."
                            priceDbl == null || priceDbl <= 0.0 -> "El precio debe ser mayor que cero."
                            else -> null
                        }
                        if (validationMessage == null && capInt != null && priceDbl != null) {
                            viewModel.createRoomType(
                                name = name,
                                description = description,
                                capacity = capInt,
                                pricePerNight = priceDbl,
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
