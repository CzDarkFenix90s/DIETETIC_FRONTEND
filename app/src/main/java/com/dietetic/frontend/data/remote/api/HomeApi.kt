package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.HomeStatsDto
import retrofit2.Response
import retrofit2.http.GET

interface HomeApi {
    @GET("planes/stats/")
    suspend fun getStats(): Response<HomeStatsDto>
}
