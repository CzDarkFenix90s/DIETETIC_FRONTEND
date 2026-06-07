package com.dietetic.frontend.domain.model

data class Nutricionista(
    val id: Int,
    val firstName: String? = null,
    val lastName: String? = null,
    val professionalId: String? = null,
    val specialty: String? = null,
    val consultationFee: Double? = null,
    
    // Campos para creación de usuario
    val username: String? = null,
    val email: String? = null,
    val password: String? = null
)
