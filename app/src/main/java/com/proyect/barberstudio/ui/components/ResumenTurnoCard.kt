package com.proyect.barberstudio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
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
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Tarjeta con el resumen de un turno: servicio, fecha y hora, y barbero.
// "extra" es un hueco (slot): un pedazo de interfaz que pasa quien la usa y se dibuja al final.
// Por defecto es {} (nada), así el paso de Revisar la usa sin extra y el paso de Listo le agrega la dirección.
@Composable
fun ResumenTurnoCard(
    turno: Turno,
    modifier: Modifier = Modifier,
    extra: @Composable () -> Unit = {}
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Servicio: foto + nombre + "45 min · $55"
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = turno.servicio.imagen),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = turno.servicio.nombre, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = stringResource(R.string.reservar_duracion_precio, turno.servicio.duracionMin, turno.servicio.precio),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outline
            )

            // Fecha y hora
            Text(
                text = stringResource(R.string.reservar_fecha_y_hora),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.texto_con_punto, turno.fecha, turno.horario),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Barbero: círculo con ícono + "Tu barbero" + nombre
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = stringResource(R.string.reservar_tu_barbero),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(text = turno.barbero, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // El hueco: lo que haya mandado quien usa la tarjeta se dibuja acá, al final de la Column
            extra()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResumenTurnoCardPreview() {
    BarberStudioTheme {
        ResumenTurnoCard(turno = DatosDePrueba.proximoTurno)
    }
}