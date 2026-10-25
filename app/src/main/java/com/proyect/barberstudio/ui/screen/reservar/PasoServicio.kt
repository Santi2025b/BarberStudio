package com.proyect.barberstudio.ui.screen.reservar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.Servicio
import com.proyect.barberstudio.ui.components.BarraContinuar
import com.proyect.barberstudio.ui.components.TituloPaso
import com.proyect.barberstudio.ui.components.bordeOpcion
import com.proyect.barberstudio.ui.components.coloresOpcion
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Paso 2: elegir el servicio de una lista.
@Composable
fun PasoServicio(
    servicios: List<Servicio>,
    servicioElegido: Servicio?,
    onServicioClick: (Servicio) -> Unit,
    onContinuar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Acá usamos LazyColumn (y no Column + verticalScroll) porque es una LISTA de datos
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TituloPaso(
                    titulo = stringResource(R.string.reservar_servicio_titulo),
                    subtitulo = stringResource(R.string.reservar_servicio_subtitulo),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(servicios, key = { it.id }) { servicio ->
                ServicioOpcion(
                    servicio = servicio,
                    // ?.id: si servicioElegido es null, da null (y null no es igual a ningún id)
                    elegido = servicio.id == servicioElegido?.id,
                    onClick = { onServicioClick(servicio) }
                )
            }
        }

        BarraContinuar(
            // Si hay servicio elegido, ?.let arma "Corte + barba · 45 min · $55".
            // Si es null, el let no se ejecuta y ?: pone "" (vacío).
            resumen = servicioElegido?.let {
                stringResource(R.string.reservar_resumen_servicio, it.nombre, it.duracionMin, it.precio)
            } ?: "",
            textoBoton = stringResource(R.string.reservar_continuar),
            habilitado = servicioElegido != null,
            onClick = onContinuar
        )
    }
}

// Una fila de la lista: foto, nombre, descripción, duración, y a la derecha el círculo de selección y el precio.
@Composable
private fun ServicioOpcion(
    servicio: Servicio,
    elegido: Boolean,
    onClick: () -> Unit
) {
    // OutlinedCard con onClick: toda la tarjeta se puede tocar
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = coloresOpcion(elegido),   // de PasosReserva.kt
        border = bordeOpcion(elegido)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = servicio.imagen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = servicio.nombre, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = servicio.descripcion, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.reservar_duracion, servicio.duracionMin),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                // RadioButton: el circulito de "elegí uno solo". Tocarlo hace lo mismo que tocar la tarjeta.
                RadioButton(selected = elegido, onClick = onClick)
                Text(text = stringResource(R.string.precio, servicio.precio), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PasoServicioPreview() {
    BarberStudioTheme {
        PasoServicio(
            servicios = DatosDePrueba.servicios,
            servicioElegido = DatosDePrueba.servicios[2],
            onServicioClick = {},
            onContinuar = {}
        )
    }
}