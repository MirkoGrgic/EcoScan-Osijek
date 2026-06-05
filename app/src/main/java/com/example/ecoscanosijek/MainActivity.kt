package com.example.ecoscanosijek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ecoscanosijek.ui.navigation.AppNavigation
import com.example.ecoscanosijek.ui.theme.EcoScanOsijekTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoScanOsijekTheme {
                AppNavigation()
            }
        }
    }
}
