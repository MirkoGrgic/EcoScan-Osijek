package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoscanosijek.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var agreeTerms by remember { mutableStateOf(true) }
    var passwordError by remember { mutableStateOf("") }

    val user by viewModel.currentUser.collectAsState()

    LaunchedEffect(user) {
        if (user != null) onRegisterSuccess()
    }

    val bg = Color(0xFFF9FAFB)
    val greenDark = Color(0xFF1B5E20)
    val green = Color(0xFF2E7D32)
    val greenLight = Color(0xFF4CAF50)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.linearGradient(
                        listOf(greenDark, green, Color(0xFF388E3C))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .offset(x = (-30).dp, y = (-20).dp)
                    .align(Alignment.TopStart)
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.06f), CircleShape)
            )

            IconButton(
                onClick = onBackToLogin,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Natrag",
                    tint = Color.White
                )
            }

            Card(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(48.dp)
                    .shadow(12.dp, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Eco,
                        contentDescription = null,
                        tint = green,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(
                        bg,
                        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kreiraj račun",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Pridruži se i pomozi održati Osijek čistim.",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(8.dp, CircleShape)
                        .background(Color(0xFFE5E7EB), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(34.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(30.dp)
                        .background(greenLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            RegisterField(
                label = "Korisničko ime",
                value = username,
                onValueChange = { username = it },
                placeholder = "Unesi korisničko ime",
                leadingIcon = Icons.Outlined.Person,
                trailingIcon = if (username.length > 2) Icons.Outlined.CheckCircle else null
            )

            Spacer(Modifier.height(14.dp))

            RegisterField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "Unesi email",
                leadingIcon = Icons.Outlined.Mail,
                trailingIcon = if (email.contains("@")) Icons.Outlined.CheckCircle else null,
                keyboardType = KeyboardType.Email
            )

            Spacer(Modifier.height(14.dp))

            RegisterField(
                label = "Lozinka",
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                placeholder = "Kreiraj lozinku",
                leadingIcon = Icons.Outlined.Lock,
                trailingIcon = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                onTrailingClick = { showPassword = !showPassword },
                isPassword = !showPassword,
                keyboardType = KeyboardType.Password
            )

            Spacer(Modifier.height(14.dp))

            RegisterField(
                label = "Potvrdi lozinku",
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    passwordError = ""
                },
                placeholder = "Ponovi lozinku",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = !showPassword,
                isError = passwordError.isNotEmpty(),
                keyboardType = KeyboardType.Password
            )

            if (passwordError.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = passwordError,
                        fontSize = 12.sp,
                        color = Color(0xFFEF4444)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = agreeTerms,
                    onCheckedChange = { agreeTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = greenLight,
                        uncheckedColor = Color(0xFFD1D5DB)
                    )
                )

                Text(
                    text = "Slažem se s uvjetima korištenja i pravilima privatnosti.",
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        password != confirmPassword -> {
                            passwordError = "Lozinke se ne podudaraju"
                        }

                        !agreeTerms -> Unit

                        else -> {
                            viewModel.register(email, password, username)
                        }
                    }
                },
                enabled = agreeTerms,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(10.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    green.copy(alpha = if (agreeTerms) 1f else 0.6f),
                                    greenLight.copy(alpha = if (agreeTerms) 1f else 0.6f)
                                )
                            ),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Registriraj se",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Novi korisnici se automatski registriraju kao građani.",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Već imaš račun? ",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )

                Text(
                    text = "Prijavi se",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = green,
                    modifier = Modifier.clickable { onBackToLogin() }
                )
            }
        }
    }
}

@Composable
private fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    onTrailingClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Text(
        text = label,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF4B5563),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 14.sp,
                color = Color(0xFF9CA3AF)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = trailingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = if (it == Icons.Outlined.CheckCircle) {
                        Color(0xFF4CAF50)
                    } else {
                        Color(0xFF9CA3AF)
                    },
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(enabled = onTrailingClick != null) {
                            onTrailingClick?.invoke()
                        }
                )
            }
        },
        singleLine = true,
        isError = isError,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF4CAF50),
            unfocusedBorderColor = Color(0xFFE5E7EB),
            errorBorderColor = Color(0xFFEF4444),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            errorContainerColor = Color.White,
            cursorColor = Color(0xFF2E7D32)
        )
    )
}