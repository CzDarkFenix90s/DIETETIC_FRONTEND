package com.dietetic.frontend.ui.consultas

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dietetic.frontend.domain.model.*
import com.dietetic.frontend.ui.theme.*
import com.dietetic.frontend.ui.viewmodel.ConsultasViewModel
import java.time.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultasScreen(
    onLogout: (() -> Unit)? = null,
    viewModel: ConsultasViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBookingSheet by remember { mutableStateOf(false) }
    var selectedConsulta by remember { mutableStateOf<ConsultaDietetica?>(null) }

    // Dialogo de Éxito con estilo Pro
    if (uiState.successMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.resetStatus() },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetStatus() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("ENTENDIDO", fontWeight = FontWeight.Black) }
            },
            icon = { Icon(Icons.Rounded.Verified, null, tint = PrimaryGreen, modifier = Modifier.size(54.dp)) },
            title = { Text("Operación Exitosa", fontWeight = FontWeight.Black) },
            text = { Text(uiState.successMessage!!, textAlign = TextAlign.Center) },
            shape = RoundedCornerShape(28.dp),
            containerColor = Surface
        )
    }

    // Modal para Gestionar Cita (Confirmar/Negar)
    if (selectedConsulta != null) {
        ManageAppointmentDialog(
            consulta = selectedConsulta!!,
            isNutri = uiState.userRole == "nutricionista",
            onDismiss = { selectedConsulta = null },
            onUpdateStatus = { status ->
                viewModel.updateConsultaStatus(selectedConsulta!!.id, status)
                selectedConsulta = null
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Brush.verticalGradient(listOf(ForestGreen.copy(alpha = 0.12f), Background)))
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text("CONSULTAS", style = MaterialTheme.typography.titleLarge, color = ForestGreen, letterSpacing = 2.sp, fontWeight = FontWeight.Black)
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                    actions = {
                        IconButton(
                            onClick = { viewModel.loadData() },
                            modifier = Modifier.background(Color.White.copy(alpha = 0.6f), CircleShape)
                        ) { Icon(Icons.Default.Refresh, null, tint = ForestGreen) }
                        
                        if (onLogout != null && uiState.userRole == "nutricionista") {
                            IconButton(onClick = onLogout) {
                                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Error)
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                if (uiState.userRole != "nutricionista") {
                    Surface(
                        onClick = { showBookingSheet = true },
                        color = ForestGreen,
                        shape = RoundedCornerShape(24.dp),
                        shadowElevation = 12.dp,
                        border = BorderStroke(1.dp, PremiumGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.CalendarToday, null, tint = PremiumGold)
                            Spacer(Modifier.width(12.dp))
                            Text("AGENDAR", color = Color.White, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // Resumen Nutricionista
                if (uiState.userRole == "nutricionista") {
                    item {
                        NutriSummaryDashboard(uiState.consultas)
                    }
                }

                item {
                    SectionHeader("Especialistas Pro", "Equipo médico de élite")
                    Spacer(Modifier.height(16.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        items(uiState.nutricionistas) { nut ->
                            NutricionistaOfficeCard(nut)
                        }
                    }
                }

                item {
                    SectionHeader("Mis Próximas Citas", "Tu camino al éxito nutricional")
                    if (uiState.isLoading && uiState.consultas.isEmpty()) {
                        Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = ForestGreen)
                        }
                    }
                }

                if (uiState.consultas.isEmpty() && !uiState.isLoading) {
                    item {
                        EmptyConsultasStatePro(onAgendar = { showBookingSheet = true }, isNutri = uiState.userRole == "nutricionista")
                    }
                } else {
                    items(uiState.consultas) { consulta ->
                        ConsultaItemPro(
                            consulta = consulta,
                            onClick = { selectedConsulta = consulta }
                        )
                    }
                }
                
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showBookingSheet) {
        BookingBottomSheet(
            nutricionistas = uiState.nutricionistas,
            planes = uiState.planes,
            isBooking = uiState.isBooking,
            error = uiState.error,
            onDismiss = { 
                showBookingSheet = false
                viewModel.resetStatus()
            },
            onConfirm = { n: Int, p: Int, d: String -> viewModel.agendarCita(n, p, d) }
        )
    }

    LaunchedEffect(uiState.bookingSuccess) {
        if (uiState.bookingSuccess) showBookingSheet = false
    }
}

@Composable
fun NutriSummaryDashboard(consultas: List<ConsultaDietetica>) {
    val hoy = consultas.filter { it.scheduledTime?.contains(LocalDate.now().toString()) == true }.size
    val pendientes = consultas.filter { it.status == "programada" }.size

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        SummaryCard(Modifier.weight(1f), "Citas Hoy", "$hoy", ForestGreen)
        SummaryCard(Modifier.weight(1f), "Pendientes", "$pendientes", PremiumGold)
    }
}

@Composable
fun SummaryCard(modifier: Modifier, label: String, value: String, color: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = color.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextFaint, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ManageAppointmentDialog(
    consulta: ConsultaDietetica,
    isNutri: Boolean,
    onDismiss: () -> Unit,
    onUpdateStatus: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Gestionar Cita #${consulta.id}", fontWeight = FontWeight.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("¿Qué desea hacer con la cita de ${consulta.pacienteNombre}?", style = MaterialTheme.typography.bodyMedium)
                
                if (isNutri) {
                    Button(
                        onClick = { onUpdateStatus("en_curso") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, null)
                        Spacer(Modifier.width(8.dp))
                        Text("CONFIRMAR", fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = { onUpdateStatus("retrasada") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Error),
                    border = BorderStroke(1.dp, Error),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Cancel, null)
                    Spacer(Modifier.width(8.dp))
                    Text("CANCELAR / NEGAR", fontWeight = FontWeight.Bold)
                }

                if (isNutri) {
                    TextButton(
                        onClick = { /* Implementar Delete en Repo si es necesario, o usar cancel */ onUpdateStatus("retrasada") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ELIMINAR DEFINITIVAMENTE", color = TextFaint, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        },
        containerColor = Surface,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun ConsultaItemPro(consulta: ConsultaDietetica, onClick: () -> Unit = {}) {
    val statusColor = when(consulta.status?.lowercase()) {
        "programada" -> PremiumGold
        "en_curso" -> PrimaryGreen
        "retrasada" -> Error
        else -> TextFaint
    }

    val dateStr = try {
        val raw = consulta.scheduledTime?.replace("Z", "") ?: ""
        val dt = if (raw.contains("T")) LocalDateTime.parse(raw.take(19)) else LocalDateTime.now()
        dt.format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM")).uppercase()
    } catch (e: Exception) {
        consulta.scheduledTime?.take(10) ?: "FECHA PENDIENTE"
    }

    val timeStr = try {
        val raw = consulta.scheduledTime?.replace("Z", "") ?: ""
        val dt = if (raw.contains("T")) LocalDateTime.parse(raw.take(19)) else LocalDateTime.now()
        dt.format(DateTimeFormatter.ofPattern("HH:mm 'HRS'"))
    } catch (e: Exception) {
        ""
    }

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, Border.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).background(statusColor, CircleShape))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            consulta.status?.replace("_", " ")?.uppercase() ?: "PENDIENTE",
                            style = MaterialTheme.typography.labelSmall,
                            color = statusColor,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Text("#${consulta.id}", style = MaterialTheme.typography.labelSmall, color = TextFaint)
            }
            
            Spacer(Modifier.height(20.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(dateStr, style = MaterialTheme.typography.labelSmall, color = ForestGreen, fontWeight = FontWeight.Black)
                    Text(timeStr, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = TextPrimary)
                }
                Icon(Icons.Rounded.ChevronRight, null, tint = TextFaint)
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Border.copy(alpha = 0.3f))
            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(32.dp).background(PrimaryBlue.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Person, null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("PACIENTE", style = MaterialTheme.typography.labelSmall, color = TextFaint, fontWeight = FontWeight.Bold)
                    Text(consulta.pacienteNombre?.uppercase() ?: "USUARIO PRO", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column {
        Text(title, style = MaterialTheme.typography.titleLarge, color = ForestGreen, fontWeight = FontWeight.Black)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextFaint)
    }
}

@Composable
fun NutricionistaOfficeCard(nut: Nutricionista) {
    Surface(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(FreshGradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(48.dp))
                Icon(
                    Icons.Rounded.LocationOn, null, 
                    tint = PremiumGold, 
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).size(18.dp)
                )
            }
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${nut.firstName} ${nut.lastName}", style = MaterialTheme.typography.titleMedium, color = ForestGreen, maxLines = 1)
                Text(nut.specialty ?: "Nutricionista", style = MaterialTheme.typography.labelSmall, color = PremiumGold)
                Spacer(Modifier.height(12.dp))
                Surface(color = SageGreen, shape = RoundedCornerShape(8.dp)) {
                    Text("$${nut.consultationFee}", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = ForestGreen)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingBottomSheet(
    nutricionistas: List<Nutricionista>,
    planes: List<Course>,
    isBooking: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String) -> Unit
) {
    var selectedNutId by remember { mutableStateOf<Int?>(null) }
    var selectedPlanId by remember { mutableStateOf<Int?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("Confirmar", fontWeight = FontWeight.Bold) }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) { Text("Confirmar", fontWeight = FontWeight.Bold) }
            },
            text = { TimePicker(state = timePickerState) },
            shape = RoundedCornerShape(28.dp)
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 40.dp).fillMaxWidth().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Agendar Nueva Cita", style = MaterialTheme.typography.titleLarge, color = ForestGreen, fontWeight = FontWeight.Black)
            if (error != null) {
                Surface(color = Error.copy(alpha = 0.08f), shape = RoundedCornerShape(16.dp)) {
                    Text(error, color = Error, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("1. ESPECIALISTA", style = MaterialTheme.typography.labelSmall, color = PremiumGold, fontWeight = FontWeight.Black)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
                    items(nutricionistas) { nut ->
                        FilterChip(
                            selected = selectedNutId == nut.id,
                            onClick = { selectedNutId = nut.id },
                            label = { Text("${nut.firstName} ${nut.lastName}") },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ForestGreen, selectedLabelColor = Color.White)
                        )
                    }
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("2. PLAN NUTRICIONAL", style = MaterialTheme.typography.labelSmall, color = PremiumGold, fontWeight = FontWeight.Black)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
                    items(planes) { plan ->
                        FilterChip(
                            selected = selectedPlanId == plan.id,
                            onClick = { selectedPlanId = plan.id },
                            label = { Text(plan.title ?: "Plan") },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ForestGreen, selectedLabelColor = Color.White)
                        )
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.weight(1f).height(52.dp), shape = RoundedCornerShape(12.dp)) { Text(selectedDate?.toString() ?: "FECHA", fontWeight = FontWeight.Bold) }
                OutlinedButton(onClick = { showTimePicker = true }, modifier = Modifier.weight(1f).height(52.dp), shape = RoundedCornerShape(12.dp)) { Text(selectedTime?.toString() ?: "HORA", fontWeight = FontWeight.Bold) }
            }
            Button(
                onClick = {
                    if (selectedNutId != null && selectedPlanId != null && selectedDate != null && selectedTime != null) {
                        val dt = LocalDateTime.of(selectedDate, selectedTime)
                        onConfirm(selectedNutId!!, selectedPlanId!!, dt.format(DateTimeFormatter.ISO_DATE_TIME))
                    }
                },
                enabled = selectedNutId != null && selectedPlanId != null && selectedDate != null && selectedTime != null && !isBooking,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                if (isBooking) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("RESERVAR AHORA", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun EmptyConsultasStatePro(onAgendar: () -> Unit, isNutri: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Border.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(80.dp).background(PrimaryGreen.copy(alpha = 0.05f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.EventBusy, null, tint = PrimaryGreen, modifier = Modifier.size(40.dp))
            }
            Spacer(Modifier.height(24.dp))
            Text(
                if (isNutri) "No tienes citas hoy" else "Sin citas agendadas", 
                style = MaterialTheme.typography.titleMedium, 
                fontWeight = FontWeight.Black, 
                color = TextPrimary
            )
            Text(
                if (isNutri) "Relájate, avisaremos cuando un paciente reserve." else "Agenda una cita con nuestros expertos para comenzar.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (!isNutri) {
                Spacer(Modifier.height(28.dp))
                Button(
                    onClick = onAgendar,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text("AGENDAR MI PRIMERA CITA", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
