package com.proyect.barberstudio.data
import com.proyect.barberstudio.data.model.Barbero
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.model.Producto
import com.proyect.barberstudio.data.model.Servicio
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.data.model.EstadoTurno

// Datos falsos para mostrar en las pantallas mientras no tengamos base de datos ni servidor.
// Es un "object": existe UNA sola instancia en toda la app y se usa sin crearla (DatosDePrueba.servicios).
// Estos textos NO van en strings.xml: strings.xml es para los textos de la interfaz (títulos, botones).

object DatosDePrueba {

    const val NOMBRE_USUARIO = "Carlos"

    // no tenemos fotos de cada servicio, a usamos la de la barbería para todos.

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
            "Marcos Red", EstadoTurno.COMPLETADO),
        Turno(4, "18", "SEP", "Viernes, 18 de septiembre", "17:00–17:30",
            servicios[0], "Matías Rojas", EstadoTurno.COMPLETADO),
        Turno(5, "04", "SEP", "Viernes, 4 de septiembre", "11:00–11:45", servicios[2],
            "Santiago Barreto", EstadoTurno.CANCELADO)
    )



    // ---------- Reservar turno ----------

    val barberos = listOf(
        Barbero(1, "Marcos Red", "Degradados y diseño de barba", 4.9, 128),
        Barbero(2, "Leo Martinez", "Textura y cortes modernos", 4.8, 96),
        Barbero(3, "James Mamani", "Cortes clásicos y afeitados", 4.8, 112),
        Barbero(4, "Gold D Roger", "Precisión y peinado", 4.6, 84)
    )

    // La opción "Cualquier barbero disponible". Es un Barbero más, con id 0.
    // Así el resto del código la trata igual que a los otros: se elige, se guarda y va en el turno.
    val cualquierBarbero = Barbero(0, "Cualquier barbero", "El mismo cuidado, con todo el equipo", 0.0, 0)

    val horarios = listOf("09:00", "09:30", "10:00", "11:00", "13:00", "14:30")

    // El calendario. Para simplificar, solo existe octubre 2026.
    const val MES_TITULO = "Octubre 2026"
    const val MES_CORTO = "OCT"
    const val DIAS_DEL_MES = 31
    const val PRIMER_DIA_SEMANA = 3  // el 1 de octubre de 2026 es jueves

    // Días que no se pueden elegir: los que ya pasaron (1 al 12) y los domingos 18 y 25 (está cerrado).
    val diasNoDisponibles = (1..12).toSet() + setOf(18, 25)

    // "private": solo se usa acá adentro, desde nombreDia()
    private val nombresDias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    // ¿Qué día de la semana es el "dia" de octubre? Arranca en jueves y da la vuelta cada 7 (el % es el resto).
    // Ejemplo: dia 16 -> (3 + 15) % 7 = 4 -> nombresDias[4] = "Viernes"
    fun nombreDia(dia: Int): String = nombresDias[(PRIMER_DIA_SEMANA + dia - 1) % 7]

    fun fechaLarga(dia: Int): String = "${nombreDia(dia)}, $dia de octubre"            // "Viernes, 16 de octubre"
    fun fechaCorta(dia: Int): String = "${nombreDia(dia).take(3)}, $dia de octubre"    // "Vie, 16 de octubre"


}