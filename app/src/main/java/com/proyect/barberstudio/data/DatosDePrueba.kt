package com.proyect.barberstudio.data

import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.model.Producto
import com.proyect.barberstudio.data.model.Servicio
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.data.model.EstadoTurno

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

    val turnosProximos = listOf(
        proximoTurno,
        Turno(2, "30", "OCT", "Viernes, 30 de octubre", "18:00–18:30", servicios[0],
            "Leo Martínez")
    )

    val turnosPasados = listOf(
        Turno(3, "02", "OCT", "Viernes, 2 de octubre", "10:00–10:45", servicios[2],
            "Marcus Reed", EstadoTurno.COMPLETADO),
        Turno(4, "18", "SEP", "Viernes, 18 de septiembre", "17:00–17:30",
            servicios[0], "Matías Rojas", EstadoTurno.COMPLETADO),
        Turno(5, "04", "SEP", "Viernes, 4 de septiembre", "11:00–11:45", servicios[2],
            "Santiago Barreto", EstadoTurno.CANCELADO)
    )


}