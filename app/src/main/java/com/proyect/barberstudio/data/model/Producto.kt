package com.proyect.barberstudio.data.model

import androidx.annotation.DrawableRes

// Un producto a la venta (ceras, champú, etc)
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    @DrawableRes val imagen: Int
)