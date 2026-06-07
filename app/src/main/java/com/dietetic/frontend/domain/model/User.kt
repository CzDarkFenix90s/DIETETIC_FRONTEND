package com.dietetic.frontend.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val isStaff: Boolean,
    val role: String
)
