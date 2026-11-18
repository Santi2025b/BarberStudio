package com.proyect.barberstudio.ui.screen.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.proyect.barberstudio.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    //el estado vive en un MutableStateFlow. Es privado (con guion bajo): solo el ViewModel lo cambia.
    private val _uiState = MutableStateFlow(LoginUiState())

    //hacia afuera mostramos un StateFlow, que es de SOLO LECTURA. La pantalla lo puede leer, no cambiar.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    //Se llama cada vez que el usuario escribe una letra en el correo.
    fun onEmailChange(nuevoEmail: String) {
        // update { } toma la foto actual ("it") y la reemplaza por una copia con el email nuevo.
        // También borramos el error: si está corrigiendo, no tiene sentido seguir mostrándolo en rojo.
        _uiState.update { it.copy(email = nuevoEmail, emailError = null) }
    }

    fun onPasswordChange(nuevaPassword: String) {
        _uiState.update { it.copy(password = nuevaPassword, passwordError = null) }
    }

    /**
     * Revisa los dos campos. Si alguno está mal, guarda el error en el estado (y la pantalla lo muestra).
     * Devuelve true si está todo bien, para que la pantalla sepa si puede navegar.
     */
    fun validar(): Boolean {
        val estado = _uiState.value // .value = la foto actual
        val email = estado.email.trim()

        val emailError = when {
            email.isBlank() -> R.string.error_email_vacio
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_email_invalido
            else -> null
        }

        val passwordError = when {
            estado.password.isBlank() -> R.string.error_contrasena_vacia
            estado.password.length < MIN_CARACTERES_CONTRASENA -> R.string.error_contrasena_corta
            else -> null
        }

        _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }

        return emailError == null && passwordError == null
    }

    companion object {
        const val MIN_CARACTERES_CONTRASENA = 6
    }
}