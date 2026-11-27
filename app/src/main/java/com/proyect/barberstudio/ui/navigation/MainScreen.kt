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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.ui.screen.home.InicioScreen
import com.proyect.barberstudio.ui.screen.home.ProfileScreen
import com.proyect.barberstudio.ui.screen.reservar.ReservarTurnoScreen
import com.proyect.barberstudio.ui.screen.turnos.MisTurnosScreen
import com.proyect.barberstudio.ui.screen.turnos.TurnosViewModel
import kotlinx.serialization.Serializable
import com.proyect.barberstudio.ui.screen.reservar.ReservaViewModel

// ": NavKey" = estos destinos se pueden guardar en la pila
sealed interface DestinoMain : NavKey {
    // @Serializable en cada destino = Kotlin sabe convertirlo a datos para guardarlo al girar
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
    // rememberNavBackStack = una pila que sobrevive al girar el celular
    val backStack = rememberNavBackStack(DestinoMain.Inicio)

    // ANTES:
    // var turnosProximos by remember { mutableStateOf(DatosDePrueba.turnosProximos) }
    // var turnosPasados by remember { mutableStateOf(DatosDePrueba.turnosPasados) }
    // CAMBIO: los turnos viven en un ViewModel, que no se borra al girar.
    // Se crea acá arriba (y no dentro de un entry) porque lo usan DOS pantallas: Reservar y Mis turnos.
    val turnosViewModel: TurnosViewModel = viewModel()
    val turnos by turnosViewModel.uiState.collectAsState() // turnos.proximos y turnos.pasados

    // Barra de abajo: si ya estás en esa pestaña no hace nada; si no, deja solo Inicio y agrega la nueva
    fun irAPestana(destino: DestinoMain) {
        // 1) ¿Qué pantalla está arriba de la pila ahora? (null si la pila está vacía)
        val pantallaActual = backStack.lastOrNull()

        // 2) Si ya estoy en esa pestaña, no hago nada y salgo
        val yaEstoyAhi = pantallaActual == destino
        if (yaEstoyAhi) {
            return
        }

        // 3) Vacío la pila dejando SOLO Inicio (retainAll = "quedate solo con los que cumplan esto")
        backStack.retainAll { pantalla -> pantalla == DestinoMain.Inicio }

        // 4) Si el destino no es Inicio, lo pongo arriba de Inicio
        val esInicio = destino == DestinoMain.Inicio
        if (!esInicio) {
            backStack.add(destino)
        }
    }

    // La pestaña iluminada = la última de la pila, o sea la que estamos viendo en pantalla
    val pestanaActual = backStack.lastOrNull()

    // Botones de navegacion
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
            onBack = {  //
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
                    // NUEVO: el ViewModel de la reserva se crea DENTRO del entry
                    val reservaViewModel: ReservaViewModel = viewModel()

                    ReservarTurnoScreen(
                        viewModel = reservaViewModel, // NUEVO
                        nombreUsuario = DatosDePrueba.NOMBRE_USUARIO,
                        servicios = DatosDePrueba.servicios,
                        barberos = DatosDePrueba.barberos,
                        horarios = DatosDePrueba.horarios,
                        onTurnoConfirmado = { turno -> turnosViewModel.agregarTurno(turno) },
                        onVerMisTurnos = { irAPestana(DestinoMain.MisTurnos) },
                        onVolverAlInicio = { irAPestana(DestinoMain.Inicio) }
                    )
                }

                entry<DestinoMain.MisTurnos> {
                    MisTurnosScreen(
                        // CAMBIO: las listas salen del estado del ViewModel
                        turnosProximos = turnos.proximos,
                        turnosPasados = turnos.pasados,
                        // ANTES: { turno -> turnosProximos = turnosProximos - turno ; turnosPasados = ... }
                        // CAMBIO: le avisamos al ViewModel qué turno cancelar
                        onCancelarTurno = { turno -> turnosViewModel.cancelarTurno(turno) },
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