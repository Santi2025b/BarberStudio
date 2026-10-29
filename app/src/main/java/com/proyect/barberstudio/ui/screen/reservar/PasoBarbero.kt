package com.proyect.barberstudio.ui.screen.reservar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.Barbero
import com.proyect.barberstudio.ui.components.BarraContinuar
import com.proyect.barberstudio.ui.components.TituloPaso
import com.proyect.barberstudio.ui.components.bordeOpcion
import com.proyect.barberstudio.ui.components.coloresOpcion
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Paso 3: elegir barbero, o "cualquiera".
@Composable
fun PasoBarbero(
    barberos: List<Barbero>,
    barberoElegido: Barbero?,
    resumenFechaHora: String,   // "Vie, 16 de octubre · 10:00", para el resumen de abajo
    onBarberoClick: (Barbero) -> Unit,
    onContinuar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TituloPaso(
                titulo = stringResource(R.string.reservar_barbero_titulo),
                subtitulo = stringResource(R.string.reservar_barbero_subtitulo)
            )

            // Opción "Cualquier barbero disponible" (es el Barbero con id 0 de DatosDePrueba)
            val cualquiera = DatosDePrueba.cualquierBarbero
            val eligioCualquiera = barberoElegido?.id == cualquiera.id

            OutlinedCard(
                onClick = { onBarberoClick(cualquiera) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = coloresOpcion(eligioCualquiera),
                border = bordeOpcion(eligioCualquiera)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.reservar_cualquier_barbero),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(R.string.reservar_cualquier_barbero_detalle),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    RadioButton(selected = eligioCualquiera, onClick = { onBarberoClick(cualquiera) })
                }
            }

            // La grilla: de a 2 barberos por fila
            barberos.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    fila.forEach { barbero ->
                        BarberoCard(
                            barbero = barbero,
                            elegido = barbero.id == barberoElegido?.id,
                            onClick = { onBarberoClick(barbero) },
                            modifier = Modifier.weight(1f) // las 2 tarjetas de la fila miden lo mismo
                        )
                    }
                    // Si la última fila tiene un solo barbero, un espacio ocupa el lugar del segundo
                    if (fila.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }

        BarraContinuar(
            // "Marcos Red · Vie, 16 de octubre · 10:00"
            resumen = barberoElegido?.let {
                stringResource(R.string.texto_con_punto, it.nombre, resumenFechaHora)
            } ?: "",
            textoBoton = stringResource(R.string.reservar_continuar),
            habilitado = barberoElegido != null,
            onClick = onContinuar
        )
    }
}

// Tarjeta de un barbero en la grilla: foto , nombre, especialidad y puntaje.
@Composable
private fun BarberoCard(
    barbero: Barbero,
    elegido: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = coloresOpcion(elegido),
        border = bordeOpcion(elegido)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Todavía no hay fotos de los barberos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = barbero.nombre, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            // minLines = 2: aunque la especialidad entre en 1 renglón, ocupa 2. Así las tarjetas quedan parejas.
            Text(
                text = barbero.especialidad,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 2,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.barbero_puntaje, barbero.puntaje, barbero.resenas),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PasoBarberoPreview() {
    BarberStudioTheme {
        PasoBarbero(
            barberos = DatosDePrueba.barberos,
            barberoElegido = DatosDePrueba.barberos[0],
            resumenFechaHora = "Vie, 16 de octubre · 10:00",
            onBarberoClick = {},
            onContinuar = {}
        )
    }
}