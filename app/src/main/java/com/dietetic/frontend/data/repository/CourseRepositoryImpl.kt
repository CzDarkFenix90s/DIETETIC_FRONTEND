package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.CourseApi
import com.dietetic.frontend.domain.model.Course
import com.dietetic.frontend.domain.model.CoursePayload
import com.dietetic.frontend.domain.repository.CourseFilters
import com.dietetic.frontend.domain.repository.CourseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepositoryImpl @Inject constructor(
    private val api: CourseApi
) : CourseRepository {

    override suspend fun getCourses(filters: CourseFilters): Result<Pair<List<Course>, Int>> = runCatching {
        val params = buildMap<String, String> {
            filters.search?.let { put("search", it) }
            filters.goal?.let { put("goal", it) }
            filters.targetCalories?.let { put("target_calories", it.toString()) }
            filters.isActive?.let { put("is_active", it.toString()) }
            put("page", filters.page.toString())
            put("page_size", filters.pageSize.toString())
        }
        val response = api.getCourses(params)
        if (response.isSuccessful) {
            val body = response.body()!!
            Pair(body.results, body.count)
        } else throw Exception("Error ${response.code()}")
    }

    private fun handleError(code: Int, body: String?): Exception {
        val msg = try {
            val map = com.google.gson.Gson().fromJson(body, Map::class.java)
            map["detail"]?.toString() ?: map.values.firstOrNull()?.toString()
        } catch (e: Exception) {
            null
        }
        
        return when (code) {
            400 -> Exception(msg ?: "Datos inválidos (400)")
            401 -> Exception("Sesión caducada. Reingrese. (401)")
            403 -> Exception("No tiene permisos para esto. (403)")
            404 -> Exception("Registro no encontrado. (404)")
            500 -> Exception("Error interno del servidor. (500)")
            else -> Exception(msg ?: "Error de conexión ($code)")
        }
    }

    override suspend fun getCourseById(id: Int): Result<Course> = runCatching {
        val response = api.getCourseById(id)
        if (response.isSuccessful) response.body()!!
        else throw handleError(response.code(), response.errorBody()?.string())
    }

    override suspend fun createCourse(payload: CoursePayload): Result<Course> = runCatching {
        val response = api.createCourse(payload)
        if (response.isSuccessful) response.body()!!
        else throw handleError(response.code(), response.errorBody()?.string())
    }

    override suspend fun updateCourse(id: Int, payload: CoursePayload): Result<Course> = runCatching {
        val response = api.updateCourse(id, payload)
        if (response.isSuccessful) response.body()!!
        else throw handleError(response.code(), response.errorBody()?.string())
    }

    override suspend fun deleteCourse(id: Int): Result<Unit> = runCatching {
        val response = api.deleteCourse(id)
        if (response.isSuccessful) Unit
        else throw handleError(response.code(), response.errorBody()?.string())
    }
}
