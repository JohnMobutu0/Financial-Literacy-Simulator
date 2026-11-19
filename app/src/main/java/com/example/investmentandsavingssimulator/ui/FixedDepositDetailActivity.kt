package com.example.investmentandsavingssimulator.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.investmentandsavingssimulator.R
import com.google.android.material.appbar.MaterialToolbar
import java.util.Locale

class FixedDepositDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fixed_deposit_detail)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarFdDetail)
        toolbar.title = "Invest in Fixed Deposit"
        toolbar.setNavigationOnClickListener { finish() }

        val fdName = intent.getStringExtra("FD_NAME") ?: "Fixed Deposit"
        val annualRate = intent.getDoubleExtra("ANNUAL_RATE", 10.0)
        val lockMonths = intent.getIntExtra("LOCK_MONTHS", 6)
        val minInvestment = intent.getDoubleExtra("MIN_INVESTMENT", 5000.0)

        val tvTitle = findViewById<TextView>(R.id.tvFdTitle)
        val tvTerms = findViewById<TextView>(R.id.tvFdTerms)
        val etAmount = findViewById<EditText>(R.id.etFdAmount)
        val btnCalculate = findViewById<Button>(R.id.btnCalculateFd)
        val tvResult = findViewById<TextView>(R.id.tvFdResult)

        tvTitle.text = fdName
        tvTerms.text = String.format(
            Locale.getDefault(),
            "Rate: %.1f%% p.a. • Locked for %d months • Min: KES %,.0f",
            annualRate,
            lockMonths,
            minInvestment
        )

        btnCalculate.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull()
            if (amount == null || amount <= 0.0) {
                tvResult.text = "Enter a valid amount first."
                return@setOnClickListener
            }
            if (amount < minInvestment) {
                tvResult.text = String.format(
                    Locale.getDefault(),
                    "Minimum for this term is KES %,.0f.",
                    minInvestment
                )
                return@setOnClickListener
            }

            // Simple interest for the locked period: Principal * Rate * (Months / 12)
            val interest = amount * (annualRate / 100.0) * (lockMonths / 12.0)
            val payout = amount + interest

            tvResult.text = String.format(
                Locale.getDefault(),
                "After %d months:\nInterest Earned: KES %,.2f\nTotal Payout: KES %,.2f",
                lockMonths,
                interest,
                payout
            )
        }
    }
}