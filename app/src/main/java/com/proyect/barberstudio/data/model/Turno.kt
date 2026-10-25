package com.proyect.barberstudio.data.model

// Un turno reservado por el cliente.
// Las fechas van como texto ya armado para simplificar. En una app real usaríamos LocalDate/LocalTime
data class Turno(
    val id: Int,
    val dia: String,       // "16"
    val mes: String,       // "OCT"
    val fecha: String,     // "Viernes, 16 de octubre"
    val horario: String,   // "10:00–10:45"
    val servicio: Servicio, // una data class puede tener adentro otra data class
    val barbero: String
)