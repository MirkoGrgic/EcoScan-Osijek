package com.example.ecoscanosijek.repository

import com.example.ecoscanosijek.data.MockData
import com.example.ecoscanosijek.model.User

class UserRepository {
    fun getUsers(): List<User> = MockData.users
    
    fun getLeaderboard(): List<User> = MockData.users
        .filter { it.role == com.example.ecoscanosijek.model.UserRole.CITIZEN }
        .sortedByDescending { it.points }
}
