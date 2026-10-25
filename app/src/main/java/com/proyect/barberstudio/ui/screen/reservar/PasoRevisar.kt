package com.proyect.barberstudio.ui.screen.reservar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.proyect.barberstudio.ui.components.AppTextField
import com.proyect.barberstudio.ui.components.BarraContinuar
import com.proyect.barberstudio.ui.components.ResumenTurnoCard
import com.proyect.barberstudio.ui.components.TituloPaso
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Paso 4: revisar todo antes de confirmar. Recibe el Turno ya armado (no puede ser null ).
@Composable
fun PasoRevisar(
    nombreUsuario: String,
    turno: Turno,
    notas: String,
    onNotasChange: (String) -> Unit,
    onEditarClick: () -> Unit,
    onConfirmar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            TituloPaso(
                titulo = stringResource(R.string.reservar_revisar_titulo, nombreUsuario),
                subtitulo = stringResource(R.string.reservar_revisar_subtitulo)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // "Tu turno ........ Editar"
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.reservar_tu_turno),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onEditarClick) {
                    Text(text = stringResource(R.string.reservar_editar))
                }
            }

            ResumenTurnoCard(turno = turno) // sin "extra": usa el {} por defecto

            Spacer(modifier = Modifier.height(16.dp))

            // Detalle del precio
            FilaPrecio(concepto = turno.servicio.nombre, monto = turno.servicio.precio)
            FilaPrecio(concepto = stringResource(R.string.reservar_cargo_reserva), monto = 0)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outline
            )
            FilaPrecio(
                concepto = stringResource(R.string.reservar_total),
                monto = turno.servicio.precio,
                destacada = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Notas: reusamos AppTextField
            Text(
                text = stringResource(R.string.reservar_notas_pregunta),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppTextField(
                value = notas,
                onValueChange = onNotasChange,
                label = stringResource(R.string.campo_notas),
                placeholder = stringResource(R.string.reservar_notas_placeholder)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.reservar_aviso_pago),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        BarraContinuar(
            resumen = "",
            textoBoton = stringResource(R.string.reservar_confirmar),
            habilitado = true,
            onClick = onConfirmar
        )
    }
}

// Una fila "concepto ........ $55,00". La del total va más grande y en negrita (destacada = true).
@Composable
private fun FilaPrecio(
    concepto: String,
    monto: Int,
    destacada: Boolean = false
) {
    val tamano = if (destacada) 18.sp else 14.sp
    val peso = if (destacada) FontWeight.Bold else FontWeight.Normal
    val color = if (destacada) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text = concepto, modifier = Modifier.weight(1f), fontSize = tamano, fontWeight = peso, color = color)
        Text(text = stringResource(R.string.precio_decimal, monto), fontSize = tamano, fontWeight = peso)
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun PasoRevisarPreview() {
    BarberStudioTheme {
        PasoRevisar(
            nombreUsuario = DatosDePrueba.NOMBRE_USUARIO,
            turno = DatosDePrueba.proximoTurno,
            notas = "",
            onNotasChange = {},
            onEditarClick = {},
            onConfirmar = {}
        )
    }
}