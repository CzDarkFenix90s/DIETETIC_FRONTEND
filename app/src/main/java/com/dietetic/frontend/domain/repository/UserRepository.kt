package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.User

interface UserRepository {
    suspend fun getUsers(isStaff: Boolean? = null): Result<List<User>>
    suspend fun deleteUser(id: Int): Result<Unit>
}
