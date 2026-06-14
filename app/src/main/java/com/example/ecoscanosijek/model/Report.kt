package com.example.ecoscanosijek.model

data class Report(
    val id: String = "",
    val userId: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: ReportStatus = ReportStatus.NEW,
    val createdAt: Long = System.currentTimeMillis(),
    val pointsAwarded: Boolean = false
)
