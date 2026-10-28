package pe.edu.upeu.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSecondary: Boolean = false
){
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isSecondary) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.primary
        )
    ){
        Text(text, style = MaterialTheme.typography.titleMedium)
    }

}