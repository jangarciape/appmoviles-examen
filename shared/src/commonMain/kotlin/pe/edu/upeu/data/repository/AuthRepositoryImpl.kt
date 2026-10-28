package pe.edu.upeu.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import pe.edu.upeu.data.local.SessionManager
import pe.edu.upeu.data.remote.api.ApiClient
import pe.edu.upeu.data.remote.dto.AuthResponse
import pe.edu.upeu.data.remote.dto.LoginRequest
import pe.edu.upeu.data.remote.dto.RegisterRequest
import pe.edu.upeu.data.remote.mapper.toDomain
import pe.edu.upeu.domain.model.User
import pe.edu.upeu.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) : AuthRepository {

    private val baseUrl = ApiClient.getBaseUrl()

    override suspend fun register(
        name: String,
        lastName: String,
        email: String,
        phone: String,
        password: String
    ): Result<User> {
        return try {
            val response = client.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterRequest(
                        name = name,
                        lastname = lastName,
                        email = email,
                        phone = phone,
                        password = password
                    )
                )
            }

            if (response.status.isSuccess()) {
                val authResponse = response.body<AuthResponse>()
                Result.success(authResponse.toDomain())
            } else {
                Result.failure(Exception("Failed to register: ${response.bodyAsText()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return try {
            val response = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginRequest(
                        email = email,
                        password = password
                    )
                )
            }

            if (response.status.isSuccess()) {
                val authResponse = response.body<AuthResponse>()
                Result.success(authResponse.toDomain())
            } else {
                Result.failure(Exception("Error de autenticación: ${response.bodyAsText()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(
        name: String,
        lastName: String,
        phone: String,
        imageBytes: ByteArray?
    ): Result<User> {
        TODO("Not yet implemented")
    }
}
