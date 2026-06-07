package com.dietetic.frontend.data.repository

import com.dietetic.frontend.data.remote.api.PacienteApi
import com.dietetic.frontend.data.remote.dto.PacienteDto
import com.dietetic.frontend.domain.model.Paciente
import com.dietetic.frontend.domain.repository.PacienteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PacienteRepositoryImpl @Inject constructor(
    private val api: PacienteApi,
) : PacienteRepository {

    private fun handleError(code: Int): Exception {
        return when (code) {
            400 -> Exception("Datos de paciente inválidos (400)")
            401 -> Exception("Sesión expirada (401)")
            403 -> Exception("No tiene permisos (403)")
            404 -> Exception("Paciente no encontrado (404)")
            500 -> Exception("Error del servidor médico (500)")
            else -> Exception("Error de conexión ($code)")
        }
    }

    override suspend fun getPacientes(): Result<List<Paciente>> = runCatching {
        val resp = api.list()
        if (!resp.isSuccessful) throw handleError(resp.code())
        resp.body()!!.results.map { it.toDomain() }
    }

    override suspend fun getPaciente(id: Int): Result<Paciente> = runCatching {
        val resp = api.retrieve(id)
        if (!resp.isSuccessful) throw handleError(resp.code())
        resp.body()!!.toDomain()
    }

    override suspend fun createPaciente(paciente: Paciente): Result<Paciente> = runCatching {
        val resp = api.create(paciente.toDto())
        if (!resp.isSuccessful) throw handleError(resp.code())
        resp.body()!!.toDomain()
    }

    override suspend fun updatePaciente(id: Int, paciente: Paciente): Result<Paciente> = runCatching {
        val resp = api.update(id, paciente.toDto())
        if (!resp.isSuccessful) throw handleError(resp.code())
        resp.body()!!.toDomain()
    }

    override suspend fun deletePaciente(id: Int): Result<Unit> = runCatching {
        val resp = api.delete(id)
        if (!resp.isSuccessful) throw handleError(resp.code())
    }

    private fun PacienteDto.toDomain() = Paciente(
        id = id,
        userId = userId,
        patientCode = patientCode ?: "",
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        fullName = fullName ?: "",
        age = age ?: 0,
        goal = goal ?: "",
        dietaryRestrictions = dietaryRestrictions ?: "",
        currentWeight = currentWeight ?: 0.0,
        heightCm = heightCm ?: 0.0,
        status = status ?: "",
    )

    private fun Paciente.toDto() = PacienteDto(
        id = id,
        userId = userId,
        patientCode = patientCode,
        firstName = firstName,
        lastName = lastName,
        fullName = fullName,
        age = age,
        goal = goal,
        dietaryRestrictions = dietaryRestrictions,
        currentWeight = currentWeight,
        heightCm = heightCm,
        status = status,
    )
}
