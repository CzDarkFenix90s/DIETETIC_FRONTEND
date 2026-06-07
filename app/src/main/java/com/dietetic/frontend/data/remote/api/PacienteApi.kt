package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.PacienteDto
import com.dietetic.frontend.data.remote.dto.PacienteResponseDto
import retrofit2.Response
import retrofit2.http.*

interface PacienteApi {
    @GET("pacientes/")
    suspend fun list(): Response<PacienteResponseDto>

    @GET("pacientes/{id}/")
    suspend fun retrieve(@Path("id") id: Int): Response<PacienteDto>

    @POST("pacientes/")
    suspend fun create(@Body request: PacienteDto): Response<PacienteDto>

    @PUT("pacientes/{id}/")
    suspend fun update(@Path("id") id: Int, @Body request: PacienteDto): Response<PacienteDto>

    @DELETE("pacientes/{id}/")
    suspend fun delete(@Path("id") id: Int): Response<Unit>
}
