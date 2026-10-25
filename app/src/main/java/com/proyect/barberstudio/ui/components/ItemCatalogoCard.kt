package com.proyect.barberstudio.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Tarjeta con foto, título y subtítulo. (Card Generica)
//  las cards de Servicios y las de Productos tienen misma forma, solo cambia el texto de abajo.
// recibe textos sueltos, y quien la usa decide qué poner.
@Composable
fun ItemCatalogoCard(
    titulo: String,
    subtitulo: String,
    @DrawableRes imagen: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(150.dp)
            .clip(RoundedCornerShape(12.dp)) // recorta el efecto de "toque" con bordes redondeados
            .clickable(onClick = onClick)    // toda la tarjeta se puede tocar
    ) {
        Image(
            painter = painterResource(id = imagen),
            // null = imagen decorativa. TalkBack ya lee el título de abajo; si describiera la foto, repetiría.
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = titulo,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,                       // una sola línea...
            overflow = TextOverflow.Ellipsis    // ...y si no entra, termina en "..."
        )
        Text(text = subtitulo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun ItemCatalogoCardPreview() {
    BarberStudioTheme {
        ItemCatalogoCard(titulo = "Corte + barba", subtitulo = "$55 · 45 min", imagen = R.drawable.img_barber_bg, onClick = {})
    }
}