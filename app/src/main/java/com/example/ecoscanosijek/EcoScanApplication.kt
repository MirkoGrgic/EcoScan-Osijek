package com.example.ecoscanosijek

import android.app.Application
import com.cloudinary.android.MediaManager

class EcoScanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Cloudinary
        val config = mapOf(
            "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME,
            "secure" to true,
        )
        MediaManager.init(this, config)
    }
}
