package com.dietetic.frontend.domain.model

import com.google.gson.annotations.SerializedName

enum class OrderStatus(val value: String) {
    PROGRAMADA("programada"),
    EN_CURSO("en_curso"),
    COMPLETADA("completada"),
    RETRASADA("retrasada"),
    CANCELADA("cancelada");

    companion object {
        fun fromValue(value: String): OrderStatus = 
            entries.find { it.value == value } ?: PROGRAMADA
    }
}

data class Order(
    val id: Int,
    @SerializedName("paciente_nombre") val patientName: String = "",
    @SerializedName("plan_nutricional") val plan: Course? = null,
    @SerializedName("session_notes") val notes: String = "",
    val status: OrderStatus,
    @SerializedName("scheduled_time") val scheduledTime: String = "",
    @SerializedName("created_at") val createdAt: String
)

// Mantenemos OrderItem por compatibilidad con el carrito por ahora, 
// aunque en este dominio una consulta suele ser por un plan.
data class OrderItem(
    val id: Int,
    val courseId: Int,
    val courseTitle: String,
    val price: Double,
    val quantity: Int
)
