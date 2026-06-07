package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.AlimentoApi
import com.dietetic.frontend.data.remote.dto.UpdateOrderRequest
import com.dietetic.frontend.domain.model.AlimentoProgramado
import com.dietetic.frontend.domain.repository.AlimentoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlimentoRepositoryImpl @Inject constructor(
    private val api: AlimentoApi,
) : AlimentoRepository {

    override suspend fun getAlimentos(): Result<List<AlimentoProgramado>> = runCatching {
        val resp = api.list()
        if (!resp.isSuccessful) throw Exception("Error listando alimentos")
        resp.body()!!.results.map { dto ->
            AlimentoProgramado(
                id = dto.id,
                name = dto.name,
                mealType = dto.mealType,
                portionGrams = dto.portionGrams,
                sequence = dto.sequence,
                isActive = dto.isActive,
                planName = dto.planNutricional?.name
            )
        }
    }

    override suspend fun getAlimento(id: Int): Result<AlimentoProgramado> = runCatching {
        val resp = api.retrieve(id)
        if (!resp.isSuccessful) throw Exception("Error obteniendo alimento")
        val dto = resp.body()!!
        AlimentoProgramado(
            id = dto.id,
            name = dto.name,
            mealType = dto.mealType,
            portionGrams = dto.portionGrams,
            sequence = dto.sequence,
            isActive = dto.isActive,
            planName = dto.planNutricional?.name
        )
    }

    override suspend fun available(): Result<List<AlimentoProgramado>> = runCatching {
        val resp = api.available()
        if (!resp.isSuccessful) throw Exception("Error listando disponibles")
        resp.body()!!.results.map { dto ->
            AlimentoProgramado(
                id = dto.id,
                name = dto.name,
                mealType = dto.mealType,
                portionGrams = dto.portionGrams,
                sequence = dto.sequence,
                isActive = dto.isActive,
                planName = dto.planNutricional?.name
            )
        }
    }

    override suspend fun updateOrder(id: Int, sequence: Int): Result<AlimentoProgramado> = runCatching {
        val resp = api.updateOrder(id, UpdateOrderRequest(sequence))
        if (!resp.isSuccessful) throw Exception("Error actualizando orden")
        val dto = resp.body()!!
        AlimentoProgramado(
            id = dto.id,
            name = dto.name,
            mealType = dto.mealType,
            portionGrams = dto.portionGrams,
            sequence = dto.sequence,
            isActive = dto.isActive,
            planName = dto.planNutricional?.name
        )
    }
}
