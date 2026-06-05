package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoscanosijek.ui.components.EcoButton
import com.example.ecoscanosijek.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user?.username ?: "Gost",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = user?.email ?: "", color = MaterialTheme.colorScheme.secondary)

        Spacer(modifier = Modifier.height(32.dp))

        ProfileInfoRow(label = "Uloga", value = user?.role?.name ?: "GOST")
        if (user?.role == com.example.ecoscanosijek.model.UserRole.CITIZEN) {
            ProfileInfoRow(label = "Bodovi", value = "${user?.points ?: 0}")
            ProfileInfoRow(label = "Broj prijava", value = "${user?.reportCount ?: 0}")
        }

        Spacer(modifier = Modifier.weight(1f))

        EcoButton(
            text = "Odjava",
            onClick = {
                viewModel.logout()
                onLogout()
            },
            containerColor = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Text(text = value, color = MaterialTheme.colorScheme.primary)
    }
}
