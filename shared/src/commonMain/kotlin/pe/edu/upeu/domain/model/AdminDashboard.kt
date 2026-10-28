package pe.edu.upeu.domain.model

data class AdminDashboard(
    val totalUsers: Int,
    val totalRoomTypes: Int,
    val totalRooms: Int,
    val availableRooms: Int,
    val totalBookings: Int
)
