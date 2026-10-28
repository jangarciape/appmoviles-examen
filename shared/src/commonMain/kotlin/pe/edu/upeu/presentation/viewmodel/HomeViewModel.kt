package pe.edu.upeu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.data.local.SessionManager
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.domain.model.User
import pe.edu.upeu.domain.usecase.GetRoomTypesUseCase

data class Reservation(
    val id: String,
    val roomName: String,
    val checkIn: String,
    val checkOut: String,
    val price: Double,
    val status: String = "OCUPADO"
)

data class HomeUiState(
    val rooms: List<RoomType> = emptyList(),
    val cartItems: List<RoomType> = emptyList(),
    val reservations: List<Reservation> = emptyList(),
    val isLoggedIn: Boolean = false,
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

class HomeViewModel(
    private val getRoomTypesUseCase: GetRoomTypesUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSession()
        loadRooms()
    }

    fun loadSession() {
        val user = sessionManager.getUser()
        _uiState.update {
            it.copy(
                currentUser = user,
                isLoggedIn = user != null,
                errorMessage = null,
                successMessage = if (user != null) "Bienvenido ${user.name}" else null
            )
        }
    }

    fun loadRooms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = getRoomTypesUseCase()
            result.onSuccess { rooms ->
                _uiState.update { it.copy(rooms = rooms, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun addToCart(room: RoomType) {
        _uiState.update { it.copy(cartItems = it.cartItems + room) }
    }

    fun removeFromCart(room: RoomType) {
        _uiState.update { it.copy(cartItems = it.cartItems - room) }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }

    fun confirmPaymentAndBooking(checkIn: String, checkOut: String) {
        val newReservations = _uiState.value.cartItems.map { room ->
            Reservation(
                id = "RES-${(1000..9999).random()}",
                roomName = room.name,
                checkIn = if (checkIn.isNotBlank() && checkIn != "Seleccionar fecha") checkIn else "2026-03-30",
                checkOut = if (checkOut.isNotBlank() && checkOut != "Seleccionar fecha") checkOut else "2026-04-02",
                price = room.price,
                status = "OCUPADO"
            )
        }
        _uiState.update {
            it.copy(
                cartItems = emptyList(),
                reservations = it.reservations + newReservations,
                successMessage = "¡Pago Realizado con Éxito! Reserva Confirmada."
            )
        }
    }

    fun toggleLogin(status: Boolean) {
        if (!status) {
            sessionManager.clear()
            _uiState.update { it.copy(isLoggedIn = false, currentUser = null) }
        } else {
            loadSession()
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
