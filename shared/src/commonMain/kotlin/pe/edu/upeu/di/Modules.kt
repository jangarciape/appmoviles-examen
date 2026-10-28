package pe.edu.upeu.di

import org.koin.dsl.module
import pe.edu.upeu.data.local.SessionManager
import pe.edu.upeu.data.remote.api.ApiClient
import pe.edu.upeu.data.repository.AuthRepositoryImpl
import pe.edu.upeu.data.repository.AdminRepositoryImpl
import pe.edu.upeu.data.repository.RoomRepositoryImpl
import pe.edu.upeu.domain.repository.AdminRepository
import pe.edu.upeu.domain.repository.AuthRepository
import pe.edu.upeu.domain.repository.RoomRepository
import pe.edu.upeu.domain.usecase.CreateRoomTypeUseCase
import pe.edu.upeu.domain.usecase.CreateRoomUseCase
import pe.edu.upeu.domain.usecase.GetRoomTypesUseCase
import pe.edu.upeu.domain.usecase.GetAdminDashboardUseCase
import pe.edu.upeu.domain.usecase.GetAdminProfileUseCase
import pe.edu.upeu.domain.usecase.LoginUseCase
import pe.edu.upeu.domain.usecase.RegisterUserCase
import pe.edu.upeu.presentation.viewmodel.AdminViewModel
import pe.edu.upeu.presentation.viewmodel.AuthViewModel
import pe.edu.upeu.presentation.viewmodel.HomeViewModel

val appModule = module {
    single { createSettings() }
    // API Cliente & SessionManager
    single { SessionManager(get()) }
    single { ApiClient.httpClient }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<AdminRepository> { AdminRepositoryImpl(get(), get()) }
    // Repositories
    single<RoomRepository> { RoomRepositoryImpl(get(), get()) }
    // UseCases
    factory { GetRoomTypesUseCase(get()) }
    factory { RegisterUserCase(get()) }
    factory { LoginUseCase(get()) }
    factory { CreateRoomTypeUseCase(get()) }
    factory { CreateRoomUseCase(get()) }
    factory { GetAdminProfileUseCase(get()) }
    factory { GetAdminDashboardUseCase(get()) }
    // ViewModels
    factory { HomeViewModel(get(), get()) }
    factory { AuthViewModel(get(), get(), get()) }
    factory { AdminViewModel(get(), get(), get(), get(), get()) }
}
