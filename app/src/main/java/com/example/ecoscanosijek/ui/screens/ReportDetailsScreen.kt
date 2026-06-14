package com.example.ecoscanosijek.ui.screens

import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecoscanosijek.model.ReportStatus
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.viewmodel.AuthViewModel
import com.example.ecoscanosijek.viewmodel.ReportViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ReportDetailsScreen(
    reportId: String,
    authViewModel: AuthViewModel,
    reportViewModel: ReportViewModel,
    onBack: () -> Unit,
    onOpenMap: () -> Unit
) {
    val user by authViewModel.currentUser.collectAsState()
    var report by remember { mutableStateOf<com.example.ecoscanosijek.model.Report?>(null) }

    LaunchedEffect(reportId) {
        reportViewModel.getReportById(reportId) { fetchedReport ->
            report = fetchedReport
        }
    }

    val currentReport = report
    val context = LocalContext.current

    var addressText by remember(currentReport?.latitude, currentReport?.longitude) {
        mutableStateOf("Dohvaćanje lokacije...")
    }

    LaunchedEffect(currentReport?.latitude, currentReport?.longitude) {
        if (currentReport != null) {
            try {
                val geocoder = Geocoder(context, java.util.Locale.getDefault())
                val result = geocoder.getFromLocation(
                    currentReport.latitude,
                    currentReport.longitude,
                    1
                )

                val address = result?.firstOrNull()

                addressText = when {
                    address?.thoroughfare != null && address.locality != null ->
                        "${address.thoroughfare}, ${address.locality}"

                    address?.subLocality != null && address.locality != null ->
                        "${address.subLocality}, ${address.locality}"

                    address?.locality != null ->
                        address.locality

                    address?.adminArea != null ->
                        address.adminArea

                    else -> "Lokacija nije pronađena"
                }
            } catch (e: Exception) {
                addressText = "Lokacija nije dostupna"
            }
        }
    }

    if (currentReport == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Prijava nije pronađena")
        }
        return
    }

    val statusInfo = getStatusInfo(currentReport.status)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            if (currentReport.imageUrl.isNotBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(currentReport.imageUrl),
                    contentDescription = "Fotografija prijave",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE5E7EB)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Nema fotografije", color = Color.Gray)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.45f)
                            )
                        )
                    )
            )

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(start = 16.dp, top = 42.dp)
                    .align(Alignment.TopStart)
                    .background(Color.White.copy(alpha = 0.92f), MaterialTheme.shapes.medium)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Natrag",
                    tint = Color(0xFF374151)
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp),
                shape = CircleShape,
                color = statusInfo.background,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = statusInfo.icon,
                        contentDescription = null,
                        tint = statusInfo.color,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = statusInfo.label,
                        color = statusInfo.color,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                shape = MaterialTheme.shapes.small,
                color = Color.Black.copy(alpha = 0.35f)
            ) {
                Text(
                    text = "#${currentReport.id.take(6)}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                text = currentReport.description.ifBlank { "Prijava otpada" },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E),
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MiniMetaItem(
                    icon = Icons.Outlined.LocationOn,
                    text = addressText
                )

                val sdf = remember {
                    java.text.SimpleDateFormat(
                        "dd.MM.yyyy",
                        java.util.Locale.getDefault()
                    )
                }

                MiniMetaItem(
                    icon = Icons.Outlined.CalendarMonth,
                    text = sdf.format(java.util.Date(currentReport.createdAt))
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            StatusProgressCard(statusInfo = statusInfo)

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                title = "Opis",
                text = currentReport.description.ifBlank { "Nema opisa." }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LocationCard(
                latitude = currentReport.latitude,
                longitude = currentReport.longitude,
                addressText = addressText,
                onOpenMap = onOpenMap
            )

            if (user?.role == UserRole.WORKER) {
                Spacer(modifier = Modifier.height(16.dp))

                WorkerStatusCard(
                    currentStatus = currentReport.status,
                    onStatusChange = { status ->
                        if (currentReport.status != ReportStatus.RESOLVED && status != currentReport.status) {
                            reportViewModel.updateReportStatus(
                                currentReport.id,
                                status,
                                user?.id
                            )

                            report = currentReport.copy(status = status)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MiniMetaItem(
    icon: ImageVector,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(15.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF)
        )
    }
}

@Composable
private fun StatusProgressCard(statusInfo: StatusInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status prijave",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF374151)
                )

                Text(
                    text = "${statusInfo.progress}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = statusInfo.color
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { statusInfo.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(CircleShape),
                color = statusInfo.barColor,
                trackColor = Color(0xFFF3F4F6)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = statusInfo.description,
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProgressStep(
                    label = "Prijavljeno",
                    active = statusInfo.progress >= 10,
                    color = statusInfo.barColor
                )
                ProgressStep(
                    label = "U obradi",
                    active = statusInfo.progress >= 55,
                    color = statusInfo.barColor
                )
                ProgressStep(
                    label = "Riješeno",
                    active = statusInfo.progress >= 100,
                    color = statusInfo.barColor
                )
            }
        }
    }
}

@Composable
private fun ProgressStep(
    label: String,
    active: Boolean,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = if (active) color else Color(0xFFE5E7EB)
        ) {
            Box(
                modifier = Modifier.size(22.dp),
                contentAlignment = Alignment.Center
            ) {
                if (active) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 9.sp,
            color = if (active) color else Color(0xFF9CA3AF),
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                fontSize = 13.sp,
                color = Color(0xFF6B7280),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun LocationCard(
    latitude: Double,
    longitude: Double,
    addressText: String,
    onOpenMap: () -> Unit
) {
    val position = LatLng(latitude, longitude)

    val cameraPositionState = rememberCameraPositionState {
        this.position = CameraPosition.fromLatLngZoom(position, 15f)
    }

    val markerState = remember(position) {
        MarkerState(position = position)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenMap() },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = false
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = false,
                        scrollGesturesEnabled = false,
                        zoomGesturesEnabled = false,
                        tiltGesturesEnabled = false
                    )
                ) {
                    Marker(
                        state = markerState,
                        title = "Lokacija prijave"
                    )
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "Otvori kartu",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = addressText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151)
                )
            }
        }
    }
}

