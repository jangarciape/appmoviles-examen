package pe.edu.upeu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Int = 0,
    val roomNumber: String,
    val roomTypeId: Int,
    val status: String = "available",
    val floor: Int? = null
)
