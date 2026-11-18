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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.EstadoTurno
import com.proyect.barberstudio.ui.screen.home.InicioScreen
import com.proyect.barberstudio.ui.screen.home.ProfileScreen
import com.proyect.barberstudio.ui.screen.reservar.ReservarTurnoScreen
import com.proyect.barberstudio.ui.screen.turnos.MisTurnosScreen
import kotlinx.serialization.Serializable

// ANTES: sealed interface DestinoMain {
// CAMBIO: ": NavKey" = estos destinos se pueden guardar en la pila
sealed interface DestinoMain : NavKey {
    // CAMBIO: @Serializable en cada destino = Kotlin sabe convertirlo a datos para guardarlo al girar
    @Serializable data object Inicio : DestinoMain
    @Serializable data object Reservar : DestinoMain
    @Serializable data object MisTurnos : DestinoMain
    @Serializable data object Perfil : DestinoMain
}

// elementos de la barra de navegacion
sealed class BottomBarItem(val destino: DestinoMain, val titulo: String, val icono: ImageVector) {
    object Inicio : BottomBarItem(DestinoMain.Inicio, "Inicio", Icons.Default.Home)
    object Reservar : BottomBarItem(DestinoMain.Reservar, "Reservar", Icons.Default.DateRange)
    object MisTurnos : BottomBarItem(DestinoMain.MisTurnos, "Mis turnos", Icons.Default.AccessTime)
    object Perfil : BottomBarItem(DestinoMain.Perfil, "Perfil", Icons.Default.Person)

    //se agrupa esos botones en una lista para usar un bucle foreach mas abajo
    companion object {
        val items = listOf(Inicio, Reservar, MisTurnos, Perfil)
    }
}

//contenedor base
@Composable
fun MainScreen() {
    // ANTES: val backStack = remember { mutableStateListOf<DestinoMain>(DestinoMain.Inicio) }
    // CAMBIO: rememberNavBackStack = una pila que sobrevive al girar el celular
    val backStack = rememberNavBackStack(DestinoMain.Inicio)

    var turnosProximos by remember { mutableStateOf(DatosDePrueba.turnosProximos) }
    var turnosPasados by remember { mutableStateOf(DatosDePrueba.turnosPasados) }

    // Barra de abajo: si ya estás en esa pestaña no hace nada; si no, deja solo Inicio y agrega la nueva
    val irAPestana: (DestinoMain) -> Unit = { destino ->
        if (backStack.lastOrNull() != destino) {
            backStack.retainAll { it == DestinoMain.Inicio } // retainAll = "quedate solo con los que cumplan esto"
            if (destino != DestinoMain.Inicio) {
                backStack.add(destino)
            }
        }
    }

    // La pestaña iluminada = la última de la pila
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
                        onClick = { irAPestana(item.destino) }
                    )
                }
            }
        }
    ) { innerPadding ->
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
                        onReservarClick = { irAPestana(DestinoMain.Reservar) },
                        onServicioClick = { },
                        onProductoClick = { },
                        onVerTodosProductos = { },
                        onNotificacionesClick = { }
                    )
                }

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
                        onVerMisTurnos = { irAPestana(DestinoMain.MisTurnos) },
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
                        onVolverAReservar = { irAPestana(DestinoMain.Reservar) },
                        onReservarClick = { irAPestana(DestinoMain.Reservar) },
                        onNotificacionesClick = {}
                    )
                }

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