package pe.edu.upeu.domain.usecase

import pe.edu.upeu.domain.repository.AdminRepository

class GetAdminDashboardUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke() = repository.getDashboard()
}
