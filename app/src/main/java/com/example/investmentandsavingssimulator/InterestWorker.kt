package com.example.investmentandsavingssimulator

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.investmentandsavingssimulator.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InterestWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val db = AppDatabase.getDatabase(applicationContext)

            // Simulate a daily interest payout for MMFs
            // For demo purposes, we give a small cash bonus every time the worker runs
            val allUsers = db.userDao().getAllUsers() // You'd need to add this to UserDao

            allUsers.forEach { user ->
                // Simulate +1% interest for demonstration
                val interest = user.virtualCashBalance * 0.01
                val updatedUser = user.copy(virtualCashBalance = user.virtualCashBalance + interest)
                db.userDao().updateUser(updatedUser)
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}