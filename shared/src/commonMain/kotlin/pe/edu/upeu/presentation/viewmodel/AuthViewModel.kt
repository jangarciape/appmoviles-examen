package pe.edu.upeu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.data.local.SessionManager
import pe.edu.upeu.domain.model.User
import pe.edu.upeu.domain.usecase.LoginUseCase
import pe.edu.upeu.domain.usecase.RegisterUserCase

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val registerUserCase: RegisterUserCase,
    private val loginUseCase: LoginUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun register(
        name: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = registerUserCase(name, lastName, email, phone, password)
            result.onSuccess { user ->
                println("Debug_Auth:Register Success for ${user.email}")
                user.token?.let { sessionManager.saveToken(it) }
                sessionManager.saveUser(user)
                _state.value = AuthState.Success(user)
            }.onFailure { e ->
                println("Debug_Auth:Register Error for ${e.message}")
                _state.value = AuthState.Error(e.message ?: "Error al registrar")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            val result = loginUseCase(email, password)
            result.onSuccess { user ->
                println("Debug_Auth:Login Success for ${user.email}")
                user.token?.let { sessionManager.saveToken(it) }
                sessionManager.saveUser(user)
                _state.value = AuthState.Success(user)
            }.onFailure { e ->
                println("Debug_Auth:Login Error for ${e.message}")
                _state.value = AuthState.Error(e.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun logout() {
        sessionManager.clear()
        _state.value = AuthState.Idle
    }

    fun getSavedUser(): User? {
        return sessionManager.getUser()
    }

    fun resetState() {
        _state.value = AuthState.Idle
    }
}
