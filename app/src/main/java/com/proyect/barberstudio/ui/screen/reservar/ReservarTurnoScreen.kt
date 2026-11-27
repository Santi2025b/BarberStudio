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

// La pantalla "Reservar turno". Muestra el paso segun el estado del ViewModel.
// Cuando se confirma, AVISA con onTurnoConfirmado (no guarda el turno, eso lo hace TurnosViewModel).
@Composable
fun ReservarTurnoScreen(
    // el ViewModel lo crea la navegación (en MainScreen) y nos lo pasa por parametro.
    viewModel: ReservaViewModel,
    nombreUsuario: String,
    servicios: List<Servicio>,
    barberos: List<Barbero>,
    horarios: List<String>,
    onTurnoConfirmado: (Turno) -> Unit,
    onVerMisTurnos: () -> Unit,
    onVolverAlInicio: () -> Unit
) {
    // ANTES (6 variables sueltas que se borraban al girar):
    // var paso by remember { mutableIntStateOf(1) } ... y demas variables

    // CAMBIO: una sola foto que viene del ViewModel y podemos solo leer.
    val estado by viewModel.uiState.collectAsState()

    // ----- Valores calculados a partir del estado (se recalculan solos en cada redibujo) -----
    // CAMBIO: ahora se leen de "estado."
    val resumenFechaHora = textoFechaHora(estado.dia, estado.hora)
    val turno = armarTurno(estado.dia, estado.hora, estado.servicio, estado.barbero) // null mientras falte algo

    // El botón Atrás del celular, en los pasos 2 a 4 vuelve un paso.
    BackHandler(enabled = estado.paso in 2..4) {
        viewModel.volverUnPaso()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Encabezado: flecha atrás + "Reservar turno"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (estado.paso < 5) {
                IconButton(
                    onClick = {
                        if (estado.paso > 1) {
                            viewModel.volverUnPaso()
                        } else {
                            onVolverAlInicio()       // en el paso 1, la flecha sale a Inicio
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

        PasosReserva(pasoActual = estado.paso, modifier = Modifier.padding(horizontal = 24.dp))

        Spacer(modifier = Modifier.height(20.dp))

        // El contenido del paso: ocupa todo el alto que queda
        Box(modifier = Modifier.weight(1f)) {
            // "when" elige qué paso dibujar. Cambia el paso en el ViewModel = Compose redibuja y aparece otro.
            when (estado.paso) {
                1 -> PasoFechaHora(
                    diaElegido = estado.dia,
                    horaElegida = estado.hora,
                    horarios = horarios,
                    resumen = resumenFechaHora,
                    onDiaClick = { dia -> viewModel.onDiaClick(dia) },
                    onHoraClick = { hora -> viewModel.onHoraClick(hora) },
                    onContinuar = { viewModel.irAPaso(2) }
                )

                2 -> PasoServicio(
                    servicios = servicios,
                    servicioElegido = estado.servicio,
                    onServicioClick = { servicio -> viewModel.onServicioClick(servicio) },
                    onContinuar = { viewModel.irAPaso(3) }
                )

                3 -> PasoBarbero(
                    barberos = barberos,
                    barberoElegido = estado.barbero,
                    resumenFechaHora = resumenFechaHora,
                    onBarberoClick = { barbero -> viewModel.onBarberoClick(barbero) },
                    onContinuar = { viewModel.irAPaso(4) }
                )

                // Los pasos 4 y 5 necesitan el turno completo. "turno" es una val local,
                // así que adentro del if Kotlin ya sabe que no es null.
                4 -> if (turno != null) {
                    PasoRevisar(
                        nombreUsuario = nombreUsuario,
                        turno = turno,
                        notas = estado.notas,
                        onNotasChange = { notas -> viewModel.onNotasChange(notas) },
                        onEditarClick = { viewModel.irAPaso(1) }, // vuelve al principio, pero lo elegido sigue guardado
                        onConfirmar = {
                            onTurnoConfirmado(turno) // aviso hacia afuera: MainScreen se lo pasa a TurnosViewModel
                            viewModel.irAPaso(5)
                        }
                    )
                }

                5 -> if (turno != null) {
                    PasoConfirmado(
                        nombreUsuario = nombreUsuario,
                        turno = turno,
                        // NUEVO: al salir de "Listo" vaciamos la reserva. Si no, al volver a la
                        // pestaña Reservar aparecería otra vez la pantalla "Listo" del turno anterior.
                        onVerMisTurnos = {
                            viewModel.reiniciar()
                            onVerMisTurnos()
                        },
                        onVolverAlInicio = {
                            viewModel.reiniciar()
                            onVolverAlInicio()
                        }
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
        id = 0,                                  // el id definitivo lo pone TurnosViewModel al guardarlo
        dia = dia.toString().padStart(2, '0'),   // 5 -> "05", 16 -> "16" (para la cajita de fecha)
        mes = DatosDePrueba.MES_CORTO,
        fecha = DatosDePrueba.fechaLarga(dia),
        horario = hora,
        servicio = servicio,
        barbero = barbero.nombre
    )
}