package com.dietetic.frontend.domain.model

import com.google.gson.annotations.SerializedName

data class Course(
    val id: Int,
    @SerializedName("goal") val goal: String? = null,
    @SerializedName("name") val title: String? = null,
    val description: String? = null,
    @SerializedName("target_calories") val targetCalories: Int? = null, 
    @SerializedName("estimated_cost") val price: Double? = null,
    @SerializedName("duration_weeks") val durationWeeks: Int? = null,
    val stock: Int = 1,
    val imageUrl: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

data class CoursePayload(
    @SerializedName("name") val title: String,
    val description: String,
    @SerializedName("goal") val goal: String,
    @SerializedName("target_calories") val targetCalories: Int,
    @SerializedName("duration_weeks") val durationWeeks: Int,
    @SerializedName("estimated_cost") val price: Double,
    @SerializedName("is_active") val isActive: Boolean
)
