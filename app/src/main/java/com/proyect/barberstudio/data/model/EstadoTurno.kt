package com.proyect.barberstudio.data.model

// Los 3 estados posibles de un turno. un enum es una lista CERRADA de opciones:
// un turno no puede tener un estado que no este aqui.

enum class EstadoTurno {
    CONFIRMADO,
    COMPLETADO,
    CANCELADO
}