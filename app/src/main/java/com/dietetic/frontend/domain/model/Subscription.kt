package com.dietetic.frontend.domain.model

data class Subscription(
    val id: Int,
    val userId: Int,
    val planName: String, // ej: "Premium", "Free"
    val price: Double,
    val status: String, // ej: "active", "expired"
    val startDate: String,
    val endDate: String
)

data class SubscriptionPayload(
    val userId: Int,
    val planName: String,
    val price: Double,
    val status: String,
    val endDate: String
)
