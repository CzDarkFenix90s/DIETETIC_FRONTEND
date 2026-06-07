package com.dietetic.frontend.data.repository

import com.google.gson.Gson
import com.dietetic.frontend.data.local.TokenDataStore
import com.dietetic.frontend.data.remote.api.AuthApi
import com.dietetic.frontend.data.remote.api.PacienteApi
import com.dietetic.frontend.data.remote.dto.*
import com.dietetic.frontend.domain.model.LoggedUser
import com.dietetic.frontend.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val pacienteApi: PacienteApi,
    private val tokenDataStore: TokenDataStore,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoggedUser> =
        runCatching {
            val response = api.login(LoginRequest(email, password))
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string() ?: ""
                error(parseErrorMessage(errorBody, response.code()))
            }
            val body = response.body() ?: throw Exception("Cuerpo de respuesta vacío")

            // Extraer datos del usuario de forma segura
            val userId = body.userId ?: body.user?.id ?: 0
            val username = body.username ?: body.user?.username ?: ""
            val emailStr = body.email ?: body.user?.email ?: ""
            val roleName = body.role ?: body.user?.role?.name?.lowercase()?.trim() ?: "paciente"

            val isStaff = body.isStaff || body.user?.isStaff == true || roleName in setOf(
                "admin", "staff", "superuser"
            )

            tokenDataStore.saveTokens(body.access ?: "", body.refresh ?: "")
            tokenDataStore.saveUser(userId, username, emailStr, isStaff, roleName)

            // Verificar y crear perfil de paciente si no existe (Proactivo)
            if (!isStaff && roleName == "paciente") {
                ensurePacienteProfileExists(userId, username)
            }

            LoggedUser(userId, username, emailStr, isStaff, roleName)
        }

    private suspend fun ensurePacienteProfileExists(userId: Int, username: String) {
        runCatching {
            val resp = pacienteApi.list()
            if (resp.isSuccessful) {
                // Verificamos por full_name o por los campos nuevos firstName/lastName
                val results = resp.body()?.results ?: emptyList()
                val exists = results.any { 
                    it.userId == userId ||
                    it.fullName.equals(username, ignoreCase = true) ||
                    it.firstName.equals(username, ignoreCase = true)
                }
                
                if (!exists) {
                    val result = pacienteApi.create(
                        PacienteDto(
                            id = 0,
                            userId = userId,
                            patientCode = "PAC-${userId % 10000}-${System.currentTimeMillis() % 1000}",
                            firstName = username,
                            lastName = "N/A",
                            age = 0,
                            goal = "Salud integral",
                            status = "activo",
                            currentWeight = 0.0,
                            heightCm = 0.0
                        )
                    )
                    if (!result.isSuccessful) {
                        android.util.Log.e("DIETETIC_AUTH", "Error creando paciente: ${result.code()} - ${result.errorBody()?.string()}")
                    }
                }
            }
        }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        password2: String,
        firstName: String?,
        lastName: String?
    ): Result<LoggedUser> = runCatching {
        val response = api.register(RegisterRequest(username, email, password, password2, firstName, lastName))
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string() ?: ""
            error(parseErrorMessage(errorBody, response.code()))
        }
        val body = response.body() ?: throw Exception("Cuerpo de respuesta vacío")

        val userId = body.userId ?: body.user?.id ?: 0
        val usernameStr = body.username ?: body.user?.username ?: username
        val emailStr = body.email ?: body.user?.email ?: email
        val roleName = body.role ?: body.user?.role?.name?.lowercase()?.trim() ?: "paciente"
        
        val isStaff = body.isStaff || body.user?.isStaff == true || roleName in setOf(
            "admin", "staff", "superuser"
        )

        tokenDataStore.saveTokens(body.access ?: "", body.refresh ?: "")
        tokenDataStore.saveUser(userId, usernameStr, emailStr, isStaff, roleName)

        // Crear automáticamente el perfil de paciente en el servidor
        if (!isStaff && roleName == "paciente") {
            ensurePacienteProfileExists(userId, usernameStr)
        }

        LoggedUser(userId, usernameStr, emailStr, isStaff, roleName)
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val refresh = tokenDataStore.getRefreshToken()
        if (refresh != null) {
            runCatching { api.logout(LogoutRequest(refresh)) }
        }
        tokenDataStore.clearSession()
    }

    override suspend fun getStoredUser(): TokenDataStore.UserSnapshot? =
        tokenDataStore.userSnapshot.first()

    override suspend fun isLoggedIn(): Boolean =
        !tokenDataStore.getAccessToken().isNullOrBlank()

    private fun parseErrorMessage(body: String, code: Int): String {
        return try {
            val map = Gson().fromJson(body, Map::class.java)
            // Si el error es por campos faltantes, mostrar el primero que aparezca
            map["detail"]?.toString()
                ?: map["non_field_errors"]?.toString()
                ?: map.entries.firstOrNull { it.key != "detail" }?.let { "${it.key}: ${it.value}" }
                ?: "Error $code"
        } catch (e: Exception) {
            "Error $code"
        }
    }
}
