package com.example.investmentandsavingssimulator.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.example.investmentandsavingssimulator.viewmodel.MmfViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class MmfDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MmfViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mmf_detail)

        val userId = intent.getIntExtra("USER_ID", -1)
        val fundName = intent.getStringExtra("FUND_NAME") ?: "MMF Fund"

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarMmfDetail)
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        toolbar.setNavigationOnClickListener { finish() }

        val tvName = findViewById<TextView>(R.id.tvMmfDetailName)
        val etAmount = findViewById<TextInputEditText>(R.id.etMmfAmount)
        val btnInvest = findViewById<Button>(R.id.btnInvestMmf)
        val tvError = findViewById<TextView>(R.id.tvMmfError)

        tvName.text = fundName

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(db.userDao(), db.transactionDao())

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MmfViewModel(repository) as T
            }
        })[MmfViewModel::class.java]

        btnInvest.setOnClickListener {
            lifecycleScope.launch {
                val user = repository.getUserById(userId)
                if (user != null) {
                    viewModel.invest(user, fundName, etAmount.text.toString())
                }
            }
        }

        lifecycleScope.launch {
            viewModel.mmfState.collect { state ->
                when (state) {
                    is MmfViewModel.MmfState.Idle -> tvError.visibility = View.GONE
                    is MmfViewModel.MmfState.Error -> {
                        tvError.text = state.message
                        tvError.visibility = View.VISIBLE
                    }
                    is MmfViewModel.MmfState.Success -> {
                        Toast.makeText(this@MmfDetailActivity, "Successfully invested in $fundName!", Toast.LENGTH_LONG).show()
                        finish() // Return to Dashboard
                    }
                }
            }
        }
    }
}