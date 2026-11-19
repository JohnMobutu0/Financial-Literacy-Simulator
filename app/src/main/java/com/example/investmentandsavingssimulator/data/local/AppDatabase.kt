package com.example.investmentandsavingssimulator.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 1. We added TransactionEntity to the list of entities.
// 2. We bumped the version to 3.
// 3. We set exportSchema to false to silence the Room warning.
@Database(
    entities = [
        UserEntity::class,
        AssetEntity::class,
        TransactionEntity::class,
        TreasuryBondEntity::class,
        SaccoEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun assetDao(): AssetDao
    abstract fun transactionDao(): TransactionDao // Added the new DAO
    abstract fun saccoDao(): SaccoDao
    abstract fun treasuryBondDao(): TreasuryBondDao
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
                    // This deletes the old database if the version changes.
                    // Perfect for development so you don't have to uninstall the app.
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate the investment assets when the database is first created
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {

                                    database.assetDao().insertAll(
                                        listOf(
                                            AssetEntity(
                                                name = "CIC Money Market Fund",
                                                type = "MMF",
                                                expectedReturn = "12% p.a.",
                                                price = 1.0
                                            ),
                                            AssetEntity(
                                                name = "Safaricom (NSE)",
                                                type = "Stock",
                                                expectedReturn = "Dividends",
                                                price = 25.50
                                            ),
                                            AssetEntity(
                                                name = "Infrastructure Bond",
                                                type = "Bond",
                                                expectedReturn = "14% p.a.",
                                                price = 1000.0
                                            ),
                                            AssetEntity(
                                                name = "Stima SACCO Shares",
                                                type = "SACCO",
                                                expectedReturn = "15% p.a.",
                                                price = 500.0
                                            )
                                        )
                                    )

// Add sample SACCO investment options
                                    if (database.saccoDao().getAllSaccos().isEmpty()) {

                                        database.saccoDao().insertSacco(
                                            SaccoEntity(
                                                name = "Stima SACCO",
                                                description = "Simulated savings and shares investment",
                                                minimumContribution = 1000.0,
                                                annualDividendRate = 12.0,
                                                sharePrice = 100.0,
                                                totalShares = 10000
                                            )
                                        )

                                        database.saccoDao().insertSacco(
                                            SaccoEntity(
                                                name = "Mwalimu SACCO",
                                                description = "Simulated member savings and shares",
                                                minimumContribution = 2000.0,
                                                annualDividendRate = 10.0,
                                                sharePrice = 200.0,
                                                totalShares = 15000
                                            )
                                        )

                                        database.saccoDao().insertSacco(
                                            SaccoEntity(
                                                name = "Harambee SACCO",
                                                description = "Simulated savings and investment shares",
                                                minimumContribution = 500.0,
                                                annualDividendRate = 11.0,
                                                sharePrice = 50.0,
                                                totalShares = 20000
                                            )
                                        )
                                    }

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