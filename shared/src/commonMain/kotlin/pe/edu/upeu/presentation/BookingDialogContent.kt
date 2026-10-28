package pe.edu.upeu.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.presentation.components.BookingButton
import pe.edu.upeu.presentation.components.BookingTextField
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDialogContent(
    cartItems: List<RoomType>,
    onConfirm: (checkIn: String, checkOut: String, guests: Int) -> Unit
) {
    var checkIn by remember { mutableStateOf("Seleccionar fecha") }
    var checkOut by remember { mutableStateOf("Seleccionar fecha") }
    var guests by remember { mutableStateOf("1") }

    // ESTADOS PARA EL CALENDARIO
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var pickingForCheckIn by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(24.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Finalizar Reserva", style = MaterialTheme.typography.headlineSmall)
        Text("Habitaciones: ${cartItems.size}", style = MaterialTheme.typography.bodySmall)

        Spacer(Modifier.height(16.dp))

        // CAMPO FECHA ENTRADA (Clickable)
        OutlinedTextField(
            value = checkIn,
            onValueChange = {},
            readOnly = true, // <--- No permite escribir
            label = { Text("Fecha Entrada") },
            leadingIcon = {
                IconButton(onClick = { pickingForCheckIn = true; showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, null)
                }
            },
            modifier = Modifier.fillMaxWidth().clickable {
                pickingForCheckIn = true; showDatePicker = true }
        )

        Spacer(Modifier.height(8.dp))

        // CAMPO FECHA SALIDA (Clickable)
        OutlinedTextField(
            value = checkOut,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha Salida") },
            leadingIcon = { IconButton(onClick = { pickingForCheckIn = false; showDatePicker = true }) {
                Icon(Icons.Default.DateRange, null)
            }},
            modifier = Modifier.fillMaxWidth().clickable { pickingForCheckIn = false; showDatePicker = true }
        )

        Spacer(Modifier.height(8.dp))

        BookingTextField(value = guests, onValueChange = { guests = it }, label = "Huéspedes",
            icono = Icons.Default.Person)

        Spacer(Modifier.height(24.dp))

        BookingButton(
            text = "Siguiente: Pago",
            onClick = { onConfirm(checkIn, checkOut, guests.toIntOrNull() ?: 1) }
        )
    }

    // LÓGICA DEL CALENDARIO (MODAL)
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        // Función simple para formatear a YYYY-MM-DD
                        instantToDateString(it)
                    } ?: ""
                    if (pickingForCheckIn) checkIn = selectedDate else checkOut = selectedDate
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Función auxiliar para formatear la fecha (Explicar a los alumnos)
fun instantToDateString(millis: Long): String {
    val date = kotlinx.datetime.Instant.fromEpochMilliseconds(millis)
        .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
    return "${date.year}-${date.monthNumber.toString().padStart(2, '0')}-${date.dayOfMonth.toString().padStart(2, '0')}"
}
