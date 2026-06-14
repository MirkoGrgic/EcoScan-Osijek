package com.example.ecoscanosijek.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoscanosijek.model.User
import com.example.ecoscanosijek.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _leaderboard = MutableStateFlow<List<User>>(emptyList())
    val leaderboard: StateFlow<List<User>> = _leaderboard.asStateFlow()

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            userRepository.getLeaderboard().collectLatest {
                _leaderboard.value = it
            }
        }
    }
}
