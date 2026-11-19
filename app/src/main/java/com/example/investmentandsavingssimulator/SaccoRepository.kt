package com.example.investmentandsavingssimulator.data

import com.example.investmentandsavingssimulator.data.local.SaccoDao
import com.example.investmentandsavingssimulator.data.local.SaccoEntity

class SaccoRepository(
    private val saccoDao: SaccoDao
) {
    suspend fun insertSacco(sacco: SaccoEntity) {
        saccoDao.insertSacco(sacco)
    }

    suspend fun getAllSaccos(): List<SaccoEntity> {
        return saccoDao.getAllSaccos()
    }

    suspend fun getSaccoById(id: Int): SaccoEntity? {
        return saccoDao.getSaccoById(id)
    }
}
