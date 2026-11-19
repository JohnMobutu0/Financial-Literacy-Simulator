package com.example.investmentandsavingssimulator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SaccoDao {

    @Insert
    suspend fun insertSacco(sacco: SaccoEntity)

    @Query("SELECT * FROM sacco")
    suspend fun getAllSaccos(): List<SaccoEntity>

    @Query("SELECT * FROM sacco WHERE id = :id")
    suspend fun getSaccoById(id: Int): SaccoEntity?
}
