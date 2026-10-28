package pe.edu.upeu.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
fun LoginDialogContent(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("¡Hola de nuevo!", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        BookingTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            icono = Icons.Default.Email
        )
        Spacer(Modifier.height(8.dp))
        BookingTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            icono = Icons.Default.Lock,
            isPassword = true
        )
        if (state is AuthState.Loading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        } else {
            Spacer(Modifier.height(24.dp))
            BookingButton(text = "Entrar", onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(email, password)
                }
            })
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
        TextButton(onClick = onNavigateToRegister) {
            Text("¿Eres nuevo? Regístrate")
        }

        LaunchedEffect(state) {
            if (state is AuthState.Success) {
                onLoginSuccess()
            }
        }
    }
}
