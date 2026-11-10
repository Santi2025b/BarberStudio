package com.proyect.barberstudio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.proyect.barberstudio.ui.screen.auth.LoginScreen
import com.proyect.barberstudio.ui.screen.auth.RegisterScreen


// ANTES:
// object Rutas {
//     const val LOGIN = "login"
//     const val REGISTRO = "registro"
//     const val MAIN = "main"
// }


// "sealed interface" = la lista cerrada de destinos posibles.
// "data object" = un objeto único con nombre
sealed interface DestinoApp {
    data object Login : DestinoApp
    data object Registro : DestinoApp
    data object Main : DestinoApp // la app principal, con la barra de abajo
}

@Composable
fun AppNavigation() {
    // ANTES: val navController = rememberNavController()

    // CAMBIO: la pila de pantallas es una LISTA NUESTRA. Arranca con Login adentro.
    // mutableStateListOf: una lista que, cuando cambia, hace que Compose redibuje
    val backStack = remember { mutableStateListOf<DestinoApp>(DestinoApp.Login) }

    // ANTES: NavHost(navController = navController, startDestination = Rutas.LOGIN) { ... }

    // CAMBIO: NavDisplay dibuja SIEMPRE la última pantalla de la lista.
    NavDisplay(
        backStack = backStack,
        // Botón Atrás del celular saca la última pantalla.
        //  si hay más de una, toca y vuelve una pantalla atrtas (si queda una, Atrás cierra la app).
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        // entryProvider: el "mapa". Para cada tipo de destino, qué pantalla se dibuja.
        entryProvider = entryProvider {

            // ANTES: composable(Rutas.LOGIN) { ... }
            entry<DestinoApp.Login> {
                LoginScreen(
                    onLoginClick = {
                        //  vaciamos la lista y dejamos solo Main. Así Atrás no vuelve al Login
                        backStack.clear()
                        backStack.add(DestinoApp.Main)
                    },
                    onNavigateToRegister = { backStack.add(DestinoApp.Registro) }, // ir = agregar
                    onForgotPasswordClick = { }
                )
            }

            // ANTES: composable(Rutas.REGISTRO) { ... }
            entry<DestinoApp.Registro> {
                RegisterScreen(
                    onRegistered = { backStack.removeLastOrNull() }, // volver = sacar el último
                    onClose = { backStack.removeLastOrNull() }
                )
            }

            // ANTES: composable(Rutas.MAIN) { MainScreen() }
            entry<DestinoApp.Main> {
                MainScreen()
            }
        }
    )
}