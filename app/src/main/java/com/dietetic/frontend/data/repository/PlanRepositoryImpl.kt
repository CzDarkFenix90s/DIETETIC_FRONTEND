package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.PlanApi
import com.dietetic.frontend.domain.model.PlanNutricional
import com.dietetic.frontend.domain.repository.PlanRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanRepositoryImpl @Inject constructor(
    private val api: PlanApi,
) : PlanRepository {

    override suspend fun getPlanes(): Result<List<PlanNutricional>> = runCatching {
        val resp = api.list()
        if (!resp.isSuccessful) throw Exception("Error listando planes: ${resp.code()}")
        resp.body()!!.results.map { dto ->
            PlanNutricional(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                goal = dto.goal,
                targetCalories = dto.targetCalories,
                durationWeeks = dto.durationWeeks,
                estimatedCost = dto.estimatedCost,
                isActive = dto.isActive,
            )
        }
    }

    override suspend fun getPlan(id: Int): Result<PlanNutricional> = runCatching {
        val resp = api.retrieve(id)
        if (!resp.isSuccessful) throw Exception("Error obteniendo plan: ${resp.code()}")
        val dto = resp.body()!!
        PlanNutricional(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            goal = dto.goal,
            targetCalories = dto.targetCalories,
            durationWeeks = dto.durationWeeks,
            estimatedCost = dto.estimatedCost,
            isActive = dto.isActive,
        )
    }

    override suspend fun createPlan(plan: PlanNutricional): Result<PlanNutricional> = runCatching {
        val dtoRequest = com.dietetic.frontend.data.remote.dto.PlanDto(
            name = plan.name,
            description = plan.description,
            goal = plan.goal,
            targetCalories = plan.targetCalories,
            durationWeeks = plan.durationWeeks,
            estimatedCost = plan.estimatedCost,
            isActive = plan.isActive,
        )
        val resp = api.create(dtoRequest)
        if (!resp.isSuccessful) throw Exception("Error creando plan: ${resp.code()}")
        val dto = resp.body()!!
        PlanNutricional(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            goal = dto.goal,
            targetCalories = dto.targetCalories,
            durationWeeks = dto.durationWeeks,
            estimatedCost = dto.estimatedCost,
            isActive = dto.isActive,
        )
    }
}
