package com.dietetic.frontend.navigation

sealed class Screen(val route: String) {
    // Auth
    data object Login    : Screen("login")
    data object Register : Screen("register")

    // Public / Client
    data object Home     : Screen("home")
    data object Catalog  : Screen("planes")
    data object Diet     : Screen("dieta")
    data object Orders   : Screen("consultas")
    data object Profile  : Screen("perfil")
    data object Premium  : Screen("premium")
    data object Verification : Screen("verification")

    // Admin Dashboard
    data object AdminDashboard : Screen("admin/dashboard")
    
    // Gestión de Planes
    data object PlanCreate : Screen("admin/plan/nuevo")
    data object PlanEdit   : Screen("admin/plan/editar/{id}") {
        fun createRoute(id: Int) = "admin/plan/editar/$id"
    }
    
    // Gestión de Alimentos (dentro de un plan)
    data object AlimentoManagement : Screen("admin/plan/{planId}/alimentos") {
        fun createRoute(planId: Int) = "admin/plan/$planId/alimentos"
    }
    data object AlimentoCreate : Screen("admin/plan/{planId}/alimento/nuevo") {
        fun createRoute(planId: Int) = "admin/plan/$planId/alimento/nuevo"
    }
    data object AlimentoEdit : Screen("admin/plan/{planId}/alimento/editar/{alimentoId}") {
        fun createRoute(planId: Int, alimentoId: Int) = "admin/plan/$planId/alimento/editar/$alimentoId"
    }
    
    // Gestión de Especialistas
    data object NutricionistaCreate : Screen("admin/especialista/nuevo")
    data object NutricionistaEdit : Screen("admin/especialista/editar/{id}") {
        fun createRoute(id: Int) = "admin/especialista/editar/$id"
    }
}
