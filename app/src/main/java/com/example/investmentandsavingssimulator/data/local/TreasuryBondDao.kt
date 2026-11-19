package com.example.investmentandsavingssimulator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TreasuryBondDao {

    @Insert
    suspend fun insertBond(bond: TreasuryBondEntity)

    @Query("SELECT * FROM treasury_bonds")
    suspend fun getAllBonds(): List<TreasuryBondEntity>
}
