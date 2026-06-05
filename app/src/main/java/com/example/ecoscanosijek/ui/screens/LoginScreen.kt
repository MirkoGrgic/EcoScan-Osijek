package com.example.ecoscanosijek.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecoscanosijek.ui.components.EcoButton
import com.example.ecoscanosijek.ui.components.EcoTextField
import com.example.ecoscanosijek.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "EcoScan Osijek",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        EcoTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        EcoTextField(
            value = password,
            onValueChange = { password = it },
            label = "Lozinka",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        EcoButton(
            text = "Prijava",
            onClick = {
                viewModel.login(email)
                onLoginSuccess()
            }
        )

        TextButton(onClick = { /* TODO: Register */ }) {
            Text("Nemaš račun? Registriraj se")
        }

        EcoButton(
            text = "Nastavi kao gost",
            onClick = {
                viewModel.continueAsGuest()
                onLoginSuccess()
            },
            containerColor = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Brza prijava (za testiranje):", fontSize = 12.sp)
        Row {
            TextButton(onClick = { 
                viewModel.loginAsCitizen()
                onLoginSuccess()
            }) {
                Text("Građanin")
            }
            TextButton(onClick = { 
                viewModel.loginAsMunicipalWorker()
                onLoginSuccess()
            }) {
                Text("Komunalac")
            }
        }
    }
}
