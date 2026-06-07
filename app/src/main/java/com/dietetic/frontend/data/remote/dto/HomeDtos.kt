package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HomeStatsDto(
    val total: Int = 0,
    val active: Int = 0,
    val inactive: Int = 0,
    @SerializedName("total_xp") val totalXp: Int = 0,
    @SerializedName("current_streak") val currentStreak: Int = 0
)

data class AchievementDto(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    @SerializedName("is_unlocked") val isUnlocked: Boolean
)

data class AchievementResponseDto(
    val count: Int,
    val results: List<AchievementDto>
)

data class ProgressResponseDto(
    @SerializedName("total_completed") val totalCompleted: Int,
    @SerializedName("next_lesson") val nextLesson: String?,
    @SerializedName("current_course") val currentCourse: String?,
    @SerializedName("lesson_progress") val lessonProgress: List<LessonProgressDto>
)

data class LessonProgressDto(
    @SerializedName("lesson_id") val lessonId: Int,
    val status: String,
    @SerializedName("completed_at") val completedAt: String?
)
