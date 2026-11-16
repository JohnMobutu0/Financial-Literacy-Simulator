package com.mutobu.financial_literacy_simulator.ui.treasurybonds

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mutobu.financial_literacy_simulator.R

class TreasuryBondsFragment :
    Fragment(R.layout.fragment_treasury_bonds) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val investmentInput =
            view.findViewById<EditText>(R.id.investmentAmountInput)

        val rateInput =
            view.findViewById<EditText>(R.id.annualRateInput)

        val yearsInput =
            view.findViewById<EditText>(R.id.investmentPeriodInput)

        val calculateButton =
            view.findViewById<Button>(R.id.calculateButton)

        calculateButton.setOnClickListener {

            val investment =
                investmentInput.text.toString().toDoubleOrNull()

            val rate =
                rateInput.text.toString().toDoubleOrNull()

            val years =
                yearsInput.text.toString().toIntOrNull()

            if (investment == null || rate == null || years == null) {
                Toast.makeText(
                    requireContext(),
                    "Please enter all the required values.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            try {
                val result = TreasuryCalculator.calculate(
                    investment = investment,
                    annualRate = rate,
                    years = years
                )

                val bundle = Bundle().apply {
                    putDouble("investment", result.investment)
                    putDouble("annualInterest", result.annualInterest)
                    putDouble("halfYearPayment", result.halfYearPayment)
                    putDouble("totalInterest", result.totalInterest)
                    putDouble(
                        "totalCashReceived",
                        result.maturityAmount
                    )
                }

                findNavController().navigate(
                    R.id.action_treasuryBondsFragment_to_treasuryResultsFragment,
                    bundle
                )

            } catch (e: IllegalArgumentException) {
                Toast.makeText(
                    requireContext(),
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
