package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.Nutricionista

interface NutricionistaRepository {
    suspend fun getNutricionistas(): Result<List<Nutricionista>>
    suspend fun getNutricionista(id: Int): Result<Nutricionista>
    suspend fun createNutricionista(nut: Nutricionista): Result<Nutricionista>
    suspend fun updateNutricionista(id: Int, nut: Nutricionista): Result<Nutricionista>
    suspend fun deleteNutricionista(id: Int): Result<Unit>
}
