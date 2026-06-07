package com.dietetic.frontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dietetic.frontend.domain.model.*
import com.dietetic.frontend.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUiState(
    val courses: List<Course> = emptyList(),
    val modules: List<Module> = emptyList(),
    val nutricionistas: List<Nutricionista> = emptyList(),
    val pacientes: List<Paciente> = emptyList(),
    val users: List<User> = emptyList(),
    val consultas: List<ConsultaDietetica> = emptyList(),
    val allAlimentos: List<AlimentoProgramado> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val total: Int = 0,
    val isDeleting: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val deleteSuccess: Boolean = false
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val moduleRepository: ModuleRepository,
    private val nutricionistaRepository: NutricionistaRepository,
    private val pacienteRepository: PacienteRepository,
    private val userRepository: UserRepository,
    private val consultaRepository: ConsultaRepository,
    private val alimentoRepository: AlimentoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminUiState())
    val state: StateFlow<AdminUiState> = _state.asStateFlow()

    init {
        refreshAll()
    }

    fun refreshAll() {
        loadCourses()
        loadNutricionistas()
        loadPacientes()
        loadUsers()
        loadConsultas()
        loadAllAlimentos()
    }

    fun loadCourses() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            courseRepository.getCourses(CourseFilters(pageSize = 100))
                .onSuccess { (courses, total) ->
                    _state.update { it.copy(courses = courses, total = total, isLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun loadNutricionistas() {
        viewModelScope.launch {
            nutricionistaRepository.getNutricionistas()
                .onSuccess { list -> _state.update { it.copy(nutricionistas = list) } }
        }
    }

    fun loadPacientes() {
        viewModelScope.launch {
            pacienteRepository.getPacientes()
                .onSuccess { list -> _state.update { it.copy(pacientes = list) } }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            userRepository.getUsers(isStaff = false)
                .onSuccess { list -> _state.update { it.copy(users = list) } }
        }
    }

    fun loadConsultas() {
        viewModelScope.launch {
            consultaRepository.getConsultas()
                .onSuccess { list -> _state.update { it.copy(consultas = list) } }
        }
    }

    fun loadAllAlimentos() {
        viewModelScope.launch {
            alimentoRepository.getAlimentos()
                .onSuccess { list -> _state.update { it.copy(allAlimentos = list) } }
        }
    }

    fun deleteCourse(id: Int) {
        _state.update { it.copy(isDeleting = true) }
        viewModelScope.launch {
            courseRepository.deleteCourse(id)
                .onSuccess {
                    _state.update { it.copy(isDeleting = false, deleteSuccess = true) }
                    loadCourses()
                }
                .onFailure { e ->
                    _state.update { it.copy(isDeleting = false, error = e.message) }
                }
        }
    }

    fun saveCourse(
        id: Int? = null,
        title: String,
        description: String,
        price: Double,
        level: String,
        isActive: Boolean,
        durationWeeks: Int = 4
    ) {
        _state.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val payload = CoursePayload(
                title = title,
                description = description,
                goal = "Saludable",
                targetCalories = level.toIntOrNull() ?: 2000,
                durationWeeks = durationWeeks,
                price = price,
                isActive = isActive
            )
            
            val result = if (id == null) courseRepository.createCourse(payload)
            else courseRepository.updateCourse(id, payload)

            result.onSuccess {
                _state.update { it.copy(isSaving = false, saveSuccess = true) }
                loadCourses()
            }
            .onFailure { e ->
                _state.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    // Nutricionista Management
    fun saveNutricionista(nut: Nutricionista) {
        _state.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val result = if (nut.id == 0) nutricionistaRepository.createNutricionista(nut)
            else nutricionistaRepository.updateNutricionista(nut.id, nut)

            result.onSuccess {
                _state.update { it.copy(isSaving = false, saveSuccess = true) }
                loadNutricionistas()
            }
            .onFailure { e ->
                _state.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    fun deleteNutricionista(id: Int) {
        viewModelScope.launch {
            nutricionistaRepository.deleteNutricionista(id).onSuccess { loadNutricionistas() }
        }
    }

    // Paciente Management
    fun deletePaciente(id: Int) {
        _state.update { it.copy(isDeleting = true) }
        viewModelScope.launch {
            pacienteRepository.deletePaciente(id)
                .onSuccess {
                    _state.update { it.copy(isDeleting = false, deleteSuccess = true) }
                    loadPacientes()
                }
                .onFailure { e ->
                    _state.update { it.copy(isDeleting = false, error = e.message) }
                }
        }
    }

    fun deleteUser(id: Int) {
        _state.update { it.copy(isDeleting = true) }
        viewModelScope.launch {
            userRepository.deleteUser(id)
                .onSuccess {
                    _state.update { it.copy(isDeleting = false, deleteSuccess = true) }
                    loadUsers()
                }
                .onFailure { e ->
                    _state.update { it.copy(isDeleting = false, error = e.message) }
                }
        }
    }

    fun resetStatus() {
        _state.update { it.copy(deleteSuccess = false, saveSuccess = false, error = null) }
    }

    // Module Management (Alimentos por curso)
    fun loadModules(courseId: Int) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            moduleRepository.getModulesByCourse(courseId)
                .onSuccess { modules ->
                    _state.update { it.copy(modules = modules, isLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun saveModule(
        id: Int? = null,
        courseId: Int,
        title: String,
        description: String,
        order: Int,
        mealType: String = "snack",
        portionGrams: Double = 100.0,
        isActive: Boolean
    ) {
        _state.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val payload = ModulePayload(
                courseId = courseId,
                title = title,
                description = description,
                order = order,
                mealType = mealType,
                portionGrams = portionGrams,
                isActive = isActive
            )
            
            val result = if (id == null) moduleRepository.createModule(payload)
            else moduleRepository.updateModule(id, payload)

            result.onSuccess {
                _state.update { it.copy(isSaving = false, saveSuccess = true) }
                loadModules(courseId)
                loadAllAlimentos()
            }
            .onFailure { e ->
                _state.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    fun deleteModule(id: Int, courseId: Int) {
        _state.update { it.copy(isDeleting = true) }
        viewModelScope.launch {
            moduleRepository.deleteModule(id)
                .onSuccess {
                    _state.update { it.copy(isDeleting = false, deleteSuccess = true) }
                    loadModules(courseId)
                    loadAllAlimentos()
                }
                .onFailure { e ->
                    _state.update { it.copy(isDeleting = false, error = e.message) }
                }
        }
    }
}
