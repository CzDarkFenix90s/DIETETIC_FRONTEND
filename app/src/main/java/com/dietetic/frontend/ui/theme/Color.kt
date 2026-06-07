package com.dietetic.frontend.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta "Lujo Orgánico" - Premium Dietetic Theme
val CreamBackground = Color(0xFFFDFBF7) // Crema suave / Papel texturizado
val ForestGreen     = Color(0xFF1B3022) // Verde Bosque Profundo
val SageGreen       = Color(0xFFE8F5E9) // Verde Salvia Suave
val PremiumGold     = Color(0xFFC5A059) // Oro Premium / Latón
val BrassDark       = Color(0xFF8E7341) // Dorado oscuro para fuentes

val Success = Color(0xFF2D6A4F)
val Error   = Color(0xFF9B2226)
val Warning = Color(0xFFD4A373)

// Aliases para compatibilidad y uso general
val Background    = CreamBackground
val BackgroundColor = CreamBackground
val Surface       = Color(0xFFFFFFFF)
val SurfaceColor    = Color(0xFFFFFFFF)
val TextPrimary   = Color(0xFF1A1C1E)
val TextSecondary = Color(0xFF4A4D50)
val TextFaint     = Color(0xFF8D9199)
val Border        = Color(0xFFE5E2DE)
val MintLight     = SageGreen
val AccentOrange  = PremiumGold

// Mantenemos antiguos nombres por compatibilidad pero con nuevos colores
val PrimaryGreen = ForestGreen
val DarkGreen    = ForestGreen
val PrimaryBlue  = PremiumGold
val DarkBlue     = ForestGreen
val GoldPrimary  = PremiumGold
val DeepDark     = Color(0xFF121212)

// Gradientes Únicos
val FreshGradient = listOf(ForestGreen, Color(0xFF2D6A4F))
val GoldGradient  = listOf(PremiumGold, BrassDark)
val DarkGradient  = listOf(Color(0xFF1C1C1C), Color(0xFF121212))
val MarbleGradient = listOf(Color(0xFFF8F9FA), Color(0xFFE9ECEF))
val MetalGradient = listOf(Color(0xFFE2E2E2), Color(0xFFC0C0C0))
val WoodGradient = listOf(Color(0xFFD7BA89), Color(0xFFB38B59))
