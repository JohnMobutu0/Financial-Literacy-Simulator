
package com.mutobu.financial_literacy_simulator.ui.treasurybonds

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mutobu.financial_literacy_simulator.R

class TreasuryResultsFragment :
    Fragment(R.layout.fragment_treasury_results) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val investment =
            requireArguments().getDouble("investment")

        val annualInterest =
            requireArguments().getDouble("annualInterest")

        val halfYearPayment =
            requireArguments().getDouble("halfYearPayment")

        val totalInterest =
            requireArguments().getDouble("totalInterest")

        val totalCashReceived =
            requireArguments().getDouble("totalCashReceived")

        view.findViewById<TextView>(
            R.id.resultMaturityAmount
        ).text = "KSh %.2f".format(totalCashReceived)

        view.findViewById<TextView>(
            R.id.resultInvestment
        ).text = "KSh %.2f".format(investment)

        view.findViewById<TextView>(
            R.id.resultAnnualInterest
        ).text = "KSh %.2f".format(annualInterest)

        view.findViewById<TextView>(
            R.id.resultHalfYearPayment
        ).text = "KSh %.2f".format(halfYearPayment)

        view.findViewById<TextView>(
            R.id.resultTotalInterest
        ).text = "KSh %.2f".format(totalInterest)
    }
}
