package com.proyect.barberstudio.ui.screen.turnos

import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.Turno

// La "foto" de los turnos: los proximos y los que ya pasaron.
// Arranca con los turnos de prueba
data class TurnosUiState(
    val proximos: List<Turno> = DatosDePrueba.turnosProximos,
    val pasados: List<Turno> = DatosDePrueba.turnosPasados
)