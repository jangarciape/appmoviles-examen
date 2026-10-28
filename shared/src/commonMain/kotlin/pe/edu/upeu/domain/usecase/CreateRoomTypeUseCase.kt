package pe.edu.upeu.domain.usecase

import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.domain.repository.RoomRepository

class CreateRoomTypeUseCase(private val repository: RoomRepository) {
    suspend operator fun invoke(
        name: String,
        description: String,
        capacity: Int,
        pricePerNight: Double
    ): Result<RoomType> {
        return repository.createRoomType(name, description, capacity, pricePerNight)
    }
}
