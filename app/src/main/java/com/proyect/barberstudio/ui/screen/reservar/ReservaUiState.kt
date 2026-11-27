package com.proyect.barberstudio.ui.screen.reservar

import com.proyect.barberstudio.data.model.Barbero
import com.proyect.barberstudio.data.model.Servicio

// La "foto" de la reserva: en qué paso está y lo que el usuario fue eligiendo.
data class ReservaUiState(
    val paso: Int = 1,               // 1 a 5: en que paso esta
    val dia: Int? = null,            // null = todavía no eligió
    val hora: String? = null,
    val servicio: Servicio? = null,
    val barbero: Barbero? = null,
    val notas: String = ""
)