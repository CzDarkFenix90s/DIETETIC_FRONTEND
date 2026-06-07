package com.dietetic.frontend.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dietetic.frontend.domain.model.*
import com.dietetic.frontend.domain.repository.*
import com.dietetic.frontend.data.remote.dto.ConsultaCreateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ConsultasUiState(
    val consultas: List<ConsultaDietetica> = emptyList(),
    val nutricionistas: List<Nutricionista> = emptyList(),
    val planes: List<Course> = emptyList(),
    val userRole: String? = null,
    val isLoading: Boolean = false,
    val isBooking: Boolean = false,
    val bookingSuccess: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class ConsultasViewModel @Inject constructor(
    private val consultaRepository: ConsultaRepository,
    private val nutricionistaRepository: NutricionistaRepository,
    private val planRepository: CourseRepository,
    private val pacienteRepository: PacienteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsultasUiState())
    val uiState: StateFlow<ConsultasUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val user = authRepository.getStoredUser()
            val role = user?.role?.lowercase()
            _uiState.update { it.copy(userRole = role) }
            
            Log.d("ConsultasVM", "Loading data for user: ${user?.username} (Role: $role)")

            // Si es nutricionista, usamos getMyConsultas para que solo vea las suyas
            // Si es admin (staff), usamos getConsultas para que vea todo
            // Si es paciente, usamos getMyConsultas para que vea las suyas
            val consultasResult = if (user?.isStaff == true || role in setOf("admin", "superuser")) {
                consultaRepository.getConsultas()
            } else {
                consultaRepository.getMyConsultas()
            }
            
            val nutricionistasResult = nutricionistaRepository.getNutricionistas()
            val planesResult = planRepository.getCourses()

            consultasResult.onSuccess { consultas ->
                Log.d("ConsultasVM", "Cargadas ${consultas.size} citas")
                _uiState.update { it.copy(consultas = consultas) }
            }.onFailure { e ->
                Log.e("ConsultasVM", "Error cargando citas", e)
                _uiState.update { it.copy(error = e.message) }
            }

            nutricionistasResult.onSuccess { nutricionistas ->
                _uiState.update { it.copy(nutricionistas = nutricionistas) }
            }

            planesResult.onSuccess { (planes, _) ->
                _uiState.update { it.copy(planes = planes) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun agendarCita(nutricionistaId: Int, planId: Int, fechaHora: String) {
        viewModelScope.launch {
            Log.d("ConsultasVM", "Iniciando agendamiento: Nutri=$nutricionistaId, Plan=$planId, Fecha=$fechaHora")
            _uiState.update { it.copy(isBooking = true, error = null) }
            
            val user = authRepository.getStoredUser()
            val pacienteResult = pacienteRepository.getPacientes()
            
            // 1. Intentar encontrar por userId exacto (Solución definitiva)
            var pacienteId = pacienteResult.getOrNull()?.find { it.userId == user?.id }?.id

            // 2. Si no lo encontramos por ID, buscar por nombre (para perfiles antiguos)
            if (pacienteId == null) {
                pacienteId = pacienteResult.getOrNull()?.find { 
                    it.fullName.equals(user?.username, ignoreCase = true) ||
                    it.firstName.equals(user?.username, ignoreCase = true) ||
                    it.displayName.equals(user?.username, ignoreCase = true)
                }?.id
            }

            if (pacienteId == null) {
                Log.d("ConsultasVM", "Paciente no encontrado, creando perfil vinculado al usuario ID: ${user?.id}")
                val newPaciente = Paciente(
                    id = 0,
                    userId = user?.id,
                    firstName = user?.username ?: "Nuevo",
                    lastName = "Paciente",
                    patientCode = "PAC-${System.currentTimeMillis() % 10000}",
                    status = "activo",
                    age = 0,
                    goal = "Salud integral",
                    currentWeight = 0.0,
                    heightCm = 0.0
                )
                
                val createResult = pacienteRepository.createPaciente(newPaciente)
                createResult.onSuccess {
                    pacienteId = it.id
                    Log.d("ConsultasVM", "Perfil de paciente creado automáticamente con ID: $pacienteId")
                }.onFailure { e ->
                    Log.e("ConsultasVM", "No se pudo crear el perfil de paciente automáticamente", e)
                    // Como último recurso, si hay algún paciente, usamos el primero (retrocompatibilidad)
                    pacienteId = pacienteResult.getOrNull()?.firstOrNull()?.id
                    
                    if (pacienteId == null) {
                        val detailedError = e.message ?: "Error desconocido"
                        _uiState.update { it.copy(isBooking = false, error = "No se pudo crear perfil: $detailedError") }
                        return@launch
                    }
                }
            }

            Log.d("ConsultasVM", "Usando Paciente ID: $pacienteId")

            val start = LocalDateTime.parse(fechaHora)
            val estimatedEnd = start.plusHours(1).format(DateTimeFormatter.ISO_DATE_TIME)

            val finalPacienteId = pacienteId ?: return@launch

            val request = ConsultaCreateRequest(
                paciente = finalPacienteId,
                nutricionista = nutricionistaId,
                plan_nutricional = planId,
                scheduled_time = fechaHora,
                estimated_end = estimatedEnd
            )

            consultaRepository.createConsulta(request)
                .onSuccess {
                    Log.d("ConsultasVM", "Cita creada con éxito")
                    _uiState.update { it.copy(
                        isBooking = false, 
                        bookingSuccess = true,
                        successMessage = "Su agenda ha sido registrada correctamente"
                    ) }
                    loadData()
                }
                .onFailure { e ->
                    Log.e("ConsultasVM", "Error al agendar cita", e)
                    _uiState.update { it.copy(isBooking = false, error = "Error: ${e.message}") }
                }
        }
    }

    fun updateConsultaStatus(consultaId: Int, newStatus: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isBooking = true) }
            consultaRepository.updateStatus(consultaId, newStatus)
                .onSuccess {
                    loadData()
                    _uiState.update { it.copy(isBooking = false, successMessage = "Cita actualizada a: ${newStatus.replace("_", " ").uppercase()}") }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isBooking = false, error = e.message) }
                }
        }
    }

    fun resetStatus() {
        _uiState.update { it.copy(bookingSuccess = false, successMessage = null, error = null) }
    }
}
