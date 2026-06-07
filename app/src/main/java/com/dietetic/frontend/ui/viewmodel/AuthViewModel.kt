package com.dietetic.frontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dietetic.frontend.data.local.TokenDataStore
import com.dietetic.frontend.domain.model.LoggedUser
import com.dietetic.frontend.domain.repository.AuthRepository
import com.dietetic.frontend.ui.auth.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<LoggedUser?>(null)
    val currentUser: StateFlow<LoggedUser?> = _currentUser.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _isCheckingSession = MutableStateFlow(true)
    val isCheckingSession: StateFlow<Boolean> = _isCheckingSession.asStateFlow()

    init {
        observeSession()
    }

    private fun observeSession() {
        tokenDataStore.userSnapshot
            .onEach { snapshot ->
                if (snapshot != null) {
                    val user = LoggedUser(
                        id       = snapshot.id,
                        username = snapshot.username,
                        email    = snapshot.email,
                        isStaff  = snapshot.isStaff,
                        role     = snapshot.role,
                    )
                    _currentUser.value = user
                    _isAuthenticated.value = true
                } else {
                    _currentUser.value = null
                    _isAuthenticated.value = false
                }
                _isCheckingSession.value = false
            }
            .launchIn(viewModelScope)
    }

    fun login(username: String, password: String) {
        if (_uiState.value is AuthUiState.Loading) return
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.login(username.trim(), password)
                .onSuccess { user ->
                    _uiState.value = AuthUiState.Success(user)
                    // observeSession will pick up the changes from DataStore
                }
                .onFailure { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Error al iniciar sesión")
                }
        }
    }

    fun register(username: String, email: String, password: String, password2: String, firstName: String? = null, lastName: String? = null) {
        if (_uiState.value is AuthUiState.Loading) return
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.register(username.trim(), email.trim(), password, password2, firstName, lastName)
                .onSuccess { user ->
                    _uiState.value = AuthUiState.Success(user)
                }
                .onFailure { e ->
                    _uiState.value = AuthUiState.Error(e.message ?: "Error al registrarse")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (_: Exception) {}
            tokenDataStore.clearSession()
        }
    }

    fun clearError() {
        if (_uiState.value is AuthUiState.Error) {
            _uiState.value = AuthUiState.Idle
        }
    }
}
