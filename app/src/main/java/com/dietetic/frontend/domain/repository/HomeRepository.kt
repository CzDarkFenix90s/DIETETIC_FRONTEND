package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.data.remote.dto.AchievementDto
import com.dietetic.frontend.data.remote.dto.HomeStatsDto
import com.dietetic.frontend.data.remote.dto.ProgressResponseDto

interface HomeRepository {
    suspend fun getStats(): Result<List<HomeStatsDto>>
    suspend fun getAchievements(): Result<List<AchievementDto>>
    suspend fun getProgress(): Result<ProgressResponseDto>
}
