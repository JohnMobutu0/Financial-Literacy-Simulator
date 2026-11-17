package com.example.investmentandsavingssimulator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String, // e.g., "CIC Money Market Fund"
    val type: String, // e.g., "MMF", "Stock", "Bond"
    val expectedReturn: String, // e.g., "12% p.a."
    val price: Double // Simulated current price
)