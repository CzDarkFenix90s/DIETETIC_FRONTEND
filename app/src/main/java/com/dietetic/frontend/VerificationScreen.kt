package com.dietetic.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dietetic.frontend.ui.theme.*

@Composable
fun VerificationScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToAdmin: () -> Unit = {}
) {
    val blueGradient = Brush.linearGradient(listOf(PrimaryBlue, DarkBlue))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(



                BackgroundColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(40.dp))
            
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(blueGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.VerifiedUser, null, tint = Color.White, modifier = Modifier.size(40.dp))
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "DIETETIC UTE",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
            )

            Text(
                text = "Módulo de Verificación del Sistema",
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryBlue,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(32.dp))

            // Backend Status
            StatusCard(
                title = "Estado del Backend",
                icon = Icons.Default.Dns,
                items = listOf(
                    "API Base" to "http://10.0.2.2:8000/api/",
                    "Auth Service" to "Conectado",
                    "Database" to "Online",
                    "Version" to "v1.2.4"
                )
            )

            Spacer(Modifier.height(16.dp))

            // Entities Status
            StatusCard(
                title = "Modelos de Dominio",
                icon = Icons.AutoMirrored.Filled.FactCheck,
                items = listOf(
                    "Autenticación" to "Activo",
                    "Paciente" to "Activo",
                    "Nutricionista" to "Activo",
                    "Plan Nutricional" to "Activo",
                    "Consulta Dietética" to "Activo",
                    "Alimentos" to "Activo"
                ),
                showCheckmarks = true
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Accesos Directos",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(start = 4.dp, bottom = 12.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ShortcutButton(
                    modifier = Modifier.weight(1f),
                    title = "App Cliente",
                    icon = Icons.Default.Smartphone,
                    color = PrimaryBlue,
                    onClick = onNavigateToHome
                )
                ShortcutButton(
                    modifier = Modifier.weight(1f),
                    title = "App Admin",
                    icon = Icons.Default.AdminPanelSettings,
                    color = DarkBlue,
                    onClick = onNavigateToAdmin
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun StatusCard(
    title: String,
    icon: ImageVector,
    items: List<Pair<String, String>>,
    showCheckmarks: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9ECEF)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
            }
            
            Spacer(Modifier.height(16.dp))
            
            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (showCheckmarks) {
                            Icon(Icons.Default.CheckCircle, null, tint = Success, modifier = Modifier.size(14.dp).padding(end = 4.dp))
                        }
                        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
                if (items.last() != (label to value)) {
                    HorizontalDivider(color = Color(0xFFF8F9FA))
                }
            }
        }
    }
}

@Composable
fun ShortcutButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9ECEF)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, color = TextPrimary, fontWeight = FontWeight.Black)
        }
    }
}
