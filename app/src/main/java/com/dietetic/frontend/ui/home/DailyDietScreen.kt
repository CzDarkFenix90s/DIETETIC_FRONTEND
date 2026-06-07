package com.dietetic.frontend.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dietetic.frontend.ui.theme.*
import com.dietetic.frontend.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyDietScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MI DIETA DIARIA", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Surface),
                actions = {
                    IconButton(onClick = { viewModel.loadHomeData() }) {
                        Icon(Icons.Default.Refresh, "Actualizar")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading && uiState.dailyMeals.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else if (uiState.dailyMeals.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🍎", fontSize = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("No tienes alimentos programados", fontWeight = FontWeight.Bold, color = TextSecondary)
                    Text("Contrata un plan para ver tu dieta aquí", style = MaterialTheme.typography.bodySmall, color = TextFaint)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Alimentos para hoy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Text("Sigue las recomendaciones de tu nutricionista", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Spacer(Modifier.height(8.dp))
                }
                items(uiState.dailyMeals) { meal ->
                    DetailedMealCard(meal)
                }
            }
        }
    }
}

@Composable
fun DetailedMealCard(meal: com.dietetic.frontend.domain.model.Module) {
    val mealType = meal.mealType ?: "snack"
    val mealIcon = when (mealType.lowercase()) {
        "breakfast", "desayuno" -> "🍳"
        "lunch", "almuerzo" -> "🥗"
        "dinner", "cena" -> "🍲"
        "snack", "merienda" -> "🍎"
        else -> "🍴"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).background(PrimaryGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Text(mealIcon, fontSize = 24.sp)
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(mealType.uppercase(), style = MaterialTheme.typography.labelSmall, color = PrimaryGreen, fontWeight = FontWeight.Bold)
                    Text(meal.title ?: "Alimento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            if (!meal.description.isNullOrBlank()) {
                Text(meal.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(12.dp))
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = PrimaryGreen.copy(alpha = 0.08f), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        "${meal.portionGrams ?: 0.0}g sugeridos",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text("Plan: ${meal.courseTitle ?: "General"}", style = MaterialTheme.typography.labelSmall, color = TextFaint)
            }
        }
    }
}
