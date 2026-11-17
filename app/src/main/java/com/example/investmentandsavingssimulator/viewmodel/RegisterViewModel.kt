package com.example.investmentandsavingssimulator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(username: String, email: String, password: String) {
        // 1. Validation
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = RegisterState.Error("All fields are required")
            return
        }
        if (password.length < 4) {
            _registerState.value = RegisterState.Error("Password must be at least 4 characters")
            return
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            // Check if user already exists
            val existingUser = repository.loginUser(username, password)
            // Note: Since we don't have a getByUsername function in DAO yet,
            // for now we just insert. If duplicate, Room will throw PrimaryKey error (if username was PK).
            // Since ID is PK, duplicates are allowed for now unless we add a unique constraint to username.

            try {
                val newUser = UserEntity(
                    username = username,
                    email = email,
                    password = password
                )
                repository.registerUser(newUser)
                _registerState.value = RegisterState.Success
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Registration failed: ${e.message}")
            }
        }
    }
}

// Factory for the Register ViewModel
class RegisterViewModelFactory(private val repository: UserRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}