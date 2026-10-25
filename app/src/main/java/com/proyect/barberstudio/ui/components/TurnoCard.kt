package com.proyect.barberstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.EstadoTurno
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.ui.theme.BarberStudioTheme
import com.proyect.barberstudio.ui.theme.Verde


// Tarjeta de un turno en "Mis turnos". La usan las dos pestañas (Próximos e Historial).
// La tarjeta NO cancela ni reserva nada: solo AVISA que tocaron el botón.
@Composable
fun TurnoCard(
    turno: Turno,
    modifier: Modifier = Modifier,
    onCancelarClick: () -> Unit = {},
    onVolverAReservarClick: () -> Unit = {}
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila 1: cajita de fecha (ProximaVisitaCard.kt) + día y horario
            Row(verticalAlignment = Alignment.CenterVertically) {
                FechaCaja(dia = turno.dia, mes = turno.mes)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = turno.fecha, fontSize = 15.sp, fontWeight =
                            FontWeight.Bold
                    )
                    // "10:00–10:45 · 45 min": la duración sale del servicio del turno
                    Text(
                        text = stringResource(
                            R.string.turno_horario_duracion,
                            turno.horario, turno.servicio.duracionMin
                        ),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outline
            )
            // Fila 2: foto del barbero + servicio y barbero
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Person, contentDescription =
                                null, modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = stringResource(
                            R.string.turno_servicio_precio,
                            turno.servicio.nombre, turno.servicio.precio
                        ),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(
                            R.string.turno_con_barbero,
                            turno.barbero
                        ),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Fila 3: estado a la izquierda, botón a la derecha
            Row(verticalAlignment = Alignment.CenterVertically) {
                EstadoEtiqueta(estado = turno.estado)
                Spacer(modifier = Modifier.weight(1f)) // empuja el botón al borde derecho
                // "when" elige qué dibujar según el estado
                when (turno.estado) {
                    EstadoTurno.CONFIRMADO -> {
                        // Botón de solo texto, en rojo (el color "error" del tema)
                        TextButton(onClick = onCancelarClick) {
                            Text(
                                text = stringResource(R.string.turno_cancelar),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    // COMPLETADO y CANCELADO hacen lo mismo: se pueden volver a reservar
                            EstadoTurno . COMPLETADO, EstadoTurno.CANCELADO -> {
                        OutlinedButton(
                            onClick = onVolverAReservarClick,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text =
                                    stringResource(R.string.turno_volver_a_reservar)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Puntito de color + texto ("● Confirmado"). Es "private": solo la usa este archivo.
@Composable
private fun EstadoEtiqueta(estado: EstadoTurno) {
    // "when" también DEVUELVE un valor: lo guardamos en una variable
    val texto = when (estado) {
        EstadoTurno.CONFIRMADO -> stringResource(R.string.estado_confirmado)
        EstadoTurno.COMPLETADO -> stringResource(R.string.estado_completado)
        EstadoTurno.CANCELADO -> stringResource(R.string.estado_cancelado)
    }
    val color = when (estado) {
        EstadoTurno.CONFIRMADO -> MaterialTheme.colorScheme.primary // azul
        EstadoTurno.COMPLETADO -> Verde // de Color.kt
        EstadoTurno.CANCELADO -> MaterialTheme.colorScheme.onSurfaceVariant // gris
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = texto, color = color, fontSize = 13.sp, fontWeight =
                FontWeight.SemiBold
        )
    }
}
@Preview(showBackground = true)
@Composable
fun TurnoCardProximoPreview() {
    BarberStudioTheme {
        TurnoCard(turno = DatosDePrueba.turnosProximos[0])
    }
}
@Preview(showBackground = true)
@Composable
fun TurnoCardCanceladoPreview() {
    BarberStudioTheme {
        TurnoCard(turno = DatosDePrueba.turnosPasados[2])
    }
}
