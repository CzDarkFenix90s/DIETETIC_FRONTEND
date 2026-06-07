package com.dietetic.frontend.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HealthAndSafety
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

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

@Composable
fun RegisterScreen(
    onRegisterSuccess:  (role: String) -> Unit,
    onNavigateToLogin:  () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    var username  by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    val passwordMismatch = password.isNotEmpty() &&
                           password2.isNotEmpty() &&
                           password != password2

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            val user = (uiState as AuthUiState.Success).user
            onRegisterSuccess(user.role)
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
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = PrimaryBlue.copy(alpha = 0.1f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.HealthAndSafety,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = PrimaryBlue
                    )
                }
            }
            
            Spacer(Modifier.height(20.dp))
            
            Text(
                text       = "Crea tu cuenta",
                style      = MaterialTheme.typography.headlineLarge,
                color      = DarkBlue,
                letterSpacing = (-0.5).sp
            )
            
            Text(
                text  = "Comienza tu vida saludable hoy",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
            )
            
            Spacer(Modifier.height(32.dp))

            Surface(
                shape            = MaterialTheme.shapes.extraLarge,
                color            = Surface,
                shadowElevation  = 4.dp,
                modifier         = Modifier.fillMaxWidth(),
                border           = BorderStroke(1.dp, Border)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

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
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                    }

                    DieteticTextField(
                        value         = username,
                        onValueChange = { username = it; viewModel.clearError() },
                        label         = "Nombre de usuario",
                        placeholder   = "mínimo 3 caracteres",
                        enabled       = !isLoading,
                        isError       = username.isNotEmpty() && username.length < 3,
                        errorMessage  = "Mínimo 3 caracteres",
                        imeAction     = ImeAction.Next,
                    )
                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            DieteticTextField(
                                value         = firstName,
                                onValueChange = { firstName = it },
                                label         = "Nombre",
                                placeholder   = "Tu nombre",
                                enabled       = !isLoading,
                                imeAction     = ImeAction.Next,
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            DieteticTextField(
                                value         = lastName,
                                onValueChange = { lastName = it },
                                label         = "Apellido",
                                placeholder   = "Tu apellido",
                                enabled       = !isLoading,
                                imeAction     = ImeAction.Next,
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    DieteticTextField(
                        value         = email,
                        onValueChange = { email = it; viewModel.clearError() },
                        label         = "Correo electrónico",
                        placeholder   = "ejemplo@dominio.com",
                        enabled       = !isLoading,
                        keyboardType  = KeyboardType.Email,
                        isError       = email.isNotEmpty() && !email.contains("@"),
                        errorMessage  = "Email inválido",
                        imeAction     = ImeAction.Next,
                    )
                    Spacer(Modifier.height(16.dp))

                    DieteticTextField(
                        value         = password,
                        onValueChange = { password = it; viewModel.clearError() },
                        label         = "Contraseña",
                        placeholder   = "mínimo 8 caracteres",
                        isPassword    = true,
                        enabled       = !isLoading,
                        keyboardType  = KeyboardType.Password,
                        isError       = password.isNotEmpty() && password.length < 8,
                        errorMessage  = "Mínimo 8 caracteres",
                        imeAction     = ImeAction.Next,
                    )
                    Spacer(Modifier.height(16.dp))

                    DieteticTextField(
                        value         = password2,
                        onValueChange = { password2 = it; viewModel.clearError() },
                        label         = "Confirmar contraseña",
                        placeholder   = "repite la contraseña",
                        isPassword    = true,
                        enabled       = !isLoading,
                        keyboardType  = KeyboardType.Password,
                        isError       = passwordMismatch,
                        errorMessage  = "Las contraseñas no coinciden",
                        imeAction     = ImeAction.Done,
                    )
                    Spacer(Modifier.height(32.dp))

                    val canSubmit = username.length >= 3 &&
                                   email.contains("@") &&
                                   password.length >= 8 &&
                                   !passwordMismatch &&
                                   !isLoading

                    DieteticButton(
                        text      = "Crear Cuenta",
                        onClick   = { viewModel.register(username.trim(), email.trim(), password, password2, firstName.trim(), lastName.trim()) },
                        isLoading = isLoading,
                        enabled   = canSubmit,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text  = "¿Ya tienes una cuenta? ",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text       = "Inicia sesión",
                        color      = PrimaryBlue,
                        style      = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
