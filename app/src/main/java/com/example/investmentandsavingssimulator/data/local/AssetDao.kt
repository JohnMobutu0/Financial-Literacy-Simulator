package com.example.investmentandsavingssimulator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AssetDao {
    @Insert
    suspend fun insertAll(assets: List<AssetEntity>)

    @Query("SELECT * FROM assets")
    suspend fun getAllAssets(): List<AssetEntity>
}