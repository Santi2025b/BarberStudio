package com.proyect.barberstudio.data.model

//  Mismo tipo de molde que Servicio y Producto.
data class Barbero(
    val id: Int,
    val nombre: String,
    val especialidad: String,  // "Degradados, diseño de barba"
    val puntaje: Double,
    val resenas: Int
)