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
import com.proyect.barberstudio.R
import com.proyect.barberstudio.ui.components.AppTextField
import com.proyect.barberstudio.ui.theme.BarberStudioTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // ANTES: .background(Color.White)
            // CAMBIO: Color.White es blanco siempre. "background" es blanco en modo claro y oscuro en modo oscuro.
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
                // ANTES: color = Color(0xFF1E1E1E),
                // CAMBIO: la caja del logo usa el color del texto principal (casi negro en modo claro,
                // casi blanco en modo oscuro). siempre resalta sobre el fondo.
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_scissors),
                        // ANTES: contentDescription = "Logo Barberia",
                        // CAMBIO: este texto lo lee TalkBack (lector de pantalla), así que también va en strings.xml.
                        contentDescription = stringResource(R.string.logo_descripcion),
                        // ANTES: tint = Color.White,
                        // CAMBIO: la tijera usa el color del fondo, o sea lo contrario de la caja. Siempre contrasta.
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                // ANTES: Text(text = "INICIO DE SESION", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                // CAMBIO: el texto sale de strings.xml (login_titulo),  puede traducir o cambiar en un solo lugar.
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
        // ANTES:
        // Text(text = "Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        // Spacer(modifier = Modifier.height(4.dp))
        // OutlinedTextField(
        //     value = email,
        //     onValueChange = { email = it },
        //     placeholder = { Text("tuEmail@email.com", color = Color.LightGray) },
        //     modifier = Modifier.fillMaxWidth(),
        //     keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        //     shape = RoundedCornerShape(12.dp),
        //     colors = OutlinedTextFieldDefaults.colors(
        //         focusedTextColor = Color.Black,
        //         unfocusedTextColor = Color.Black
        //     )
        // )

        // CAMBIO: todo eso ahora es un solo AppTextField.
        // - El título "Correo electrónico" pasa a ser el "label", que va en el borde del campo como en el Figma.
        // - La forma redondeada, el ancho completo y el teclado los resuelve el componente.
        // - Ya no hace falta forzar Color.Black: ahora lomtoma por defecto de el tema .
        // - La pantalla sigue guardando el email (state hoisting): el componente solo lo muestra y avisa cambios.
        AppTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.campo_email),
            placeholder = stringResource(R.string.ejemplo_email),
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ANTES (contraseña, 3 piezas más la lógica del ojito):
        // Text(text = "Contraseña", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        // Spacer(modifier = Modifier.height(4.dp))
        // OutlinedTextField(
        //     value = password,
        //     onValueChange = { password = it },
        //     placeholder = { Text("••••••••", color = Color.LightGray) },
        //     modifier = Modifier.fillMaxWidth(),
        //     keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        //     visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        //     trailingIcon = {
        //         val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
        //         IconButton(onClick = { passwordVisible = !passwordVisible }) {
        //             Icon(imageVector = icon, contentDescription = "Ver password")
        //         }
        //     },
        //     shape = RoundedCornerShape(12.dp),
        //     colors = OutlinedTextFieldDefaults.colors(
        //         focusedTextColor = Color.Black,
        //         unfocusedTextColor = Color.Black
        //     )
        // )
        // CAMBIO: con esPassword = true, AppTextField oculta el texto y pone el ojito él solo.
        // Esa lógica ahora está escrita una sola vez (en el componente) y la reusan Login y Register.
        AppTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.campo_contrasena),
            keyboardType = KeyboardType.Password,
            esPassword = true
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
            // ANTES: colors = ButtonDefaults.buttonColors(containerColor = BtnColor),
            // CAMBIO: se borró la línea. Button usa por defecto el color "primary" del tema,
            shape = RoundedCornerShape(12.dp)
        ) {
            // ANTES: Text(text = "Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            // CAMBIO: el texto va a strings.xml. El color blanco del texto lo pone el tema solo ("onPrimary").
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
            // ANTES: Text(text = "G   Continuar con Google", color = Color.Black, fontWeight = FontWeight.Bold)
            // CAMBIO: el texto va a strings.xml. Color.Black no se vería en modo oscuro, así que pasa a "onSurface"

            Text(
                text = "G   " + stringResource(R.string.continuar_google),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        //TEXTO FINAL
        // CAMBIO: antes había un TextButton DENTRO de otro TextButton.
        // Ahora es una Row con un texto común (gris) y un solo botón: solo "Crear cuenta" es clickeable.
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
        LoginScreen(
            onLoginClick = {},
            onNavigateToRegister = {},
            onForgotPasswordClick = {}
        )
    }
}