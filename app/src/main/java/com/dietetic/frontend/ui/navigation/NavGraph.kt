package com.dietetic.frontend.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.dietetic.frontend.navigation.Screen
import com.dietetic.frontend.ui.auth.LoginScreen
import com.dietetic.frontend.ui.auth.RegisterScreen
import com.dietetic.frontend.ui.home.CourseDetailScreen
import com.dietetic.frontend.ui.home.HomeScreen
import com.dietetic.frontend.ui.home.DailyDietScreen
import com.dietetic.frontend.ui.home.CatalogScreen
import com.dietetic.frontend.ui.consultas.ConsultasScreen
import com.dietetic.frontend.ui.admin.*
import com.dietetic.frontend.ui.profile.PremiumScreen
import com.dietetic.frontend.ui.profile.ProfileScreen
import com.dietetic.frontend.ui.viewmodel.AuthViewModel
import com.dietetic.frontend.ui.viewmodel.CartViewModel
import com.dietetic.frontend.ui.components.LoadingScreen
import com.dietetic.frontend.ui.components.CartBottomSheet
import com.dietetic.frontend.ui.theme.Background

@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel = hiltViewModel(),
) {
    val isCheckingSession by authViewModel.isCheckingSession.collectAsState()
    val isAuthenticated   by authViewModel.isAuthenticated.collectAsState()

    if (isCheckingSession) {
        LoadingScreen("Iniciando Dietetic Pro...")
        return
    }

    // Resetear todo el estado de navegación al cambiar la autenticación
    key(isAuthenticated) {
        NavGraphContent(
            authViewModel = authViewModel,
            cartViewModel = cartViewModel,
            isAuthenticated = isAuthenticated
        )
    }
}

