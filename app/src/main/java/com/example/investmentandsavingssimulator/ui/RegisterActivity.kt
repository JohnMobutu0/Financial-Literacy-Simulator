package com.example.investmentandsavingssimulator.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.example.investmentandsavingssimulator.viewmodel.RegisterViewModel
import com.example.investmentandsavingssimulator.viewmodel.RegisterViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Setup Database & Repository (Same as Login)
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(db.userDao(), db.transactionDao())

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, RegisterViewModelFactory(repository))[RegisterViewModel::class.java]

        // Bind UI
        val etUsername = findViewById<TextInputEditText>(R.id.etRegUsername)
        val etEmail = findViewById<TextInputEditText>(R.id.etRegEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etRegPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvError = findViewById<TextView>(R.id.tvRegError)
        val progressBar = findViewById<ProgressBar>(R.id.regProgressBar)

        btnRegister.setOnClickListener {
            viewModel.register(
                etUsername.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString()
            )
        }

        // Observe State
        lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                when (state) {
                    is RegisterViewModel.RegisterState.Idle -> {
                        progressBar.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                    is RegisterViewModel.RegisterState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                    }
                    is RegisterViewModel.RegisterState.Error -> {
                        progressBar.visibility = View.GONE
                        tvError.text = state.message
                        tvError.visibility = View.VISIBLE
                    }
                    is RegisterViewModel.RegisterState.Success -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity, "Account created! Please login.", Toast.LENGTH_LONG).show()
                        finish() // This closes the register screen and takes you back to Login
                    }
                }
            }
        }
    }
}