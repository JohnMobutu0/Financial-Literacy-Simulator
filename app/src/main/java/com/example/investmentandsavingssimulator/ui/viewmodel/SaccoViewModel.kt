
package com.example.investmentandsavingssimulator.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investmentandsavingssimulator.data.SaccoRepository
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.SaccoEntity
import com.example.investmentandsavingssimulator.data.local.TransactionEntity
import com.example.investmentandsavingssimulator.data.local.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SaccoViewModel(
    private val repository: SaccoRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _saccos = MutableStateFlow<List<SaccoEntity>>(emptyList())
    val saccos: StateFlow<List<SaccoEntity>> = _saccos

    sealed class PurchaseState {
        object Idle : PurchaseState()
        object Success : PurchaseState()
        data class Error(val message: String) : PurchaseState()
    }

    private val _purchaseState =
        MutableStateFlow<PurchaseState>(PurchaseState.Idle)

    val purchaseState: StateFlow<PurchaseState> = _purchaseState

    fun loadSaccos() {
        viewModelScope.launch {
            _saccos.value = repository.getAllSaccos()
        }
    }

    fun addSacco(sacco: SaccoEntity) {
        viewModelScope.launch {
            repository.insertSacco(sacco)
            loadSaccos()
        }
    }

    fun buyShares(
        user: UserEntity,
        sacco: SaccoEntity,
        numberOfShares: Int
    ) {
        if (numberOfShares <= 0) {
            _purchaseState.value =
                PurchaseState.Error("Enter a valid number of shares")
            return
        }

        val amount = sacco.sharePrice * numberOfShares

        if (amount < sacco.minimumContribution) {
            _purchaseState.value = PurchaseState.Error(
                "Minimum contribution is KES ${sacco.minimumContribution}"
            )
            return
        }

        if (amount > user.virtualCashBalance) {
            _purchaseState.value =
                PurchaseState.Error("Insufficient virtual wallet balance")
            return
        }

        viewModelScope.launch {
            val updatedUser = user.copy(
                virtualCashBalance = user.virtualCashBalance - amount
            )

            userRepository.updateUser(updatedUser)

            val transaction = TransactionEntity(
                userId = user.id,
                assetName = "${sacco.name} Shares",
                amountInvested = amount
            )

            userRepository.insertTransaction(transaction)

            _purchaseState.value = PurchaseState.Success
        }
    }
}
