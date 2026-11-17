package com.example.investmentandsavingssimulator.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.example.investmentandsavingssimulator.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. SETUP DATABASE & REPOSITORY
        val db = AppDatabase.getDatabase(applicationContext)

        // FIXED: We now pass BOTH the userDao and transactionDao
        val repository = UserRepository(db.userDao(), db.transactionDao())

        // 2. INITIALIZE VIEWMODEL USING THE FACTORY
        viewModel = ViewModelProvider(this, LoginViewModelFactory(repository))[LoginViewModel::class.java]

        // 3. BIND UI ELEMENTS
        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)
        val tvError = findViewById<TextView>(R.id.tvError)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            viewModel.login(username, password)
        }

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        // 4. OBSERVE STATE
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginViewModel.LoginState.Idle -> {
                        progressBar.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                    is LoginViewModel.LoginState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                    }
                    is LoginViewModel.LoginState.Error -> {
                        progressBar.visibility = View.GONE
                        tvError.text = state.message
                        tvError.visibility = View.VISIBLE
                    }
                    is LoginViewModel.LoginState.Success -> {
                        progressBar.visibility = View.GONE
                        // Navigate to Dashboard
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("USER_ID", state.user.id)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}

// --- VIEWMODEL FACTORY ---
class LoginViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}