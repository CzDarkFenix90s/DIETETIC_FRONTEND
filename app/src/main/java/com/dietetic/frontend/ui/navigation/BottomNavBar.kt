package com.dietetic.frontend.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dietetic.frontend.navigation.Screen
import com.dietetic.frontend.ui.theme.*

data class BottomNavItem(
    val screen:       Screen?,
    val label:        String,
    val icon:         ImageVector,
    val iconSelected: ImageVector,
    val badgeCount:   Int = 0,
    val onClick:      (() -> Unit)? = null
)

@Composable
fun BottomNavBar(
    navController: NavController,
    cartCount:     Int = 0,
    onCartClick:   () -> Unit = {},
) {
    val items = listOf(
        BottomNavItem(Screen.Home,    "Home",      Icons.Outlined.Home,          Icons.Filled.Home),
        BottomNavItem(Screen.Diet,    "Dieta",     Icons.Outlined.Fastfood,      Icons.Filled.Fastfood),
        BottomNavItem(Screen.Catalog, "Planes",    Icons.Outlined.RestaurantMenu, Icons.Filled.RestaurantMenu),
        BottomNavItem(Screen.Orders,  "Citas",     Icons.Outlined.EventAvailable, Icons.Filled.EventAvailable),
        BottomNavItem(Screen.Profile, "Perfil",    Icons.Outlined.Person,        Icons.Filled.Person),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute      = navBackStackEntry?.destination?.route

    Surface(
        color = Background,
        shadowElevation = 24.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            // Sutil línea de luz metálica
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Brush.horizontalGradient(MetalGradient)))
            
            NavigationBar(
                containerColor = Background,
                tonalElevation = 0.dp,
                modifier = Modifier.navigationBarsPadding().height(72.dp)
            ) {
                items.forEach { item ->
                    val isSelected = if (item.screen != null) currentRoute == item.screen.route else false

                    NavigationBarItem(
                        selected = isSelected,
                        onClick  = {
                            if (item.onClick != null) {
                                item.onClick.invoke()
                            } else if (item.screen != null) {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (isSelected) item.iconSelected else item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(26.dp)
                                )
                                if (isSelected) {
                                    // Punto de luz inferior
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .size(4.dp)
                                            .background(PremiumGold, CircleShape)
                                    )
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor       = ForestGreen,
                            indicatorColor          = Color.Transparent,
                            unselectedIconColor     = TextFaint.copy(alpha = 0.5f),
                        ),
                    )
                }
            }
        }
    }
}
