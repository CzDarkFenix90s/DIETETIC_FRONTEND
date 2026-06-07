package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.UserApi
import com.dietetic.frontend.domain.model.User
import com.dietetic.frontend.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {
    override suspend fun getUsers(isStaff: Boolean?): Result<List<User>> = runCatching {
        val resp = api.list(isStaff)
        if (!resp.isSuccessful) throw Exception("Error listando usuarios")
        resp.body()!!.results.map { dto ->
            User(
                id = dto.id,
                username = dto.username,
                email = dto.email,
                isStaff = dto.isStaff,
                role = dto.role?.name ?: "user"
            )
        }
    }

    override suspend fun deleteUser(id: Int): Result<Unit> = runCatching {
        val resp = api.delete(id)
        if (!resp.isSuccessful) throw Exception("Error eliminando usuario")
    }
}
