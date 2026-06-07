package com.dietetic.frontend.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dietetic.frontend.ui.theme.*
import com.dietetic.frontend.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentoFormScreen(
    courseId: Int,
    moduleId: Int? = null,
    onBack: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val module = remember(moduleId, state.modules) {
        state.modules.find { it.id == moduleId }
    }

    var title by remember { mutableStateOf(module?.title ?: "") }
    var description by remember { mutableStateOf(module?.description ?: "") }
    var order by remember { mutableStateOf(module?.order?.toString() ?: "1") }
    var mealType by remember { mutableStateOf(module?.mealType ?: "desayuno") }
    var portionGrams by remember { mutableStateOf(module?.portionGrams?.toString() ?: "150.0") }
    var isActive by remember { mutableStateOf(module?.isActive ?: true) }

    val mealTypes = listOf("desayuno", "almuerzo", "cena", "snack", "merienda")

    Scaffold(
        containerColor = Background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = TextPrimary.copy(alpha = 0.05f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Volver",
                            modifier = Modifier.size(20.dp),
                            tint = TextPrimary
                        )
                    }
                    
                    Spacer(Modifier.width(16.dp))
                    
                    Text(
                        text = if (moduleId == null) "Nuevo Alimento" else "Editar Alimento",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.error != null) {
                    Surface(
                        color = Error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = state.error!!,
                            color = Error,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nombre del alimento / comida") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Instrucciones o descripción") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = order,
                        onValueChange = { order = it },
                        label = { Text("Orden") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = portionGrams,
                        onValueChange = { portionGrams = it },
                        label = { Text("Porción (g)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Text("Tipo de Comida", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mealTypes.forEach { type ->
                        FilterChip(
                            selected = mealType == type,
                            onClick = { mealType = type },
                            label = { Text(type.uppercase()) },
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isActive, onCheckedChange = { isActive = it }, colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen))
                    Text("Alimento activo")
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.saveModule(
                            id = moduleId,
                            courseId = courseId,
                            title = title,
                            description = description,
                            order = order.toIntOrNull() ?: 1,
                            mealType = mealType,
                            portionGrams = portionGrams.toDoubleOrNull() ?: 0.0,
                            isActive = isActive
                        )
                    },
                    enabled = !state.isSaving && title.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.Save, null)
                        Spacer(Modifier.width(8.dp))
                        Text("GUARDAR ALIMENTO", fontWeight = FontWeight.Bold)
                    }
                }

                LaunchedEffect(state.saveSuccess) {
                    if (state.saveSuccess) {
                        viewModel.resetStatus()
                        onBack()
                    }
                }
            }
        }
    }
}
