package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

data class ConsultaResponseDto(
    val count: Int,
    val results: List<ConsultaDto>
)

interface ConsultaApi {
    @GET("consultas/")
    suspend fun list(): Response<ConsultaResponseDto>

    @GET("consultas/mine/")
    suspend fun listMine(): Response<ConsultaResponseDto>

    @GET("consultas/{id}/")
    suspend fun retrieve(@Path("id") id: Int): Response<ConsultaDto>

    @POST("consultas/")
    suspend fun create(@Body payload: ConsultaCreateRequest): Response<ConsultaDto>

    @POST("consultas/{id}/add-session-note/")
    suspend fun addSessionNote(@Path("id") id: Int, @Body payload: AddNoteRequest): Response<ConsultaDto>

    @POST("consultas/{id}/start-consultation/")
    suspend fun startConsultation(@Path("id") id: Int): Response<ConsultaDto>

    @PATCH("consultas/{id}/update-status/")
    suspend fun updateStatus(@Path("id") id: Int, @Body payload: UpdateStatusRequest): Response<ConsultaDto>
}
