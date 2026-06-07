package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PacienteResponseDto(
    val count: Int,
    val results: List<PacienteDto>
)

data class PacienteDto(
    val id: Int = 0,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("patient_code") val patientCode: String? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    val age: Int? = null,
    val goal: String? = null,
    @SerializedName("dietary_restrictions") val dietaryRestrictions: String? = null,
    @SerializedName("current_weight") val currentWeight: Double? = null,
    @SerializedName("height_cm") val heightCm: Double? = null,
    val status: String? = null,
    @SerializedName("medical_notes") val medicalNotes: String? = null,
    val bmi: Double? = null,
    @SerializedName("num_seguimientos") val numSeguimientos: Int? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
)
