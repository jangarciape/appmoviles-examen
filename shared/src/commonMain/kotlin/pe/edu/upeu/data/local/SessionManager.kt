package pe.edu.upeu.data.local

import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pe.edu.upeu.domain.model.User

class SessionManager(private val settings: Settings) {
    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER = "user_data"
    }

    fun saveToken(token: String) {
        println("Debug_SessionManager:saveToken for $token")
        settings.putString(KEY_TOKEN, token)
    }

    fun getToken(): String? {
        val token = settings.getStringOrNull(KEY_TOKEN)
        println("Debug_SessionManager:getToken for $token")
        return token
    }



    fun saveUser(user: User) {
        println("Debug_SessionManager:saveUser for ${user.name}")
        val json = Json.encodeToString(user)
        settings.putString(KEY_USER, json)
    }

    fun getUser(): User? {
        val json = settings.getStringOrNull(KEY_USER) ?: return null
        println("Debug_SessionManager:getUser $json")
        return try {
            Json.decodeFromString<User>(json)
        } catch (e: Exception) {
            println("Debug_SessionManager:getUser Error ${e.message}")
            null
        }
    }

    fun clear() {
        settings.remove(KEY_USER)
        settings.remove(KEY_TOKEN)
    }
}
