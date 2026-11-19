package com.example.investmentandsavingssimulator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sacco")
data class SaccoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String,
    val minimumContribution: Double,
    val annualDividendRate: Double,
    val sharePrice: Double,
    val totalShares: Int
)
