package com.example.investmentandsavingssimulator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investmentandsavingssimulator.data.UserRepository
import com.example.investmentandsavingssimulator.data.local.AssetEntity
import com.example.investmentandsavingssimulator.data.local.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: UserRepository,
    private val assetDao: com.example.investmentandsavingssimulator.data.local.AssetDao
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _assets = MutableStateFlow<List<AssetEntity>>(emptyList())
    val assets: StateFlow<List<AssetEntity>> = _assets

    fun loadDashboardData(userId: Int) {
        viewModelScope.launch {
            _user.value = repository.getUserById(userId)
            _assets.value = assetDao.getAllAssets()
        }
    }
}