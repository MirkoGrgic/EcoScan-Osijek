package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StarterScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onGuestClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE8F5E9), Color.White)
                )
            )
    ) {
        NatureIllustration(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoSection()

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
                            ),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Prijava",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
                    )
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2E7D32)
                )
            ) {
                Text(
                    text = "Registracija",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(4.dp))

            TextButton(
                onClick = onGuestClick,
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = "Nastavi kao gost",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF9CA3AF)
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "Projekt · Osijek 2026",
                fontSize = 11.sp,
                color = Color(0xFFC4C4C4),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LogoSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .size(64.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color(0xFF2E7D32),
                    spotColor = Color(0xFF2E7D32)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Eco,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = "EcoScan",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20),
            letterSpacing = (-0.5).sp
        )

        Text(
            text = "OSIJEK",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF4CAF50),
            letterSpacing = 2.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Čuvajmo Osijek zajedno.\nPrijavi ilegalno odlagalište otpada.",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )
    }
}

@Composable
private fun NatureIllustration(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    Color(0xFFB3E5FC),
                    Color(0xFFC8E6C9),
                    Color(0xFFA5D6A7)
                )
            )
        )
    ) {
        val w = size.width
        val h = size.height
        val sx = w / 362f
        val sy = h / 240f

        fun x(v: Float) = v * sx
        fun y(v: Float) = v * sy

        // Clouds
        drawOval(Color.White.copy(alpha = 0.7f), topLeft = Offset(x(25f), y(34f)), size = androidx.compose.ui.geometry.Size(x(70f), y(32f)))
        drawOval(Color.White.copy(alpha = 0.7f), topLeft = Offset(x(57f), y(26f)), size = androidx.compose.ui.geometry.Size(x(56f), y(36f)))
        drawOval(Color.White.copy(alpha = 0.7f), topLeft = Offset(x(18f), y(41f)), size = androidx.compose.ui.geometry.Size(x(44f), y(26f)))

        drawOval(Color.White.copy(alpha = 0.5f), topLeft = Offset(x(220f), y(22f)), size = androidx.compose.ui.geometry.Size(x(80f), y(36f)))
        drawOval(Color.White.copy(alpha = 0.5f), topLeft = Offset(x(260f), y(14f)), size = androidx.compose.ui.geometry.Size(x(60f), y(40f)))

        // Sun
        drawCircle(Color(0xFFFFD54F).copy(alpha = 0.9f), radius = x(28f), center = Offset(x(300f), y(60f)))
        drawCircle(Color(0xFFFFEB3B), radius = x(22f), center = Offset(x(300f), y(60f)))

        // Hills
        drawOval(Color(0xFF66BB6A).copy(alpha = 0.6f), topLeft = Offset(x(-60f), y(110f)), size = androidx.compose.ui.geometry.Size(x(320f), y(180f)))
        drawOval(Color(0xFF81C784).copy(alpha = 0.5f), topLeft = Offset(x(140f), y(130f)), size = androidx.compose.ui.geometry.Size(x(280f), y(160f)))

        // Ground
        drawOval(Color(0xFF4CAF50), topLeft = Offset(x(-39f), y(170f)), size = androidx.compose.ui.geometry.Size(x(440f), y(120f)))
        drawRect(Color(0xFF4CAF50), topLeft = Offset(0f, y(210f)), size = androidx.compose.ui.geometry.Size(w, y(30f)))

        // Left tree
        drawRoundRect(Color(0xFF5D4037), topLeft = Offset(x(60f), y(130f)), size = androidx.compose.ui.geometry.Size(x(14f), y(80f)), cornerRadius = androidx.compose.ui.geometry.CornerRadius(x(4f)))
        drawOval(Color(0xFF2E7D32), topLeft = Offset(x(27f), y(75f)), size = androidx.compose.ui.geometry.Size(x(80f), y(90f)))
        drawOval(Color(0xFF388E3C), topLeft = Offset(x(22f), y(103f)), size = androidx.compose.ui.geometry.Size(x(56f), y(64f)))
        drawOval(Color(0xFF43A047), topLeft = Offset(x(52f), y(94f)), size = androidx.compose.ui.geometry.Size(x(60f), y(72f)))
        drawOval(Color(0xFF1B5E20), topLeft = Offset(x(32f), y(70f)), size = androidx.compose.ui.geometry.Size(x(70f), y(76f)))

        // Right tree
        drawRoundRect(Color(0xFF5D4037), topLeft = Offset(x(270f), y(145f)), size = androidx.compose.ui.geometry.Size(x(12f), y(70f)), cornerRadius = androidx.compose.ui.geometry.CornerRadius(x(4f)))
        drawOval(Color(0xFF2E7D32), topLeft = Offset(x(242f), y(98f)), size = androidx.compose.ui.geometry.Size(x(68f), y(76f)))
        drawOval(Color(0xFF388E3C), topLeft = Offset(x(236f), y(120f)), size = androidx.compose.ui.geometry.Size(x(48f), y(56f)))
        drawOval(Color(0xFF43A047), topLeft = Offset(x(264f), y(111f)), size = androidx.compose.ui.geometry.Size(x(52f), y(64f)))
        drawOval(Color(0xFF1B5E20), topLeft = Offset(x(246f), y(88f)), size = androidx.compose.ui.geometry.Size(x(60f), y(68f)))

        // Bush
        drawOval(Color(0xFF388E3C), topLeft = Offset(x(121f), y(195f)), size = androidx.compose.ui.geometry.Size(x(120f), y(40f)))
        drawOval(Color(0xFF43A047), topLeft = Offset(x(115f), y(203f)), size = androidx.compose.ui.geometry.Size(x(70f), y(30f)))
        drawOval(Color(0xFF2E7D32), topLeft = Offset(x(170f), y(202f)), size = androidx.compose.ui.geometry.Size(x(80f), y(32f)))

        // Flowers
        drawCircle(Color(0xFFFFEB3B), radius = x(5f), center = Offset(x(130f), y(216f)))
        drawCircle(Color(0xFFFF7043), radius = x(4f), center = Offset(x(145f), y(213f)))
        drawCircle(Color(0xFFFFEB3B), radius = x(5f), center = Offset(x(220f), y(215f)))
        drawCircle(Color(0xFFFF7043), radius = x(4f), center = Offset(x(235f), y(212f)))

        // Butterflies
        rotate(-20f, Offset(x(170f), y(160f))) {
            drawOval(Color(0xFFFF8A65).copy(alpha = 0.8f), topLeft = Offset(x(162f), y(155f)), size = androidx.compose.ui.geometry.Size(x(16f), y(10f)))
        }
        rotate(20f, Offset(x(185f), y(158f))) {
            drawOval(Color(0xFFFF8A65).copy(alpha = 0.8f), topLeft = Offset(x(177f), y(153f)), size = androidx.compose.ui.geometry.Size(x(16f), y(10f)))
        }
        drawCircle(Color(0xFF5D4037), radius = x(2f), center = Offset(x(177f), y(162f)))

        // Leaves
        rotate(-30f, Offset(x(230f), y(100f))) {
            drawOval(Color(0xFF81C784).copy(alpha = 0.7f), topLeft = Offset(x(223f), y(97f)), size = androidx.compose.ui.geometry.Size(x(14f), y(6f)))
        }
        rotate(45f, Offset(x(120f), y(90f))) {
            drawOval(Color(0xFFA5D6A7).copy(alpha = 0.7f), topLeft = Offset(x(114f), y(87f)), size = androidx.compose.ui.geometry.Size(x(12f), y(6f)))
        }
    }
}