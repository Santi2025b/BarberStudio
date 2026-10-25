package com.proyect.barberstudio.ui.screen.turnos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.ui.components.AppTopBar
import com.proyect.barberstudio.ui.components.CancelarTurnoDialog
import com.proyect.barberstudio.ui.components.TurnoCard
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

@Composable
fun MisTurnosScreen(
    turnosProximos: List<Turno>,
    turnosPasados: List<Turno>,
    onCancelarTurno: (Turno) -> Unit, // lambda que RECIBE el turno a cancelar
    onVolverAReservar: (Turno) -> Unit,
    onReservarClick: () -> Unit,
    onNotificacionesClick: () -> Unit
) {

    var pestanaElegida by rememberSaveable { mutableIntStateOf(0) }

    var turnoACancelar by remember { mutableStateOf<Turno?>(null) }
    val esProximos = pestanaElegida == 0
    val turnos = if (esProximos) turnosProximos else turnosPasados
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // Igual que en Inicio: margen a los costados para todo menos las pestañas,
        // que van de borde a borde como en el Figma.
        val margen = Modifier.padding(horizontal = 24.dp)
        AppTopBar(onNotificacionesClick = onNotificacionesClick, modifier = margen)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.turnos_titulo),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = margen
        )
        // El subtítulo cambia según la pestaña
        Text(
            text = stringResource(
                if (esProximos) R.string.turnos_subtitulo_proximos
                else R.string.turnos_subtitulo_historial
            ),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = margen
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Las pestañas. La rayita azul se mueve sola a la pestaña de ese título.selectedTabIndex.
        PrimaryTabRow(
            selectedTabIndex = pestanaElegida,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Tab(
                selected = pestanaElegida == 0,
                onClick = {
                    pestanaElegida = 0
                }, // cambiar el estado = Compose redibuja la pantalla
                text = { Text(stringResource(R.string.turnos_tab_proximos)) }
            )
            Tab(
                selected = pestanaElegida == 1,
                onClick = { pestanaElegida = 1 },
                text = { Text(stringResource(R.string.turnos_tab_historial)) }
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // item { } = UN elemento suelto de la lista. Acá: "PRÓXIMOS · 2 turnos"
            item {
                Text(
                    text = stringResource(
                        R.string.turnos_seccion,
                        stringResource(
                            if (esProximos)
                                R.string.turnos_seccion_proximos else R.string.turnos_seccion_historial
                        ),// (recurso, cantidad que elige "turno" o "turnos", número que va en % d)
                        pluralStringResource(R.plurals.cantidad_turnos, turnos.size, turnos.size)
                    ),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Si no hay turnos, un mensaje en lugar de la lista vacía
            if (turnos.isEmpty()) {
                item {
                    Text(
                        text = stringResource(
                            if (esProximos)
                                R.string.turnos_vacio_proximos else R.string.turnos_vacio_historial
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            // items(lista) = un elemento por cada turno. key = el id, para no confundir tarjetas
            // cuando un turno desaparece de la lista (al cancelarlo).
            items(turnos, key = { it.id }) { turno ->
                TurnoCard(
                    turno = turno,
                    // Tocar "Cancelar turno" NO cancela: solo guarda cuál es, y eso abre el diálogo
                    onCancelarClick = { turnoACancelar = turno },
                    onVolverAReservarClick = { onVolverAReservar(turno) }
                )
            }
            // El pie (aviso + botón) solo va en Próximos
            if (esProximos) {
                item {
                    Column {
                        Text(
                            text = stringResource(R.string.turnos_aviso_cancelacion),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onReservarClick,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                text =
                                    stringResource(R.string.turnos_reservar_nuevo),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
    // El diálogo. "?.let" = "si turnoACancelar NO es null, hacé esto con él".
    // Si es null no se llama, y el diálogo no existe (está cerrado).
    turnoACancelar?.let { turno ->
        CancelarTurnoDialog(
            turno = turno,
            onConfirmar = {
                onCancelarTurno(turno) // aviso hacia afuera: "cancelaron este turno"
                turnoACancelar = null // y cierro el diálogo
            },
            onDismiss = { turnoACancelar = null } // "Mantener turno" o tocar
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MisTurnosScreenPreview() {
    BarberStudioTheme {
        MisTurnosScreen(
            turnosProximos = DatosDePrueba.turnosProximos,
            turnosPasados = DatosDePrueba.turnosPasados,
            onCancelarTurno = {},
            onVolverAReservar = {},
            onReservarClick = {},
            onNotificacionesClick = {}
        )
    }
}