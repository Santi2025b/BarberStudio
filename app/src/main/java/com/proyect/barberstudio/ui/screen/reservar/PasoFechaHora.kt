package com.proyect.barberstudio.ui.screen.reservar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.ui.components.BarraContinuar
import com.proyect.barberstudio.ui.components.TituloPaso
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Paso 1: elegir día y hora. No guarda nada: recibe lo elegido y avisa los toques.
@Composable
fun PasoFechaHora(
    diaElegido: Int?,          // null = todavía no eligió día
    horaElegida: String?,      // null = todavía no eligió hora
    horarios: List<String>,
    resumen: String,           // "Vie, 16 de octubre · 10:00" (lo arma ReservarTurnoScreen)
    onDiaClick: (Int) -> Unit,
    onHoraClick: (String) -> Unit,
    onContinuar: () -> Unit
) {
    // Todos los pasos tienen esta forma: arriba el contenido (se scrollea), abajo la BarraContinuar (fija)
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f) // ocupa todo el alto menos lo que necesita la barra de abajo
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            TituloPaso(
                titulo = stringResource(R.string.reservar_fecha_titulo),
                subtitulo = stringResource(R.string.reservar_fecha_subtitulo)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = DatosDePrueba.MES_TITULO, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Calendario(diaElegido = diaElegido, onDiaClick = onDiaClick)

            // Los horarios aparecen recién cuando hay un día elegido.
            // Adentro del if, Kotlin ya sabe que diaElegido NO es null  y lo deja usar como Int.
            if (diaElegido != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = DatosDePrueba.fechaLarga(diaElegido), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                // chunked(3) parte la lista en grupos de 3: [[09:00, 09:30, 10:00], [11:00, 13:00, 14:30]]
                // Cada grupo es una fila.
                horarios.chunked(3).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fila.forEach { hora ->
                            val elegida = hora == horaElegida
                            // FilterChip: el botoncito que se "prende" al elegirlo (fondo celeste)
                            FilterChip(
                                selected = elegida,
                                onClick = { onHoraClick(hora) },
                                label = {
                                    Text(text = hora, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                                },
                                // El tilde solo si está elegida. Mismo truco que el ojito de AppTextField:
                                // un ícono o null.
                                leadingIcon = if (elegida) {
                                    { Icon(imageVector = Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else {
                                    null
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.reservar_horario_estudio),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        BarraContinuar(
            resumen = resumen,
            textoBoton = stringResource(R.string.reservar_continuar),
            habilitado = diaElegido != null && horaElegida != null, // && = "y": los dos tienen que estar
            onClick = onContinuar
        )
    }
}

// El calendario del mes: una fila con las letras de los días y debajo una fila por semana.
@Composable
private fun Calendario(
    diaElegido: Int?,
    onDiaClick: (Int) -> Unit
) {
    // Las celdas: 3 vacías (null) hasta el jueves 1, y después los días 1 a 31.
    // List<Int?> = lista de números que PUEDEN ser null (los huecos del principio).
    val celdas = List<Int?>(DatosDePrueba.PRIMER_DIA_SEMANA) { null } + (1..DatosDePrueba.DIAS_DEL_MES)

    // De a 7 celdas: cada grupo es una semana, o sea, una fila del calendario.
    val semanas = celdas.chunked(7)

    Column {
        // Fila de letras: L M X J V S D (vienen del string-array)
        Row(modifier = Modifier.fillMaxWidth()) {
            stringArrayResource(R.array.dias_semana_corto).forEach { letra ->
                Text(
                    text = letra,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        semanas.forEach { semana ->
            Row(modifier = Modifier.fillMaxWidth()) {
                semana.forEach { dia ->
                    DiaCelda(
                        dia = dia,
                        elegido = dia != null && dia == diaElegido,
                        disponible = dia != null && dia !in DatosDePrueba.diasNoDisponibles,
                        onClick = { if (dia != null) onDiaClick(dia) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // La última semana puede tener menos de 7 días: rellenamos lo que falta con espacio vacío
                repeat(7 - semana.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// Un día del calendario: un círculo con el número. Azul si está elegido, gris claro si no se puede elegir.
@Composable
private fun DiaCelda(
    dia: Int?,
    elegido: Boolean,
    disponible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f) // cuadrada: alto igual al ancho
            .padding(3.dp)
            .clip(CircleShape) // recorta en círculo: el fondo y el efecto del toque quedan redondos
            .background(if (elegido) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(enabled = disponible, onClick = onClick), // si no está disponible, no se puede tocar
        contentAlignment = Alignment.Center
    ) {
        if (dia != null) {
            Text(
                text = dia.toString(),
                fontSize = 14.sp,
                fontWeight = if (elegido) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    elegido -> MaterialTheme.colorScheme.onPrimary   // blanco sobre azul
                    disponible -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.outline        // gris claro: no se puede elegir
                }
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PasoFechaHoraPreview() {
    BarberStudioTheme {
        PasoFechaHora(
            diaElegido = 16,
            horaElegida = "10:00",
            horarios = DatosDePrueba.horarios,
            resumen = "Vie, 16 de octubre · 10:00",
            onDiaClick = {},
            onHoraClick = {},
            onContinuar = {}
        )
    }
}