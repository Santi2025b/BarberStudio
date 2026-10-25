package com.proyect.barberstudio.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.data.DatosDePrueba
import com.proyect.barberstudio.data.model.Producto
import com.proyect.barberstudio.data.model.Servicio
import com.proyect.barberstudio.data.model.Turno
import com.proyect.barberstudio.ui.components.AppTopBar
import com.proyect.barberstudio.ui.components.ItemCatalogoCard
import com.proyect.barberstudio.ui.components.ProximaVisitaCard
import com.proyect.barberstudio.ui.components.SeccionTitulo
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Pantalla de Inicio. Es "stateless" (sin estado propio): TODO lo que muestra le llega por parámetro
// y TODO lo que el usuario toca lo avisa con una lambda.  no sabe de dónde vienen los datos
// (hoy de DatosDePrueba, mañana de un ViewModel) ni a dónde lleva cada botón ( lo decide AppNavigation).
@Composable
fun InicioScreen(
    nombreUsuario: String,
    proximoTurno: Turno?,           // Turno? = puede no haber turno. Si es null, no mostramos la tarjeta.
    servicios: List<Servicio>,
    productos: List<Producto>,
    onReservarClick: () -> Unit,
    onServicioClick: (Servicio) -> Unit, // la lambda RECIBE el servicio tocado, para saber cuál fue
    onProductoClick: (Producto) -> Unit,
    onVerTodosProductos: () -> Unit,
    onNotificacionesClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // el contenido es más alto que la pantalla
            .padding(vertical = 16.dp)
    ) {
        val margen = Modifier.padding(horizontal = 24.dp) // lo guardamos en una variable para no repetirlo

        AppTopBar(onNotificacionesClick = onNotificacionesClick, modifier = margen)

        Spacer(modifier = Modifier.height(24.dp))

        // SALUDO: stringResource con un argumento rellena el %1$s -> "Hola, carlos."
        Text(
            text = stringResource(R.string.inicio_saludo, nombreUsuario),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = margen
        )
        Text(
            text = stringResource(R.string.inicio_subtitulo),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = margen
        )

        // PRÓXIMA VISITA: solo si hay turno. Adentro del if, Kotlin sabe que proximoTurno no es null.
        if (proximoTurno != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.inicio_proxima_visita),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = margen
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProximaVisitaCard(turno = proximoTurno, modifier = margen)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onReservarClick,
            modifier = margen
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = stringResource(R.string.inicio_reservar), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SERVICIOS
        SeccionTitulo(titulo = stringResource(R.string.inicio_servicios), modifier = margen)
        Spacer(modifier = Modifier.height(8.dp))

        // LazyRow = fila que se desliza de costado. "Lazy" (perezosa) porque solo dibuja las tarjetas
        // que se ven en pantalla
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),  // margen al principio y al final de la lista,
            // pero al deslizar las tarjetas llegan al borde
            horizontalArrangement = Arrangement.spacedBy(12.dp)  // espacio entre tarjeta y tarjeta
        ) {
            // items(lista) { elemento -> ... } se ejecuta una vez por cada servicio.
            // key = { it.id }: le dice a Compose cómo identificar cada tarjeta. Si la lista cambia
            // (se agrega o borra un servicio), Compose sabe cuál es cuál y no redibuja todo.
            items(servicios, key = { it.id }) { servicio ->
                ItemCatalogoCard(
                    titulo = servicio.nombre,
                    // "$%1$d · %2$d min" -> "$55 · 45 min" (%d = número entero)
                    subtitulo = stringResource(R.string.servicio_precio_duracion, servicio.precio, servicio.duracionMin),
                    imagen = servicio.imagen,
                    onClick = { onServicioClick(servicio) } // avisamos CUÁL servicio se tocó
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // PRODUCTOS: misma tarjeta que servicios, con otro subtítulo.
        SeccionTitulo(
            titulo = stringResource(R.string.inicio_productos),
            accion = stringResource(R.string.ver_todo),
            onAccionClick = onVerTodosProductos,
            modifier = margen
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productos, key = { it.id }) { producto ->
                ItemCatalogoCard(
                    titulo = producto.nombre,
                    // "$%1$,d": la coma pide separador de miles según el idioma del celular -> "$1.250"
                    subtitulo = stringResource(R.string.precio, producto.precio),
                    imagen = producto.imagen,
                    onClick = { onProductoClick(producto) }
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun InicioScreenPreview() {
    BarberStudioTheme {
        InicioScreen(
            nombreUsuario = DatosDePrueba.NOMBRE_USUARIO,
            proximoTurno = DatosDePrueba.proximoTurno,
            servicios = DatosDePrueba.servicios,
            productos = DatosDePrueba.productos,
            onReservarClick = {},
            onServicioClick = {},
            onProductoClick = {},
            onVerTodosProductos = {},
            onNotificacionesClick = {}
        )
    }
}