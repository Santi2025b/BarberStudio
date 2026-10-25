package com.proyect.barberstudio.ui.components

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
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Tarjeta "Tu próxima visita" de Inicio. Recibe un Turno entero porque
// esta tarjeta SIEMPRE muestra un turno. quien la usa escribe ProximaVisitaCard(turno) y listo.
@Composable
fun ProximaVisitaCard(
    turno: Turno,
    modifier: Modifier = Modifier
) {
    // OutlinedCard =  tarjeta con borde. Toma del tema el fondo (surface) y el borde (outline).
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Fila de arriba: cajita con la fecha + día y horario
            Row(verticalAlignment = Alignment.CenterVertically) {
                FechaCaja(dia = turno.dia, mes = turno.mes)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = turno.fecha, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(text = turno.horario, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Línea separadora
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outline
            )

            // Fila de abajo: foto del barbero + servicio y barbero
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Todavía no hay fotos de los barberos: un círculo con un ícono de persona
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
                    // Texto con huecos: "%1$s · $%2$d" -> "Corte + barba · $55"
                    Text(
                        text = stringResource(R.string.turno_servicio_precio, turno.servicio.nombre, turno.servicio.precio),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    // "Con %1$s" -> "Con Marcus Reed"
                    Text(
                        text = stringResource(R.string.turno_con_barbero, turno.barbero),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Cajita gris con el mes y el día. "Mis turnos" también la usa en cada turno.
@Composable
fun FechaCaja(
    dia: String,
    mes: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.size(48.dp)
    ) {
        // fillMaxSize para que la Column ocupe toda la caja y el centrado funcione
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = mes, fontSize = 10.sp)
            // Surface le da a su contenido el color "onSurfaceVariant" (gris). Para que el número
            // se vea fuerte le ponemos a mano el color del texto principal.
            Text(text = dia, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProximaVisitaCardPreview() {
    BarberStudioTheme {
        ProximaVisitaCard(turno = DatosDePrueba.proximoTurno)
    }
}