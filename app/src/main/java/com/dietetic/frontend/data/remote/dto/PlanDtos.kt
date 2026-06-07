package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PlanDto(
    val id: Int = 0,
    val name: String? = null,
    val description: String? = null,
    val goal: String? = null,
    @SerializedName("target_calories") val targetCalories: Int? = null,
    @SerializedName("duration_weeks") val durationWeeks: Int? = null,
    @SerializedName("estimated_cost") val estimatedCost: Double? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("total_alimentos") val totalAlimentos: Int? = null,
    @SerializedName("created_at") val createdAt: String? = null,
)
