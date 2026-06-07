package com.dietetic.frontend.ui.home

import com.dietetic.frontend.domain.model.*

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val patientProfile: Paciente? = null,
    val nextAppointment: ConsultaDietetica? = null,
    val dailyMeals: List<Module> = emptyList(),
    val recommendedPlanes: List<Course> = emptyList(),
    val error: String? = null,
    val hasProfile: Boolean = false
)
