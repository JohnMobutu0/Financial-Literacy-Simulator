package com.example.investmentandsavingssimulator.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class MmfDetailActivity : AppCompatActivity() {

    private var simulationJob: Job? = null
    private var isSimulating = false
    private lateinit var repository: UserRepository
    private var userId: Int = -1
    private var minInvestment: Double = 0.0

    private lateinit var cardSimulationStatus: com.google.android.material.card.MaterialCardView
    private lateinit var tvLiveBalance: TextView
    private lateinit var tvYearsPassed: TextView
    private lateinit var tvMmfError: TextView
    private lateinit var btnInvestMmf: Button
    private lateinit var etMmfAmount: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mmf_detail)

        // 1. Setup Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarMmfDetail)
        toolbar.setNavigationOnClickListener {
            stopSimulation()
            finish()
        }

        // 2. Get Intent Data
        userId = intent.getIntExtra("USER_ID", -1)
        val fundName = intent.getStringExtra("FUND_NAME") ?: "Money Market Fund"
        val fundYield = intent.getStringExtra("FUND_YIELD") ?: "12% p.a."
        minInvestment = intent.getDoubleExtra("MIN_INVESTMENT", 1000.0)

        // 3. Setup Database
        val db = AppDatabase.getDatabase(applicationContext)
        repository = UserRepository(db.userDao(), db.transactionDao())

        // 4. Bind UI Elements
        val tvMmfDetailName = findViewById<TextView>(R.id.tvMmfDetailName)
        val tvMmfDetailYield = findViewById<TextView>(R.id.tvMmfDetailYield)
        val tvMmfDetailMin = findViewById<TextView>(R.id.tvMmfDetailMin)

        etMmfAmount = findViewById(R.id.etMmfAmount)
        btnInvestMmf = findViewById(R.id.btnInvestMmf)
        tvMmfError = findViewById(R.id.tvMmfError)
        cardSimulationStatus = findViewById(R.id.cardSimulationStatus)
        tvLiveBalance = findViewById(R.id.tvLiveBalance)
        tvYearsPassed = findViewById(R.id.tvYearsPassed)

        // Set dynamic UI text
        tvMmfDetailName.text = fundName
        tvMmfDetailYield.text = "Projected Yield: $fundYield"
        tvMmfDetailMin.text = "Minimum Investment: KES ${String.format(Locale.getDefault(), "%,.2f", minInvestment)}"

        // 5. Handle Start/Stop Button Click
        btnInvestMmf.setOnClickListener {
            if (!isSimulating) {
                // --- START SIMULATION ---
                val amountText = etMmfAmount.text.toString()

                if (amountText.isBlank()) {
                    tvMmfError.text = getString(R.string.error_enter_amount)
                    tvMmfError.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                val amount = amountText.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    tvMmfError.text = getString(R.string.error_invalid_amount)
                    tvMmfError.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                // --- NEW VALIDATION: Minimum Investment ---
                if (amount < minInvestment) {
                    tvMmfError.text = "Minimum investment for this fund is KES ${String.format(Locale.getDefault(), "%,.2f", minInvestment)}"
                    tvMmfError.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                // Fetch user to check wallet balance
                lifecycleScope.launch {
                    val user = repository.getUserById(userId)

                    if (user == null) {
                        tvMmfError.text = "User not found"
                        tvMmfError.visibility = View.VISIBLE
                        return@launch
                    }

                    // --- NEW VALIDATION: Wallet Balance ---
                    if (amount > user.virtualCashBalance) {
                        tvMmfError.text = "Insufficient funds! Your wallet balance is KES ${String.format(Locale.getDefault(), "%,.2f", user.virtualCashBalance)}"
                        tvMmfError.visibility = View.VISIBLE
                        return@launch
                    }

                    // Start Simulation
                    tvMmfError.visibility = View.GONE
                    isSimulating = true

                    btnInvestMmf.text = getString(R.string.stop_simulation)
                    btnInvestMmf.setBackgroundColor(android.graphics.Color.RED)
                    etMmfAmount.isEnabled = false
                    cardSimulationStatus.visibility = View.VISIBLE

                    val walletCash = user.virtualCashBalance - amount
                    startSimulation(userId, walletCash, amount)
                }
            } else {
                // --- STOP SIMULATION ---
                stopSimulation()

                btnInvestMmf.text = getString(R.string.start_simulation)
                btnInvestMmf.setBackgroundColor(android.graphics.Color.parseColor("#1E88E5"))
                etMmfAmount.isEnabled = true
                cardSimulationStatus.visibility = View.GONE

                tvMmfError.text = getString(R.string.simulation_stopped)
                tvMmfError.setTextColor(android.graphics.Color.GREEN)
                tvMmfError.visibility = View.VISIBLE
            }
        }
    }

    private fun startSimulation(userId: Int, walletCash: Double, initialInvestment: Double) {
        simulationJob?.cancel()

        simulationJob = lifecycleScope.launch {
            var year = 1
            var currentMmfValue = initialInvestment

            while (true) {
                delay(1.seconds)

                val minReturn = -0.01
                val maxReturn = 0.12
                val returnRate = Random.nextDouble(minReturn, maxReturn)

                currentMmfValue += currentMmfValue * returnRate
                val totalBalance = walletCash + currentMmfValue

                repository.updateUserBalance(userId, totalBalance)

                tvYearsPassed.text = getString(R.string.simulated_year, year)
                tvLiveBalance.text = String.format(Locale.getDefault(), "MMF: KES %,.2f", currentMmfValue)

                if (returnRate < 0) {
                    tvLiveBalance.setTextColor(android.graphics.Color.RED)
                } else {
                    tvLiveBalance.setTextColor(android.graphics.Color.parseColor("#1B5E20"))
                }
                year++
            }
        }
    }

    private fun stopSimulation() {
        simulationJob?.cancel()
        simulationJob = null
        isSimulating = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSimulation()
    }
}