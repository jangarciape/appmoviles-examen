package pe.edu.upeu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomTypeDtoRequest(
    val name: String,
    val description: String,
    val capacity: Int,
    val price_per_night: Double
)

@Serializable
data class CreateRoomDtoRequest(
    val room_number: String,
    val roomTypeId: Int,
    val floor: Int? = null,
    val status: String = "available"
)

@Serializable
data class RoomDto(
    val id: Int = 0,
    val room_number: String? = null,
    val roomNumber: String? = null,
    val room_type_id: Int? = null,
    val roomTypeId: Int? = null,
    val status: String = "available",
    val floor: Int? = null
)

@Serializable
data class AdminDashboardDto(
    val totalUsers: Int,
    val totalRoomTypes: Int,
    val totalRooms: Int,
    val availableRooms: Int,
    val totalBookings: Int
)
