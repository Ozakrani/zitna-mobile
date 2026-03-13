package com.example.zitnamobile.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val response: AuthResponse) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.login(email, password).fold(
                onSuccess = { _uiState.value = AuthUiState.Success(it) },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Erreur") }
            )
        }
    }

    fun register(firstName: String, lastName: String, email: String,
                 password: String, phone: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            repository.register(firstName, lastName, email, password, phone).fold(
                onSuccess = { _uiState.value = AuthUiState.Success(it) },
                onFailure = { _uiState.value = AuthUiState.Error(it.message ?: "Erreur") }
            )
        }
    }
}