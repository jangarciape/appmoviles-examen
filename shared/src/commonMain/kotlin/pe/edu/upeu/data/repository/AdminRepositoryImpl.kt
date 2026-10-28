package pe.edu.upeu.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import pe.edu.upeu.data.local.SessionManager
import pe.edu.upeu.data.remote.api.ApiClient
import pe.edu.upeu.data.remote.dto.AdminDashboardDto
import pe.edu.upeu.data.remote.dto.UserDto
import pe.edu.upeu.data.remote.mapper.toDomain
import pe.edu.upeu.domain.model.AdminDashboard
import pe.edu.upeu.domain.model.User
import pe.edu.upeu.domain.repository.AdminRepository

class AdminRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) : AdminRepository {
    private val baseUrl = ApiClient.getBaseUrl()

    private fun authHeader(): String {
        val token = sessionManager.getToken()?.takeIf(String::isNotBlank)
            ?: throw IllegalStateException("Inicia sesión con una cuenta administradora.")
        return if (token.startsWith("Bearer ", ignoreCase = true)) token else "Bearer $token"
    }

    override suspend fun getProfile(): Result<User> {
        return try {
            val response = client.get("$baseUrl/api/users/me") {
                header(HttpHeaders.Authorization, authHeader())
            }
            if (!response.status.isSuccess()) {
                return Result.failure(
                    Exception("No se pudo cargar el perfil admin: ${response.bodyAsText()}")
                )
            }
            Result.success(response.body<UserDto>().toDomain(sessionManager.getToken()))
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    override suspend fun getDashboard(): Result<AdminDashboard> {
        return try {
            val response = client.get("$baseUrl/api/admin/dashboard") {
                header(HttpHeaders.Authorization, authHeader())
            }
            if (!response.status.isSuccess()) {
                return Result.failure(
                    Exception("No se pudieron cargar las métricas: ${response.bodyAsText()}")
                )
            }
            Result.success(response.body<AdminDashboardDto>().toDomain())
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}

private fun AdminDashboardDto.toDomain() = AdminDashboard(
    totalUsers = totalUsers,
    totalRoomTypes = totalRoomTypes,
    totalRooms = totalRooms,
    availableRooms = availableRooms,
    totalBookings = totalBookings
)
