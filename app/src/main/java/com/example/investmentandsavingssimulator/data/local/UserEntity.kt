package com.example.investmentandsavingssimulator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // <--- Must be here
    val username: String,
    val email: String,
    val password: String,
    val virtualCashBalance: Double = 100000.0
)