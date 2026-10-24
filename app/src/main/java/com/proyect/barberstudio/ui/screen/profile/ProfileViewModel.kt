package com.proyect.barberstudio.ui.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    // El estado se expone a la vista, pero solo se puede modificar desde aquí
    var profileUiState by mutableStateOf(ProfileUiState())
        private set //sig que la vista nunca modifica los datos por si misma

    // unico canal de entrada para los eventos de la vista
    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnEditProfileClick -> { /* Lógica de navegación */ }
            is ProfileUiEvent.OnPersonalDataClick -> { /* Lógica de navegación */ }
            is ProfileUiEvent.OnNotificationsClick -> { /* Lógica de navegación */ }
            is ProfileUiEvent.OnHistoryClick -> { /* Lógica de navegación */ }
            is ProfileUiEvent.OnLogoutClick -> {
                println("Cerrando sesión para: ${profileUiState.name}")
            }
        }
    }
}