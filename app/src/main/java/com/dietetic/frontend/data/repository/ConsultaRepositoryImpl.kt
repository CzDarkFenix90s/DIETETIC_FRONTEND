package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.ConsultaApi
import com.dietetic.frontend.data.remote.dto.*
import com.dietetic.frontend.domain.model.ConsultaDietetica
import com.dietetic.frontend.domain.repository.ConsultaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsultaRepositoryImpl @Inject constructor(
    private val api: ConsultaApi,
) : ConsultaRepository {

    override suspend fun getConsultas(): Result<List<ConsultaDietetica>> = runCatching {
        val resp = api.list()
        if (!resp.isSuccessful) throw Exception("Error del servidor: ${resp.code()}")
        val body = resp.body() ?: throw Exception("Respuesta vacía")
        body.results.map { it.toDomain() }
    }

    override suspend fun getMyConsultas(): Result<List<ConsultaDietetica>> = runCatching {
        val resp = api.listMine()
        if (!resp.isSuccessful) throw Exception("No pudimos cargar tus citas (Error ${resp.code()})")
        val body = resp.body() ?: throw Exception("Sin datos")
        body.results.map { it.toDomain() }
    }

    override suspend fun getConsulta(id: Int): Result<ConsultaDietetica> = runCatching {
        val resp = api.retrieve(id)
        if (!resp.isSuccessful) throw Exception("Error al obtener detalles")
        val dto = resp.body() ?: throw Exception("Cita no encontrada")
        dto.toDomain()
    }

    override suspend fun createConsulta(request: ConsultaCreateRequest): Result<ConsultaDietetica> = runCatching {
        val resp = api.create(request)
        if (!resp.isSuccessful) {
            val errorBody = resp.errorBody()?.string() ?: ""
            throw Exception("No se pudo agendar: $errorBody")
        }
        val dto = resp.body() ?: throw Exception("Error al procesar la cita")
        dto.toDomain()
    }

    override suspend fun addSessionNote(id: Int, notes: String): Result<ConsultaDietetica> = runCatching {
        val resp = api.addSessionNote(id, AddNoteRequest(notes))
        if (!resp.isSuccessful) throw Exception("Error al guardar nota")
        resp.body()!!.toDomain()
    }

    override suspend fun startConsultation(id: Int): Result<ConsultaDietetica> = runCatching {
        val resp = api.startConsultation(id)
        if (!resp.isSuccessful) throw Exception("Error al iniciar consulta")
        resp.body()!!.toDomain()
    }

    override suspend fun updateStatus(id: Int, status: String): Result<ConsultaDietetica> = runCatching {
        val resp = api.updateStatus(id, UpdateStatusRequest(status))
        if (!resp.isSuccessful) throw Exception("Error al actualizar estado")
        resp.body()!!.toDomain()
    }

    private fun ConsultaDto.toDomain(): ConsultaDietetica {
        // Intentar extraer el nombre del objeto 'paciente' si 'pacienteNombre' es nulo
        val extractedName = if (pacienteNombre.isNullOrBlank()) {
            try {
                val map = paciente as? Map<*, *>
                val firstName = map?.get("first_name") as? String
                val lastName = map?.get("last_name") as? String
                if (!firstName.isNullOrBlank()) {
                    "$firstName ${lastName ?: ""}".trim()
                } else {
                    map?.get("full_name") as? String
                }
            } catch (e: Exception) {
                null
            }
        } else {
            pacienteNombre
        }

        return ConsultaDietetica(
            id = id,
            status = status ?: "pendiente",
            sessionNotes = sessionNotes ?: "",
            scheduledTime = scheduledTime ?: "",
            estimatedEnd = estimatedEnd ?: "",
            pacienteNombre = extractedName ?: "USUARIO DIETETIC",
            planId = try {
                val pMap = plan as? Map<*, *>
                (pMap?.get("id") as? Number)?.toInt()
            } catch (e: Exception) {
                null
            }
        )
    }
}
