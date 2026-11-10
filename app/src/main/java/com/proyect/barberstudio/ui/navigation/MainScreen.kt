package com.proyect.barberstudio.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.EstadoTurno
import com.proyect.barberstudio.ui.screen.home.InicioScreen
import com.proyect.barberstudio.ui.screen.home.ProfileScreen
import com.proyect.barberstudio.ui.screen.reservar.ReservarTurnoScreen
import com.proyect.barberstudio.ui.screen.turnos.MisTurnosScreen


// ANTES:
// object MainRutas {
//     const val INICIO = "inicio"
//     const val RESERVAR = "reservar"
//     const val MIS_TURNOS = "mis_turnos"
//     const val PERFIL = "perfil"
// }

// CAMBIO: los destinos de las pestañas
sealed interface DestinoMain {
    data object Inicio : DestinoMain
    data object Reservar : DestinoMain
    data object MisTurnos : DestinoMain
    data object Perfil : DestinoMain
}

// elementos de la barra de navegacion
// ANTES: sealed class BottomBarItem(val ruta: String, val titulo: String, val icono: ImageVector)
// CAMBIO: cada botón guarda su DESTINO (un objeto) en vez de una ruta
sealed class BottomBarItem(val destino: DestinoMain, val titulo: String, val icono: ImageVector) {
    object Inicio : BottomBarItem(DestinoMain.Inicio, "Inicio", Icons.Default.Home)
    object Reservar : BottomBarItem(DestinoMain.Reservar, "Reservar", Icons.Default.DateRange)
    object MisTurnos : BottomBarItem(DestinoMain.MisTurnos, "Mis turnos", Icons.Default.AccessTime)
    object Perfil : BottomBarItem(DestinoMain.Perfil, "Perfil", Icons.Default.Person)

    //agrupamos botones en una lista para usar un bucle foreach mas abajo
    companion object {
        val items = listOf(Inicio, Reservar, MisTurnos, Perfil)
    }
}

//contenedor base
@Composable
fun MainScreen() {
    // ANTES: val navController = rememberNavController()
    // CAMBIO: la pila de las pestañas, arranca en Inicio
    val backStack = remember { mutableStateListOf<DestinoMain>(DestinoMain.Inicio) }

    var turnosProximos by remember { mutableStateOf(DatosDePrueba.turnosProximos) }
    var turnosPasados by remember { mutableStateOf(DatosDePrueba.turnosPasados) }

    // ANTES:
    // val irAPestana: (String) -> Unit = { ruta ->
    //     navController.navigate(ruta) {
    //         popUpTo(navController.graph.findStartDestination().id) { saveState = true }
    //         launchSingleTop = true
    //         restoreState = true
    //     }
    // }

    // CAMBIO:
    // - Si ya estás en esa pestaña, no hace nada (era launchSingleTop).
    // - Deja solo Inicio en la lista y agrega la pestaña nueva.
    //   Así la pila es siempre [Inicio] o [Inicio, otra pestaña], y Atrás desde cualquier pestaña vuelve a Inicio.
    val irAPestana: (DestinoMain) -> Unit = { destino ->
        if (backStack.lastOrNull() != destino) {
            backStack.retainAll { it == DestinoMain.Inicio } // retainAll = "quedate solo con los que cumplan esto"
            if (destino != DestinoMain.Inicio) {
                backStack.add(destino)
            }
        }
    }


    //  la pestaña actual es simplemente la última de la lista
    val pestanaActual = backStack.lastOrNull()

    //reserva un espacio exclusivo en la parte inferior para el bottomBar y provee un margen dinamico
    // para que los botonos no queden tapados
    Scaffold(
        bottomBar = {
            //contenedor principal visual de la franja inferior de la app
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                BottomBarItem.items.forEach { item ->
                    //es el boton individual que se construye en cada vuelta del bucle
                    NavigationBarItem(
                        icon = { Icon(item.icono, contentDescription = item.titulo) },
                        label = { Text(item.titulo) },
                        selected = pestanaActual == item.destino,
                        // CAMBIO: la misma función que usan los botones de las pantallas
                        onClick = { irAPestana(item.destino) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // ANTES: NavHost(navController, startDestination = MainRutas.INICIO, modifier = ...) { ... }
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            modifier = Modifier.padding(innerPadding), // para que el contenido no quede tapado por la barra
            entryProvider = entryProvider {


                entry<DestinoMain.Inicio> {
                    InicioScreen(
                        nombreUsuario = DatosDePrueba.NOMBRE_USUARIO,
                        proximoTurno = DatosDePrueba.proximoTurno,
                        servicios = DatosDePrueba.servicios,
                        productos = DatosDePrueba.productos,
                        // ANTES: onReservarClick = { irAPestana(MainRutas.RESERVAR) },
                        onReservarClick = { irAPestana(DestinoMain.Reservar) },
                        onServicioClick = { },
                        onProductoClick = { },
                        onVerTodosProductos = { },
                        onNotificacionesClick = { }
                    )
                }

                // ANTES: composable(MainRutas.RESERVAR) { ... }
                entry<DestinoMain.Reservar> {
                    ReservarTurnoScreen(
                        nombreUsuario = DatosDePrueba.NOMBRE_USUARIO,
                        servicios = DatosDePrueba.servicios,
                        barberos = DatosDePrueba.barberos,
                        horarios = DatosDePrueba.horarios,
                        onTurnoConfirmado = { turno ->
                            val nuevoId = ((turnosProximos + turnosPasados).maxOfOrNull { it.id } ?: 0) + 1
                            turnosProximos = turnosProximos + turno.copy(id = nuevoId)
                        },
                        // ANTES: onVerMisTurnos = { irAPestana(MainRutas.MIS_TURNOS) },
                        onVerMisTurnos = { irAPestana(DestinoMain.MisTurnos) },
                        // ANTES: onVolverAlInicio = { irAPestana(MainRutas.INICIO) }
                        onVolverAlInicio = { irAPestana(DestinoMain.Inicio) }
                    )
                }


                entry<DestinoMain.MisTurnos> {
                    MisTurnosScreen(
                        turnosProximos = turnosProximos,
                        turnosPasados = turnosPasados,
                        onCancelarTurno = { turno ->
                            turnosProximos = turnosProximos - turno
                            turnosPasados =
                                listOf(turno.copy(estado = EstadoTurno.CANCELADO)) + turnosPasados
                        },
                        // ANTES: { irAPestana(MainRutas.RESERVAR) }
                        onVolverAReservar = { irAPestana(DestinoMain.Reservar) },
                        onReservarClick = { irAPestana(DestinoMain.Reservar) },
                        onNotificacionesClick = {}
                    )
                }

                // ANTES: composable(MainRutas.PERFIL) { ... }
                entry<DestinoMain.Perfil> {
                    ProfileScreen() // Pantalla de perfil con UDF
                }
            }
        )
    }
}

// pantalla temporal para llenar las vistas que todavía no hicimos
@Composable
fun PlaceholderScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}