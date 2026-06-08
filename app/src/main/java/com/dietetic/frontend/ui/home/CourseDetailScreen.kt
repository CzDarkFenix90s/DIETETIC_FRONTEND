package com.dietetic.frontend.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dietetic.frontend.domain.model.Course
import com.dietetic.frontend.ui.components.ErrorScreen
import com.dietetic.frontend.ui.components.LoadingScreen
import com.dietetic.frontend.ui.theme.*
import com.dietetic.frontend.ui.viewmodel.CartViewModel
import com.dietetic.frontend.ui.viewmodel.CourseDetailUiState
import com.dietetic.frontend.ui.viewmodel.CourseDetailViewModel
import kotlinx.coroutines.delay

@Composable
fun CourseDetailScreen(
    courseId: Int,
    onBack: () -> Unit,
    onOpenCart: () -> Unit = {},
    cartViewModel: CartViewModel,
    viewModel: CourseDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val cartCount by cartViewModel.totalItems.collectAsState()

    LaunchedEffect(courseId) { viewModel.load(courseId) }

    when (val s = state) {
        is CourseDetailUiState.Loading -> LoadingScreen("Cargando detalles del plan...")
        is CourseDetailUiState.Error -> ErrorScreen(s.message, onRetry = { viewModel.load(courseId) })
        is CourseDetailUiState.Success -> CourseDetailContent(
            course = s.course,
            modules = s.modules,
            onBack = onBack,
            cartViewModel = cartViewModel,
            cartCount = cartCount,
            onOpenCart = onOpenCart
        )
    }
}

@Composable
private fun RecommendationItem(module: com.dietetic.frontend.domain.model.Module, index: Int) {
    val mealIcon = when ((module.mealType ?: "").lowercase()) {
        "breakfast", "desayuno" -> "🍳"
        "lunch", "almuerzo" -> "🥗"
        "dinner", "cena" -> "🍲"
        "snack", "merienda" -> "🍎"
        else -> "🍴"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Border.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp).background(PrimaryGreen.copy(alpha = 0.08f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(mealIcon, fontSize = 26.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = module.title ?: "Alimento Sugerido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        (module.mealType ?: "Dieta").uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.size(3.dp).background(TextFaint, CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${module.portionGrams ?: 0.0}g sugeridos",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseDetailContent(
    course: Course,
    modules: List<com.dietetic.frontend.domain.model.Module>,
    onBack: () -> Unit,
    cartViewModel: CartViewModel,
    cartCount: Int,
    onOpenCart: () -> Unit
) {
    var added by remember { mutableStateOf(false) }
    val subtotal = (course.price ?: 0.0)

    val goal = course.goal ?: "saludable"
    val (flag, heroBg) = when (goal.lowercase()) {
        "pérdida de peso" -> "⚖️" to listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
        "masa muscular" -> "💪" to listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
        "saludable" -> "🥗" to listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
        "vegano" -> "🥦" to listOf(Color(0xFFF1F8E9), Color(0xFFDCEDC8))
        else -> "🍎" to listOf(Color(0xFFF9FBE7), Color(0xFFF0F4C3))
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (course.isActive == true) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Surface,
                    shadowElevation = 16.dp,
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.navigationBarsPadding().padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Inversión en Salud", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Text("$${"%.2f".format(subtotal)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = TextPrimary)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { 
                                cartViewModel.addItem(course, 1)
                                onOpenCart() 
                            },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                        ) {
                            Icon(Icons.Default.ShoppingCart, null)
                            Spacer(Modifier.width(12.dp))
                            Text("CONTRATAR PLAN AHORA", fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Icon Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(heroBg)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(flag, fontSize = 120.sp)
                }
                
                // Overlay buttons
                Row(
                    modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.background(Color.White.copy(alpha = 0.8f), CircleShape)
                    ) { Icon(Icons.Default.ArrowBack, null) }
                    
                    IconButton(
                        onClick = onOpenCart,
                        modifier = Modifier.background(Color.White.copy(alpha = 0.8f), CircleShape)
                    ) {
                        BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                            Icon(Icons.Default.ShoppingCart, null)
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(24.dp)) {
                // Goal Chip
                Surface(
                    color = PrimaryGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        goal.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(Modifier.height(12.dp))
                
                Text(
                    course.title ?: "Plan Nutricional",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )

                Spacer(Modifier.height(16.dp))

                // Stats Row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DetailStat(Icons.Outlined.Whatshot, "${course.targetCalories ?: 2000}", "kcal", Warning)
                    DetailStat(Icons.Outlined.Timer, "${course.durationWeeks ?: 4}", "sem", PrimaryBlue)
                    DetailStat(Icons.Default.Star, "Premium", "Calidad", Color(0xFFFFD700))
                }

                Spacer(Modifier.height(24.dp))
                Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                Text(
                    course.description ?: "Este plan incluye un seguimiento detallado de tus comidas diarias diseñadas por expertos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    lineHeight = 22.sp
                )

                Spacer(Modifier.height(32.dp))
                Text("RECOMENDACIONES NUTRICIONALES", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = PrimaryGreen)
                Spacer(Modifier.height(16.dp))
                
                if (modules.isEmpty()) {
                    Box(Modifier.fillMaxWidth().height(100.dp).background(Background, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                        Text("Cargando recomendaciones...", style = MaterialTheme.typography.bodySmall, color = TextFaint)
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        modules.forEachIndexed { index, module ->
                            RecommendationItem(module, index)
                        }
                    }
                }
                
                Spacer(Modifier.height(40.dp))
            }
        }
    }
    
    LaunchedEffect(added) {
        if (added) { delay(2000); added = false }
    }
}

@Composable
private fun DetailStat(icon: ImageVector, value: String, unit: String, color: Color) {
    Surface(
        modifier = Modifier.height(60.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Border.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Column {
                Text(value, fontWeight = FontWeight.Black, fontSize = 14.sp)
                Text(unit, style = MaterialTheme.typography.labelSmall, color = TextFaint)
            }
        }
    }
}
