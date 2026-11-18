package com.proyect.barberstudio.ui.screen.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.screen.auth.LoginViewModel.Companion.MIN_CARACTERES_CONTRASENA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())

    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEmailChange(nuevoEmail: String){
        _uiState.update { it.copy(email = nuevoEmail, emailError = null) }
    }
    fun onPasswordChange(nuevoPassword: String){
        _uiState.update { it.copy(password = nuevoPassword, passwordError = null) }
    }
    fun onNombreChange(nuevoEmail: String){
        _uiState.update { it.copy(nombre = nuevoEmail, nombreError = null) }
    }
    fun onTelefonoChange(nuevoTelefono: String){
        _uiState.update { it.copy(telefono = nuevoTelefono, telefonoError = null) }
    }

    fun validar(): Boolean{
        val estado = _uiState.value
        val email = estado.email.trim()
        val nombre = estado.nombre.trim()
        val telefono = estado.telefono.trim()

        val emailError = when{
            email.isBlank() -> R.string.error_email_vacio
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_email_invalido
            else -> null
        }
        val nombreError = when{
            nombre.isBlank() -> R.string.error_nombre_vacio
            nombre.length < 2 -> R.string.error_nombre_corto
            else -> null
        }

        val passwordError = when {
            estado.password.isBlank() -> R.string.error_contrasena_vacia
            estado.password.length < MIN_CARACTERES_CONTRASENA -> R.string.error_contrasena_corta
            else -> null
        }
        val telefonoError = when {
            telefono.count(){ it.isDigit() } < MIN_CARACTERES_TELEFONO -> R.string.error_telefono_invalido
            else -> null
        }

        _uiState.update { it.copy(emailError= emailError, passwordError = passwordError, telefonoError = telefonoError, nombreError = nombreError  ) }

        return emailError == null  && nombreError == null && telefonoError == null && passwordError == null
    }
    companion object {
        const val MIN_CARACTERES_CONTRASENA = 6
        const val MIN_CARACTERES_TELEFONO = 6
    }
}