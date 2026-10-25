package com.proyect.barberstudio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Parte de abajo de cada paso: un resumen chiquito de lo elegido + el botón grande.
// habilitado = false deja el botón gris y sin funcionar, hasta que el usuario elija algo.
@Composable
fun BarraContinuar(
    resumen: String,
    textoBoton: String,
    habilitado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Si no hay resumen (todavía no eligió nada), no dibujamos el texto ni su espacio
        if (resumen.isNotEmpty()) {
            Text(text = resumen, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = onClick,
            enabled = habilitado, //  el botón deshabilitado en gris
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(text = textoBoton, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarraContinuarPreview() {
    BarberStudioTheme {
        Column {
            BarraContinuar(resumen = "Vie, 16 de octubre · 10:00", textoBoton = "Continuar", habilitado = true, onClick = {})
            BarraContinuar(resumen = "", textoBoton = "Continuar", habilitado = false, onClick = {})
        }
    }
}