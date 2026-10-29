package com.proyect.barberstudio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.proyect.barberstudio.ui.screen.auth.LoginScreen
import com.proyect.barberstudio.ui.screen.auth.RegisterScreen
import com.proyect.barberstudio.ui.screen.home.ProfileScreen
import kotlinx.serialization.Serializable


object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MAIN = "main" // ruta que contiene la app principal
}
@Serializable
data object RouteLogin : NavKey

@Serializable
data object RouteRegistro : NavKey

@Serializable
data object RouteMain : NavKey

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(RouteLogin)

    NavDisplay(
        backStack = backStack,
        onBack = {backStack.removeLastOrNull()},
        entryProvider = entryProvider {
            //entrada para login
            entry<RouteLogin> {
                LoginScreen(
                    onLoginClick = dropUnlessResumed{
                        //limpiamos el login e ingresamos la pantalla principal
                        backStack.clear()
                        backStack.add(RouteMain)
                    },
                    onNavigateToRegister = dropUnlessResumed() {
                        backStack.add(RouteRegistro)
                    },
                    onForgotPasswordClick = {}
                )
            }

            //entrada para registro
            entry<RouteRegistro> {
                RegisterScreen(
                    onRegistered = {backStack.removeLastOrNull()},
                    onClose = {backStack.removeLastOrNull()}
                )
            }

            //entrada para el main q es la pantalla principal
            entry<RouteMain> {
                MainScreen()
            }
        }
    )
}