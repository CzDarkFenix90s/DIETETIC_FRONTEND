package com.dietetic.frontend.domain.model

data class PlanNutricional(
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val goal: String? = null,
    val targetCalories: Int? = null,
    val durationWeeks: Int? = null,
    val estimatedCost: Double? = null,
    val isActive: Boolean? = null,
)
