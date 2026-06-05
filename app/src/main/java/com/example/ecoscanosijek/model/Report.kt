package com.example.ecoscanosijek.model

data class Report(
    val id: String,
    val userId: String,
    val imageUrl: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val status: ReportStatus,
    val createdAt: Long
)
