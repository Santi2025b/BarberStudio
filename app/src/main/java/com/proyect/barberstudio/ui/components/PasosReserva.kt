package com.proyect.barberstudio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

//  nombres de los 5 pasos
private val nombresPasos = listOf(
    R.string.paso_fecha,
    R.string.paso_servicio,
    R.string.paso_barbero,
    R.string.paso_revision,
    R.string.paso_listo
)

// Barrita de pasos. 5 rayitas con su nombre abajo. La del paso actual va en azul.
@Composable
fun PasosReserva(
    pasoActual: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.paso_x_de_y, pasoActual, nombresPasos.size),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // forEachIndexed: como forEach, pero además te da la posición (0, 1, 2...)
            nombresPasos.forEachIndexed { indice, nombre ->
                val numero = indice + 1

                // "when" sin nada entre paréntesis: elige la primera condición que sea verdadera
                val colorRayita = when {
                    numero == pasoActual -> MaterialTheme.colorScheme.primary    // el paso actual: azul
                    numero < pasoActual -> MaterialTheme.colorScheme.onBackground // ya hecho: oscuro
                    else -> MaterialTheme.colorScheme.outline                     // todavía no llego al paso: gris claro
                }

                // weight(1f) en las 5 columnas = las 5 miden lo mismo
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(color = colorRayita, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(nombre),
                        fontSize = 10.sp,
                        maxLines = 1,
                        color = if (numero == pasoActual) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}



// Título grande + subtítulo gris que tiene cada paso
@Composable
fun TituloPaso(
    titulo: String,
    subtitulo: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = titulo, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = subtitulo, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// Borde de una opción que se puede elegir, azul y más grueso si está elegida.
// Es @Composable porque lee los colores MaterialTheme. No dibuja nada: DEVUELVE un borde.
@Composable
fun bordeOpcion(elegida: Boolean): BorderStroke =
    if (elegida) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)


// Colores de esa misma tarjeta: fondo celeste si está elegida. El texto siempre con el color normal.
@Composable
fun coloresOpcion(elegida: Boolean): CardColors =
    CardDefaults.outlinedCardColors(
        containerColor = if (elegida) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    )

@Preview(showBackground = true)
@Composable
fun PasosReservaPreview() {
    BarberStudioTheme {
        Column(modifier = Modifier.padding(24.dp)) {
            PasosReserva(pasoActual = 2)
            Spacer(modifier = Modifier.height(16.dp))
            TituloPaso(titulo = "¿Cuál es tu estilo?", subtitulo = "Cuidamos cada detalle de tu corte.")
        }
    }
}