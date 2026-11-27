package com.proyect.barberstudio.ui.screen.reservar

import androidx.lifecycle.ViewModel
import com.proyect.barberstudio.data.model.Barbero
import com.proyect.barberstudio.data.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReservaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReservaUiState())
    val uiState: StateFlow<ReservaUiState> = _uiState.asStateFlow()

    // ----- Lo que el usuario elige -----
    // funcines que leen estado actual y solo actualizan lo pasado por parametro
    fun onDiaClick(dia: Int) {
        // 1) Leo la foto actual
        val actual = _uiState.value
        // 2) Copio la foto cambiando solo "dia" la guardo
        _uiState.value = actual.copy(dia = dia)
    }

    fun onHoraClick(hora: String) {
        val actual = _uiState.value
        _uiState.value = actual.copy(hora = hora)
    }

    fun onServicioClick(servicio: Servicio) {
        val actual = _uiState.value
        _uiState.value = actual.copy(servicio = servicio)
    }

    fun onBarberoClick(barbero: Barbero) {
        val actual = _uiState.value
        _uiState.value = actual.copy(barbero = barbero)
    }

    fun onNotasChange(notas: String) {
        val actual = _uiState.value
        _uiState.value = actual.copy(notas = notas)
    }

    // ----- Moverse entre pasos -----
    fun irAPaso(paso: Int) {
        // 1) Leo la foto actual
        val actual = _uiState.value
        // 2) Copio la foto cambiando solo "paso"  la guardo
        _uiState.value = actual.copy(paso = paso)
    }

    fun volverUnPaso() {
        //  Leo la foto actual
        val actual = _uiState.value
        //  Calculo el paso de atrás
        val pasoAnterior = actual.paso - 1
        //  Copio la foto cambiando el paso y la guardo
        _uiState.value = actual.copy(paso = pasoAnterior)
    }

    // Deja la reserva vacía otra vez (paso 1, nada elegido).
    // Se llama al salir de la pantalla de "Listo", para que la próxima reserva arranque de cero.
    fun reiniciar() {
        _uiState.value = ReservaUiState()
    }
}