package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.ModuleApi
import com.dietetic.frontend.domain.model.Module
import com.dietetic.frontend.domain.model.ModulePayload
import com.dietetic.frontend.domain.repository.ModuleRepository
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleRepositoryImpl @Inject constructor(
    private val api: ModuleApi
) : ModuleRepository {

    private fun handleError(code: Int, body: String?): Exception {
        val msg = try {
            val map = Gson().fromJson(body, Map::class.java)
            map["detail"]?.toString() ?: map.values.firstOrNull()?.toString()
        } catch (e: Exception) {
            null
        }
        return Exception(msg ?: "Error de servidor ($code)")
    }

    override suspend fun getModules(): Result<List<Module>> = runCatching {
        val response = api.getModules()
        if (response.isSuccessful) response.body()?.results?.map { it.toDomain() } ?: emptyList()
        else throw handleError(response.code(), response.errorBody()?.string())
    }

    override suspend fun getModulesByCourse(courseId: Int): Result<List<Module>> = runCatching {
        val response = api.getModulesByCourse(courseId)
        if (response.isSuccessful) response.body()?.results?.map { it.toDomain() } ?: emptyList()
        else throw handleError(response.code(), response.errorBody()?.string())
    }

    override suspend fun getModuleById(id: Int): Result<Module> = runCatching {
        val response = api.getModuleById(id)
        if (response.isSuccessful) response.body()!!.toDomain()
        else throw handleError(response.code(), response.errorBody()?.string())
    }

    override suspend fun createModule(payload: ModulePayload): Result<Module> = runCatching {
        val response = api.createModule(payload)
        if (response.isSuccessful) response.body()!!.toDomain()
        else throw handleError(response.code(), response.errorBody()?.string())
    }

    override suspend fun updateModule(id: Int, payload: ModulePayload): Result<Module> = runCatching {
        val response = api.updateModule(id, payload)
        if (response.isSuccessful) response.body()!!.toDomain()
        else throw handleError(response.code(), response.errorBody()?.string())
    }

    private fun com.dietetic.frontend.data.remote.dto.ModuleDto.toDomain(): Module {
        val pMap = plan as? Map<*, *>
        return Module(
            id = id,
            courseId = (pMap?.get("id") as? Number)?.toInt(),
            courseTitle = pMap?.get("name") as? String ?: "General",
            title = title,
            description = description,
            order = sequence,
            mealType = mealType,
            portionGrams = portionGrams,
            isActive = isActive
        )
    }

    override suspend fun deleteModule(id: Int): Result<Unit> = runCatching {
        val response = api.deleteModule(id)
        if (!response.isSuccessful) throw handleError(response.code(), response.errorBody()?.string())
    }
}
