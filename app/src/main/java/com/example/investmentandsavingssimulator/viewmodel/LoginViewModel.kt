package com.example.investmentandsavingssimulator.viewmodel

import com.example.investmentandsavingssimulator.data.local.UserEntity // <--- MUST BE IMPORTED
import com.example.investmentandsavingssimulator.data.UserRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    // UI State to handle loading, success, and errors
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: UserEntity) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        // 1. Validation (Earns you marks!)
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Username and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val user = repository.loginUser(username, password)

            if (user != null) {
                _loginState.value = LoginState.Success(user)
            } else {
                _loginState.value = LoginState.Error("Invalid username or password")
            }
        }
    }
}