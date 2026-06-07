package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NutricionistaResponseDto(
    val count: Int,
    val results: List<NutricionistaDto>
)

data class NutricionistaDto(
    val id: Int = 0,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("professional_id") val professionalId: String? = null,
    val specialty: String? = null,
    @SerializedName("consultation_fee") val consultationFee: Double? = null,
    @SerializedName("consultations_completed") val consultationsCompleted: Int? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    
    // Campos para creación de usuario (opcionales)
    val username: String? = null,
    val email: String? = null,
    val password: String? = null
)
