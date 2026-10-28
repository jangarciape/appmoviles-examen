package pe.edu.upeu.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import pe.edu.upeu.presentation.viewmodel.AuthState
import pe.edu.upeu.presentation.viewmodel.AuthViewModel

@Composable
fun RegisterDialogContent(
    viewModel: AuthViewModel,
    onNavigationToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Completa tus datos para empezar a reservar",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        BookingTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nombre",
            icono = Icons.Default.Person
        )
        Spacer(Modifier.height(12.dp))

        BookingTextField(
            value = lastname,
            onValueChange = { lastname = it },
            label = "Apellidos",
            icono = Icons.Default.AccountBox
        )
        Spacer(Modifier.height(12.dp))

        BookingTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            icono = Icons.Default.Email
        )
        Spacer(Modifier.height(12.dp))

        BookingTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Teléfono",
            icono = Icons.Default.Phone
        )
        Spacer(Modifier.height(12.dp))

        BookingTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            icono = Icons.Default.Lock,
            isPassword = true
        )
        Spacer(Modifier.height(32.dp))

        if (state is AuthState.Loading) {
            CircularProgressIndicator()
        } else {
            BookingButton(
                text = "Registrarme",
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        viewModel.register(name, lastname, email, phone, password)
                    }
                }
            )
        }

        if (state is AuthState.Error) {
            Text(
                text = (state as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onNavigationToLogin) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }

        LaunchedEffect(state) {
            if (state is AuthState.Success) {
                onRegisterSuccess()
            }
        }
    }
}
