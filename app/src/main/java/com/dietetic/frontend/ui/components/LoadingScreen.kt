package com.dietetic.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dietetic.frontend.ui.theme.*

@Composable
fun LoadingScreen(message: String = "Cargando...") {
    Box(
        modifier          = Modifier.fillMaxSize().background(Background),
        contentAlignment  = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = GoldPrimary, strokeWidth = 3.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text      = message,
                color     = TextSecondary,
                style     = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: (() -> Unit)? = null) {
    val authViewModel: com.dietetic.frontend.ui.viewmodel.AuthViewModel = hiltViewModel()

    Box(
        modifier          = Modifier.fillMaxSize().background(Background),
        contentAlignment  = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.padding(24.dp),
        ) {
            Text("⚠️", style = MaterialTheme.typography.displayMedium)
            Spacer(Modifier.height(12.dp))
            Text(
                text      = "Algo salió mal",
                color     = TextPrimary,
                style     = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text  = message,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(24.dp))
            
            if (onRetry != null) {
                DieteticButton(text = "Reintentar", onClick = onRetry, modifier = Modifier.width(200.dp))
                Spacer(Modifier.height(12.dp))
            }

            // Forced logout for 401 errors
            OutlinedButton(
                onClick = { authViewModel.logout() },
                modifier = Modifier.width(200.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}
