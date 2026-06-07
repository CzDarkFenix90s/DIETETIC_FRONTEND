package com.dietetic.frontend.domain.model

data class ConsultaDietetica(
    val id: Int,
    val status: String? = null,
    val sessionNotes: String? = null,
    val scheduledTime: String? = null,
    val estimatedEnd: String? = null,
    val pacienteNombre: String? = null,
    val planId: Int? = null,
)
