package com.dietetic.frontend.ui.profile
 
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dietetic.frontend.ui.theme.*

@Composable
fun PremiumScreen(
    onBack: () -> Unit,
    onSubscribe: (String) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            PremiumTopBar(onBack = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                PremiumHero()
            }

            item {
                Spacer(Modifier.height(32.dp))
                Text(
                    "Elige tu plan",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(Modifier.height(16.dp))
            }

            item {
                PlanCard(
                    title = "Mensual",
                    price = "$9.99",
                    period = "/ mes",
                    features = listOf("Acceso a todos los planes", "Sin anuncios", "Guías digitales"),
                    isPopular = false,
                    onClick = { onSubscribe("monthly") }
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
                PlanCard(
                    title = "Anual",
                    price = "$79.99",
                    period = "/ año",
                    features = listOf("Acceso a todos los planes", "Sin anuncios", "Guías digitales", "Soporte prioritario", "2 meses gratis"),
                    isPopular = true,
                    onClick = { onSubscribe("yearly") }
                )
            }

            item {
                Spacer(Modifier.height(48.dp))
                Text(
                    "Historial de Transacciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(16.dp))
            }

            // Mock Transactions
            val transactions = listOf(
                Transaction("1", "Plan Mensual", "31 May 2026", "$9.99"),
                Transaction("2", "Plan Pérdida de Peso", "15 May 2026", "$25.00")
            )

            items(transactions) { tx ->
                TransactionRow(tx)
            }
        }
    }
}

@Composable
private fun PremiumHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(PrimaryBlue, DarkBlue)))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(12.dp))
            Text(
                "Desbloquea tu potencial",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                "Come saludable sin límites con DIETETIC Premium",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PremiumTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFF1A1A1A).copy(alpha = 0.05f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, 
                    contentDescription = "Regresar",
                    tint = Color(0xFF1A1A1A),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "DIETETIC Premium",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "MEMBRESÍA EXCLUSIVA",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF0052CC),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            }
        }
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    period: String,
    features: List<String>,
    isPopular: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(if (isPopular) 2.dp else 1.dp, if (isPopular) PrimaryBlue else PrimaryBlue.copy(alpha = 0.2f)),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            if (isPopular) {
                Surface(
                    color = PrimaryBlue,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        "MÁS POPULAR",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(price, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = PrimaryBlue)
                Text(period, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = PrimaryBlue.copy(alpha = 0.1f))
            Spacer(Modifier.height(16.dp))

            features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Icon(Icons.Default.Check, null, tint = Success, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(feature, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPopular) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    contentColor = if (isPopular) Color.White else PrimaryBlue
                )
            ) {
                Text("Seleccionar Plan", fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class Transaction(val id: String, val title: String, val date: String, val amount: String)

@Composable
private fun TransactionRow(tx: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(tx.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(tx.date, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
        Text(tx.amount, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Black, color = Success)
    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 24.dp),
        color = Border.copy(alpha = 0.5f)
    )
}
