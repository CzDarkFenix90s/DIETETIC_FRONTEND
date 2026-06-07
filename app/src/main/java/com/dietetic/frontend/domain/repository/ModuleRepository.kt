package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.Module
import com.dietetic.frontend.domain.model.ModulePayload

interface ModuleRepository {
    suspend fun getModules(): Result<List<Module>>
    suspend fun getModulesByCourse(courseId: Int): Result<List<Module>>
    suspend fun getModuleById(id: Int): Result<Module>
    suspend fun createModule(payload: ModulePayload): Result<Module>
    suspend fun updateModule(id: Int, payload: ModulePayload): Result<Module>
    suspend fun deleteModule(id: Int): Result<Unit>
}
