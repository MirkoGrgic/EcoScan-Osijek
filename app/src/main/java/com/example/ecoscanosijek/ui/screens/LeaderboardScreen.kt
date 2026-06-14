package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.viewmodel.UserViewModel

@Composable
fun LeaderboardScreen(viewModel: UserViewModel) {
    val users by viewModel.leaderboard.collectAsState()
    val listState = rememberLazyListState()

    val top3 = users.take(3)

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8)),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        item {
            LeaderboardTopSection(
                top3 = top3,
                totalUsers = users.size,
                topPoints = users.firstOrNull()?.points ?: 0
            )
        }

        item {
            SectionTitle(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp, bottom = 10.dp)
            )
        }

        if (users.isEmpty()) {
            item {
                EmptyLeaderboardCard(
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } else {
            itemsIndexed(users) { index, user ->
                LeaderboardRow(
                    rank = index + 1,
                    user = user,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun LeaderboardTopSection(
    top3: List<User>,
    totalUsers: Int,
    topPoints: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1B5E20),
                        Color(0xFF2E7D32),
                        Color(0xFF66BB6A),
                        Color(0xFFE8F5E9)
                    )
                )
            )
            .padding(start = 20.dp, end = 20.dp, top = 54.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 45.dp, y = (-35).dp)
                .size(150.dp)
                .background(Color.White.copy(alpha = 0.07f), CircleShape)
        )

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Rang lista",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Eko heroji Osijeka 🌿",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.72f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                MiniHeaderStat(
                    label = "Sudionika",
                    value = totalUsers.toString(),
                    modifier = Modifier.weight(1f)
                )

                MiniHeaderStat(
                    label = "Najviše bodova",
                    value = topPoints.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(26.dp))

            CompactPodium(top3 = top3)
        }
    }
}

@Composable
private fun MiniHeaderStat(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(70.dp)
            .background(Color.White.copy(alpha = 0.13f), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text(
            text = value,
            fontSize = 21.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFFD54F)
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.75f),
            maxLines = 1
        )
    }
}

@Composable
private fun CompactPodium(top3: List<User>) {
    val second = top3.getOrNull(1)
    val first = top3.getOrNull(0)
    val third = top3.getOrNull(2)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        PodiumPlace(
            user = second,
            rank = 2,
            medal = "🥈",
            avatarSize = 56.dp,
            podiumHeight = 40.dp,
            avatarBrush = Brush.linearGradient(
                listOf(Color(0xFFB0BEC5), Color(0xFF78909C))
            ),
            modifier = Modifier.weight(1f)
        )

        PodiumPlace(
            user = first,
            rank = 1,
            medal = "🥇",
            avatarSize = 70.dp,
            podiumHeight = 54.dp,
            avatarBrush = Brush.linearGradient(
                listOf(Color(0xFFFFD54F), Color(0xFFFFA000))
            ),
            modifier = Modifier.weight(1f)
        )

        PodiumPlace(
            user = third,
            rank = 3,
            medal = "🥉",
            avatarSize = 54.dp,
            podiumHeight = 34.dp,
            avatarBrush = Brush.linearGradient(
                listOf(Color(0xFFCD7F32), Color(0xFFA1562B))
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PodiumPlace(
    user: User?,
    rank: Int,
    medal: String,
    avatarSize: Dp,
    podiumHeight: Dp,
    avatarBrush: Brush,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .shadow(6.dp, CircleShape)
                    .background(avatarBrush, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user?.username?.initials() ?: "?",
                    fontSize = if (rank == 1) 21.sp else 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
                    .size(22.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = medal, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = user?.username?.substringBefore(" ") ?: "Nema",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${user?.points ?: 0}",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFFD54F)
        )

        Text(
            text = "bodova",
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.75f)
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(podiumHeight)
                .background(
                    Color.White.copy(alpha = 0.22f),
                    RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$rank",
                fontSize = if (rank == 1) 22.sp else 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun SectionTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.TrendingUp,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(17.dp)
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = "Poredak građana",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF374151)
        )
    }
}

@Composable
private fun LeaderboardRow(
    rank: Int,
    user: User,
    modifier: Modifier = Modifier
) {
    val rankColor = when (rank) {
        1 -> Color(0xFFFFA000)
        2 -> Color(0xFF78909C)
        3 -> Color(0xFFA1562B)
        else -> Color(0xFF6B7280)
    }

    val rankBg = when (rank) {
        1 -> Color(0xFFFFF8E1)
        2 -> Color(0xFFECEFF1)
        3 -> Color(0xFFFFF3E0)
        else -> Color(0xFFF3F4F6)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(rankBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$rank",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = rankColor
                )
            }

            Spacer(Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.username.initials(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.username.ifBlank { "Korisnik" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A2E),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${user.reportCount} prijava · ${user.resolvedCount} riješeno",
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = user.points.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2E7D32)
                )

                Text(
                    text = "bodova",
                    fontSize = 10.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    }
}

@Composable
private fun EmptyLeaderboardCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Eco,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(38.dp)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Još nema korisnika na rang listi",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4B5563)
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