@Composable
private fun WorkerStatusCard(
    currentStatus: ReportStatus,
    onStatusChange: (ReportStatus) -> Unit
) {
    val isLocked = currentStatus == ReportStatus.RESOLVED

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (isLocked) "Status zaključan" else "Promijeni status",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )

            if (isLocked) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Ova prijava je označena kao riješena i više se ne može vratiti na prethodni status.",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ReportStatus.entries.forEach { status ->
                val info = getStatusInfo(status)
                val enabled = !isLocked && status != currentStatus

                Button(
                    onClick = { onStatusChange(status) },
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .height(44.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (currentStatus == status) info.barColor else info.background,
                        contentColor =
                            if (currentStatus == status) Color.White else info.color,
                        disabledContainerColor =
                            if (currentStatus == status) info.barColor.copy(alpha = 0.75f)
                            else Color(0xFFE5E7EB),
                        disabledContentColor =
                            if (currentStatus == status) Color.White.copy(alpha = 0.9f)
                            else Color(0xFF9CA3AF)
                    )
                ) {
                    Icon(
                        imageVector = info.icon,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(info.label)
                }
            }
        }
    }
}

private data class StatusInfo(
    val label: String,
    val background: Color,
    val color: Color,
    val icon: ImageVector,
    val barColor: Color,
    val progress: Int,
    val description: String
)

private fun getStatusInfo(status: ReportStatus): StatusInfo {
    return when (status) {
        ReportStatus.NEW -> StatusInfo(
            label = "Novo",
            background = Color(0xFFFFF3E0),
            color = Color(0xFFE65100),
            icon = Icons.Outlined.Flag,
            barColor = Color(0xFFFF9800),
            progress = 10,
            description = "Prijava je zaprimljena i čeka pregled komunalne službe."
        )

        ReportStatus.IN_PROGRESS -> StatusInfo(
            label = "U obradi",
            background = Color(0xFFE3F2FD),
            color = Color(0xFF1565C0),
            icon = Icons.Outlined.Pending,
            barColor = Color(0xFF2196F3),
            progress = 55,
            description = "Komunalna služba je obaviještena i radi na rješavanju prijave."
        )

        ReportStatus.RESOLVED -> StatusInfo(
            label = "Riješeno",
            background = Color(0xFFE8F5E9),
            color = Color(0xFF2E7D32),
            icon = Icons.Outlined.CheckCircle,
            barColor = Color(0xFF4CAF50),
            progress = 100,
            description = "Prijava je riješena. Lokacija je sanirana."
        )
    }
}