package com.example.investmentandsavingssimulator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getUserTransactions(userId: Int): List<TransactionEntity>
}