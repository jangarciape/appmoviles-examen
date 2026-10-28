package pe.edu.upeu.domain.usecase

import pe.edu.upeu.domain.model.Room
import pe.edu.upeu.domain.repository.RoomRepository

class CreateRoomUseCase(private val repository: RoomRepository) {
    suspend operator fun invoke(
        roomNumber: String,
        roomTypeId: Int,
        status: String = "available",
        floor: Int? = null
    ): Result<Room> {
        return repository.createRoom(roomNumber, roomTypeId, status, floor)
    }
}
