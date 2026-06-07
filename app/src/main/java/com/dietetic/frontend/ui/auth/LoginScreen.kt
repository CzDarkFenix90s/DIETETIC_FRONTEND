package com.dietetic.frontend.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dietetic.frontend.ui.components.*
import com.dietetic.frontend.ui.viewmodel.AuthViewModel
import com.dietetic.frontend.ui.theme.*

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import com.dietetic.frontend.R

@Composable
fun LoginScreen(
    onLoginSuccess:  (role: String) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isEmailValid = true // Permitimos cualquier formato para soportar usernames (como admin1)

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            val user = (uiState as AuthUiState.Success).user
            onLoginSuccess(user.role)
        }
    }

    val isLoading = uiState is AuthUiState.Loading
    val errorMsg  = (uiState as? AuthUiState.Error)?.message

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 450.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(20.dp))
            
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = PrimaryBlue.copy(alpha = 0.1f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = PrimaryBlue
                    )
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                text       = "DIETETIC FRONTEND",
                style      = MaterialTheme.typography.headlineLarge,
                color      = DarkBlue,
                letterSpacing = (-0.5).sp
            )
            
            Text(
                text  = "Tu puerta al sistema de dietas",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                fontWeight = FontWeight.Normal
            )
            
            Spacer(Modifier.height(48.dp))

            Surface(
                shape            = MaterialTheme.shapes.extraLarge,
                color            = Surface,
                shadowElevation  = 4.dp,
                modifier         = Modifier.fillMaxWidth(),
                border           = BorderStroke(1.dp, Border)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Inicia sesión para continuar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    
                    Spacer(Modifier.height(32.dp))

                    if (errorMsg != null) {
                        Surface(
                            color  = Error.copy(alpha = 0.05f),
                            shape  = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Error.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = Error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text     = errorMsg,
                                    color    = Error,
                                    style    = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                    }

                    DieteticTextField(
                        value         = username,
                        onValueChange = { username = it; viewModel.clearError() },
                        label         = "Usuario o Correo",
                        placeholder   = "ejemplo o ejemplo@dominio.com",
                        enabled       = !isLoading,
                        keyboardType  = KeyboardType.Text,
                        imeAction     = ImeAction.Next,
                    )
                    Spacer(Modifier.height(20.dp))

                    DieteticTextField(
                        value         = password,
                        onValueChange = { password = it; viewModel.clearError() },
                        label         = "Contraseña",
                        placeholder   = "Tu contraseña segura",
                        isPassword    = true,
                        enabled       = !isLoading,
                        keyboardType  = KeyboardType.Password,
                        imeAction     = ImeAction.Done,
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    
                    TextButton(
                        onClick = { },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            "¿Olvidaste tu contraseña?",
                            style = MaterialTheme.typography.labelLarge,
                            color = PrimaryGreen
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    DieteticButton(
                        text      = "Iniciar Sesión",
                        onClick   = { viewModel.login(username.trim(), password) },
                        isLoading = isLoading,
                        enabled   = username.isNotBlank() && password.isNotBlank(),
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text  = "¿No tienes una cuenta? ",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text(
                        text  = "Regístrate ahora",
                        color = PrimaryBlue,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
