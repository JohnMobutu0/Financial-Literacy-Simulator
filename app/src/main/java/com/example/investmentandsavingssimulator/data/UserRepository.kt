package com.example.investmentandsavingssimulator.data

// ADD THESE TWO IMPORT LINES:
import com.example.investmentandsavingssimulator.data.local.UserDao
import com.example.investmentandsavingssimulator.data.local.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun loginUser(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    suspend fun registerUser(user: UserEntity) {
        userDao.insertUser(user)
    }
}