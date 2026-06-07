package com.dietetic.frontend.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dietetic.frontend.ui.theme.PrimaryGreen
import com.dietetic.frontend.ui.theme.Surface

@Composable
fun DieteticButton(
    text:      String,
    onClick:   () -> Unit,
    modifier:  Modifier = Modifier,
    isLoading: Boolean  = false,
    enabled:   Boolean  = true,
) {
    Button(
        onClick  = onClick,
        enabled  = enabled && !isLoading,
        modifier = modifier.fillMaxWidth().height(56.dp),
        colors   = ButtonDefaults.buttonColors(
            containerColor         = PrimaryGreen,
            contentColor           = Surface,
            disabledContainerColor = PrimaryGreen.copy(alpha = 0.5f),
            disabledContentColor   = Surface.copy(alpha = 0.5f),
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 0.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color     = Surface,
                modifier  = Modifier.size(24.dp),
                strokeWidth = 3.dp,
            )
            Spacer(Modifier.width(12.dp))
        }
        Text(
            text = if (isLoading) "Cargando..." else text, 
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
