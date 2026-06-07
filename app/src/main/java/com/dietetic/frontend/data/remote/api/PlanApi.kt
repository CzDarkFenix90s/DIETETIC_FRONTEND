package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.PlanDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class PlanResponseDto(
    val count: Int,
    val results: List<PlanDto>
)

interface PlanApi {
    @GET("planes/")
    suspend fun list(): Response<PlanResponseDto>

    @GET("planes/{id}/")
    suspend fun retrieve(@Path("id") id: Int): Response<PlanDto>

    @POST("planes/")
    suspend fun create(@Body request: PlanDto): Response<PlanDto>
}
