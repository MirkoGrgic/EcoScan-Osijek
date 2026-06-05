package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoscanosijek.ui.components.EcoButton
import com.example.ecoscanosijek.ui.components.EcoTextField
import com.example.ecoscanosijek.viewmodel.AuthViewModel
import com.example.ecoscanosijek.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReportScreen(
    authViewModel: AuthViewModel,
    reportViewModel: ReportViewModel,
    onBack: () -> Unit
) {
    val user by authViewModel.currentUser.collectAsState()
    var description by remember { mutableStateOf("") }
    
    // Mock location
    val latitude = 45.555
    val longitude = 18.685

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova prijava") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("TODO: Camera Preview/Photo", color = Color.DarkGray)
            }
            
            EcoButton(
                text = "Snimi fotografiju",
                onClick = { /* TODO: Camera */ },
                containerColor = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            EcoTextField(
                value = description,
                onValueChange = { description = it },
                label = "Opis problema"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Lokacija", fontWeight = FontWeight.Bold)
                    Text("Lat: $latitude, Lon: $longitude")
                    Text("TODO: GPS Implementation", fontSize = 10.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            EcoButton(
                text = "Pošalji prijavu",
                onClick = {
                    user?.let {
                        reportViewModel.addReport(description, latitude, longitude, it.id)
                        onBack()
                    }
                }
            )
        }
    }
}
