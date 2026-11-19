package com.example.investmentandsavingssimulator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "treasury_bonds")
data class treasuryBondEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val couponRate: Double,
    val maturityYears: Int,
    val minimumInvestment: Double
)
