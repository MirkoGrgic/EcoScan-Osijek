package com.example.ecoscanosijek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecoscanosijek.data.MockData
import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.model.UserRole
import com.example.ecoscanosijek.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(email: String) {
        // TODO: Real login logic
        val user = authRepository.login(email)
        _currentUser.value = user
    }

    fun loginAsCitizen() {
        _currentUser.value = MockData.citizenUser
    }

    fun loginAsMunicipalWorker() {
        _currentUser.value = MockData.workerUser
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }

    fun continueAsGuest() {
        // For simplicity, guest can be a special citizen user or just a null user with guest permissions
        // Here we'll just login as a default citizen for demo purposes if needed, 
        // but user asked for guest button.
        _currentUser.value = MockData.citizenUser.copy(username = "Guest", email = "guest@example.com")
    }
}
