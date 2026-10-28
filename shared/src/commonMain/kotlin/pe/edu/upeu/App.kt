package pe.edu.upeu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.di.appModule
import pe.edu.upeu.domain.model.RoomType
import pe.edu.upeu.presentation.BookingDialogContent
import pe.edu.upeu.presentation.CartContent
import pe.edu.upeu.presentation.HomeContent
import pe.edu.upeu.presentation.LoginDialogContent
import pe.edu.upeu.presentation.ProfileUpdateContent
import pe.edu.upeu.presentation.RegisterDialogContent
import pe.edu.upeu.presentation.RoomDetailContent
import pe.edu.upeu.presentation.Screen
import pe.edu.upeu.presentation.SplashContent
import pe.edu.upeu.presentation.WelcomeContent
import pe.edu.upeu.presentation.admin.AdminPanelContent
import pe.edu.upeu.presentation.admin.CreateRoomContent
import pe.edu.upeu.presentation.admin.CreateRoomTypeContent
import pe.edu.upeu.presentation.components.BookingButton
import pe.edu.upeu.presentation.theme.AppTheme
import pe.edu.upeu.presentation.viewmodel.AdminViewModel
import pe.edu.upeu.presentation.viewmodel.AuthViewModel
import pe.edu.upeu.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {

    KoinApplication(application = {
        modules(appModule)
    }) {
        // OBTENCION DEL VIEWMODEL Y ESTADOS GLOBALES
        val homeViewModel = koinViewModel<HomeViewModel>()
        val state by homeViewModel.uiState.collectAsState()
        val authViewModel = koinViewModel<AuthViewModel>()
        val adminViewModel = koinViewModel<AdminViewModel>()

        var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
        var showAuthDialog by remember { mutableStateOf(false) }
        var authMode by remember { mutableStateOf(AuthMode.LOGIN) }
        var showBookingDialog by remember { mutableStateOf(false) }
        var showPaymentDialog by remember { mutableStateOf(false) }
        var selectedRoomForBooking by remember { mutableStateOf<RoomType?>(null) }
        var totalToPay by remember { mutableStateOf(0.0) }
        var bookingCheckIn by remember { mutableStateOf("") }
        var bookingCheckOut by remember { mutableStateOf("") }

        AppTheme { // aplicamos el tema construido

            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                when (currentScreen) {
                    is Screen.Splash -> SplashContent(
                        onTimeout = {
                            currentScreen = Screen.Welcome
                        }
                    )

                    is Screen.Welcome -> WelcomeContent(
                        onStartClick = {
                            currentScreen = Screen.Home
                        }
                    )

                    is Screen.Home -> HomeContent(
                        viewModel = homeViewModel,
                        isLoggedIn = state.isLoggedIn,
                        onLoginClick = {
                            authMode = AuthMode.LOGIN
                            showAuthDialog = true
                        },
                        onLogout = {
                            homeViewModel.toggleLogin(false)
                        },
                        onEditProfile = { currentScreen = Screen.ProfileUpdate },
                        onReserveClick = { room -> currentScreen = Screen.RoomDetail(room) },
                        onCartClick = { currentScreen = Screen.Cart },
                        cartSize = state.cartItems.size,
                        onAdminClick = { currentScreen = Screen.AdminPanel }
                    )

                    is Screen.RoomDetail -> RoomDetailContent(
                        room = (currentScreen as Screen.RoomDetail).room,
                        onBack = { currentScreen = Screen.Home },
                        onAddToCart = { room ->
                            homeViewModel.addToCart(room)
                            currentScreen = Screen.Home
                        }
                    )

                    is Screen.Cart -> CartContent(
                        cartItems = state.cartItems,
                        onBack = { currentScreen = Screen.Home },
                        onRemoveItem = { room -> homeViewModel.removeFromCart(room) },
                        onCheckout = { showBookingDialog = true }
                    )

                    is Screen.ProfileUpdate -> ProfileUpdateContent(
                        onBack = { currentScreen = Screen.Home },
                        onSave = { bytes ->
                            currentScreen = Screen.Home
                        }
                    )

                    // Navegación de Administración (Examen Práctico)
                    is Screen.AdminPanel -> AdminPanelContent(
                        viewModel = adminViewModel,
                        user = state.currentUser,
                        onBack = { currentScreen = Screen.Home },
                        onNavigateToCreateRoomType = { currentScreen = Screen.CreateRoomType },
                        onNavigateToCreateRoom = { currentScreen = Screen.CreateRoom }
                    )

                    is Screen.CreateRoomType -> CreateRoomTypeContent(
                        viewModel = adminViewModel,
                        onBack = { currentScreen = Screen.AdminPanel }
                    )

                    is Screen.CreateRoom -> CreateRoomContent(
                        viewModel = adminViewModel,
                        onBack = { currentScreen = Screen.AdminPanel }
                    )
                }

                // Gestion de modales
                if (showAuthDialog) {
                    BasicAlertDialog(
                        onDismissRequest = { showAuthDialog = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Surface(shape = RoundedCornerShape(28.dp), tonalElevation = 6.dp) {
                            if (authMode == AuthMode.LOGIN) {
                                LoginDialogContent(
                                    viewModel = authViewModel,
                                    onNavigateToRegister = { authMode = AuthMode.REGISTER },
                                    onLoginSuccess = {
                                        homeViewModel.toggleLogin(status = true)
                                        showAuthDialog = false
                                    }
                                )
                            } else {
                                RegisterDialogContent(
                                    viewModel = authViewModel,
                                    onNavigationToLogin = { authMode = AuthMode.LOGIN },
                                    onRegisterSuccess = { authMode = AuthMode.LOGIN }
                                )
                            }
                        }
                    }
                }

                // Diálogo de Fechas (Calendario)
                if (showBookingDialog) {
                    BasicAlertDialog(onDismissRequest = { showBookingDialog = false }) {
                        Surface(shape = RoundedCornerShape(28.dp)) {
                            BookingDialogContent(
                                cartItems = state.cartItems,
                                onConfirm = { checkIn, checkOut, guests ->
                                    bookingCheckIn = checkIn
                                    bookingCheckOut = checkOut
                                    showBookingDialog = false
                                    totalToPay = state.cartItems.sumOf { it.price }
                                    showPaymentDialog = true
                                }
                            )
                        }
                    }
                }

                // Diálogo de Pago Realizado / Procesar Reserva
                if (showPaymentDialog) {
                    BasicAlertDialog(onDismissRequest = { showPaymentDialog = false }) {
                        Surface(shape = RoundedCornerShape(28.dp), tonalElevation = 6.dp) {
                            Column(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Procesar Pago", style = MaterialTheme.typography.headlineSmall)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Habitaciones: ${state.cartItems.size}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "Monto Total: S/ $totalToPay",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(24.dp))
                                BookingButton(
                                    text = "Confirmar Pago (S/ $totalToPay)",
                                    onClick = {
                                        homeViewModel.confirmPaymentAndBooking(bookingCheckIn, bookingCheckOut)
                                        showPaymentDialog = false
                                        currentScreen = Screen.Home
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
