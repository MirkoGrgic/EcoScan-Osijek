package com.example.ecoscanosijek.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.repository.AuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var userListenerJob: Job? = null

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            _currentUser.value = user

            if (user != null) {
                startUserListener()
            }
        }
    }

    private fun startUserListener() {
        userListenerJob?.cancel()

        userListenerJob = viewModelScope.launch {
            authRepository.observeCurrentUser()
                .collectLatest { user ->
                    _currentUser.value = user
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val user = authRepository.login(email, password)
                _currentUser.value = user

                if (user != null) {
                    startUserListener()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(
        email: String,
        password: String,
        username: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val user = authRepository.register(
                    email = email,
                    password = password,
                    username = username
                )

                _currentUser.value = user

                if (user != null) {
                    startUserListener()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                userListenerJob?.cancel()

                val user = authRepository.loginAnonymously()
                _currentUser.value = user
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _currentUser.value = null
            }
        }
    }
}