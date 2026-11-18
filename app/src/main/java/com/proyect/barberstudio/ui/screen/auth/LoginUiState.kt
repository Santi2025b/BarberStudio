package com.proyect.barberstudio.ui.screen.auth

import androidx.annotation.StringRes

data class LoginUiState(
    val email: String="",
    val password: String ="",
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null
)