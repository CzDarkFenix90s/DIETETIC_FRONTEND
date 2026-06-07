package com.dietetic.frontend.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dietetic.frontend.domain.model.*
import com.dietetic.frontend.ui.theme.*
import com.dietetic.frontend.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    onEditCourse: (Int) -> Unit = {},
    onCreateCourse: () -> Unit = {},
    onManageModules: (Int) -> Unit = {},
    onCreateNutricionista: () -> Unit = {},
    onEditNutricionista: (Int) -> Unit = {},
    viewModel: AdminViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Planes", "Nutris", "Pacientes", "Consultas", "Alimentos")

    Scaffold(
        containerColor = Background,
        topBar = {
            AdminTopBar(onLogout = onLogout, onRefresh = viewModel::refreshAll)
        },
        floatingActionButton = {
            if (selectedTab == 0 || selectedTab == 1 || selectedTab == 4) {
                FloatingActionButton(
                    onClick = {
                        when (selectedTab) {
                            0 -> onCreateCourse()
                            1 -> onCreateNutricionista()
                            4 -> {
                                // Para crear un alimento suelto, redirigimos al primer plan o uno genérico
                                val firstPlanId = state.courses.firstOrNull()?.id ?: 0
                                onManageModules(firstPlanId) 
                            }
                        }
                    },
                    containerColor = PrimaryGreen,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Surface,
                contentColor = PrimaryGreen,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimaryGreen,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Black else FontWeight.Medium) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
                } else if (state.error != null) {
                    AdminErrorView(state.error!!, viewModel::refreshAll, onLogout)
                } else {
                    when (selectedTab) {
                        0 -> PlanesManagementView(state, viewModel, onEditCourse, onManageModules)
                        1 -> NutricionistasManagementView(state, viewModel, onEditNutricionista)
                        2 -> PacientesManagementView(state, viewModel)
                        3 -> ConsultasManagementView(state, viewModel)
                        4 -> AllAlimentosManagementView(state, viewModel, onManageModules)
                    }
                }
            }
        }

        if (state.isDeleting) {
            AdminDeletingOverlay()
        }
    }
}

@Composable
private fun PlanesManagementView(
    state: com.dietetic.frontend.ui.viewmodel.AdminUiState,
    viewModel: AdminViewModel,
    onEdit: (Int) -> Unit,
    onManageModules: (Int) -> Unit
) {
    if (state.courses.isEmpty()) {
        EmptyState("No hay planes creados")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Gestión de Planes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text("${state.courses.size} planes activos", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Spacer(Modifier.height(8.dp))
            }
            items(state.courses, key = { it.id }) { course ->
                AdminCourseItem(
                    course = course,
                    onEdit = { onEdit(course.id) },
                    onDelete = { viewModel.deleteCourse(course.id) },
                    onManageModules = { onManageModules(course.id) }
                )
            }
        }
    }
}

