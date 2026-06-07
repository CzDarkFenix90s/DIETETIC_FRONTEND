package com.dietetic.frontend.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.ShoppingCart
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.dietetic.frontend.ui.viewmodel.CartViewModel
import com.dietetic.frontend.ui.viewmodel.CatalogViewModel
import com.dietetic.frontend.ui.home.components.ModernCourseCard
import com.dietetic.frontend.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onCourseClick: (Int) -> Unit,
    onOpenCart: () -> Unit = {},
    viewModel: CatalogViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val gridState = rememberLazyGridState()
    val cartCount by cartViewModel.totalItems.collectAsState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = gridState.layoutInfo.totalItemsCount
            lastVisible >= total - 3 && !state.isLoadingMore && state.hasMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.loadMore()
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            CatalogHeader(
                search = state.search,
                onSearchChange = viewModel::setSearch,
                cartCount = cartCount,
                onOpenCart = onOpenCart
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isLoading && state.courses.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            } else if (state.error != null) {
                CatalogErrorState(state.error!!, viewModel::refresh)
            } else if (state.courses.isEmpty() && !state.isLoading) {
                CatalogEmptyState()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    state = gridState,
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            "RECOMENDADOS PARA TI",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextFaint,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    
                    items(state.courses, key = { it.id }) { course ->
                        ModernCourseCard(
                            course = course,
                            onClick = { onCourseClick(course.id) }
                        )
                    }

                    if (state.isLoadingMore) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogHeader(
    search: String,
    onSearchChange: (String) -> Unit,
    cartCount: Int,
    onOpenCart: () -> Unit
) {
    Surface(
        color = Surface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "CATÁLOGO",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryGreen,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        "Planes Premium",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                }
                IconButton(
                    onClick = onOpenCart,
                    modifier = Modifier.size(46.dp).background(PrimaryGreen.copy(alpha = 0.08f), CircleShape)
                ) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) Badge(containerColor = Error) { Text(cartCount.toString()) }
                        }
                    ) {
                        Icon(Icons.Rounded.ShoppingCart, "Carrito", tint = PrimaryGreen)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = search,
                onValueChange = onSearchChange,
                placeholder = { Text("Buscar plan, objetivo...", color = TextFaint) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = PrimaryGreen) },
                trailingIcon = { Icon(Icons.Rounded.FilterList, null, tint = TextSecondary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = Border.copy(alpha = 0.3f),
                    focusedContainerColor = Background,
                    unfocusedContainerColor = Background,
                )
            )
        }
    }
}

@Composable
private fun CatalogErrorState(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("⚠️", fontSize = 52.sp)
        Spacer(Modifier.height(16.dp))
        Text("Error de Conexión", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        Text(error, color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(52.dp).width(200.dp)
        ) {
            Text("REINTENTAR", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun CatalogEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔍", fontSize = 72.sp)
        Spacer(Modifier.height(20.dp))
        Text("Sin resultados", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = TextPrimary)
        Text("Prueba con otros términos de búsqueda", color = TextSecondary)
    }
}
