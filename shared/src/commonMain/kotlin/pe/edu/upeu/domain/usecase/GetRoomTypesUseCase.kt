package pe.edu.upeu.domain.usecase

import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.domain.repository.RoomRepository

class GetRoomTypesUseCase(private val repository: RoomRepository){
    suspend operator fun invoke(): Result<List<RoomType>>{
        return repository.getRooms()
    }
}