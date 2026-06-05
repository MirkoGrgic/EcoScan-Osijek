package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.viewmodel.AuthViewModel
import com.example.ecoscanosijek.viewmodel.ReportViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    reportViewModel: ReportViewModel,
    onNewReportClick: () -> Unit,
    onReportClick: (String) -> Unit
) {
    val user by authViewModel.currentUser.collectAsState()
    val reports by reportViewModel.reports.collectAsState()

    LaunchedEffect(user) {
        reportViewModel.loadReports(user)
    }

    Scaffold(
        floatingActionButton = {
            if (user?.role == UserRole.CITIZEN) {
                FloatingActionButton(
                    onClick = onNewReportClick,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Report")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Pozdrav, ${user?.username ?: "Gost"}!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Statistics Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Ukupno", reports.size.toString(), Modifier.weight(1f))
                StatCard("Riješeno", reports.count { it.status == com.example.ecoscanosijek.model.ReportStatus.RESOLVED }.toString(), Modifier.weight(1f))
                if (user?.role == UserRole.CITIZEN) {
                    StatCard("Bodovi", user?.points?.toString() ?: "0", Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (user?.role == UserRole.MUNICIPAL_WORKER) "Prijave za pregled" else "Moje nedavne prijave",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reports) { report ->
                    ReportItem(report = report, onClick = { onReportClick(report.id) })
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = label, fontSize = 12.sp)
        }
    }
}

@Composable
fun ReportItem(report: Report, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = report.description, fontWeight = FontWeight.Medium, maxLines = 1)
                Text(text = "Status: ${report.status}", fontSize = 12.sp, color = Color.Gray)
            }
            val sdf = remember { java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()) }
            Text(
                text = sdf.format(java.util.Date(report.createdAt)),
                fontSize = 12.sp
            )
        }
    }
}
