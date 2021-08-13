package com.example.beaconexample.repositories

import com.example.beaconexample.data.User
import com.example.beaconexample.utils.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UserRepository {
    suspend fun login(email: String, password: String): User? {
        return withContext(Dispatchers.Default) {
            val emailTest = "gustavo.salesforce@gmail.com"
            val passwordTest = "123456"

            if (email.toLowerCase(Locale.ROOT) == emailTest && password == passwordTest) {
                User(
                    id = 1,
                    firstName = "Gustavo",
                    lastName = "Mora",
                    email = "gustavo.salesforce@gmail.com"
                )
            } else {
                null
            }
        }
    }

    suspend fun getUser(): User? {
        return withContext(Dispatchers.Default) {
            LocalStorage.user
        }
    }
}