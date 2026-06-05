package com.example.ecoscanosijek.repository

import com.example.ecoscanosijek.data.MockData
import com.example.ecoscanosijek.model.User

class AuthRepository {
    private var currentUser: User? = null

    fun login(email: String): User? {
        currentUser = MockData.users.find { it.email == email }
        return currentUser
    }

    fun getCurrentUser(): User? = currentUser

    fun logout() {
        currentUser = null
    }
}
