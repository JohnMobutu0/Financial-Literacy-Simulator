package com.example.investmentandsavingssimulator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val assetName: String,
    val amountInvested: Double,
    val timestamp: Long = System.currentTimeMillis()
)