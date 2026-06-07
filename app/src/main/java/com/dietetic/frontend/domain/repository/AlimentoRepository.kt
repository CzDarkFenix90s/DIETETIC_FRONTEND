package com.dietetic.frontend.domain.repository

import com.dietetic.frontend.domain.model.AlimentoProgramado

interface AlimentoRepository {
    suspend fun getAlimentos(): Result<List<AlimentoProgramado>>
    suspend fun getAlimento(id: Int): Result<AlimentoProgramado>
    suspend fun available(): Result<List<AlimentoProgramado>>
    suspend fun updateOrder(id: Int, sequence: Int): Result<AlimentoProgramado>
}
