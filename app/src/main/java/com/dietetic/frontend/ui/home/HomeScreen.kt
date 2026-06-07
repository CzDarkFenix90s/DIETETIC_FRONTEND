package com.dietetic.frontend.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dietetic.frontend.ui.viewmodel.HomeViewModel
import com.dietetic.frontend.ui.viewmodel.CartViewModel
import com.dietetic.frontend.ui.home.components.*
import com.dietetic.frontend.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onCourseClick: (Int) -> Unit = {},
    onCatalogClick: () -> Unit = {},
    onDietClick: () -> Unit = {},
    onOpenCart: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val cartCount by cartViewModel.totalItems.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            HomeTopBar(
                cartCount = cartCount,
                onOpenCart = onOpenCart,
                onLogout = onLogout,
                onRefresh = { viewModel.loadHomeData() }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading && uiState.recommendedPlanes.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ForestGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // Dashboard Nutricional (Objetivos Diarios)
                item {
                    val activePlan = uiState.recommendedPlanes.firstOrNull()
                    DailyNutritionBar(
                        calories = activePlan?.targetCalories ?: 2000,
                        waterGoal = 2.5,
                        mealsCount = uiState.dailyMeals.size
                    )
                }

                // Banner de Prioridad (Textura Tejido Orgánico)
                item { 
                    HeroBannerCard(
                        userName = uiState.userName,
                        onCatalogClick = onCatalogClick 
                    ) 
                }

                // Mi Dieta de Hoy (Recomendación Inteligente)
                if (uiState.dailyMeals.isNotEmpty()) {
                    item {
                        HomeSectionHeader(
                            title = "Recomendación Diaria",
                            subtitle = "Plan nutricional sugerido para ti",
                            onSeeAll = onDietClick
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(uiState.dailyMeals) { meal ->
                                PhotoMealCard(meal)
                            }
                        }
                    }
                }

                // Recomendación de Planes (Lifestyle Photos)
                item {
                    HomeSectionHeader(
                        title = "Sugerencias Pro",
                        subtitle = "Planes nutricionales de élite",
                        onSeeAll = onCatalogClick
                    )
                }

                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        uiState.recommendedPlanes.take(3).forEach { plan ->
                            ModernCourseCard(
                                course = plan,
                                onClick = { onCourseClick(plan.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoMealCard(meal: com.dietetic.frontend.domain.model.Module) {
    val mealType = meal.mealType ?: "snack"
    // URL de ejemplo fotorrealista basada en el tipo de comida
    val imageUrl = when(mealType.lowercase()) {
        "desayuno" -> "https://images.unsplash.com/photo-1525351484163-7529414344d8?q=80&w=500&auto=format&fit=crop"
        "almuerzo" -> "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=500&auto=format&fit=crop"
        "cena" -> "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?q=80&w=500&auto=format&fit=crop"
        else -> "https://images.unsplash.com/photo-1511690656952-34342bb7c2f2?q=80&w=500&auto=format&fit=crop"
    }

    Surface(
        modifier = Modifier.width(220.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column {
            Box(modifier = Modifier.height(140.dp).fillMaxWidth()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                )
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopEnd),
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Search, null, tint = PremiumGold, modifier = Modifier.padding(6.dp).size(16.dp))
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(mealType.uppercase(), style = MaterialTheme.typography.labelSmall, color = PremiumGold)
                Text(meal.title ?: "Alimento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                Text("${meal.portionGrams ?: 0.0}g sugeridos", style = MaterialTheme.typography.bodySmall, color = TextFaint)
            }
        }
    }
}

@Composable
private fun DailyNutritionBar(calories: Int, waterGoal: Double, mealsCount: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 10.dp,
        border = BorderStroke(1.dp, Brush.linearGradient(MetalGradient))
    ) {
        Row(
            modifier = Modifier.padding(vertical = 28.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(icon = Icons.Default.Whatshot, color = Color(0xFFFF5722), value = "$calories", label = "Meta Kcal")
            Box(Modifier.width(1.dp).height(40.dp).background(Border.copy(alpha = 0.2f)))
            StatItem(icon = Icons.Default.WaterDrop, color = Color(0xFF2196F3), value = "$waterGoal", label = "Agua (L)")
            Box(Modifier.width(1.dp).height(40.dp).background(Border.copy(alpha = 0.2f)))
            StatItem(icon = Icons.Default.Restaurant, color = ForestGreen, value = "$mealsCount", label = "Comidas")
        }
    }
}

@Composable
private fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(8.dp))
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = TextPrimary)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextFaint)
    }
}

@Composable
private fun HeroBannerCard(userName: String, onCatalogClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Brush.radialGradient(listOf(ForestGreen, Color(0xFF0D1B13))))
            .clickable { onCatalogClick() }
    ) {
        Column(modifier = Modifier.padding(32.dp)) {
            Text("HOLA, $userName 👋", color = PremiumGold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text("Tu Salud, Nuestra\nPrioridad", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Spacer(Modifier.height(28.dp))
            Surface(
                color = ForestGreen,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, PremiumGold)
            ) {
                Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("EXPLORAR PLANES", color = Color.White, style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.Eco, null, tint = PremiumGold, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar(cartCount: Int, onOpenCart: () -> Unit, onLogout: () -> Unit, onRefresh: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = Background, shadowElevation = 0.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Logo grabado en madera
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFB38B59),
                    border = BorderStroke(1.dp, Color(0xFF8B6B42))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("D", fontWeight = FontWeight.Black, color = Color(0xFF4E3629), fontSize = 22.sp)
                    }
                }
                Spacer(Modifier.width(14.dp))
                Text("DIETETIC", style = MaterialTheme.typography.titleLarge, color = ForestGreen, letterSpacing = 1.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, null, tint = TextFaint, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onOpenCart) {
                    BadgedBox(badge = { if (cartCount > 0) Badge(containerColor = Error) { Text("$cartCount") } }) {
                        Icon(Icons.Default.ShoppingBag, null, tint = ForestGreen, modifier = Modifier.size(22.dp))
                    }
                }
                IconButton(onClick = onLogout) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = TextFaint.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun HomeSectionHeader(title: String, subtitle: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge, color = ForestGreen)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextFaint)
        }
        TextButton(onClick = onSeeAll) {
            Text("VER TODO", color = PremiumGold, style = MaterialTheme.typography.labelSmall)
        }
    }
}
