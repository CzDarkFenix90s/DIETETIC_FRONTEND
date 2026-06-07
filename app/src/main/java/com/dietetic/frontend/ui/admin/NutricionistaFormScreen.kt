package com.dietetic.frontend.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dietetic.frontend.domain.model.Nutricionista
import com.dietetic.frontend.ui.components.DieteticButton
import com.dietetic.frontend.ui.components.DieteticTextField
import com.dietetic.frontend.ui.theme.*
import com.dietetic.frontend.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutricionistaFormScreen(
    onBack: () -> Unit,
    nutId: Int? = null,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var profId by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("") }
    
    // Campos para la cuenta de usuario
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(nutId) {
        if (nutId != null) {
            val nut = state.nutricionistas.find { it.id == nutId }
            nut?.let {
                firstName = it.firstName ?: ""
                lastName = it.lastName ?: ""
                profId = it.professionalId ?: ""
                specialty = it.specialty ?: ""
                fee = it.consultationFee?.toString() ?: ""
            }
        }
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            viewModel.resetStatus()
            onBack()
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(if (nutId == null) "Nuevo Especialista" else "Editar Especialista", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (nutId == null) {
                Text(
                    "DATOS DE ACCESO",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Black
                )
                
                DieteticTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Nombre de Usuario",
                    placeholder = "Ej: nutri_juan"
                )

                DieteticTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Correo Electrónico",
                    placeholder = "ejemplo@dietetic.com"
                )

                DieteticTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña Temporal",
                    placeholder = "Mínimo 8 caracteres",
                    isPassword = true
                )
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Border.copy(alpha = 0.5f))
            }

            Text(
                "INFORMACIÓN PROFESIONAL",
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryGreen,
                fontWeight = FontWeight.Black
            )

            DieteticTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "Nombre",
                placeholder = "Ej: Juan"
            )

            DieteticTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Apellido",
                placeholder = "Ej: Pérez"
            )

            DieteticTextField(
                value = profId,
                onValueChange = { profId = it },
                label = "ID Profesional",
                placeholder = "Matrícula o Código"
            )

            DieteticTextField(
                value = specialty,
                onValueChange = { specialty = it },
                label = "Especialidad",
                placeholder = "Ej: Nutrición Deportiva"
            )

            DieteticTextField(
                value = fee,
                onValueChange = { fee = it },
                label = "Costo Consulta ($)",
                placeholder = "Ej: 30.00"
            )

            Spacer(Modifier.height(12.dp))

            DieteticButton(
                text = if (nutId == null) "CREAR ESPECIALISTA Y CUENTA" else "GUARDAR CAMBIOS",
                isLoading = state.isSaving,
                onClick = {
                    viewModel.saveNutricionista(
                        Nutricionista(
                            id = nutId ?: 0,
                            firstName = firstName,
                            lastName = lastName,
                            professionalId = profId,
                            specialty = specialty,
                            consultationFee = fee.toDoubleOrNull() ?: 0.0,
                            username = if (nutId == null) username else null,
                            email = if (nutId == null) email else null,
                            password = if (nutId == null) password else null
                        )
                    )
                }
            )
        }
    }
}
