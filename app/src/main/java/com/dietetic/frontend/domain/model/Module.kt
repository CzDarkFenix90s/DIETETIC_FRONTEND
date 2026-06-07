package com.dietetic.frontend.domain.model

import com.google.gson.annotations.SerializedName

data class Module(
    val id: Int,
    @SerializedName("plan_nutricional_id") val courseId: Int? = null,
    @SerializedName("plan_nutricional_name") val courseTitle: String? = null,
    @SerializedName("name") val title: String? = null,
    val description: String? = null,
    @SerializedName("sequence") val order: Int? = null,
    @SerializedName("meal_type") val mealType: String? = null,
    @SerializedName("portion_grams") val portionGrams: Double? = null,
    @SerializedName("is_active") val isActive: Boolean? = null
)

data class ModulePayload(
    @SerializedName("plan_nutricional_id") val courseId: Int,
    @SerializedName("name") val title: String,
    val description: String,
    @SerializedName("sequence") val order: Int,
    @SerializedName("meal_type") val mealType: String,
    @SerializedName("portion_grams") val portionGrams: Double,
    @SerializedName("is_active") val isActive: Boolean
)
