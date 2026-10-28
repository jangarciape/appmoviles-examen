package pe.edu.upeu.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AmenityDto(
    val id: Int,
    val name: String,
    val description: String
)
@Serializable
data class RoomImageDto(
    val id: Int,
    val url: String,
    val alt_text: String
)

@Serializable
data class RoomTypeDto (
    val id: Int,
    val name: String,
    val description: String,
    val capacity: Int,
    val price_per_night : String,
    val amenities : List<AmenityDto> = emptyList(),
    val images : List<RoomImageDto> = emptyList()
)