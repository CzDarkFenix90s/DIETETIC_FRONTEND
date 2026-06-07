package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.Course
import com.dietetic.frontend.domain.model.CoursePayload

data class CourseFilters(
    val search: String? = null,
    val goal: String? = null,
    val targetCalories: Int? = null,
    val isActive: Boolean? = null,
    val page: Int = 1,
    val pageSize: Int = 12
)

interface CourseRepository {
    suspend fun getCourses(filters: CourseFilters = CourseFilters()): Result<Pair<List<Course>, Int>>
    suspend fun getCourseById(id: Int): Result<Course>
    suspend fun createCourse(payload: CoursePayload): Result<Course>
    suspend fun updateCourse(id: Int, payload: CoursePayload): Result<Course>
    suspend fun deleteCourse(id: Int): Result<Unit>
}
