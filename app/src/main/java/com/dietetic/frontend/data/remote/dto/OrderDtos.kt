package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.dietetic.frontend.domain.model.Course
import com.dietetic.frontend.domain.model.Order
import com.dietetic.frontend.domain.model.OrderStatus

data class OrderResponseDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<OrderDto>
)

data class OrderDto(
    val id: Int,
    val status: String,
    @SerializedName("session_notes") val sessionNotes: String?,
    @SerializedName("scheduled_time") val scheduledTime: String,
    @SerializedName("plan_nutricional") val plan: Course?,
    @SerializedName("paciente_nombre") val pacienteNombre: String?,
    @SerializedName("created_at") val createdAt: String
) {
    fun toDomain(): Order = Order(
        id = id,
        patientName = pacienteNombre ?: "",
        plan = plan,
        notes = sessionNotes ?: "",
        status = OrderStatus.fromValue(status),
        scheduledTime = scheduledTime,
        createdAt = createdAt
    )
}

data class OrderItemDto(
    val id: Int,
    @SerializedName("course") val courseId: Int,
    @SerializedName("course_title") val courseTitle: String,
    val price: Double,
    val quantity: Int
)

data class AddItemRequestDto(
    @SerializedName("course_id") val courseId: Int,
    val quantity: Int
)

data class UpdateStatusRequestDto(
    val status: String
)

data class OrderStatsDto(
    @SerializedName("total_consultas") val totalOrders: Int,
    @SerializedName("by_status") val byStatus: Map<String, Int>
)
