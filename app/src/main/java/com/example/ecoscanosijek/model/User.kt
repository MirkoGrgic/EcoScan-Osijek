package com.example.ecoscanosijek.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val points: Int,
    val reportCount: Int,
    val role: UserRole
)
