package com.proyect.barberstudio.ui.screen.home

import android.content.res.Configuration // NUEVO: para el preview en modo oscuro
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout // CAMBIO: la versión "AutoMirrored"
import androidx.compose.material.icons.filled.History // NUEVO: ícono de reloj para el historial
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme // NUEVO: para leer los colores del tema
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource // NUEVO: para leer los textos de strings.xml
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.components.ProfileMenuItem
import com.proyect.barberstudio.ui.screen.profile.ProfileUiEvent
import com.proyect.barberstudio.ui.screen.profile.ProfileUiState
import com.proyect.barberstudio.ui.screen.profile.ProfileViewModel
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    ProfileUI(
        state = viewModel.profileUiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ProfileUI(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
){
    ProfileElements(
        state = state,
        onEvent = onEvent
    )
}


@Composable
fun ProfileElements( //Stateless View sin estado
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()) // deja deslizar la pantalla
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                //cuadro del logo de tijera
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_scissors),
                            contentDescription = stringResource(R.string.logo_descripcion),
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = stringResource(R.string.app_name), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            }

            Surface(
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                color = Color.Transparent, // transparente se ve bien en los dos modos, se queda
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = stringResource(R.string.notificaciones),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = stringResource(R.string.perfil_titulo), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.perfil_subtitulo),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.perfil_foto_descripcion),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = state.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = state.email, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = state.phone, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onEvent(ProfileUiEvent.OnEditProfileClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = stringResource(R.string.perfil_editar),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.perfil_mi_cuenta),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        //seccion de mi cuenta
        Surface(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    title = stringResource(R.string.perfil_datos_titulo),
                    subtitle = stringResource(R.string.perfil_datos_subtitulo),
                    onClick = { onEvent(ProfileUiEvent.OnPersonalDataClick) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.NotificationsNone,
                    title = stringResource(R.string.notificaciones),
                    subtitle = stringResource(R.string.perfil_notificaciones_subtitulo),
                    onClick = { onEvent(ProfileUiEvent.OnNotificationsClick) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.History,
                    title = stringResource(R.string.perfil_historial_titulo),
                    subtitle = stringResource(R.string.perfil_historial_subtitulo),
                    onClick = { onEvent(ProfileUiEvent.OnHistoryClick) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(ProfileUiEvent.OnLogoutClick) }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                // AutoMirrored = la flecha se da vuelta sola en idiomas que se leen de derecha a izquierda.
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null, // el texto de al lado ya dice "Cerrar sesión"
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.perfil_cerrar_sesion),
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileElements() {
    BarberStudioTheme {
        ProfileElements(
            state = ProfileUiState(),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProfileElementsOscuro() {
    BarberStudioTheme {
        ProfileElements(
            state = ProfileUiState(),
            onEvent = {}
        )
    }
}