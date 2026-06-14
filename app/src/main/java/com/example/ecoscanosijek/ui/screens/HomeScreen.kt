package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecoscanosijek.model.Report
import com.example.ecoscanosijek.model.ReportStatus
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.viewmodel.AuthViewModel
import com.example.ecoscanosijek.viewmodel.ReportViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    reportViewModel: ReportViewModel,
    onNewReportClick: () -> Unit,
    onReportClick: (String) -> Unit
) {
    val user by authViewModel.currentUser.collectAsState()
    val reports by reportViewModel.reports.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(user) {
        reportViewModel.loadReports(user)
    }

    val totalValue = if (user?.role == UserRole.CITIZEN) {
        user?.reportCount ?: reports.size
    } else {
        reports.size
    }

    val resolvedValue = user?.resolvedCount ?: 0

    val activeCount = reports.count { it.status != ReportStatus.RESOLVED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
    ) {
        HomeHeader(
            username = user?.username ?: "Gost",
            totalReports = totalValue,
            resolvedReports = resolvedValue,
            thirdLabel = if (user?.role == UserRole.CITIZEN) "Bodovi" else "Aktivnih",
            thirdValue = if (user?.role == UserRole.CITIZEN) {
                user?.points?.toString() ?: "0"
            } else {
                activeCount.toString()
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 18.dp,
                    bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (user?.role == UserRole.CITIZEN) {
                    item {
                        NewReportButton(onClick = onNewReportClick)
                    }
                }

                item {
                    Text(
                        text = if (user?.role == UserRole.WORKER) {
                            "Prijave za pregled"
                        } else {
                            "Nedavne prijave"
                        },
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (reports.isEmpty()) {
                    item {
                        EmptyReportsCard()
                    }
                } else {
                    items(reports) { report ->
                        ModernReportItem(
                            report = report,
                            onClick = { onReportClick(report.id) }
                        )
                    }
                }

                item {
                    EcoTipCard()
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    username: String,
    totalReports: Int,
    resolvedReports: Int,
    thirdLabel: String,
    thirdValue: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF1B5E20),
                        Color(0xFF2E7D32),
                        Color(0xFF43A047)
                    )
                )
            )
            .padding(start = 20.dp, end = 20.dp, top = 64.dp, bottom = 26.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .size(170.dp)
                .background(Color.White.copy(alpha = 0.07f), CircleShape)
        )

        Column {
            Text(
                text = "Dobrodošao,",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.75f)
            )

            Text(
                text = "$username 👋",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HeaderStatCard(
                    label = "Ukupno",
                    value = totalReports.toString(),
                    valueColor = Color(0xFFA5D6A7),
                    modifier = Modifier.weight(1f)
                )

                HeaderStatCard(
                    label = "Riješeno",
                    value = resolvedReports.toString(),
                    valueColor = Color(0xFF80CBC4),
                    modifier = Modifier.weight(1f)
                )

                HeaderStatCard(
                    label = thirdLabel,
                    value = thirdValue,
                    valueColor = Color(0xFFFFD54F),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun HeaderStatCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.13f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = value,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )

            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.78f)
            )
        }
    }
}

@Composable
private fun NewReportButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(10.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                    ),
                    RoundedCornerShape(18.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Outlined.Add, null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Prijavi ilegalni otpad",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ModernReportItem(
    report: Report,
    onClick: () -> Unit
) {
    val sdf = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                if (report.imageUrl.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(report.imageUrl),
                        contentDescription = "Fotografija prijave",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = report.description.ifBlank { "Prijava otpada" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A2E),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = sdf.format(Date(report.createdAt)),
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF)
                )

                Spacer(Modifier.height(7.dp))

                StatusBadge(report.status)
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFD1D5DB),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun StatusBadge(status: ReportStatus) {
    val config = when (status) {
        ReportStatus.NEW -> Triple("Novo", Color(0xFFFFF3E0), Color(0xFFE65100))
        ReportStatus.IN_PROGRESS -> Triple("U tijeku", Color(0xFFE3F2FD), Color(0xFF1565C0))
        ReportStatus.RESOLVED -> Triple("Riješeno", Color(0xFFE8F5E9), Color(0xFF2E7D32))
    }

    val icon = when (status) {
        ReportStatus.NEW -> Icons.Outlined.DeleteOutline
        ReportStatus.IN_PROGRESS -> Icons.Outlined.Schedule
        ReportStatus.RESOLVED -> Icons.Outlined.CheckCircle
    }

    Row(
        modifier = Modifier
            .background(config.second, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = config.third,
            modifier = Modifier.size(12.dp)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = config.first,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = config.third
        )
    }
}

@Composable
private fun EcoTipCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF8EF))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Eco,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = "Eko savjet dana",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )

                Text(
                    text = "Svaka prijava pomaže gradu Osijeku u borbi protiv ilegalnog odlaganja otpada.",
                    fontSize = 11.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun EmptyReportsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(36.dp)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Nema prijava za prikaz",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4B5563)
            )
        }
    }
}