package com.proyect.barberstudio.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Título de una sección ("Servicios", "Productos") con la acción opcional de "Ver todo"
@Composable
fun SeccionTitulo(
    titulo: String,
    modifier: Modifier = Modifier,
    accion: String? = null,         // String? = puede ser null. Si es null, no se muestra el botón.
    onAccionClick: () -> Unit = {}  // por defecto una lambda vacía, así no es obligatorio pasarla
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // weight(1f) en el texto: ocupa todo el ancho que sobra y deja el botón pegado a la derecha
        Text(text = titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

        // Adentro del if, Kotlin sabe que accion NO es null así que la podemos pasar a Text.
        if (accion != null) {
            // TextButton usa el color "primary" del tema (azul)
            TextButton(onClick = onAccionClick) {
                Text(text = accion, fontSize = 13.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previwSeccionTitulo(){
    BarberStudioTheme() {
        SeccionTitulo(
            titulo = "Servicios",
            accion = "Ver todo",
            onAccionClick = {}
        )
    }
}