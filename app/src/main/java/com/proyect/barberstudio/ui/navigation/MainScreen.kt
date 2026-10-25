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
import com.proyect.barberstudio.data.model.EstadoTurno
import com.proyect.barberstudio.ui.screen.turnos.MisTurnosScreen

// rutas del menu principal
object MainRutas {
    const val INICIO = "inicio"
    const val RESERVAR = "reservar"
    const val MIS_TURNOS = "mis_turnos"
    const val PERFIL = "perfil"
}

// elementos de la barra de navegacion
sealed class BottomBarItem(val ruta: String, val titulo: String, val icono: ImageVector) {
    object Inicio : BottomBarItem(MainRutas.INICIO, "Inicio", Icons.Default.Home)
    object Reservar : BottomBarItem(MainRutas.RESERVAR, "Reservar", Icons.Default.DateRange)
    object MisTurnos : BottomBarItem(MainRutas.MIS_TURNOS, "Mis turnos", Icons.Default.AccessTime)
    object Perfil : BottomBarItem(MainRutas.PERFIL, "Perfil", Icons.Default.Person)

    //se agrupa esos botones en una lista para usar un bucle foreach mas abajo
    companion object {
        val items = listOf(Inicio, Reservar, MisTurnos, Perfil)
    }
}

//contenedor base
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var turnosProximos by remember { mutableStateOf(DatosDePrueba.turnosProximos) }
    var turnosPasados by remember { mutableStateOf(DatosDePrueba.turnosPasados) }
    val irAReservar: () -> Unit = {
        navController.navigate(MainRutas.RESERVAR) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
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
                //observa en tiempo real en que pantalla esta el usuario
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                //Esta línea averigua exactamente en que pantalla esta parado el usuario en este instante
                val currentRoute = navBackStackEntry?.destination?.route


                BottomBarItem.items.forEach { item ->
                    //es el boton individual que se construye en cada vuelta del bucle
                    NavigationBarItem(
                        icon = { Icon(item.icono, contentDescription = item.titulo) },
                        label = { Text(item.titulo) },
                        //Compara la ubicación actual con la ruta de cada botón. Si coinciden, Compose
                        // ilumina y resalta ese botón
                        selected = currentRoute == item.ruta,
                        onClick = {
                            //evita que la app colapse la memoria RAM si el usuario toca las pestañas compulsivamente
                            if (currentRoute != item.ruta) {
                                navController.navigate(item.ruta) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    //evita que se abran copias duplicadas de una misma pantalla si presiona el mismo btn varias veces rapido.
                                    launchSingleTop = true
                                    //Recupera el estado guardado si me voy a otra pantalla y vuelvo sigue marcando en la mimsa parte donde quede con el scroll
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainRutas.INICIO, // que muestre por defecto inicio
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainRutas.INICIO) {
                // CAMBIO: la pantalla real de inicio, con los datos de prueba
                InicioScreen(
                    nombreUsuario = DatosDePrueba.NOMBRE_USUARIO,
                    proximoTurno = DatosDePrueba.proximoTurno,
                    servicios = DatosDePrueba.servicios,
                    productos = DatosDePrueba.productos,
                    // "Reservar nuevo turno" va a la pestaña Reservar, igual que la barra de abajo
                    onReservarClick = {
                        navController.navigate(MainRutas.RESERVAR) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onServicioClick = { },
                    onProductoClick = { },
                    onVerTodosProductos = { },
                    onNotificacionesClick = { }
                )
            }

            composable(MainRutas.RESERVAR) {
                PlaceholderScreen("Pantalla de Reservar")
            }
            composable(MainRutas.MIS_TURNOS) {
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
            composable(MainRutas.PERFIL) {
                ProfileScreen() // Pantalla de perfil con UDF
            }
        }
    }
}

// pantalla temporal para llenar las vistas que todavía no hicimos
@Composable
fun PlaceholderScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}