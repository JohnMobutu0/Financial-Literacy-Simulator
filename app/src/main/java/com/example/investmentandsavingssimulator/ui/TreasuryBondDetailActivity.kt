package com.example.investmentandsavingssimulator.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.investmentandsavingssimulator.R
import com.example.investmentandsavingssimulator.data.TreasuryBondRepository
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import com.example.investmentandsavingssimulator.data.local.TreasuryBondEntity
import com.example.investmentandsavingssimulator.viewmodel.TreasuryBondViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.util.Locale

class TreasuryBondDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: TreasuryBondViewModel
    private lateinit var bond: TreasuryBondEntity
    private var userId: Int = -1

    private lateinit var etAmount: TextInputEditText
    private lateinit var tvCalculation: TextView
    private lateinit var tvError: TextView
    private lateinit var btnInvest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasury_bond_detail)

        userId = intent.getIntExtra("USER_ID", -1)
        val bondId = intent.getIntExtra("BOND_ID", -1)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarBondDetail)
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        toolbar.setNavigationOnClickListener { finish() }

        val tvName = findViewById<TextView>(R.id.tvBondDetailName)
        val tvRate = findViewById<TextView>(R.id.tvBondDetailRate)
        val tvMaturity = findViewById<TextView>(R.id.tvBondDetailMaturity)

        etAmount = findViewById(R.id.etBondAmount)
        tvCalculation = findViewById(R.id.tvBondCalculation)
        tvError = findViewById(R.id.tvBondError)

        val btnCalculate = findViewById<Button>(R.id.btnCalculateBond)
        btnInvest = findViewById(R.id.btnInvestBond)

        val db = AppDatabase.getDatabase(applicationContext)

        val bondRepository = TreasuryBondRepository(db.treasuryBondDao())
        val userRepository = UserRepository(db.userDao(), db.transactionDao())

        viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TreasuryBondViewModel(
                        bondRepository,
                        userRepository
                    ) as T
                }
            }
        )[TreasuryBondViewModel::class.java]

        lifecycleScope.launch {
            bond = bondRepository.getBondById(bondId) ?: run {
                Toast.makeText(
                    this@TreasuryBondDetailActivity,
                    "Bond not found",
                    Toast.LENGTH_LONG
                ).show()
                finish()
                return@launch
            }

            tvName.text = bond.name
            tvRate.text = "Coupon Rate: ${bond.couponRate}% p.a."
            tvMaturity.text =
                "Maturity: ${bond.maturityYears} years | Minimum: KES ${bond.minimumInvestment}"

            btnCalculate.setOnClickListener {
                val amount = etAmount.text.toString().toDoubleOrNull()

                if (amount == null) {
                    tvError.text = "Please enter a valid investment amount."
                    tvError.visibility = TextView.VISIBLE
                    return@setOnClickListener
                }

                if (amount < bond.minimumInvestment || amount <= 0) {
                    tvError.text =
                        "Minimum investment is KES ${bond.minimumInvestment}"
                    tvError.visibility = TextView.VISIBLE
                    return@setOnClickListener
                }

                tvError.visibility = TextView.GONE
                viewModel.calculateReturns(bond, amount)
            }

            btnInvest.setOnClickListener {
                val amount = etAmount.text.toString().toDoubleOrNull()

                if (amount == null || amount <= 0) {
                    tvError.text = "Please enter a valid investment amount."
                    tvError.visibility = TextView.VISIBLE
                    return@setOnClickListener
                }

                AlertDialog.Builder(this@TreasuryBondDetailActivity)
                    .setTitle("Confirm Investment")
                    .setMessage(
                        "Are you sure you want to invest KES $amount in ${bond.name}?"
                    )
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Confirm") { _, _ ->
                        lifecycleScope.launch {
                            val user = userRepository.getUserById(userId)

                            if (user == null) {
                                tvError.text = "User account not found."
                                tvError.visibility = TextView.VISIBLE
                                return@launch
                            }

                            viewModel.invest(user, bond, amount)
                        }
                    }
                    .show()
            }
        }

        lifecycleScope.launch {
            viewModel.calculation.collect { result ->
                if (result != null) {
                    tvCalculation.text = String.format(
                        Locale.getDefault(),
                        "Estimated return\n\nInvestment: KES %,.2f\nInterest per payment: KES %,.2f\nTotal interest: KES %,.2f\nTotal estimated amount: KES %,.2f",
                        result.investmentAmount,
                        result.couponPerPayment,
                        result.totalCoupons,
                        result.totalReturn
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.investmentState.collect { state ->
                when (state) {
                    is TreasuryBondViewModel.InvestmentState.Idle -> Unit

                    is TreasuryBondViewModel.InvestmentState.Error -> {
                        tvError.text = state.message
                        tvError.visibility = TextView.VISIBLE
                    }

                    is TreasuryBondViewModel.InvestmentState.Success -> {
                        Toast.makeText(
                            this@TreasuryBondDetailActivity,
                            "Investment successful!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        }
    }
}
