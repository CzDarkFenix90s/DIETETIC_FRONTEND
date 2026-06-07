package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("password2") val password2: String,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null
)

data class LogoutRequest(
    val refresh: String
)

data class AuthResponse(
    val access: String? = null,
    val refresh: String? = null,
    @SerializedName("user_id") val userId: Int? = null,
    val username: String? = null,
    val email: String? = null,
    @SerializedName("is_staff") val isStaff: Boolean = false,
    val role: String? = null,
    val user: UserDto? = null
)

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("is_staff") val isStaff: Boolean = false,
    val role: RoleDto? = null
)

data class RoleDto(
    val id: Int,
    val name: String
)
