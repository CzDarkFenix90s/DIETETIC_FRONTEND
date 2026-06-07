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
import androidx.hilt.navigation.compose.hiltViewModel
import com.dietetic.frontend.ui.theme.*
import com.dietetic.frontend.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanFormScreen(
    courseId: Int? = null,
    onBack: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val course = remember(courseId, state.courses) {
        state.courses.find { it.id == courseId }
    }

    var title by remember { mutableStateOf(course?.title ?: "") }
    var description by remember { mutableStateOf(course?.description ?: "") }
    var price by remember { mutableStateOf(course?.price?.toString() ?: "") }
    var calories by remember { mutableStateOf(course?.targetCalories?.toString() ?: "2000") }
    var weeks by remember { mutableStateOf(course?.durationWeeks?.toString() ?: "4") }
    var isActive by remember { mutableStateOf(course?.isActive ?: true) }

    Scaffold(
        containerColor = Background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
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
                        text = if (courseId == null) "Nuevo Plan" else "Editar Plan",
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
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nombre del plan") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Costo Est.") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calorías") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.Default.LocalFireDepartment, null) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                OutlinedTextField(
                    value = weeks,
                    onValueChange = { weeks = it },
                    label = { Text("Duración (Semanas)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                    shape = RoundedCornerShape(12.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isActive, onCheckedChange = { isActive = it }, colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen))
                    Text("Plan activo (visible en catálogo)", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        val p = price.toDoubleOrNull() ?: 0.0
                        viewModel.saveCourse(
                            id = courseId,
                            title = title,
                            description = description,
                            price = p,
                            level = calories,
                            isActive = isActive,
                            durationWeeks = weeks.toIntOrNull() ?: 4
                        )
                    },
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.Save, null)
                        Spacer(Modifier.width(8.dp))
                        Text("GUARDAR PLAN", fontWeight = FontWeight.Bold)
                    }
                }

                LaunchedEffect(state.saveSuccess) {
                    if (state.saveSuccess) {
                        viewModel.resetStatus()
                        onBack()
                    }
                }
                
                if (state.error != null) {
                    Text(state.error!!, color = Error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
