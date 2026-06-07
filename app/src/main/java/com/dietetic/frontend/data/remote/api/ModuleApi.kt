package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.ModuleResponse
import com.dietetic.frontend.domain.model.Module
import com.dietetic.frontend.domain.model.ModulePayload
import retrofit2.Response
import retrofit2.http.*

interface ModuleApi {
    @GET("alimentos/")
    suspend fun getModules(): Response<ModuleResponse>

    @GET("planes/{courseId}/alimentos/")
    suspend fun getModulesByCourse(@Path("courseId") courseId: Int): Response<ModuleResponse>

    @GET("alimentos/{id}/")
    suspend fun getModuleById(@Path("id") id: Int): Response<com.dietetic.frontend.data.remote.dto.ModuleDto>

    @POST("alimentos/")
    suspend fun createModule(@Body payload: ModulePayload): Response<com.dietetic.frontend.data.remote.dto.ModuleDto>

    @PUT("alimentos/{id}/")
    suspend fun updateModule(@Path("id") id: Int, @Body payload: ModulePayload): Response<com.dietetic.frontend.data.remote.dto.ModuleDto>

    @DELETE("alimentos/{id}/")
    suspend fun deleteModule(@Path("id") id: Int): Response<Unit>
}
