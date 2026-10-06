package com.danidev.apprickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danidev.apprickmorty.data.AuthService
import com.danidev.apprickmorty.ui.theme.*

@Composable
fun RegisterScreen(
    authService: AuthService = AuthService(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "REGISTRO",
                style = RickMortyTextStyles.AppTitle,
                color = NeonGreen
            )

            Text(
                text = "CREAR NUEVA CUENTA",
                style = RickMortyTextStyles.WelcomeMain,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de usuario", color = TextSecondary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = BorderMuted,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SearchInputBg,
                    unfocusedContainerColor = SearchInputBg
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = TextSecondary) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = BorderMuted,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SearchInputBg,
                    unfocusedContainerColor = SearchInputBg
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = TextSecondary) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = BorderMuted,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SearchInputBg,
                    unfocusedContainerColor = SearchInputBg
                ),
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Registro
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || name.isBlank()) {
                        errorMessage = "Por favor completa todos los campos"
                        return@Button
                    }
                    isLoading = true
                    errorMessage = null
                    authService.registrar(
                        email = email,
                        pass = password,
                        onSuccess = {
                            authService.guardarPerfil(
                                nombre = name,
                                email = email,
                                photoUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                                onSuccess = {
                                    isLoading = false
                                    onRegisterSuccess()
                                },
                                onError = { _ ->
                                    isLoading = false
                                    // Aun si falla la BD de perfil, el usuario ya se creó
                                    onRegisterSuccess()
                                }
                            )
                        },
                        onError = { err ->
                            isLoading = false
                            errorMessage = err
                        }
                    )
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    contentColor = ChipActiveText
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = ChipActiveText
                    )
                } else {
                    Text(
                        text = "REGISTRARSE",
                        style = RickMortyTextStyles.ChipText,
                        color = ChipActiveText
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Volver a Login
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Inicia sesión",
                    color = NeonGreen,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}
