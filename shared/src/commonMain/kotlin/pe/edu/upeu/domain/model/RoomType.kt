package pe.edu.upeu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Amenity(
    val id: Int,
    val name: String,
    val description: String
)
@Serializable
data class RoomImage(
    val id: Int,
    val url: String,
    val altText: String
)

@Serializable
data class RoomType (
    val id: Int,
    val name: String,
    val description: String,
    val capacity: Int,
    val price : Double,
    val amenities : List<Amenity> = emptyList(),
    val images : List<RoomImage> = emptyList()
)