package com.proyect.barberstudio.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // NUEVO: hace que la Column se pueda scrollear
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource // NUEVO: para leer los textos de strings.xml
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.components.AppTextField // NUEVO: nuestro componente
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onClose: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    RegisterContent(
        uiState = uiState,
        onRegistered = {
            if(viewModel.validar()){
                onRegistered
            }
        },
        onClose = onClose,
        onEmailChange = viewModel::onEmailChange,
        onNombreChange = viewModel::onNombreChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTelefonoChange = viewModel::onTelefonoChange
    )
}

@Composable
fun RegisterContent(
    uiState: RegisterUiState,
    onRegistered: () -> Unit,
    onClose: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNombreChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            // ANTES: .background(Color.White)
            // CAMBIO: "background" del tema: blanco en modo claro, oscuro en modo oscuro.
            .background(MaterialTheme.colorScheme.background)
            // NUEVO: con 4 campos, en un celular chico el teclado tapa el botón "Crear cuenta".
            // verticalScroll permite deslizar la pantalla. rememberScrollState() es el estado que guarda
            // la posición del scroll (por eso empieza con "remember").
            // Va ANTES del padding para que el área scrolleable sea toda la pantalla, no solo el interior.
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {

        //HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                //cuadro del logo de tijera
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    // ANTES: color = Color(0xFF1E1E1E),
                    // CAMBIO: la caja usa el color del texto principal, así resalta en modo claro y oscuro.
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_scissors),
                            contentDescription = stringResource(R.string.logo_descripcion),
                            // ANTES: tint = Color.White, //para colorear la imagen vectorial la formita nomas
                            // CAMBIO: la tijera usa el color del fondo, lo contrario de la caja. Siempre contrasta.
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    // ANTES: Text(text = "CREAR CUENTA", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    // CAMBIO: el texto sale de strings.xml.
                    Text(
                        text = stringResource(R.string.registro_titulo).uppercase(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    // ANTES: Text(text = "BARBER STUDIO", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    // CAMBIO: texto a strings.xml y el gris pasa a ser "onSurfaceVariant" (texto secundario).
                    Text(
                        text = stringResource(R.string.marca_nombre),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Botón de la X para cerrar
            IconButton(onClick = onClose) {
                // ANTES: Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cerrar)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.registro_subtitulo),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = uiState.nombre,
            onValueChange = onNombreChange,
            label = stringResource(R.string.campo_nombre),
            placeholder = stringResource(R.string.ejemplo_nombre),
            keyboardType = KeyboardType.Text,
            //si hay error, convertimos el ID (Int) en texto. Si es null, let no se ejecuta y queda null.
            error = uiState.nombreError?.let { stringResource(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = uiState.telefono,
            onValueChange = onTelefonoChange,
            label = stringResource(R.string.campo_telefono),
            placeholder = stringResource(R.string.ejemplo_telefono),
            keyboardType = KeyboardType.Phone,
            //si hay error, convertimos el ID (Int) en texto. Si es null, let no se ejecuta y queda null.
            error = uiState.telefonoError?.let { stringResource(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.campo_email),
            placeholder = stringResource(R.string.ejemplo_email),
            keyboardType = KeyboardType.Email,
            error = uiState.emailError?.let { stringResource(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.campo_contrasena),
            keyboardType = KeyboardType.Password,
            esPassword = true,
            error = uiState.passwordError?.let { stringResource(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTONES ---
        Button(
            onClick = onRegistered,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.registro_boton),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* logica de Google para despues */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "G   " + stringResource(R.string.continuar_google),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    BarberStudioTheme {
        RegisterContent(
            uiState = RegisterUiState(),
            onRegistered = {},
            onClose = {},
            onEmailChange = {},
            onPasswordChange = {},
            onNombreChange = {},
            onTelefonoChange = {}
        )
    }
}