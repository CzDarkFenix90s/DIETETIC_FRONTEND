package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.NutricionistaApi
import com.dietetic.frontend.data.remote.dto.NutricionistaDto
import com.dietetic.frontend.domain.model.Nutricionista
import com.dietetic.frontend.domain.repository.NutricionistaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NutricionistaRepositoryImpl @Inject constructor(
    private val api: NutricionistaApi,
) : NutricionistaRepository {

    override suspend fun getNutricionistas(): Result<List<Nutricionista>> = runCatching {
        val resp = api.list()
        if (!resp.isSuccessful) throw Exception("Error listando nutricionistas")
        resp.body()!!.results.map { it.toDomain() }
    }

    override suspend fun getNutricionista(id: Int): Result<Nutricionista> = runCatching {
        val resp = api.retrieve(id)
        if (!resp.isSuccessful) throw Exception("Error al obtener nutricionista")
        resp.body()!!.toDomain()
    }

    override suspend fun createNutricionista(nut: Nutricionista): Result<Nutricionista> = runCatching {
        val resp = api.create(nut.toDto())
        if (!resp.isSuccessful) throw Exception("Error al crear nutricionista")
        resp.body()!!.toDomain()
    }

    override suspend fun updateNutricionista(id: Int, nut: Nutricionista): Result<Nutricionista> = runCatching {
        val resp = api.update(id, nut.toDto())
        if (!resp.isSuccessful) throw Exception("Error al actualizar nutricionista")
        resp.body()!!.toDomain()
    }

    override suspend fun deleteNutricionista(id: Int): Result<Unit> = runCatching {
        val resp = api.delete(id)
        if (!resp.isSuccessful) throw Exception("Error al eliminar nutricionista")
    }

    private fun NutricionistaDto.toDomain() = Nutricionista(
        id = id,
        firstName = firstName,
        lastName = lastName,
        professionalId = professionalId,
        specialty = specialty,
        consultationFee = consultationFee,
    )

    private fun Nutricionista.toDto() = NutricionistaDto(
        id = id,
        firstName = firstName,
        lastName = lastName,
        professionalId = professionalId,
        specialty = specialty,
        consultationFee = consultationFee,
        username = username,
        email = email,
        password = password
    )
}
