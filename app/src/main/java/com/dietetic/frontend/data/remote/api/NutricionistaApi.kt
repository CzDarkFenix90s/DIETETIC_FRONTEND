package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.NutricionistaDto
import com.dietetic.frontend.data.remote.dto.NutricionistaResponseDto
import retrofit2.Response
import retrofit2.http.*

interface NutricionistaApi {
    @GET("nutricionistas/")
    suspend fun list(): Response<NutricionistaResponseDto>

    @GET("nutricionistas/{id}/")
    suspend fun retrieve(@Path("id") id: Int): Response<NutricionistaDto>

    @POST("nutricionistas/")
    suspend fun create(@Body request: NutricionistaDto): Response<NutricionistaDto>

    @PUT("nutricionistas/{id}/")
    suspend fun update(@Path("id") id: Int, @Body request: NutricionistaDto): Response<NutricionistaDto>

    @DELETE("nutricionistas/{id}/")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}
