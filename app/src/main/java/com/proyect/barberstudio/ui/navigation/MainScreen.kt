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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.proyect.barberstudio.ui.screen.home.ProfileScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.ui.screen.home.InicioScreen
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.proyect.barberstudio.data.model.EstadoTurno
import com.proyect.barberstudio.ui.screen.turnos.MisTurnosScreen
import kotlinx.serialization.Serializable

// rutas del menu principal
object MainRutas {
    const val INICIO = "inicio"
    const val RESERVAR = "reservar"
    const val MIS_TURNOS = "mis_turnos"
    const val PERFIL = "perfil"
}

@Serializable
data object MainRouteInicio : NavKey

@Serializable
data object MainRouteReservar : NavKey

@Serializable
data object MainRouteMisTurnos : NavKey

@Serializable
data object MainRoutePerfil : NavKey

// elementos de la barra de navegacion
sealed class BottomBarItem(val key: NavKey, val titulo: String, val icono: ImageVector) {
    object Inicio : BottomBarItem(MainRouteInicio, "Inicio", Icons.Default.Home)
    object Reservar : BottomBarItem(MainRouteReservar, "Reservar", Icons.Default.DateRange)
    object MisTurnos : BottomBarItem(MainRouteMisTurnos, "Mis turnos", Icons.Default.AccessTime)
    object Perfil : BottomBarItem(MainRoutePerfil, "Perfil", Icons.Default.Person)

    //se agrupa esos botones en una lista para usar un bucle foreach mas abajo
    companion object {
        val items = listOf(Inicio, Reservar, MisTurnos, Perfil)
    }
}

//contenedor base
@Composable
fun MainScreen() {
    val backStack = rememberNavBackStack(MainRouteInicio)
    var turnosProximos by remember { mutableStateOf(DatosDePrueba.turnosProximos) }
    var turnosPasados by remember { mutableStateOf(DatosDePrueba.turnosPasados) }

    // funcion para ir a reservar sin acumular pantallas
    val irAReservar = dropUnlessResumed {
        if (backStack.lastOrNull() != MainRouteReservar) {
            backStack.clear()
            backStack.add(MainRouteInicio)
            backStack.add(MainRouteReservar)
        }
    }

    //reserva un espacio exclusivo en la parte inferior para el bottomBar y provee un margen dinamico
    // para que los botonos no queden tapados
    Scaffold(
        bottomBar = {
            //contenedor principal visual de la franja inferior de la app
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {

                //averiguamos cual es la pantalla activa mirando el ultimo elemento de la pila
                val currentKey = backStack.lastOrNull()

                BottomBarItem.items.forEach { item ->
                    //es el boton individual que se construye en cada vuelta del bucle
                    NavigationBarItem(
                        icon = { Icon(item.icono, contentDescription = item.titulo) },
                        label = { Text(item.titulo) },
                        //Compara la ubicación actual con la ruta de cada botón. Si coinciden, Compose
                        // ilumina y resalta ese botón
                        selected = currentKey == item.key,
                        onClick = {
                            if (currentKey != item.key) {
                                //limpiamos la pila e insertamos la nueva pestana elegida
                                backStack.clear()
                                if (item.key != MainRouteInicio) {
                                    backStack.add(MainRouteInicio)
                                }
                                backStack.add(item.key)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = Modifier.padding(innerPadding),
            entryProvider = entryProvider {
                entry<MainRouteInicio> {
                    InicioScreen(
                        nombreUsuario = DatosDePrueba.NOMBRE_USUARIO,
                        proximoTurno = DatosDePrueba.proximoTurno,
                        servicios = DatosDePrueba.servicios,
                        productos = DatosDePrueba.productos,
                        onReservarClick = irAReservar,
                        onServicioClick = { },
                        onProductoClick = { },
                        onVerTodosProductos = { },
                        onNotificacionesClick = { }
                    )
                }
                entry<MainRouteReservar> {
                    PlaceholderScreen("Pantala reservar")
                }

                entry<MainRouteMisTurnos> {
                    MisTurnosScreen(
                        turnosProximos = turnosProximos,
                        turnosPasados = turnosPasados,
                        onCancelarTurno = { turno ->
                            turnosProximos = turnosProximos - turno
                            turnosPasados =
                                listOf(turno.copy(estado = EstadoTurno.CANCELADO)) + turnosPasados
                        },
                        onVolverAReservar = { irAReservar() },
                        onReservarClick = irAReservar,
                        onNotificacionesClick = {}
                    )
                }

                entry<MainRoutePerfil> {
                    ProfileScreen()
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