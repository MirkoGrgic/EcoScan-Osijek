package com.example.ecoscanosijek.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val points: Int = 0,
    val reportCount: Int = 0,
    val resolvedCount: Int = 0,
    val role: UserRole = UserRole.CITIZEN
)
