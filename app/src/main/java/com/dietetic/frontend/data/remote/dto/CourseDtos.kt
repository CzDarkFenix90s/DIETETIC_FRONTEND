package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.dietetic.frontend.domain.model.Course

data class CourseResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Course>
)
