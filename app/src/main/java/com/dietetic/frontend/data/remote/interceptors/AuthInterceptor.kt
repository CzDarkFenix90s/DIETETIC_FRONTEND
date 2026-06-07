package com.dietetic.frontend.data.remote.interceptors

import com.dietetic.frontend.data.local.TokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        // 1. Skip paths that don't need auth
        val noAuthPaths = listOf("/auth/login", "/auth/register", "/health/")
        if (noAuthPaths.any { path.contains(it) }) {
            return chain.proceed(request)
        }

        // 2. Get the token synchronously
        val token = runBlocking { tokenDataStore.getAccessToken() }
        
        // 3. Prepare the request
        val requestBuilder = request.newBuilder()
        if (!token.isNullOrBlank()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        // 4. Handle Unauthorized (401)
        if (response.code == 401) {
            android.util.Log.e("DIETETIC_AUTH", "HTTP 401 en: $path. Token presente: ${!token.isNullOrBlank()}")
            // Si el servidor nos dice que no estamos autorizados, limpiamos la sesión local
            // Esto provocará que el AuthViewModel detecte que no hay usuario y redirija a Login
            runBlocking { tokenDataStore.clearSession() }
        }

        return response
    }
}
