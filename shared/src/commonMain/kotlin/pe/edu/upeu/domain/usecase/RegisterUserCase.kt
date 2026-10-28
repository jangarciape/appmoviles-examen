package pe.edu.upeu.domain.usecase

import pe.edu.upeu.domain.model.User
import pe.edu.upeu.domain.repository.AuthRepository

class RegisterUserCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        name: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ): Result<User> {
        return repository.register(name, lastName, email, phone, password)
    }
}
