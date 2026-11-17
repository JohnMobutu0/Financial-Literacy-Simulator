package com.example.investmentandsavingssimulator.data

import com.example.investmentandsavingssimulator.data.local.TransactionDao // ADD IMPORT
import com.example.investmentandsavingssimulator.data.local.TransactionEntity // ADD IMPORT
import com.example.investmentandsavingssimulator.data.local.UserDao
import com.example.investmentandsavingssimulator.data.local.UserEntity

// UPDATE THE CONSTRUCTOR:
class UserRepository(
    private val userDao: UserDao,
    private val transactionDao: TransactionDao
) {
    suspend fun loginUser(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    suspend fun registerUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserById(id: Int): UserEntity? {
        return userDao.getUserById(id)
    }

    // ADD THESE TWO FUNCTIONS:
    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }
}