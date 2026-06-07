package com.dietetic.frontend.ui.profile

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dietetic.frontend.ui.viewmodel.AuthViewModel
import com.dietetic.frontend.ui.viewmodel.ProfileViewModel
import com.dietetic.frontend.ui.theme.*
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onNavigateToPremium: () -> Unit = {}
) {
    val user by authViewModel.currentUser.collectAsState()
    val profileState by profileViewModel.uiState.collectAsState()
    val paciente = profileState.paciente

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        // Fondo decorativo superior con textura orgánica simulada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(ForestGreen.copy(alpha = 0.08f), Background)
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            "PERFIL PRO", 
                            style = MaterialTheme.typography.titleMedium, 
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp,
                            color = ForestGreen
                        ) 
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
                    actions = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.padding(end = 8.dp).background(Color.White.copy(alpha = 0.5f), CircleShape)
                        ) { Icon(Icons.Default.Settings, null, tint = ForestGreen, modifier = Modifier.size(20.dp)) }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // SECCIÓN AVATAR VIP
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            // Anillo metálico dorado
                            Surface(
                                modifier = Modifier.size(134.dp),
                                shape = CircleShape,
                                color = Color.Transparent,
                                border = BorderStroke(3.dp, Brush.sweepGradient(GoldGradient))
                            ) {}
                            
                            // Círculo de Madera Grabada
                            Surface(
                                modifier = Modifier.size(120.dp),
                                shape = CircleShape,
                                color = Color(0xFFB38B59), // Madera base
                                shadowElevation = 16.dp
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().background(
                                        Brush.radialGradient(listOf(Color(0xFFD7BA89), Color(0xFF8B6B42)))
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = user?.username?.take(1)?.uppercase() ?: "U",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = Color(0xFF4E3629),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 48.sp
                                    )
                                }
                            }
                            
                            // Badge de Verificado
                            Surface(
                                modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-8).dp, y = (-8).dp),
                                color = ForestGreen,
                                shape = CircleShape,
                                border = BorderStroke(2.dp, Color.White)
                            ) {
                                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.padding(4.dp).size(16.dp))
                            }
                        }
                        
                        Spacer(Modifier.height(20.dp))
                        Text(
                            user?.username?.uppercase() ?: "USUARIO DIETETIC",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = ForestGreen
                        )
                        Text(
                            user?.email ?: "Sin correo vinculado",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextFaint,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(Modifier.height(16.dp))
                        
                        Surface(
                            color = ForestGreen.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.1f))
                        ) {
                            Text(
                                paciente?.status?.uppercase() ?: "MIEMBRO ACTIVO",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = ForestGreen,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                // SECCIÓN DE ESTADO DE SALUD DINÁMICO
                item {
                    val hasStats = (paciente?.currentWeight ?: 0.0) > 0
                    if (hasStats) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(32.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            border = BorderStroke(1.dp, Border.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(24.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProfileStatCol("PESO", "${paciente?.currentWeight ?: 0.0}", "kg", Icons.Default.MonitorWeight)
                                Box(Modifier.width(1.dp).height(40.dp).background(Border.copy(alpha = 0.3f)))
                                ProfileStatCol("ALTURA", "${paciente?.heightCm ?: 0.0}", "cm", Icons.Default.Straighten)
                                Box(Modifier.width(1.dp).height(40.dp).background(Border.copy(alpha = 0.3f)))
                                ProfileStatCol("EDAD", "${paciente?.age ?: 0}", "años", Icons.Default.HourglassEmpty)
                            }
                        }
                    } else {
                        // Tarjeta de "Tu Progreso" para llenar el vacío cuando no hay medidas
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(32.dp),
                            color = ForestGreen,
                            shadowElevation = 8.dp
                        ) {
                            Column(modifier = Modifier.padding(28.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier.size(48.dp).background(Color.White.copy(alpha = 0.1f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Analytics, null, tint = PremiumGold, modifier = Modifier.size(24.dp))
                                    }
                                    Spacer(Modifier.width(16.dp))
                                    Text("Análisis de Salud", color = Color.White, style = MaterialTheme.typography.titleMedium)
                                }
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    "Aún no hemos calculado tu IMC y metas calóricas. Registra tus medidas para desbloquear tu pasaporte de salud.",
                                    color = Color.White.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(Modifier.height(20.dp))
                                Button(
                                    onClick = { /* Abrir editor */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = PremiumGold),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("CONFIGURAR MI PERFIL MÉDICO", fontWeight = FontWeight.Black, color = ForestGreen, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                // NUEVA SECCIÓN: OBJETIVOS DEL PLAN ACTUAL
                item {
                    Column {
                        Text("META DE MI PLAN", style = MaterialTheme.typography.labelLarge, color = ForestGreen, letterSpacing = 1.sp)
                        Spacer(Modifier.height(12.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Border.copy(alpha = 0.5f))
                        ) {
                            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).background(PremiumGold, CircleShape))
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    paciente?.goal?.uppercase() ?: "RECOMENDACIÓN NUTRICIONAL ACTIVA",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ForestGreen
                                )
                            }
                        }
                    }
                }

                // BANNER PREMIUM LUJO (MÁRMOL NEGRO)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF1A1A1A), Color(0xFF000000))))
                            .clickable { onNavigateToPremium() }
                    ) {
                        // Brillo sutil de mármol
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.05f), Color.Transparent))
                            )
                        )
                        
                        Row(
                            modifier = Modifier.padding(28.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(56.dp).background(PremiumGold.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Star, null, tint = PremiumGold, modifier = Modifier.size(30.dp))
                            }
                            Spacer(Modifier.width(20.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "Dietetic Premium",
                                    color = PremiumGold,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    "TU ACCESO EXCLUSIVO",
                                    color = Color.White.copy(alpha = 0.5f),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, tint = PremiumGold, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // MENÚ DE OPCIONES ESTILO BOUTIQUE
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.dp, Border.copy(alpha = 0.2f))
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            ProfileMenuItemPro(Icons.Outlined.Person, "Editar Información")
                            ProfileMenuItemPro(Icons.Outlined.History, "Historial de Planes")
                            ProfileMenuItemPro(Icons.Outlined.Shield, "Privacidad y Seguridad")
                            ProfileMenuItemPro(Icons.Outlined.Info, "Centro de Ayuda")
                            
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                color = Border.copy(alpha = 0.4f)
                            )
                            
                            ProfileMenuItemPro(
                                icon = Icons.AutoMirrored.Filled.Logout,
                                title = "Cerrar Sesión",
                                color = Error,
                                onClick = onLogout
                            )
                        }
                    }
                }
                
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
private fun ProfileStatCol(label: String, value: String, unit: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = PremiumGold, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = ForestGreen)
            Text(" $unit", style = MaterialTheme.typography.labelSmall, color = ForestGreen.copy(alpha = 0.6f))
        }
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextFaint, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProfileMenuItemPro(
    icon: ImageVector,
    title: String,
    color: Color = ForestGreen,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(
                if (color == Error) Error.copy(alpha = 0.1f) else PremiumGold.copy(alpha = 0.08f),
                CircleShape
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = if (color == Error) Error else PremiumGold, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(20.dp))
        Text(
            title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = if (color == Error) Error else ForestGreen,
            fontWeight = FontWeight.Bold
        )
        Icon(
            Icons.AutoMirrored.Filled.ArrowForwardIos,
            null,
            tint = TextFaint.copy(alpha = 0.5f),
            modifier = Modifier.size(12.dp)
        )
    }
}
