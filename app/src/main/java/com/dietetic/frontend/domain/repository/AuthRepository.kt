package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.LoggedUser
import com.dietetic.frontend.data.local.TokenDataStore

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoggedUser>
    suspend fun register(
        username: String, 
        email: String, 
        password: String, 
        password2: String,
        firstName: String? = null,
        lastName: String? = null
    ): Result<LoggedUser>
    suspend fun logout(): Result<Unit>
    suspend fun getStoredUser(): TokenDataStore.UserSnapshot?
    suspend fun isLoggedIn(): Boolean
}
