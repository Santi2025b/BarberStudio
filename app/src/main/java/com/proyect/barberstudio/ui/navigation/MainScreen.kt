package com.proyect.barberstudio.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.dropUnlessResumed
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

// Rutas de la barra inferior
@Serializable
data object MainRouteInicio : NavKey

@Serializable
data object MainRouteReservar : NavKey

@Serializable
data object MainRouteMisTurnos : NavKey

@Serializable
data object MainRoutePerfil : NavKey

// Botones de la barra inferior
sealed class BottomBarItem(val key: NavKey, val titulo: String, val icono: ImageVector) {
    object Inicio : BottomBarItem(MainRouteInicio, "Inicio", Icons.Default.Home)
    object Reservar : BottomBarItem(MainRouteReservar, "Reservar", Icons.Default.DateRange)
    object MisTurnos : BottomBarItem(MainRouteMisTurnos, "Mis turnos", Icons.Default.AccessTime)
    object Perfil : BottomBarItem(MainRoutePerfil, "Perfil", Icons.Default.Person)

    companion object {
        val items = listOf(Inicio, Reservar, MisTurnos, Perfil)
    }
}

@Composable
fun MainScreen() {
    // Pila de navegación interna para las pestañas (arranca en Inicio)
    val backStack = rememberNavBackStack(MainRouteInicio)

    // Estado de las listas de turnos
    var turnosProximos by remember { mutableStateOf(DatosDePrueba.turnosProximos) }
    var turnosPasados by remember { mutableStateOf(DatosDePrueba.turnosPasados) }

    // Función para ir a Reservar sin acumular pantallas
    val irAReservar = dropUnlessResumed {
        if (backStack.lastOrNull() != MainRouteReservar) {
            backStack.clear()
            backStack.add(MainRouteInicio)
            backStack.add(MainRouteReservar)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                // Averiguamos cuál es la pantalla activa mirando el último elemento de la pila
                val currentKey = backStack.lastOrNull()

                BottomBarItem.items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icono, contentDescription = item.titulo) },
                        label = { Text(item.titulo) },
                        selected = currentKey == item.key,
                        onClick = dropUnlessResumed {
                            //Solo ejecuta el cambio si toca una pestaña distinta a la que ya esta viendo.
                            if (currentKey != item.key) {
                                // Limpiamos la pila e insertamos la nueva pestaña elegida
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
        // NavDisplay reemplaza al NavHost
        NavDisplay(
            backStack = backStack, //La lista observable que lee el NavDisplay para saber qué dibujar.
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
                    ReservarTurnoScreen(
                        nombreUsuario = DatosDePrueba.NOMBRE_USUARIO,
                        servicios = DatosDePrueba.servicios,
                        barberos = DatosDePrueba.barberos,
                        horarios = DatosDePrueba.horarios,
                        onTurnoConfirmado = {},
                        onVerMisTurnos = {},
                        onVolverAlInicio = {}
                    )
                }

                entry<MainRouteMisTurnos> {
                    MisTurnosScreen(
                        turnosProximos = turnosProximos,
                        turnosPasados = turnosPasados,
                        onCancelarTurno = { turno ->
                            turnosProximos = turnosProximos - turno
                            turnosPasados = listOf(turno.copy(estado = EstadoTurno.CANCELADO)) + turnosPasados
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

@Composable
fun PlaceholderScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}