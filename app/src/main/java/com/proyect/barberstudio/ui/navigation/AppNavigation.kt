package com.proyect.barberstudio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyect.barberstudio.ui.screen.auth.LoginScreen
import com.proyect.barberstudio.ui.screen.auth.RegisterScreen

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
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
                onLoginClick = { },
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
    }
}