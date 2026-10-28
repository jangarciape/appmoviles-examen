package pe.edu.upeu.domain.repository

import pe.edu.upeu.domain.model.AdminDashboard
import pe.edu.upeu.domain.model.User

interface AdminRepository {
    suspend fun getProfile(): Result<User>
    suspend fun getDashboard(): Result<AdminDashboard>
}
