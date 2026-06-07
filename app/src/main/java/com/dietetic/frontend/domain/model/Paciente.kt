package com.dietetic.frontend.domain.model

data class Paciente(
    val id: Int,
    val userId: Int? = null,
    val patientCode: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val fullName: String? = null,
    val age: Int? = null,
    val goal: String? = null,
    val dietaryRestrictions: String? = null,
    val currentWeight: Double? = null,
    val heightCm: Double? = null,
    val status: String? = null,
) {
    val bmi: Double
        get() {
            val weight = currentWeight ?: 0.0
            val height = (heightCm ?: 1.0) / 100.0
            if (height <= 0) return 0.0
            val res = weight / (height * height)
            return Math.round(res * 100.0) / 100.0
        }
    
    val displayName: String
        get() = if (!firstName.isNullOrBlank() || !lastName.isNullOrBlank()) {
            "${firstName ?: ""} ${lastName ?: ""}".trim()
        } else {
            fullName ?: "Sin nombre"
        }
}
