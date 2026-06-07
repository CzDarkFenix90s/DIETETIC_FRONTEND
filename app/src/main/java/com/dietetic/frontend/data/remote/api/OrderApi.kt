package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {
    @GET("consultas/")
    suspend fun getOrders(
        @Query("page") page: Int? = null,
        @Query("status") status: String? = null
    ): Response<OrderResponseDto>

    @GET("consultas/{id}/")
    suspend fun getOrder(@Path("id") id: Int): Response<OrderDto>

    @POST("consultas/")
    suspend fun createOrder(): Response<OrderDto>

    @POST("consultas/{id}/add_item/")
    suspend fun addItem(
        @Path("id") orderId: Int,
        @Body request: AddItemRequestDto
    ): Response<OrderDto>

    @POST("consultas/{id}/confirm/")
    suspend fun confirmOrder(@Path("id") id: Int): Response<OrderDto>

    @PATCH("consultas/{id}/update_status/")
    suspend fun updateStatus(
        @Path("id") id: Int,
        @Body request: UpdateStatusRequestDto
    ): Response<OrderDto>

    @GET("planes/stats/")
    suspend fun getStats(): Response<OrderStatsDto>
}
