package pe.edu.upeu

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import pe.edu.upeu.data.remote.dto.RoleDto
import pe.edu.upeu.data.remote.dto.CreateRoomDtoRequest
import pe.edu.upeu.data.remote.dto.RoomDto
import pe.edu.upeu.data.remote.dto.UserDto
import pe.edu.upeu.data.remote.mapper.toDomain
import kotlin.test.Test
import kotlin.test.assertEquals

class AdminApiDtoTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun createRoomRequestUsesOnlyBackendFieldNames() {
        val request = CreateRoomDtoRequest(
            room_number = "20",
            roomTypeId = 3,
            floor = 2,
            status = "occupied"
        )

        val payload = json.parseToJsonElement(json.encodeToString(request)).jsonObject

        assertEquals(setOf("room_number", "roomTypeId", "floor", "status"), payload.keys)
        assertEquals("\"occupied\"", payload.getValue("status").toString())
    }

    @Test
    fun roomResponseReadsSnakeCaseTypeId() {
        val response = json.decodeFromString<RoomDto>(
            """{"id":7,"room_number":"20","room_type_id":3,"floor":2,"status":"available"}"""
        )

        assertEquals("20", response.room_number)
        assertEquals(3, response.room_type_id)
        assertEquals(2, response.floor)
    }

    @Test
    fun profileMapperUsesActualRolesInsteadOfDefaultingEveryoneToAdmin() {
        val user = UserDto(
            id = 4,
            name = "Test",
            lastname = "User",
            email = "test@example.com",
            phone = "123456789",
            roles = listOf(RoleDto(id = "CLIENT"))
        )

        assertEquals("CLIENT", user.toDomain().role)
    }

    @Test
    fun profileMapperPrefersAdminWhenUserHasSeveralRoles() {
        val user = UserDto(
            id = 5,
            name = "Admin",
            lastname = "User",
            email = "admin@example.com",
            phone = "987654321",
            roles = listOf(RoleDto(id = "CLIENT"), RoleDto(id = "ADMIN"))
        )

        assertEquals("ADMIN", user.toDomain().role)
    }
}
