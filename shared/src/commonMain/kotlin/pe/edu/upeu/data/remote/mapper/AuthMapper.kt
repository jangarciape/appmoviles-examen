package pe.edu.upeu.data.remote.mapper

import pe.edu.upeu.data.remote.dto.AuthResponse
import pe.edu.upeu.data.remote.dto.UserDto
import pe.edu.upeu.domain.model.User

fun AuthResponse.toDomain(): User {
    val detectedRole = this.user.roles
        .firstOrNull { it.id.equals("ADMIN", ignoreCase = true) }
        ?.id ?: this.user.roles.firstOrNull()?.id ?: "CLIENT"
    return User(
        id = this.user.id.toString(),
        name = this.user.name,
        lastName = this.user.lastname,
        email = this.user.email,
        phone = this.user.phone,
        token = this.token,
        image = this.user.image,
        role = detectedRole
    )
}

fun UserDto.toDomain(token: String? = null): User {
    val detectedRole = this.roles
        .firstOrNull { it.id.equals("ADMIN", ignoreCase = true) }
        ?.id ?: this.roles.firstOrNull()?.id ?: "CLIENT"
    return User(
        id = this.id.toString(),
        name = this.name,
        lastName = this.lastname,
        email = this.email,
        phone = this.phone,
        token = token,
        image = this.image,
        role = detectedRole
    )
}
