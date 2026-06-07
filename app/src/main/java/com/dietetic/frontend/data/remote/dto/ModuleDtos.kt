package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ModuleResponse(
    val count: Int,
    val results: List<ModuleDto>
)

data class ModuleDto(
    val id: Int = 0,
    @SerializedName("name") val title: String? = null,
    val description: String? = null,
    @SerializedName("meal_type") val mealType: String? = null,
    @SerializedName("portion_grams") val portionGrams: Double? = null,
    val sequence: Int? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("plan_nutricional") val plan: Any? = null
)
