package com.proyect.barberstudio.data.model

import androidx.annotation.DrawableRes

// Un servicio de la barbería (corte, barba, etc.).
//  una data class  solo GUARDA datos: Kotlin le genera solo equals, toString y copy.
data class Servicio(
    val id: Int,              // identificador único: lo usa LazyRow para no confundir elementos
    val nombre: String,
    val descripcion: String,  // la usaremos en "Elegir servicio" (Reservar turno)
    val precio: Int,
    val duracionMin: Int,
    @DrawableRes val imagen: Int  // avisa a Android Studio que ese Int es una imagen
)