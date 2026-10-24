package com.proyect.barberstudio.ui.screen.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.theme.BarberStudioTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyect.barberstudio.ui.screen.profile.ProfileUiEvent
import com.proyect.barberstudio.ui.screen.profile.ProfileUiState
import com.proyect.barberstudio.ui.screen.profile.ProfileViewModel
import java.nio.file.WatchEvent

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    ProfileUI(
        state = viewModel.profileUiState,
        onEvent = viewModel::onEvent //referencia
    )
}

@Composable
fun ProfileUI( //Funciona como un puente organizativo
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {
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
            .background(Color.White)
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
                    color = Color(0xFF1E1E1E),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_scissors),
                            contentDescription = "Logo Barberia",
                            tint = Color.White, //para colorear la imagen vectorial la formita nomas
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Barberia", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            }

            Surface(
                shape = CircleShape,
                border = BorderStroke(1.dp, Color.LightGray),
                color = Color.Transparent,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notificaciones",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(24.dp))
        Text(text = "Mi perfil", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "Todo lo tuyo, en un solo lugar.", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                color = Color.LightGray
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = state.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = state.email, fontSize = 13.sp, color = Color.Gray)
            Text(text = state.phone, fontSize = 13.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onEvent(ProfileUiEvent.OnEditProfileClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Text(
                    text = "Editar perfil",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "MI CUENTA",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))

        //seccion de mi cuenta
        Surface(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    title = "Datos Personales",
                    subtitle = "Nombre, correo y telefono",
                    onClick = { onEvent(ProfileUiEvent.OnPersonalDataClick) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.NotificationsNone,
                    title = "Notificaciones",
                    subtitle = "Recordatorios de turnos",
                    onClick = { onEvent(ProfileUiEvent.OnNotificationsClick) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    title = "Historial de turnos",
                    subtitle = "Revisá tus visitas anteriores",
                    onClick = { onEvent(ProfileUiEvent.OnHistoryClick) }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(ProfileUiEvent.OnLogoutClick) }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Cerrar sesión",
                tint = Color(0xFFE53935),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cerrar sesión",
                color = Color(0xFFE53935),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.DarkGray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Ir a $title",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

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