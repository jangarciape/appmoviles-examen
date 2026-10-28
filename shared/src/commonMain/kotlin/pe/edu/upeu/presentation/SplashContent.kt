package pe.edu.upeu.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashContent(onTimeout: () -> Unit){

    LaunchedEffect(Unit){
        delay(2000)
        onTimeout()
    }
    Box(
       modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "Booking App",
              style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }

}