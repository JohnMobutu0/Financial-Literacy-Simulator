package com.mutobu.financial_literacy_simulator.ui.home

import androidx.lifecycle.ViewModel
import com.mutobu.financial_literacy_simulator.data.repository.InvestmentRepository

class HomeViewModel : ViewModel() {

    private val repository = InvestmentRepository()

    val investmentCategories =
        repository.getInvestmentCategories()
}