package com.dietetic.frontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dietetic.frontend.domain.model.Course
import com.dietetic.frontend.domain.repository.*
import com.dietetic.frontend.data.remote.dto.ConsultaCreateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CartItem(
    val course:   Course,
    val quantity: Int,
)

sealed interface CheckoutState {
    data object Idle                          : CheckoutState
    data object Loading                       : CheckoutState
    data class  Success(val orderId: Int)     : CheckoutState
    data class  Error(val message: String)    : CheckoutState
}

@HiltViewModel
class CartViewModel @Inject constructor(
    private val consultaRepository: ConsultaRepository,
    private val nutricionistaRepository: NutricionistaRepository,
    private val pacienteRepository: PacienteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val totalItems: StateFlow<Int> = _items
        .map { it.sumOf { i -> i.quantity } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val subtotal: StateFlow<Double> = _items
        .map { it.sumOf { i -> (i.course.price ?: 0.0) * i.quantity } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val tax: StateFlow<Double> = subtotal
        .map { it * 0.15 } 
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val totalWithTax: StateFlow<Double> = combine(subtotal, tax) { s, t -> s + t }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    fun addItem(course: Course, quantity: Int = 1) {
        _items.update { list ->
            val existing = list.find { it.course.id == course.id }
            if (existing != null) {
                // Para planes nutricionales, solo permitimos 1 unidad siempre
                list
            } else {
                list + CartItem(course, 1)
            }
        }
    }

    fun updateQuantity(courseId: Int, quantity: Int) {
        if (quantity <= 0) removeItem(courseId)
        else _items.update { list ->
            list.map { if (it.course.id == courseId) it.copy(quantity = quantity) else it }
        }
    }

    fun removeItem(courseId: Int) {
        _items.update { it.filter { i -> i.course.id != courseId } }
    }

    fun clearCart() { _items.value = emptyList() }

    fun resetCheckout() { _checkoutState.value = CheckoutState.Idle }

    fun checkout() {
        val currentItems = _items.value
        if (currentItems.isEmpty()) {
            _checkoutState.value = CheckoutState.Error("El carrito está vacío")
            return
        }
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading

            val user = authRepository.getStoredUser()
            val pacienteResult = pacienteRepository.getPacientes()
            
            var pacienteId = pacienteResult.getOrNull()?.find { 
                it.fullName.equals(user?.username, ignoreCase = true) ||
                it.firstName.equals(user?.username, ignoreCase = true) ||
                it.displayName.equals(user?.username, ignoreCase = true)
            }?.id

            if (pacienteId == null) {
                // Intentar creación automática si no existe perfil
                val newPaciente = com.dietetic.frontend.domain.model.Paciente(
                    id = 0,
                    firstName = user?.username ?: "Usuario",
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
                }.onFailure {
                    // Fallback al primero si falla creación
                    pacienteId = pacienteResult.getOrNull()?.firstOrNull()?.id
                }
            }

            if (pacienteId == null) {
                _checkoutState.value = CheckoutState.Error("No se encontró tu perfil de paciente.")
                return@launch
            }
            
            val finalPacienteId = pacienteId!!

            val nutriResult = nutricionistaRepository.getNutricionistas()
            val nutriId = nutriResult.getOrNull()?.firstOrNull()?.id

            if (nutriId == null) {
                _checkoutState.value = CheckoutState.Error("No hay nutricionistas disponibles")
                return@launch
            }

            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val start = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0)
            val end = start.plusHours(1)

            var lastCreatedId = 0
            
            for (item in currentItems) {
                val request = ConsultaCreateRequest(
                    paciente = finalPacienteId,
                    nutricionista = nutriId,
                    plan_nutricional = item.course.id,
                    scheduled_time = start.format(formatter),
                    estimated_end = end.format(formatter),
                    status = "programada",
                    session_notes = "Inscripción desde app"
                )

                val result = consultaRepository.createConsulta(request)
                if (result.isFailure) {
                    val errorBody = result.exceptionOrNull()?.message ?: "Error desconocido"
                    _checkoutState.value = CheckoutState.Error("No se pudo comprar: $errorBody")
                    return@launch
                }
                lastCreatedId = result.getOrThrow().id
            }

            clearCart()
            _checkoutState.value = CheckoutState.Success(lastCreatedId)
        }
    }
}
