package com.example.ecoscanosijek.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Login : Screen("login")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Map : Screen("map", "Map", Icons.Default.LocationOn)
    object Leaderboard : Screen("leaderboard", "Rank", Icons.Default.Star)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object NewReport : Screen("new_report")
    object ReportDetails : Screen("report_details/{reportId}") {
        fun createRoute(reportId: String) = "report_details/$reportId"
    }
}
