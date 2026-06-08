package com.dietetic.frontend.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dietetic.frontend.domain.model.Course
import com.dietetic.frontend.ui.theme.*

@Composable
fun CompleteProfileCard(onComplete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ForestGreen)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 24.sp)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Completa tu perfil", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Añade tus medidas corporales", color = PremiumGold, style = MaterialTheme.typography.labelSmall)
            }
            Button(
                onClick = onComplete,
                colors = ButtonDefaults.buttonColors(containerColor = PremiumGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ir", fontWeight = FontWeight.Black, color = ForestGreen)
            }
        }
    }
}

@Composable
fun ModernCourseCard(course: Course, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val goal = course.goal ?: "saludable"
    
    val (emoji, gradient) = when(goal.lowercase()) {
        "pérdida de peso" -> "⚖️" to listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
        "masa muscular" -> "💪" to listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
        "vegano" -> "🥦" to listOf(Color(0xFFF1F8E9), Color(0xFFDCEDC8))
        else -> "🥗" to listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 72.sp)
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                    color = ForestGreen,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PremiumGold)
                ) {
                    Text(
                        "${course.targetCalories ?: 2000} KCAL",
                        color = PremiumGold,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    goal.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = PremiumGold,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    course.title ?: "Plan Nutricional",
                    style = MaterialTheme.typography.titleLarge,
                    color = ForestGreen,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.Timer, null, modifier = Modifier.size(16.dp), tint = TextFaint)
                        Text("${course.durationWeeks ?: 4} SEM", style = MaterialTheme.typography.labelSmall, color = TextFaint)
                    }
                    Text(
                        "$${"%.2f".format(course.price ?: 0.0)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = BrassDark
                    )
                }
            }
        }
    }
}
