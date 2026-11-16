package com.mutobu.financial_literacy_simulator.data.repository

import com.mutobu.financial_literacy_simulator.data.model.InvestmentCategory

class InvestmentRepository {

    fun getInvestmentCategories(): List<InvestmentCategory> {

        return listOf(

            InvestmentCategory(
                name = "Treasury Bonds",
                description = "Lend money to the government and earn interest.",
                riskLevel = "Low to moderate"
            ),

            InvestmentCategory(
                name = "Money Market Funds",
                description = "Invest in short-term money market instruments.",
                riskLevel = "Low to moderate"
            ),

            InvestmentCategory(
                name = "SACCO Shares",
                description = "Become a member-owner of a savings and credit cooperative.",
                riskLevel = "Moderate"
            ),

            InvestmentCategory(
                name = "NSE Stocks",
                description = "Buy shares in companies listed on the Nairobi Securities Exchange.",
                riskLevel = "High"
            ),

            InvestmentCategory(
                name = "Fixed Deposits",
                description = "Deposit money with a bank for an agreed period and earn interest.",
                riskLevel = "Low to moderate"
            ),

            InvestmentCategory(
                name = "Scam Awareness",
                description = "Learn how to identify suspicious investment offers.",
                riskLevel = "Learning module"
            )
        )
    }
}