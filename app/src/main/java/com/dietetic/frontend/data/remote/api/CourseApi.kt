package com.dietetic.frontend.data.remote.api

import com.dietetic.frontend.data.remote.dto.CourseResponse
import com.dietetic.frontend.domain.model.Course
import com.dietetic.frontend.domain.model.CoursePayload
import retrofit2.Response
import retrofit2.http.*

interface CourseApi {
    @GET("planes/")
    suspend fun getCourses(
        @QueryMap params: Map<String, String>
    ): Response<CourseResponse>

    @GET("planes/{id}/")
    suspend fun getCourseById(@Path("id") id: Int): Response<Course>

    @POST("planes/")
    suspend fun createCourse(@Body payload: CoursePayload): Response<Course>

    @PUT("planes/{id}/")
    suspend fun updateCourse(@Path("id") id: Int, @Body payload: CoursePayload): Response<Course>

    @DELETE("planes/{id}/")
    suspend fun deleteCourse(@Path("id") id: Int): Response<Unit>
}
