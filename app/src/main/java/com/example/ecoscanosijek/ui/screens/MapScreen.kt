package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.model.ReportStatus
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.viewmodel.AuthViewModel
import com.example.ecoscanosijek.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    authViewModel: AuthViewModel
) {
    val reports by viewModel.allReports.collectAsState()
    val user by authViewModel.currentUser.collectAsState()

    var selectedReport by remember { mutableStateOf<Report?>(null) }

    val osijek = LatLng(45.5550, 18.6955)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(osijek, 13f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = false
            ),
            onMapClick = { selectedReport = null }
        ) {
            reports.forEach { report ->
                if (report.latitude != 0.0 && report.longitude != 0.0) {
                    Marker(
                        state = MarkerState(
                            position = LatLng(report.latitude, report.longitude)
                        ),
                        title = "Status: ${statusText(report.status)}",
                        snippet = null,
                        onClick = {
                            selectedReport = report
                            false
                        }
                    )
                }
            }
        }

        MapTopSummary(
            reportCount = reports.size,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        )

        selectedReport?.let { report ->
            ReportMapPreviewCard(
                report = report,
                isWorker = user?.role == UserRole.WORKER,
                onClose = { selectedReport = null },
                onDelete = {
                    viewModel.deleteReport(report.id)
                    selectedReport = null
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }

        if (reports.isEmpty()) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Nema prijava za prikaz",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun MapTopSummary(
    reportCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "EcoScan Osijek",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            AssistChip(
                onClick = {},
                label = { Text("$reportCount prijava") }
            )
        }
    }
}

@Composable
private fun ReportMapPreviewCard(
    report: Report,
    isWorker: Boolean,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                if (report.imageUrl.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(report.imageUrl),
                        contentDescription = "Slika prijave",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(MaterialTheme.shapes.medium),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Flag,
                                contentDescription = null
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusChip(status = report.status)

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Zatvori"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = report.description.ifBlank { "Prijava otpada" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Lat: %.5f, Lon: %.5f".format(
                            report.latitude,
                            report.longitude
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (isWorker) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Ukloni prijavu s mape")
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: ReportStatus) {
    val icon = when (status) {
        ReportStatus.NEW -> Icons.Outlined.Flag
        ReportStatus.IN_PROGRESS -> Icons.Outlined.Pending
        ReportStatus.RESOLVED -> Icons.Outlined.Done
    }

    AssistChip(
        onClick = {},
        label = { Text(statusText(status)) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    )
}

private fun statusText(status: ReportStatus): String {
    return when (status) {
        ReportStatus.NEW -> "Nova"
        ReportStatus.IN_PROGRESS -> "U obradi"
        ReportStatus.RESOLVED -> "Riješena"
    }
}