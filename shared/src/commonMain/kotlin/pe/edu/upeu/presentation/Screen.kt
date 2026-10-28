package pe.edu.upeu.presentation

import pe.edu.upeu.domain.model.RoomType

sealed class Screen {
    data object Splash : Screen()
    data object Welcome : Screen()
    data object Home : Screen()
    data class RoomDetail(val room: RoomType) : Screen()
    data object Cart : Screen()
    data object ProfileUpdate : Screen()

    // Admin Screens (Rúbrica de evaluación)
    data object AdminPanel : Screen()
    data object CreateRoomType : Screen()
    data object CreateRoom : Screen()
}
