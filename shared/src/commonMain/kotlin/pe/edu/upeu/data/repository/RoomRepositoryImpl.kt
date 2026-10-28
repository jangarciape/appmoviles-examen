package pe.edu.upeu.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import pe.edu.upeu.data.local.SessionManager
import pe.edu.upeu.data.remote.api.ApiClient
import pe.edu.upeu.data.remote.dto.CreateRoomDtoRequest
import pe.edu.upeu.data.remote.dto.CreateRoomTypeDtoRequest
import pe.edu.upeu.data.remote.dto.RoomDto
import pe.edu.upeu.data.remote.dto.RoomTypeDto
import pe.edu.upeu.data.remote.mapper.toDomain
import pe.edu.upeu.domain.model.Room
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.domain.repository.RoomRepository

class RoomRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) : RoomRepository {
    private val baseUrl = ApiClient.getBaseUrl()

    private fun formatAuthHeader(token: String?): String? {
        if (token.isNullOrBlank()) return null
        return if (token.startsWith("Bearer ", ignoreCase = true)) {
            token
        } else {
            "Bearer $token"
        }
    }

    override suspend fun getRooms(): Result<List<RoomType>> {
        return try {
            val response = client.get("$baseUrl/api/room-types") {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                val roomTypes = response.body<List<RoomTypeDto>>()
                Result.success(roomTypes.map { it.toDomain() })
            } else {
                Result.failure(Exception("Failed to fetch rooms: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRoomType(
        name: String,
        description: String,
        capacity: Int,
        pricePerNight: Double
    ): Result<RoomType> {
        return try {
            val token = sessionManager.getToken()
            val authHeader = formatAuthHeader(token)
            val response = client.post("$baseUrl/api/room-types") {
                contentType(ContentType.Application.Json)
                if (authHeader != null) {
                    header(HttpHeaders.Authorization, authHeader)
                }
                setBody(
                    CreateRoomTypeDtoRequest(
                        name = name,
                        description = description,
                        capacity = capacity,
                        price_per_night = pricePerNight
                    )
                )
            }
            if (response.status.isSuccess()) {
                val dto = response.body<RoomTypeDto>()
                Result.success(dto.toDomain())
            } else {
                val errorBody = response.bodyAsText()
                println("❌ Error al crear tipo de habitación: $errorBody")
                Result.failure(Exception("Error al crear tipo de habitación: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRoom(
        roomNumber: String,
        roomTypeId: Int,
        status: String,
        floor: Int?
    ): Result<Room> {
        return try {
            val token = sessionManager.getToken()
            val authHeader = formatAuthHeader(token)
            val response = client.post("$baseUrl/api/rooms") {
                contentType(ContentType.Application.Json)
                if (authHeader != null) {
                    header(HttpHeaders.Authorization, authHeader)
                }
                setBody(
                    CreateRoomDtoRequest(
                        room_number = roomNumber,
                        roomTypeId = roomTypeId,
                        floor = floor,
                        status = status.lowercase()
                    )
                )
            }
            if (response.status.isSuccess()) {
                val dto = response.body<RoomDto>()
                Result.success(
                    Room(
                        id = dto.id,
                        roomNumber = dto.roomNumber ?: dto.room_number ?: roomNumber,
                        roomTypeId = dto.roomTypeId ?: dto.room_type_id ?: roomTypeId,
                        status = dto.status,
                        floor = dto.floor
                    )
                )
            } else {
                val errorBody = response.bodyAsText()
                println("❌ Error al crear cuarto: $errorBody")
                Result.failure(Exception("Error al crear cuarto: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
