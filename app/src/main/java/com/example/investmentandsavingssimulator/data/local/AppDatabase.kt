package com.example.investmentandsavingssimulator.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UserEntity::class, AssetEntity::class, TransactionEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun assetDao(): AssetDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "investment_simulator_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.assetDao().insertAll(
                                        listOf(
                                            AssetEntity(name = "CIC Money Market Fund", type = "MMF", expectedReturn = "12% p.a.", price = 1.0),
                                            AssetEntity(name = "Safaricom (SCOM)", type = "Stock", expectedReturn = "Dividends", price = 25.50),
                                            AssetEntity(name = "Equity Group (EQTY)", type = "Stock", expectedReturn = "Dividends", price = 50.0),
                                            AssetEntity(name = "KCB Group (KCB)", type = "Stock", expectedReturn = "Dividends", price = 40.0),
                                            AssetEntity(name = "East African Breweries (EABL)", type = "Stock", expectedReturn = "Dividends", price = 150.0),
                                            AssetEntity(name = "Co-operative Bank (COOP)", type = "Stock", expectedReturn = "Dividends", price = 15.0),
                                            AssetEntity(name = "Infrastructure Bond", type = "Bond", expectedReturn = "14% p.a.", price = 1000.0),
                                            AssetEntity(name = "Stima SACCO Shares", type = "SACCO", expectedReturn = "15% p.a.", price = 500.0)
                                        )
                                    )
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}