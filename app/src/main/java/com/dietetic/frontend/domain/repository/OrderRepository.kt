package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.Order
import com.dietetic.frontend.domain.model.OrderStatus

interface OrderRepository {
    suspend fun getOrders(page: Int? = null, status: String? = null): Result<Pair<List<Order>, Int>>
    suspend fun getOrder(id: Int): Result<Order>
    suspend fun createOrder(): Result<Order>
    suspend fun addItem(orderId: Int, courseId: Int, quantity: Int): Result<Order>
    suspend fun confirmOrder(orderId: Int): Result<Order>
    suspend fun updateStatus(orderId: Int, status: OrderStatus): Result<Order>
    suspend fun getStats(): Result<Map<String, Any>>
}
