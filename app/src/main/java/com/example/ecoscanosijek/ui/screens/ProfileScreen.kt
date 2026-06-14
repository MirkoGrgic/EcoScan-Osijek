package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()

    val username = user?.username ?: "Gost"
    val email = user?.email ?: ""
    val role = user?.role

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
    ) {
        ProfileHeader(
            username = username,
            email = email,
            role = role,
            points = user?.points ?: 0
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            if (role == UserRole.CITIZEN) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ProfileStatCard(
                        label = "Prijava",
                        value = "${user?.reportCount ?: 0}",
                        icon = "📋",
                        valueColor = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )

                    ProfileStatCard(
                        label = "Riješeno",
                        value = "${user?.resolvedCount ?: 0}",
                        icon = "✅",
                        valueColor = Color(0xFF1565C0),
                        modifier = Modifier.weight(1f)
                    )

                    ProfileStatCard(
                        label = "Bodovi",
                        value = "${user?.points ?: 0}",
                        icon = "⭐",
                        valueColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(18.dp))

                InfoCard(
                    title = "Status korisnika",
                    subtitle = "Građanin koji prijavljuje ilegalno odlaganje otpada.",
                    icon = Icons.Outlined.Eco
                )
            } else if (role == UserRole.WORKER) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ProfileStatCard(
                        label = "Uloga",
                        value = "Radnik",
                        icon = "🛠️",
                        valueColor = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )

                    ProfileStatCard(
                        label = "Zadaci",
                        value = "Pregled",
                        icon = "🔎",
                        valueColor = Color(0xFF1565C0),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(18.dp))

                InfoCard(
                    title = "Radnički profil",
                    subtitle = "Imaš pristup pregledu prijava i promjeni statusa prijava.",
                    icon = Icons.Outlined.VerifiedUser
                )
            } else {
                InfoCard(
                    title = "Gost profil",
                    subtitle = "Prijavljen si kao gost. Neke funkcije mogu biti ograničene.",
                    icon = Icons.Outlined.Person
                )
            }

            Spacer(Modifier.height(16.dp))

            ProfileDetailsCard(
                username = username,
                email = email,
                role = role?.name ?: "GOST"
            )

            Spacer(Modifier.height(16.dp))

            LogoutCard(
                onClick = {
                    viewModel.logout()
                    onLogout()
                }
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "EcoScan Osijek v1.0.0 · Projekt·Osijek 2026",
                fontSize = 11.sp,
                color = Color(0xFFC4C4C4),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    username: String,
    email: String,
    role: UserRole?,
    points: Int
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
            .padding(start = 20.dp, end = 20.dp, top = 58.dp, bottom = 28.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-45).dp)
                .size(100.dp)
                .background(Color.White.copy(alpha = 0.06f), CircleShape)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFFD54F), Color(0xFFFFA000))
                        ),
                        RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.initials(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = username,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (email.isNotBlank()) {
                    Text(
                        text = email,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.72f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    RoleBadge(role = role)

                    if (role == UserRole.CITIZEN) {
                        PointsBadge(points = points)
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleBadge(role: UserRole?) {
    val text = when (role) {
        UserRole.CITIZEN -> "Građanin"
        UserRole.WORKER -> "Radnik"
        else -> "Gost"
    }

    Row(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (role == UserRole.WORKER) Icons.Outlined.VerifiedUser else Icons.Outlined.Person,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(12.dp)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun PointsBadge(points: Int) {
    Row(
        modifier = Modifier
            .background(Color(0xFFFFD54F).copy(alpha = 0.22f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = Color(0xFFFFD54F),
            modifier = Modifier.size(12.dp)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = "$points bodova",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD54F)
        )
    }
}

@Composable
private fun ProfileStatCard(
    label: String,
    value: String,
    icon: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 21.sp)

            Spacer(Modifier.height(4.dp))

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = valueColor,
                maxLines = 1
            )

            Text(
                text = label,
                fontSize = 10.sp,
                color = Color(0xFF9CA3AF),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
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
                    .size(42.dp)
                    .background(Color.White, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun ProfileDetailsCard(
    username: String,
    email: String,
    role: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            ProfileInfoItem(
                icon = Icons.Outlined.Person,
                label = "Korisničko ime",
                value = username
            )

            ProfileInfoItem(
                icon = Icons.Outlined.Mail,
                label = "Email",
                value = email.ifBlank { "Nije dostupno" }
            )

            ProfileInfoItem(
                icon = Icons.Outlined.Badge,
                label = "Uloga",
                value = role
            )
        }
    }
}

@Composable
private fun ProfileInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A2E),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LogoutCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFFDC2626)
            ),
            elevation = null
        ) {
            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = null,
                tint = Color(0xFFDC2626)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Odjava",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626)
            )
        }
    }
}

private fun String.initials(): String {
    val parts = trim()
        .split(" ")
        .filter { it.isNotBlank() }

    return when {
        parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "?"
    }
}