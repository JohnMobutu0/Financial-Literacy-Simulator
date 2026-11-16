
package com.mutobu.financial_literacy_simulator.ui.treasurybonds

data class TreasuryResult(
    val investment: Double,
    val annualInterest: Double,
    val halfYearPayment: Double,
    val totalInterest: Double,
    val maturityAmount: Double
)

object TreasuryCalculator {

    fun calculate(
        investment: Double,
        annualRate: Double,
        years: Int
    ): TreasuryResult {

        require(investment > 0) {
            "Investment must be greater than zero"
        }

        require(annualRate > 0) {
            "Interest rate must be greater than zero"
        }

        require(years > 0) {
            "Investment period must be greater than zero"
        }

        val annualInterest =
            investment * annualRate / 100

        val halfYearPayment =
            annualInterest / 2

        val totalInterest =
            annualInterest * years

        val maturityAmount =
            investment + totalInterest

        return TreasuryResult(
            investment = investment,
            annualInterest = annualInterest,
            halfYearPayment = halfYearPayment,
            totalInterest = totalInterest,
            maturityAmount = maturityAmount
        )
    }

    // Testing the function
    fun testCalculator() {
        val result = calculate(
            investment = 100000.0,
            annualRate = 10.0,
            years = 2
        )

        println("Annual Interest: ${result.annualInterest}")
        println("Half-year Payment: ${result.halfYearPayment}")
        println("Total Interest: ${result.totalInterest}")
        println("Maturity Amount: ${result.maturityAmount}")
    }
}