@Composable
private fun NutricionistasManagementView(
    state: com.dietetic.frontend.ui.viewmodel.AdminUiState,
    viewModel: AdminViewModel,
    onEdit: (Int) -> Unit
) {
    if (state.nutricionistas.isEmpty()) {
        EmptyState("No hay nutricionistas")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Especialistas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text("${state.nutricionistas.size} nutricionistas registrados", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Spacer(Modifier.height(8.dp))
            }
            items(state.nutricionistas) { nut ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).background(PrimaryGreen.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, null, tint = PrimaryGreen)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${nut.firstName ?: ""} ${nut.lastName ?: ""}", fontWeight = FontWeight.Bold)
                            Text(nut.specialty ?: "Especialista", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        IconButton(onClick = { onEdit(nut.id) }) {
                            Icon(Icons.Default.Edit, null, tint = AccentOrange)
                        }
                        IconButton(onClick = { viewModel.deleteNutricionista(nut.id) }) {
                            Icon(Icons.Default.Delete, null, tint = Error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PacientesManagementView(
    state: com.dietetic.frontend.ui.viewmodel.AdminUiState,
    viewModel: AdminViewModel
) {
    if (state.pacientes.isEmpty()) {
        EmptyState("No hay pacientes")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Gestión de Pacientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text("${state.pacientes.size} pacientes registrados", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Spacer(Modifier.height(8.dp))
            }
            items(state.pacientes) { pac ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).background(AccentOrange.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.AccessibilityNew, null, tint = AccentOrange)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(pac.displayName, fontWeight = FontWeight.Bold)
                            Text("ID: ${pac.patientCode ?: "N/A"}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        IconButton(onClick = { viewModel.deletePaciente(pac.id) }) {
                            Icon(Icons.Default.Delete, null, tint = Error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsultasManagementView(
    state: com.dietetic.frontend.ui.viewmodel.AdminUiState,
    viewModel: AdminViewModel
) {
    if (state.consultas.isEmpty()) {
        EmptyState("No hay consultas agendadas")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Consultas Dietéticas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text("${state.consultas.size} citas en el sistema", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Spacer(Modifier.height(8.dp))
            }
            items(state.consultas) { consulta ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).background(PrimaryGreen.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Event, null, tint = PrimaryGreen)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(consulta.pacienteNombre ?: "Paciente", fontWeight = FontWeight.Bold)
                            Text(consulta.scheduledTime?.take(16)?.replace("T", " ") ?: "Sin fecha", style = MaterialTheme.typography.labelSmall)
                            Text(consulta.status ?: "PENDIENTE", color = PrimaryGreen, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AllAlimentosManagementView(
    state: com.dietetic.frontend.ui.viewmodel.AdminUiState,
    viewModel: AdminViewModel,
    onManageModules: (Int) -> Unit
) {
    if (state.allAlimentos.isEmpty()) {
        EmptyState("No hay alimentos programados")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Inventario de Alimentos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Text("${state.allAlimentos.size} alimentos registrados", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    Button(
                        onClick = { 
                            val firstPlanId = state.courses.firstOrNull()?.id ?: 0
                            onManageModules(firstPlanId)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Añadir", fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            items(state.allAlimentos) { alimento ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🍎", fontSize = 24.sp)
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(alimento.name ?: "Alimento", fontWeight = FontWeight.Bold)
                            Text("${alimento.portionGrams ?: 0.0}g - ${alimento.mealType ?: "snack"}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            if (alimento.planName != null) {
                                Text("Plan: ${alimento.planName}", style = MaterialTheme.typography.labelSmall, color = PrimaryGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🔍", fontSize = 48.sp)
            Text(message, color = TextSecondary)
        }
    }
}

@Composable
private fun AdminTopBar(onLogout: () -> Unit, onRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(PrimaryGreen, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.AdminPanelSettings, null, tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Panel Admin", fontWeight = FontWeight.Black, color = TextPrimary)
                    Text("GESTIÓN TOTAL", style = MaterialTheme.typography.labelSmall, color = PrimaryGreen)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onRefresh, modifier = Modifier.background(PrimaryGreen.copy(alpha = 0.1f), CircleShape)) {
                    Icon(Icons.Default.Refresh, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onLogout, modifier = Modifier.background(Error.copy(alpha = 0.1f), CircleShape)) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Error, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun AdminCourseItem(
    course: Course,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onManageModules: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(course.title ?: "Plan Sin Título", fontWeight = FontWeight.Black)
                Text("${course.targetCalories ?: 0} kcal", style = MaterialTheme.typography.labelSmall, color = PrimaryGreen)
                Text("$${course.price ?: 0.0}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            }
            Row {
                IconButton(onClick = onManageModules) { Icon(Icons.AutoMirrored.Filled.List, null, tint = PrimaryGreen) }
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null, tint = AccentOrange) }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Error) }
            }
        }
    }
}

@Composable
private fun AdminErrorView(error: String, onRetry: () -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⚠️", fontSize = 48.sp)
        Spacer(Modifier.height(16.dp))
        Text("Error de Conexión", fontWeight = FontWeight.Black)
        Text(error, color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onRetry) { Text("REINTENTAR") }
            OutlinedButton(onClick = onLogout) { Text("REINICIAR SESIÓN") }
        }
    }
}

@Composable
private fun AdminDeletingOverlay() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black.copy(alpha = 0.3f)) {
        Box(contentAlignment = Alignment.Center) {
            Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(16.dp)) {
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(16.dp))
                    Text("Eliminando...", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
