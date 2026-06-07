package com.dietetic.frontend.domain.model

data class AlimentoProgramado(
    val id: Int,
    val name: String? = null,
    val mealType: String? = null,
    val portionGrams: Double? = null,
    val sequence: Int? = null,
    val isActive: Boolean? = null,
    val planName: String? = null
)
