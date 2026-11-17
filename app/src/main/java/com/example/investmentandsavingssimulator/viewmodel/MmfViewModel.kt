package com.example.investmentandsavingssimulator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.TransactionEntity
import com.example.investmentandsavingssimulator.data.local.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MmfViewModel(private val repository: UserRepository) : ViewModel() {

    sealed class MmfState {
        object Idle : MmfState()
        object Success : MmfState()
        data class Error(val message: String) : MmfState()
    }

    private val _mmfState = MutableStateFlow<MmfState>(MmfState.Idle)
    val mmfState: StateFlow<MmfState> = _mmfState

    fun invest(user: UserEntity, fundName: String, amountStr: String) {
        if (amountStr.isBlank()) {
            _mmfState.value = MmfState.Error("Please enter an amount")
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _mmfState.value = MmfState.Error("Invalid amount")
            return
        }

        if (amount > user.virtualCashBalance) {
            _mmfState.value = MmfState.Error("Insufficient funds! You have KES ${user.virtualCashBalance}")
            return
        }

        viewModelScope.launch {
            // 1. Deduct from Wallet
            val updatedUser = user.copy(virtualCashBalance = user.virtualCashBalance - amount)
            repository.updateUser(updatedUser)

            // 2. Save Transaction Record
            val transaction = TransactionEntity(
                userId = user.id,
                assetName = fundName,
                amountInvested = amount
            )
            repository.insertTransaction(transaction)

            _mmfState.value = MmfState.Success
        }
    }
}