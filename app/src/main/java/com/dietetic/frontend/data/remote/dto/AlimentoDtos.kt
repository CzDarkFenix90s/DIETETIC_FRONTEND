package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AlimentoDto(
    val id: Int = 0,
    val name: String? = null,
    val description: String? = null,
    @SerializedName("meal_type") val mealType: String? = null,
    @SerializedName("portion_grams") val portionGrams: Double? = null,
    val sequence: Int? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("plan_nutricional") val planNutricional: PlanDto? = null,
    @SerializedName("created_at") val createdAt: String? = null,
)

data class UpdateOrderRequest(
    val sequence: Int
)
