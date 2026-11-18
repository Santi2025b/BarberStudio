package com.proyect.barberstudio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.proyect.barberstudio.ui.screen.auth.LoginScreen
import com.proyect.barberstudio.ui.screen.auth.RegisterScreen
import kotlinx.serialization.Serializable


// ANTES: sealed interface DestinoApp {
// CAMBIO: ": NavKey" = "estos destinos se pueden guardar en una pila de Navigation 3"
sealed interface DestinoApp : NavKey {
    // CAMBIO: @Serializable = Kotlin sabe convertir el destino a datos para guardarlo al girar
    @Serializable data object Login : DestinoApp
    @Serializable data object Registro : DestinoApp
    @Serializable data object Main : DestinoApp // la app principal, con la barra de abajo
}

@Composable
fun AppNavigation() {
    // ANTES: val backStack = remember { mutableStateListOf<DestinoApp>(DestinoApp.Login) }
    // CAMBIO: rememberNavBackStack crea la pila Y la guarda cuando se gira el celular.
    // Por dentro usa @Serializable para convertir cada destino en datos y después volver a armarlo.
    val backStack = rememberNavBackStack(DestinoApp.Login)


    NavDisplay(
        backStack = backStack,
        // Botón Atrás del celular: saca la última pantalla, solo si hay más de una
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        // entryProvider: el "mapa". Para cada tipo de destino, qué pantalla se dibuja
        entryProvider = entryProvider {

            entry<DestinoApp.Login> {
                LoginScreen(
                    onLoginClick = {
                        // vaciamos la pila y dejamos solo Main así Atrás no vuelve al Login
                        backStack.clear()
                        backStack.add(DestinoApp.Main)
                    },
                    onNavigateToRegister = { backStack.add(DestinoApp.Registro) },
                    onForgotPasswordClick = { }
                )
            }

            entry<DestinoApp.Registro> {
                RegisterScreen(
                    onRegistered = { backStack.removeLastOrNull() }, // volver = sacar el último
                    onClose = { backStack.removeLastOrNull() }
                )
            }

            entry<DestinoApp.Main> {
                MainScreen()
            }
        }
    )
}