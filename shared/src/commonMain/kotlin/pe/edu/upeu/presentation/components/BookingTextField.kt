package pe.edu.upeu.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.HistoricalChange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun BookingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icono: ImageVector,
    isPassword : Boolean = false
){
    OutlinedTextField(
        value =value,
        onValueChange =onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icono, contentDescription =null ) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (isPassword){
            PasswordVisualTransformation()
        }else {
            VisualTransformation.None
        }
    )
}