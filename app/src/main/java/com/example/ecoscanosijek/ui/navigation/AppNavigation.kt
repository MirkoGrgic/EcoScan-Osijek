package com.example.ecoscanosijek.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    
    // Repositories (In a real app, these might be provided by DI)
    val authRepository = remember { AuthRepository() }
    val reportRepository = remember { ReportRepository() }
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

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavScreens.map { it.route }) {
                NavigationBar {
                    bottomNavScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
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
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
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
                MapScreen(viewModel = mapViewModel)
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(viewModel = userViewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = authViewModel,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
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
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
