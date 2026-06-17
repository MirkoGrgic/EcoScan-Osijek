package com.example.ecoscanosijek.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ecoscanosijek.repository.AuthRepository
import com.example.ecoscanosijek.repository.ReportRepository
import com.example.ecoscanosijek.repository.UserRepository
import com.example.ecoscanosijek.ui.screens.*
import com.example.ecoscanosijek.viewmodel.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // Repositories
    val authRepository = remember { AuthRepository() }
    val reportRepository = remember { ReportRepository(context) }
    val userRepository = remember { UserRepository() }
    
    // ViewModels
    val authViewModel = remember { AuthViewModel(authRepository) }
    val reportViewModel = remember { ReportViewModel(reportRepository) }
    val userViewModel = remember { UserViewModel(userRepository) }
    val mapViewModel = remember { MapViewModel(reportRepository) }

    val currentUser by authViewModel.currentUser.collectAsState()

    val bottomNavScreens = listOf(Screen.Home, Screen.Map, Screen.Leaderboard, Screen.Profile)

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(currentUser) {
        if (currentUser != null && currentRoute == Screen.Starter.route) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Starter.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(currentUser) {
        if (currentUser?.role == com.example.ecoscanosijek.model.UserRole.WORKER) {
            mapViewModel.listenForNewReports(context)
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavScreens.map { it.route }) {
                NavigationBar(
                    containerColor = Color(0xFFE8F5E9),
                    tonalElevation = 8.dp
                ){
                    bottomNavScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF1B5E20),
                                selectedTextColor = Color(0xFF1B5E20),
                                indicatorColor = Color(0xFFC8E6C9),
                                unselectedIconColor = Color(0xFF6B7280),
                                unselectedTextColor = Color(0xFF6B7280)
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Starter.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Starter.route) {
                StarterScreen(
                    onLoginClick = {
                        navController.navigate(Screen.Login.route)
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Register.route)
                    },
                    onGuestClick = {
                        authViewModel.continueAsGuest()
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Register.route)
                    },
                    onBackClick = {
                        if(!navController.popBackStack()){
                            navController.navigate(Screen.Starter.route){
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }

                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        if (!navController.popBackStack()) {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    authViewModel = authViewModel,
                    reportViewModel = reportViewModel,
                    onNewReportClick = { navController.navigate(Screen.NewReport.route) },
                    onReportClick = { reportId ->
                        navController.navigate(Screen.ReportDetails.createRoute(reportId))
                    }
                )
            }
            composable(Screen.Map.route) {
                MapScreen(
                    viewModel = mapViewModel,
                    authViewModel = authViewModel
                )
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(viewModel = userViewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = authViewModel,
                    onLogout = {
                        reportViewModel.clearReports()

                        navController.navigate(Screen.Starter.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.NewReport.route) {
                NewReportScreen(
                    authViewModel = authViewModel,
                    reportViewModel = reportViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.ReportDetails.route,
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
                ReportDetailsScreen(
                    reportId = reportId,
                    authViewModel = authViewModel,
                    reportViewModel = reportViewModel,
                    onBack = { navController.popBackStack() },
                    onOpenMap = {
                        navController.navigate(Screen.Map.route)
                    }
                )
            }
        }
    }
}
