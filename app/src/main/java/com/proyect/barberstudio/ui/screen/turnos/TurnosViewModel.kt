package com.proyect.barberstudio.ui.screen.turnos

import androidx.lifecycle.ViewModel
import com.proyect.barberstudio.data.model.EstadoTurno
import com.proyect.barberstudio.data.model.Turno
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TurnosViewModel : ViewModel() {

    // Privado: solo este ViewModel cambia los turnos.
    private val _uiState = MutableStateFlow(TurnosUiState())
    // Público y de solo lectura las pantallas lo leen.
    val uiState: StateFlow<TurnosUiState> = _uiState.asStateFlow()

    // Lo llama panatlla de Reservar cuando se confirma un turno. pasa el turno que se reservo como param
    fun agregarTurno(turno: Turno) {
        // 1) Leemo foto actual (estado actual)
        val actual = _uiState.value

        // 2) Juntamos todos los turnos (próximos y pasados)
        val todos = actual.proximos + actual.pasados

        // 3) Busco el id más grande. Si la lista está vacía, uso 0.
        val idMasAlto = todos.maxOfOrNull { unTurno -> unTurno.id } ?: 0 // recorre la lista, mira el id de cada turno y te devuelve el más grande.

        // 4) El id nuevo es = mas alto mas 1 (así nunca se repite)
        val nuevoId = idMasAlto + 1

        // 5) Copio el turno que llegó, poniéndole el id nuevo
        val turnoConId = turno.copy(id = nuevoId)

        // 6) Armo la lista nueva de próximos: la vieja + el turno nuevo al final
        val nuevosProximosTurnos = actual.proximos + turnoConId

        // 7) Modifico el estado. Guardo una foto nueva con la lista actualizada con nuevo turno al final
        _uiState.value = actual.copy(proximos = nuevosProximosTurnos)
    }

    // Lo llama pantalla Mis turnos cuando se confirma el diálogo de cancelar.
    // El turno sale de "próximos" y entra  en "pasados", marcado como CANCELADO.
    fun cancelarTurno(turno: Turno) {
        // 1) Leo la foto actual
        val actual = _uiState.value

        // 2) sacamos de la lista de "proximos" al turno que queremos cancelar
        val nuevosProximosTurnos = actual.proximos - turno

        // 3) Copio el turno cambiándole el estado a CANCELADO
        val turnoCancelado = turno.copy(estado = EstadoTurno.CANCELADO)

        // 4) turno canecelado lo agregamos en primer lugar de la lista de turnos pasados
        val nuevosTurnosPasados = listOf(turnoCancelado) + actual.pasados

        // 5) Guardo una foto nueva con las dos listas cambiadas
        _uiState.value = actual.copy(
            proximos = nuevosProximosTurnos,
            pasados = nuevosTurnosPasados
        )
    }
}