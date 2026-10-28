package pe.edu.upeu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.domain.model.AdminDashboard
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.domain.model.User
import pe.edu.upeu.domain.usecase.GetAdminDashboardUseCase
import pe.edu.upeu.domain.usecase.GetAdminProfileUseCase
import pe.edu.upeu.domain.usecase.CreateRoomTypeUseCase
import pe.edu.upeu.domain.usecase.CreateRoomUseCase
import pe.edu.upeu.domain.usecase.GetRoomTypesUseCase

data class AdminUiState(
    val isLoading: Boolean = false,
    val isRoomTypesLoading: Boolean = false,
    val isProfileLoading: Boolean = false,
    val isDashboardLoading: Boolean = false,
    val roomTypes: List<RoomType> = emptyList(),
    val adminProfile: User? = null,
    val dashboard: AdminDashboard? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val profileError: String? = null,
    val dashboardError: String? = null
)

class AdminViewModel(
    private val createRoomTypeUseCase: CreateRoomTypeUseCase,
    private val createRoomUseCase: CreateRoomUseCase,
    private val getRoomTypesUseCase: GetRoomTypesUseCase,
    private val getAdminProfileUseCase: GetAdminProfileUseCase,
    private val getAdminDashboardUseCase: GetAdminDashboardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    fun loadAdminData() {
        loadAdminProfile()
        loadDashboard()
        loadRoomTypes()
    }

    fun loadRoomTypes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRoomTypesLoading = true, errorMessage = null) }
            val result = getRoomTypesUseCase()
            result.onSuccess { rooms ->
                _uiState.update { it.copy(roomTypes = rooms, isRoomTypesLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isRoomTypesLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun loadAdminProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProfileLoading = true, profileError = null) }
            getAdminProfileUseCase().onSuccess { profile ->
                _uiState.update {
                    it.copy(adminProfile = profile, isProfileLoading = false)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isProfileLoading = false,
                        profileError = error.message ?: "No se pudo cargar el perfil admin"
                    )
                }
            }
        }
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDashboardLoading = true, dashboardError = null) }
            getAdminDashboardUseCase().onSuccess { dashboard ->
                _uiState.update {
                    it.copy(dashboard = dashboard, isDashboardLoading = false)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isDashboardLoading = false,
                        dashboardError = error.message ?: "No se pudieron cargar las métricas"
                    )
                }
            }
        }
    }

    fun createRoomType(
        name: String,
        description: String,
        capacity: Int,
        pricePerNight: Double,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            val result = createRoomTypeUseCase(name, description, capacity, pricePerNight)
            result.onSuccess { roomType ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "¡Tipo de habitación '${roomType.name}' creado con éxito!"
                    )
                }
                loadRoomTypes()
                loadDashboard()
                onSuccess()
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al crear tipo de habitación"
                    )
                }
            }
        }
    }

    fun createRoom(
        roomNumber: String,
        roomTypeId: Int,
        status: String,
        floor: Int?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            val result = createRoomUseCase(roomNumber, roomTypeId, status, floor)
            result.onSuccess { room ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "¡Cuarto '${room.roomNumber}' creado con éxito!"
                    )
                }
                loadDashboard()
                onSuccess()
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al crear cuarto"
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
