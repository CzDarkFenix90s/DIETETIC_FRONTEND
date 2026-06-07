package com.dietetic.frontend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NestedPlanDto(
    val id: Int,
    val name: String? = null
)

data class ConsultaDto(
    val id: Int = 0,
    val status: String? = null,
    @SerializedName("session_notes") val sessionNotes: String? = null,
    @SerializedName("scheduled_time") val scheduledTime: String? = null,
    @SerializedName("estimated_end") val estimatedEnd: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    
    // Usamos objetos para los campos que el servidor envía como BEGIN_OBJECT
    @SerializedName("plan_nutricional") val plan: Any? = null,
    @SerializedName("nutricionista") val nutricionista: Any? = null,
    @SerializedName("paciente") val paciente: Any? = null,

    @SerializedName("paciente_nombre") val pacienteNombre: String? = null,
)

data class ConsultaCreateRequest(
    val paciente: Int,
    val nutricionista: Int,
    @SerializedName("plan_nutricional") val plan_nutricional: Int,
    @SerializedName("scheduled_time") val scheduled_time: String,
    @SerializedName("estimated_end") val estimated_end: String,
    val status: String = "programada",
    @SerializedName("session_notes") val session_notes: String = ""
)

data class AddNoteRequest(
    val notes: String
)

data class UpdateStatusRequest(
    val status: String
)