@Composable
private fun NavGraphContent(
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel,
    isAuthenticated: Boolean
) {
    val navController     = rememberNavController()
    val currentUser       by authViewModel.currentUser.collectAsState()
    val cartCount         by cartViewModel.totalItems.collectAsState()
    val currentRole       = currentUser?.role?.lowercase()
    val isAdmin           = currentUser?.isStaff == true || currentRole in setOf(
        "admin", "superuser", "superusuario"
    )
    val isNutricionista = currentRole == "nutricionista"

    var showCart by remember { mutableStateOf(false) }
    var confirmedOrderId by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(confirmedOrderId) {
        confirmedOrderId?.let { id ->
            snackbarHostState.showSnackbar(
                message = "¡Consulta #$id agendada con éxito!",
                duration = SnackbarDuration.Short
            )
            confirmedOrderId = null
        }
    }

    val startDestination = if (!isAuthenticated) Screen.Login.route else {
        when {
            isAdmin         -> Screen.AdminDashboard.route
            isNutricionista -> Screen.Orders.route
            else            -> Screen.Home.route
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute      = navBackStackEntry?.destination?.route

    val showBottomBar = isAuthenticated && !isAdmin && !isNutricionista && currentRoute in listOf(
        Screen.Home.route,
        Screen.Catalog.route,
        Screen.Diet.route,
        Screen.Orders.route,
        Screen.Profile.route,
    )

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                    cartCount     = cartCount,
                    onCartClick   = { showCart = true }
                )
            }
        },
    ) { innerPadding ->

        if (showCart) {
            CartBottomSheet(
                cartViewModel   = cartViewModel,
                isAuthenticated = isAuthenticated,
                onDismiss       = { showCart = false },
                onLoginRequired = {
                    showCart = false
                    navController.navigate(Screen.Login.route)
                },
                onOrderSuccess = { orderId ->
                    confirmedOrderId = orderId
                    showCart = false
                },
            )
        }

        NavHost(
            navController    = navController,
            startDestination = startDestination,
            modifier         = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
        ) {

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = { _ ->
                        // El cambio en isAuthenticated disparará el reinicio de NavGraph
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    viewModel            = authViewModel,
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = { _ ->
                        // El cambio en isAuthenticated disparará el reinicio de NavGraph
                    },
                    onNavigateToLogin = { navController.popBackStack() },
                    viewModel         = authViewModel,
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onLogout       = { authViewModel.logout() },
                    onCourseClick  = { id -> navController.navigate("course/$id") },
                    onCatalogClick = { 
                        navController.navigate(Screen.Catalog.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onDietClick = { navController.navigate(Screen.Diet.route) },
                    onOpenCart = { showCart = true },
                    cartViewModel = cartViewModel
                )
            }

            composable(Screen.Catalog.route) {
                CatalogScreen(
                    onCourseClick = { id -> navController.navigate("course/$id") },
                    onOpenCart = { showCart = true },
                    cartViewModel = cartViewModel
                )
            }

            composable(Screen.Diet.route) {
                DailyDietScreen()
            }

            composable(
                route     = "course/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType }),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                CourseDetailScreen(
                    courseId      = id,
                    onBack        = { navController.popBackStack() },
                    onOpenCart    = { showCart = true },
                    cartViewModel = cartViewModel,
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = { authViewModel.logout() },
                    onNavigateToPremium = { navController.navigate(Screen.Premium.route) }
                )
            }

            composable(Screen.Orders.route) {
                ConsultasScreen(onLogout = { authViewModel.logout() })
            }

            composable(Screen.Verification.route) {
                com.dietetic.frontend.VerificationScreen(
                    onNavigateToHome = { navController.navigate(Screen.Home.route) },
                    onNavigateToAdmin = { navController.navigate(Screen.AdminDashboard.route) }
                )
            }

            composable(Screen.Premium.route) {
                PremiumScreen(
                    onBack = { navController.popBackStack() },
                    onSubscribe = { _ -> /* TODO */ }
                )
            }

            // --- ADMIN ROUTES ---
            composable(Screen.AdminDashboard.route) {
                AdminDashboardScreen(
                    onLogout = { authViewModel.logout() },
                    onEditCourse = { id -> navController.navigate(Screen.PlanEdit.createRoute(id)) },
                    onCreateCourse = { navController.navigate(Screen.PlanCreate.route) },
                    onManageModules = { id -> navController.navigate(Screen.AlimentoManagement.createRoute(id)) },
                    onCreateNutricionista = { navController.navigate(Screen.NutricionistaCreate.route) },
                    onEditNutricionista = { id -> navController.navigate(Screen.NutricionistaEdit.createRoute(id)) }
                )
            }

            composable(Screen.PlanCreate.route) {
                PlanFormScreen(onBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.PlanEdit.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                PlanFormScreen(courseId = id, onBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.AlimentoManagement.route,
                arguments = listOf(navArgument("planId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("planId") ?: 0
                AlimentoManagementScreen(
                    courseId = id,
                    onBack = { navController.popBackStack() },
                    onEditModule = { modId ->
                        navController.navigate(Screen.AlimentoEdit.createRoute(id, modId)) 
                    },
                    onCreateModule = { 
                        navController.navigate(Screen.AlimentoCreate.createRoute(id)) 
                    }
                )
            }

            composable(Screen.AlimentoCreate.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("planId")?.toIntOrNull() ?: 0
                AlimentoFormScreen(courseId = id, onBack = { navController.popBackStack() })
            }

            composable(Screen.AlimentoEdit.route) { backStackEntry ->
                val planId = backStackEntry.arguments?.getString("planId")?.toIntOrNull() ?: 0
                val alId = backStackEntry.arguments?.getString("alimentoId")?.toIntOrNull() ?: 0
                AlimentoFormScreen(
                    courseId = planId,
                    moduleId = alId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.NutricionistaCreate.route) {
                NutricionistaFormScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.NutricionistaEdit.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                NutricionistaFormScreen(nutId = id, onBack = { navController.popBackStack() })
            }
        }
    }
}
