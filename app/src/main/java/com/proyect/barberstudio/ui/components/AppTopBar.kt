package com.proyect.barberstudio.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

// Barra de arriba con logo, nombre y campana.
// se va a repetir en Inicio, Mis turnos y Mi perfil
@Composable
fun AppTopBar(
    onNotificacionesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mismo logo que en Login pero mas chico
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_scissors),
                    contentDescription = stringResource(R.string.logo_descripcion),
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = stringResource(R.string.marca_nombre), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)

        // weight(1f) = "ocupá todo el espacio que sobra". Empuja la campanita hasta el borde derecho.

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onNotificacionesClick) {
            Icon(
                imageVector = Icons.Outlined.Notifications, // Outlined = versión "de contorno", como en el Figma
                contentDescription = stringResource(R.string.notificaciones)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppTopBarPreview() {
    BarberStudioTheme {
        AppTopBar(onNotificacionesClick = {})
    }
}