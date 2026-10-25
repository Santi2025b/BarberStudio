package com.proyect.barberstudio.ui.screen.reservar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.Barbero
import com.proyect.barberstudio.data.model.Servicio
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.ui.components.PasosReserva

// La pantalla "Reservar turno". Guarda en qué paso está y lo que el usuario va eligiendo,
// y muestra el paso que corresponde. Cuando se confirma, AVISA con onTurnoConfirmado (no guarda el turno).
@Composable
fun ReservarTurnoScreen(
    nombreUsuario: String,
    servicios: List<Servicio>,
    barberos: List<Barbero>,
    horarios: List<String>,
    onTurnoConfirmado: (Turno) -> Unit,
    onVerMisTurnos: () -> Unit,
    onVolverAlInicio: () -> Unit
) {
    // ----- Estado del recorrido -----
    // Usamos remember (no rememberSaveable): si te vas a otra pestaña y volvés, la reserva arranca de cero.
    // Además Android no sabe guardar un Servicio o un Barbero.
    var paso by remember { mutableIntStateOf(1) }             // 1 a 5: qué paso se ve
    var dia by remember { mutableStateOf<Int?>(null) }         // null = todavía no eligió
    var hora by remember { mutableStateOf<String?>(null) }
    var servicio by remember { mutableStateOf<Servicio?>(null) }
    var barbero by remember { mutableStateOf<Barbero?>(null) }
    var notas by remember { mutableStateOf("") }

    // ----- Valores calculados a partir del estado (se recalculan solos en cada redibujo) -----
    val resumenFechaHora = textoFechaHora(dia, hora)
    val turno = armarTurno(dia, hora, servicio, barbero)  // null mientras falte algo

    // El botón Atrás del celular: en los pasos 2 a 4 vuelve un paso.
    // En el paso 1 y en el 5 no hace nada especial, y Atrás funciona como siempre.
    BackHandler(enabled = paso in 2..4) {
        paso -= 1
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Encabezado: flecha atrás + "Reservar turno"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (paso < 5) {
                IconButton(
                    onClick = {
                        if (paso > 1) {
                            paso -= 1             // vuelve un paso
                        } else {
                            onVolverAlInicio()    // en el paso 1, la flecha sale a Inicio
                        }
                    }
                ) {
                    // AutoMirrored: la flecha se da vuelta sola en idiomas que se leen de derecha a izquierda
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.volver))
                }
            } else {
                Spacer(modifier = Modifier.width(16.dp)) // en "Listo" no hay vuelta atrás
            }
            Text(text = stringResource(R.string.reservar_titulo), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }

        PasosReserva(pasoActual = paso, modifier = Modifier.padding(horizontal = 24.dp))

        Spacer(modifier = Modifier.height(20.dp))

        // El contenido del paso: ocupa todo el alto que queda
        Box(modifier = Modifier.weight(1f)) {
            // "when" elige qué paso dibujar. Cambiar "paso" = Compose redibuja y aparece otro.
            when (paso) {
                1 -> PasoFechaHora(
                    diaElegido = dia,
                    horaElegida = hora,
                    horarios = horarios,
                    resumen = resumenFechaHora,
                    onDiaClick = { dia = it },
                    onHoraClick = { hora = it },
                    onContinuar = { paso = 2 }
                )

                2 -> PasoServicio(
                    servicios = servicios,
                    servicioElegido = servicio,
                    onServicioClick = { servicio = it },
                    onContinuar = { paso = 3 }
                )

                3 -> PasoBarbero(
                    barberos = barberos,
                    barberoElegido = barbero,
                    resumenFechaHora = resumenFechaHora,
                    onBarberoClick = { barbero = it },
                    onContinuar = { paso = 4 }
                )

                // Los pasos 4 y 5 necesitan el turno completo. "turno" es una val local,
                // así que adentro del if Kotlin ya sabe que no es null.
                4 -> if (turno != null) {
                    PasoRevisar(
                        nombreUsuario = nombreUsuario,
                        turno = turno,
                        notas = notas,
                        onNotasChange = { notas = it },
                        onEditarClick = { paso = 1 }, // vuelve al principio, pero lo elegido sigue guardado
                        onConfirmar = {
                            onTurnoConfirmado(turno) // aviso hacia afuera: MainScreen lo agrega a Mis turnos
                            paso = 5
                        }
                    )
                }

                5 -> if (turno != null) {
                    PasoConfirmado(
                        nombreUsuario = nombreUsuario,
                        turno = turno,
                        onVerMisTurnos = onVerMisTurnos,
                        onVolverAlInicio = onVolverAlInicio
                    )
                }
            }
        }
    }
}

// "Vie, 16 de octubre · 10:00", o "" si falta el día o la hora.
// Es una función común (no @Composable): solo hace una cuenta con texto.
// Recibe los valores como parámetros, y después del if Kotlin sabe que NO son null .
private fun textoFechaHora(dia: Int?, hora: String?): String {
    if (dia == null || hora == null) return ""
    return "${DatosDePrueba.fechaCorta(dia)} · $hora"
}

// Arma el Turno con lo elegido, o devuelve null si todavía falta algo.
// "return null" temprano: si falta cualquier dato, cortamos acá y no seguimos.
private fun armarTurno(dia: Int?, hora: String?, servicio: Servicio?, barbero: Barbero?): Turno? {
    if (dia == null || hora == null || servicio == null || barbero == null) return null

    return Turno(
        id = 0,                                  // el id definitivo lo pone MainScreen al guardarlo
        dia = dia.toString().padStart(2, '0'),   // 5 -> "05", 16 -> "16" (para la cajita de fecha)
        mes = DatosDePrueba.MES_CORTO,
        fecha = DatosDePrueba.fechaLarga(dia),
        horario = hora,
        servicio = servicio,
        barbero = barbero.nombre
    )
}