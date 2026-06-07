package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.PlanNutricional

interface PlanRepository {
    suspend fun getPlanes(): Result<List<PlanNutricional>>
    suspend fun getPlan(id: Int): Result<PlanNutricional>
    suspend fun createPlan(plan: PlanNutricional): Result<PlanNutricional>
}
