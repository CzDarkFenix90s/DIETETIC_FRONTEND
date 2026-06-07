package com.dietetic.frontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dietetic.frontend.domain.repository.*
import com.dietetic.frontend.ui.home.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val consultaRepository: ConsultaRepository,
    private val authRepository: AuthRepository,
    private val pacienteRepository: PacienteRepository,
    private val moduleRepository: ModuleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val user = authRepository.getStoredUser()
            val userName = user?.username ?: "Usuario"

            try {
                val coursesDeferred = async { courseRepository.getCourses() }
                val profileDeferred = async { pacienteRepository.getPacientes() }
                val consultasDeferred = async { consultaRepository.getMyConsultas() }

                val coursesResult = coursesDeferred.await()
                val profileResult = profileDeferred.await()
                val consultasResult = consultasDeferred.await()

                var recommended = emptyList<com.dietetic.frontend.domain.model.Course>()
                coursesResult.onSuccess { (list, _) -> recommended = list }

                var profile: com.dietetic.frontend.domain.model.Paciente? = null
                profileResult.onSuccess { list ->
                    profile = list.find { 
                        it.userId == user?.id ||
                        it.fullName.equals(userName, ignoreCase = true) ||
                        it.firstName.equals(userName, ignoreCase = true) ||
                        it.displayName.equals(userName, ignoreCase = true)
                    } ?: list.firstOrNull()
                }

                var nextAppt: com.dietetic.frontend.domain.model.ConsultaDietetica? = null
                var activeMeals = emptyList<com.dietetic.frontend.domain.model.Module>()
                
                val consultasList = consultasResult.getOrNull() ?: emptyList()
                
                nextAppt = consultasList.filter { it.status?.lowercase() == "programada" }
                                   .sortedBy { it.scheduledTime }
                                   .firstOrNull()

                // Cargamos los alimentos de TODOS los planes que el usuario tenga agendados
                val agendedPlanIds = consultasList.mapNotNull { it.planId }.distinct()
                
                val allMeals = mutableListOf<com.dietetic.frontend.domain.model.Module>()
                
                if (agendedPlanIds.isNotEmpty()) {
                    for (planId in agendedPlanIds) {
                        moduleRepository.getModulesByCourse(planId).onSuccess {
                            allMeals.addAll(it)
                        }
                    }
                } else if (recommended.isNotEmpty()) {
                    // Si no hay citas, mostramos los del primer plan recomendado
                    recommended.firstOrNull()?.id?.let { pid ->
                        moduleRepository.getModulesByCourse(pid).onSuccess {
                            allMeals.addAll(it)
                        }
                    }
                }
                
                activeMeals = allMeals.distinctBy { it.id }

                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        userName = userName,
                        patientProfile = profile,
                        nextAppointment = nextAppt,
                        dailyMeals = activeMeals,
                        recommendedPlanes = recommended,
                        hasProfile = profile != null
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar datos del Home: ${e.message}"
                    )
                }
            }
        }
    }
}
