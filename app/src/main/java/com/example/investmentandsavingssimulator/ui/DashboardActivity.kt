package com.example.investmentandsavingssimulator.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.investmentandsavingssimulator.InterestWorker // ADDED MISSING IMPORT
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.example.investmentandsavingssimulator.viewmodel.DashboardViewModel
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit

class DashboardActivity : AppCompatActivity() {

    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 1. Get USER_ID passed from LoginActivity
        val userId = intent.getIntExtra("USER_ID", -1)

        // 2. Setup Toolbar & Logout
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_dashboard)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_logout) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            } else false
        }

        // 3. Setup Database & ViewModel
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = UserRepository(db.userDao(), db.transactionDao())

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DashboardViewModel(repository, db.assetDao()) as T
            }
        })[DashboardViewModel::class.java]

        // 4. Schedule Background Interest Simulation (WorkManager)
        val workRequest = PeriodicWorkRequestBuilder<InterestWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(workRequest)

        // 5. Bind UI Elements
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val tvBalance = findViewById<TextView>(R.id.tvBalance)
        val rvCategories = findViewById<RecyclerView>(R.id.rvCategories)

        // 6. Setup Categories Grid & Navigation
        // 6. Setup Categories Grid & Navigation
        val categories = listOf("MMF", "SACCO", "NSE Stocks", "Bonds")
        // Using professional Material icons instead of emojis
        val icons = listOf(
            R.drawable.ic_mmf,
            R.drawable.ic_sacco,
            R.drawable.ic_stocks,
            R.drawable.ic_bonds
        )

        val categoryAdapter = CategoryAdapter(categories, icons) { selectedCategory ->
            if (selectedCategory == "MMF") {
                // YOUR PART: Navigate to the dedicated MMF Screen
                val intent = Intent(this@DashboardActivity, MmfActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
            } else {
                // TEAMMATES' PARTS: Generic Asset List
                val intent = Intent(this@DashboardActivity, AssetListActivity::class.java)
                intent.putExtra("USER_ID", userId)
                intent.putExtra("CATEGORY", selectedCategory)
                startActivity(intent)
            }
        }

        rvCategories.layoutManager = GridLayoutManager(this, 2) // 2 columns
        rvCategories.adapter = categoryAdapter

        // 7. Observe User Data
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                user?.let {
                    tvWelcome.text = getString(R.string.welcome_user, it.username)
                    tvBalance.text = String.format(Locale.getDefault(), "KES %,.2f", it.virtualCashBalance)
                }
            }
        }

        // 8. Load Data
        if (userId != -1) {
            viewModel.loadDashboardData(userId)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the user data every time the user returns to the Dashboard
        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId != -1) {
            viewModel.loadDashboardData(userId)
        }
    }
}