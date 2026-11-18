package com.proyect.barberstudio.ui.screen.auth

import androidx.annotation.StringRes

data class RegisterUiState(
    val nombre: String = "",
    val telefono: String = "",
    val email: String = "",
    val password: String = "",
    @StringRes val nombreError: Int? = null,
    @StringRes val telefonoError: Int? = null,
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
)