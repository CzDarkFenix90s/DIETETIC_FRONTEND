package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.AlimentoDto
import com.dietetic.frontend.data.remote.dto.UpdateOrderRequest
import retrofit2.Response
import retrofit2.http.*

data class AlimentoResponseDto(
    val count: Int,
    val results: List<AlimentoDto>
)

interface AlimentoApi {
    @GET("alimentos/")
    suspend fun list(): Response<AlimentoResponseDto>

    @GET("alimentos/{id}/")
    suspend fun retrieve(@Path("id") id: Int): Response<AlimentoDto>

    @GET("alimentos/available/")
    suspend fun available(): Response<AlimentoResponseDto>

    @PATCH("alimentos/{id}/update-order/")
    suspend fun updateOrder(@Path("id") id: Int, @Body payload: UpdateOrderRequest): Response<AlimentoDto>
}
