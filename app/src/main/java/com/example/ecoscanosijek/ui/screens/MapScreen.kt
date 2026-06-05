package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoscanosijek.viewmodel.MapViewModel

@Composable
fun MapScreen(viewModel: MapViewModel) {
    val reports by viewModel.allReports.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0F2F1)), // Light green map-like background
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Karta prijava (Osijek)", style = MaterialTheme.typography.headlineSmall)
                Text("TODO: Google Maps integration", color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Prikazano prijava: ${reports.size}")
                
                // Simulate markers
                reports.forEach { report ->
                    Text("• ${report.description} (${report.latitude}, ${report.longitude})", fontSize = 10.sp)
                }
            }
        }
    }
}
