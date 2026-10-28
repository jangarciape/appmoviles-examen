package pe.edu.upeu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeContent(onStartClick: ()-> Unit){

    Column (
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text("Bienvenido a AppBooking!", style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center)
        Text("La mejor estrategia para gestionar tus reservas y pagos",
            modifier = Modifier.padding(vertical = 24.dp),
            textAlign = TextAlign.Center
            )
        Button(onClick = onStartClick, modifier = Modifier.fillMaxWidth()){
            Text("Comenzar ahora")
        }


    }

}