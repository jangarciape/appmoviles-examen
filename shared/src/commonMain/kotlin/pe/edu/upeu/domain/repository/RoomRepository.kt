package pe.edu.upeu.domain.repository

import pe.edu.upeu.domain.model.Room
import pe.edu.upeu.domain.model.RoomType

interface RoomRepository {
    suspend fun getRooms(): Result<List<RoomType>>
    suspend fun createRoomType(name: String, description: String, capacity: Int, pricePerNight: Double): Result<RoomType>
    suspend fun createRoom(
        roomNumber: String,
        roomTypeId: Int,
        status: String,
        floor: Int? = null
    ): Result<Room>
}
