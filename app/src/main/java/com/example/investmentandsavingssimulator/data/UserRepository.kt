package com.example.investmentandsavingssimulator.data

import com.example.investmentandsavingssimulator.data.local.UserDao
import com.example.investmentandsavingssimulator.data.local.UserEntity
// Make sure this import matches where your teammate put TransactionDao
import com.example.investmentandsavingssimulator.data.local.TransactionDao
import com.example.investmentandsavingssimulator.data.local.TransactionEntity

class UserRepository(
    private val userDao: UserDao,
    private val transactionDao: TransactionDao // <-- Added this to fix the error
) {

    // --- Authentication ---
    suspend fun registerUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun loginUser(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    // --- User Data ---
    suspend fun getUserById(userId: Int): UserEntity? {
        return userDao.getUserById(userId)
    }

    suspend fun updateUserBalance(userId: Int, newBalance: Double) {
        userDao.updateBalance(userId, newBalance)
    }

    // --- For InterestWorker ---
    suspend fun getAllUsers(): List<UserEntity> {
        return userDao.getAllUsers()
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    // --- For MmfViewModel (Transactions) ---
    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction) // Assuming this is the function name in your teammate's DAO
    }
}