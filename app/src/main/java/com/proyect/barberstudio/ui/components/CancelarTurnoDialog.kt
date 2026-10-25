package com.proyect.barberstudio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.ui.theme.BarberStudioTheme
import com.proyect.barberstudio.ui.theme.Verde

// Diálogo "¿Seguro que querés cancelar tu turno?".
// Como la tarjeta, NO cancela nada: avisa "confirmaron" (onConfirmar) o "lo cerraron" (onDismiss).
// Las dos lambdas son obligatorias (sin "="): un diálogo que no se puede cerrar sería un error.
@Composable
fun CancelarTurnoDialog(
    turno: Turno,
    onConfirmar: () -> Unit,
    onDismiss: () -> Unit
) {
    // AlertDialog de Material 3. Cada parámetro es un "hueco" (slot) donde ponés lo que quieras.
    AlertDialog(
        // Se llama si tocan afuera del diálogo o el botón Atrás del celular
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.EventBusy, contentDescription =
                    null
            )
        },
        title = {
            Text(
                text = stringResource(R.string.cancelar_titulo),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.cancelar_mensaje),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Cajita gris con el resumen del turno
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.turno_servicio_precio,
                                turno.servicio.nombre, turno.servicio.precio
                            ),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
// De "10:00–10:45" nos quedamos con lo de antes del guion largo: "10:00"
                        Text(
                            text = stringResource(
                                R.string.cancelar_fecha_hora,
                                turno.fecha, turno.horario.substringBefore("–")
                            ),
                            fontSize = 13.sp
                        )
                        Text(
                            text = stringResource(
                                R.string.cancelar_barbero_duracion,
                                turno.barbero, turno.servicio.duracionMin
                            ),
                            fontSize = 13.sp
                        )
                        Text(
                            text = stringResource(R.string.cancelar_sin_cargo),
                            fontSize = 13.sp,
                            color = Verde
                        )
                    }
                }
            }
        },
        // Botón principal (a la derecha): cancelar, en rojo
        confirmButton = {
            TextButton(onClick = onConfirmar) {

                Text(
                    text = stringResource(R.string.turno_cancelar),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        // Botón secundario (a la izquierda): mantener, con el azul del tema
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancelar_mantener))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CancelarTurnoDialogPreview() {
    BarberStudioTheme {
        CancelarTurnoDialog(
            turno = DatosDePrueba.proximoTurno, onConfirmar = {},
            onDismiss = {})
    }
}
