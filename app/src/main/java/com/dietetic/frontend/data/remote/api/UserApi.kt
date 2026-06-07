package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("users/")
    suspend fun list(@Query("is_staff") isStaff: Boolean? = null): Response<UserResponseDto>

    @GET("users/{id}/")
    suspend fun retrieve(@Path("id") id: Int): Response<UserDto>

    @DELETE("users/{id}/")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}

data class UserResponseDto(
    val count: Int,
    val results: List<UserDto>
)
