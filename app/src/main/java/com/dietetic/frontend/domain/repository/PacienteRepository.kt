package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.Paciente

interface PacienteRepository {
    suspend fun getPacientes(): Result<List<Paciente>>
    suspend fun getPaciente(id: Int): Result<Paciente>
    suspend fun createPaciente(paciente: Paciente): Result<Paciente>
    suspend fun updatePaciente(id: Int, paciente: Paciente): Result<Paciente>
    suspend fun deletePaciente(id: Int): Result<Unit>
}
