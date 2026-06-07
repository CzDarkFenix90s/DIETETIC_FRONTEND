package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.ConsultaDietetica
import com.dietetic.frontend.data.remote.dto.ConsultaCreateRequest

interface ConsultaRepository {
    suspend fun getConsultas(): Result<List<ConsultaDietetica>>
    suspend fun getMyConsultas(): Result<List<ConsultaDietetica>>
    suspend fun getConsulta(id: Int): Result<ConsultaDietetica>
    suspend fun createConsulta(request: ConsultaCreateRequest): Result<ConsultaDietetica>
    suspend fun addSessionNote(id: Int, notes: String): Result<ConsultaDietetica>
    suspend fun startConsultation(id: Int): Result<ConsultaDietetica>
    suspend fun updateStatus(id: Int, status: String): Result<ConsultaDietetica>
}
