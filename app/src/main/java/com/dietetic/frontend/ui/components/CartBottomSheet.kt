package com.dietetic.frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dietetic.frontend.ui.theme.*
import com.dietetic.frontend.ui.viewmodel.CartItem
import com.dietetic.frontend.ui.viewmodel.CartViewModel
import com.dietetic.frontend.ui.viewmodel.CheckoutState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    onDismiss: () -> Unit,
    cartViewModel: CartViewModel,
    isAuthenticated: Boolean,
    onLoginRequired: () -> Unit,
    onOrderSuccess: (Int) -> Unit = {}
) {
    val items by cartViewModel.items.collectAsState()
    val subtotal by cartViewModel.subtotal.collectAsState()
    val tax by cartViewModel.tax.collectAsState()
    val total by cartViewModel.totalWithTax.collectAsState()
    val checkoutState by cartViewModel.checkoutState.collectAsState()

    // Navegar al éxito cuando el checkout termina
    LaunchedEffect(checkoutState) {
        if (checkoutState is CheckoutState.Success) {
            onOrderSuccess((checkoutState as CheckoutState.Success).orderId)
            // No reseteamos aquí para que el componente Success se muestre
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Background,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Border) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // ── Header ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Mi carrito",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                    )
                    if (items.isNotEmpty()) {
                        Text(
                            text = "${items.size} plan${if (items.size != 1) "es" else ""} seleccionado${if (items.size != 1) "s" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                        )
                    }
                }
                if (items.isNotEmpty() && checkoutState is CheckoutState.Idle) {
                    IconButton(onClick = cartViewModel::clearCart) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Vaciar", tint = Error)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Border.copy(alpha = 0.5f))

            // ── Contenido según estado ────────────────────────
            if (items.isEmpty() && checkoutState !is CheckoutState.Success) {
                EmptyCart(onDismiss)
            } else {
                when (val state = checkoutState) {
                    is CheckoutState.Loading -> CheckoutLoading()
                    is CheckoutState.Success -> CheckoutSuccess(state.orderId, onDismiss)
                    is CheckoutState.Error -> CheckoutError(state.message, cartViewModel::resetCheckout)
                    else -> CartContent(
                        items = items,
                        subtotal = subtotal,
                        tax = tax,
                        total = total,
                        cartViewModel = cartViewModel,
                        isAuthenticated = isAuthenticated,
                        onLoginRequired = onLoginRequired,
                        onCheckout = { cartViewModel.checkout() }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartContent(
    items: List<CartItem>,
    subtotal: Double,
    tax: Double,
    total: Double,
    cartViewModel: CartViewModel,
    isAuthenticated: Boolean,
    onLoginRequired: () -> Unit,
    onCheckout: () -> Unit
) {
    Column {
        LazyColumn(
            modifier = Modifier
                .weight(1f, fill = false)
                .heightIn(max = 400.dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(items, key = { it.course.id }) { item ->
                CartItemRow(item, cartViewModel)
            }
        }

        HorizontalDivider(color = Border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 16.dp))

        // Resumen de Totales con estilo Premium
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TotalRow(label = "Subtotal", value = "$${"%.2f".format(subtotal)}", isFinal = false)
            TotalRow(label = "IVA (15%)", value = "$${"%.2f".format(tax)}", isFinal = false)
            
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = Border.copy(alpha = 0.5f))
            Spacer(Modifier.height(8.dp))
            
            TotalRow(label = "Total", value = "$${"%.2f".format(total)}", isFinal = true)
        }

        Spacer(Modifier.height(24.dp))

        // Aviso de autenticación si no está logueado
        if (!isAuthenticated) {
            Surface(
                color = GoldPrimary.copy(alpha = 0.08f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Inicia sesión para contratar tu plan",
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldPrimary
                    )
                }
            }
        }

        Button(
            onClick = {
                if (!isAuthenticated) onLoginRequired()
                else onCheckout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = DeepDark
            )
        ) {
            if (!isAuthenticated) {
                Text("Iniciar sesión para comprar", fontWeight = FontWeight.ExtraBold)
            } else {
                Icon(Icons.Default.CheckCircle, null, Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text("Confirmar Contratación — $${"%.2f".format(total)}", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
private fun TotalRow(label: String, value: String, isFinal: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = if (isFinal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isFinal) FontWeight.Bold else FontWeight.Normal,
            color = if (isFinal) TextPrimary else TextSecondary,
        )
        Text(
            text = value,
            style = if (isFinal) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isFinal) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = if (isFinal) GoldPrimary else TextPrimary,
        )
    }
}

@Composable
private fun CartItemRow(item: CartItem, viewModel: CartViewModel) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail / Icono
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(GoldPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when((item.course.goal ?: "").lowercase()) {
                        "pérdida de peso" -> "⚖️"
                        "masa muscular" -> "💪"
                        "saludable" -> "🥗"
                        "vegano" -> "🥦"
                        "diabetico" -> "🩸"
                        else -> "🍎"
                    }, 
                    fontSize = 24.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.course.title ?: "Plan Nutricional",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                Text(
                    text = "$${"%.2f".format(item.course.price ?: 0.0)} / cupo",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            IconButton(onClick = { viewModel.removeItem(item.course.id) }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.DeleteOutline, null, Modifier.size(20.dp), tint = Error.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun EmptyCart(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🛒", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            "Tu carrito está vacío",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            "¡Contrata un plan para comenzar!",
            textAlign = TextAlign.Center,
            color = TextSecondary
        )
        Spacer(Modifier.height(32.dp))
        DieteticButton(text = "Explorar Planes", onClick = onDismiss)
    }
}

@Composable
private fun CheckoutLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = PrimaryGreen, strokeWidth = 4.dp)
        Spacer(Modifier.height(24.dp))
        Text(
            "Procesando tu contratación...",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CheckoutSuccess(orderId: Int, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = Success.copy(alpha = 0.1f),
            shape = CircleShape,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Check, null, Modifier.size(48.dp), tint = Success)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "¡Contratación Exitosa!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = TextPrimary
        )
        Text(
            "Su plan se enviará a su correo electrónico (Gmail). Esté atento a su bandeja de entrada.",
            textAlign = TextAlign.Center,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(32.dp))
        DieteticButton(text = "Volver al inicio", onClick = onDismiss)
    }
}

@Composable
private fun CheckoutError(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("❌", fontSize = 48.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            "Hubo un problema",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Error
        )
        Text(
            message,
            textAlign = TextAlign.Center,
            color = TextSecondary
        )
        Spacer(Modifier.height(32.dp))
        DieteticButton(text = "Intentar de nuevo", onClick = onRetry)
    }
}
