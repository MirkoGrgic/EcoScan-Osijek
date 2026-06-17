package com.example.ecoscanosijek

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ecoscanosijek.ui.navigation.AppNavigation
import com.example.ecoscanosijek.ui.theme.EcoScanOsijekTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }

        enableEdgeToEdge()

        setContent {
            EcoScanOsijekTheme {
                AppNavigation()
            }
        }
    }
}
