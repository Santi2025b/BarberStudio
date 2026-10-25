package com.proyect.barberstudio.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.components.AppTopBar

@Composable
fun TurnosScreen(
    modifier: Modifier = Modifier

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // el contenido es más alto que la pantalla
            .padding(vertical = 16.dp)
    ) {
        val margen =
            Modifier.padding(horizontal = 24.dp) // lo guardamos en una variable para no repetirlo

        AppTopBar(onNotificacionesClick = {}, modifier = margen)

        Spacer(modifier = Modifier.height(24.dp))

        // SALUDO: stringResource con un argumento rellena el %1$s -> "Hola, carlos."
        Text(
            text = stringResource(R.string.turnos_titulo),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = margen
        )
        Text(
            text = stringResource(R.string.turnos_subtitulo),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = margen
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviwTurnos(){
    TurnosScreen()
}