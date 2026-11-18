package com.proyect.barberstudio.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // NUEVO: para pedir el ViewModel
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.components.AppTextField
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    //viewModel() busca el LoginViewModel que ya existe, o lo crea la primera vez.
    // Tiene valor por defecto, así que AppNavigation no tiene que pasarlo.
    viewModel: LoginViewModel = viewModel()
) {
    // collectAsState() convierte el StateFlow en un estado de Compose: cuando cambia, la pantalla se redibuja sola.
    val uiState by viewModel.uiState.collectAsState()

    LoginContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,       // referencia a la función
        onPasswordChange = viewModel::onPasswordChange,
        // solo navegamos si el ViewModel dice que está todo bien.
        onLoginClick = {
            if (viewModel.validar()) {
                onLoginClick()
            }
        },
        onNavigateToRegister = onNavigateToRegister,
        onForgotPasswordClick = onForgotPasswordClick
    )
}

//recibe todo de afuera.
@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {

        //HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(48.dp)
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
            Column {
                Text(text = stringResource(R.string.login_titulo), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    text = stringResource(R.string.marca_nombre),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //IMAGEN DE LA BARBERIA
        Image(
            painter = painterResource(id = R.drawable.img_barber_bg),
            contentDescription = stringResource(R.string.login_imagen_descripcion),
            contentScale = ContentScale.Crop,//esto hace que la imagen llene el espacio sin deformarse
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))// imagen rendondeada
        )

        Spacer(modifier = Modifier.height(24.dp))

        //BIENVENIDA
        Text(text = stringResource(R.string.login_bienvenida), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.login_subtitulo),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        //CAMPOS DE TEXTO
        AppTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.campo_email),
            placeholder = stringResource(R.string.ejemplo_email),
            keyboardType = KeyboardType.Email,
            //si hay error, convertimos el ID (Int) en texto. Si es null, let no se ejecuta y queda null.
            error = uiState.emailError?.let { stringResource(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.campo_contrasena),
            keyboardType = KeyboardType.Password,
            esPassword = true,
            error = uiState.passwordError?.let { stringResource(it) } // NUEVO
        )

        Spacer(modifier = Modifier.height(8.dp))

        //OLVIDASTE TU CONTRASEÑA
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            TextButton(
                onClick = onForgotPasswordClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_olvidaste),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //BOTONES
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = stringResource(R.string.login_boton), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* Logica de Google */ },
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

        Spacer(modifier = Modifier.weight(1f))

        //TEXTO FINAL
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.login_sin_cuenta),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = stringResource(R.string.login_crear_cuenta),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BarberStudioTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {},
            onForgotPasswordClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    BarberStudioTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "carlos@",
                password = "123",
                emailError = R.string.error_email_invalido,
                passwordError = R.string.error_contrasena_corta
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {},
            onForgotPasswordClick = {}
        )
    }
}