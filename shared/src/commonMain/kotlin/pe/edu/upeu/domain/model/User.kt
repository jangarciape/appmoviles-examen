package pe.edu.upeu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val token: String? = null,
    val image: String? = null,
    val role: String = "ADMIN"
)
