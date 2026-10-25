package com.proyect.barberstudio.data

import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.model.Producto
import com.proyect.barberstudio.data.model.Servicio
import com.proyect.barberstudio.data.model.Turno

// Datos falsos para mostrar en las pantallas mientras no tengamos base de datos ni servidor.
// Es un "object": existe UNA sola instancia en toda la app y se usa sin crearla (DatosDePrueba.servicios).
// Estos textos NO van en strings.xml: strings.xml es para los textos de la interfaz (títulos, botones).
// Los nombres de servicios son DATOS: en la app real vendrían del servidor.
object DatosDePrueba {

    const val NOMBRE_USUARIO = "Carlos"

    // Todavía no tenemos fotos de cada servicio, así que usamos la de la barbería para todos.

    val servicios = listOf(
        Servicio(1, "Corte de autor", "Corte a medida, lavado y peinado.", 35, 30, R.drawable.img_barber_bg),
        Servicio(2, "Arreglo de barba", "Forma, detalles y un acabado prolijo.", 20, 20, R.drawable.img_barber_bg),
        Servicio(3, "Corte + barba", "Renová tu estilo de principio a fin.", 55, 45, R.drawable.img_barber_bg),
        Servicio(4, "Afeitado con toalla caliente", "Afeitado al ras y un momento de relax.", 30, 30, R.drawable.img_barber_bg)
    )

    val productos = listOf(
        Producto(1, "Cera para peinado", "Fijación media, acabado mate.", 1250, R.drawable.img_barber_bg),
        Producto(2, "Champú hidratante", "Para uso diario.", 850, R.drawable.img_barber_bg),
        Producto(3, "Gel fijador", "Fijación fuerte, sin residuos.", 900, R.drawable.img_barber_bg)
    )


    val proximoTurno = Turno(
        id = 1,
        dia = "16",
        mes = "OCT",
        fecha = "Viernes, 16 de octubre",
        horario = "10:00–10:45",
        servicio = servicios[2],
        barbero = "Oscar Lazar"
    )
}