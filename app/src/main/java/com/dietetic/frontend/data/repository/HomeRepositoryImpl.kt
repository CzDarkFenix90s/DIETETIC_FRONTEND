package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.HomeApi
import com.dietetic.frontend.data.remote.dto.HomeStatsDto
import com.dietetic.frontend.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi
) : HomeRepository {

    override suspend fun getStats(): Result<List<HomeStatsDto>> = runCatching {
        val response = api.getStats()
        if (response.isSuccessful) {
            val stats = response.body() ?: HomeStatsDto()
            listOf(stats)
        } else {
            listOf(HomeStatsDto())
        }
    }

    // Mocking for diet domain
    override suspend fun getAchievements(): Result<List<com.dietetic.frontend.data.remote.dto.AchievementDto>> = Result.success(emptyList())
    override suspend fun getProgress(): Result<com.dietetic.frontend.data.remote.dto.ProgressResponseDto> = 
        Result.success(com.dietetic.frontend.data.remote.dto.ProgressResponseDto(0, null, null, emptyList()))
}
