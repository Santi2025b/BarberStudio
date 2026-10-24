package com.proyect.barberstudio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyect.barberstudio.ui.screen.auth.LoginScreen
import com.proyect.barberstudio.ui.screen.auth.RegisterScreen
import com.proyect.barberstudio.ui.screen.home.ProfileScreen

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MAIN = "main" // ruta que contiene la app principal
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {
        //empieza en el login
        // registra pantalla de login
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    // uso popUpTo para borrar el login del stack de pantallas
                    navController.navigate(Rutas.MAIN){
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Rutas.REGISTRO) },
                onForgotPasswordClick = { }
            )
        }

        //registra pantalla de registro
        composable(Rutas.REGISTRO) {
            RegisterScreen(
                onRegistered = { navController.popBackStack() },
                onClose = { navController.popBackStack() }
            )
        }

        composable(Rutas.MAIN) {
            MainScreen()
        }
    }
}