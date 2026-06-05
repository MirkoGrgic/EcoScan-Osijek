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
import com.example.ecoscanosijek.model.ReportStatus
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.viewmodel.AuthViewModel
import com.example.ecoscanosijek.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailsScreen(
    reportId: String,
    authViewModel: AuthViewModel,
    reportViewModel: ReportViewModel,
    onBack: () -> Unit
) {
    val user by authViewModel.currentUser.collectAsState()
    val report = remember(reportId) { reportViewModel.getReportById(reportId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalji prijave") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (report == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Prijava nije pronađena")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Fotografija prijave", color = Color.DarkGray)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Opis", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = report.description)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Lokacija", fontWeight = FontWeight.Bold)
                Text(text = "Lat: ${report.latitude}, Lon: ${report.longitude}")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Datum", fontWeight = FontWeight.Bold)
                val sdf = remember { java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault()) }
                Text(text = sdf.format(java.util.Date(report.createdAt)))

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Status", fontWeight = FontWeight.Bold)
                Text(text = report.status.name, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.height(24.dp))

                if (user?.role == UserRole.MUNICIPAL_WORKER) {
                    Text(text = "Promijeni status:", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ReportStatus.entries.forEach { status ->
                            Button(
                                onClick = { reportViewModel.updateReportStatus(report.id, status) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (report.status == status) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(text = status.name, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
