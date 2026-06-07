package com.dietetic.frontend.di

import com.dietetic.frontend.BuildConfig
import com.dietetic.frontend.data.remote.api.*
import com.dietetic.frontend.data.remote.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun providePacienteApi(retrofit: Retrofit): PacienteApi = retrofit.create(PacienteApi::class.java)

    @Provides
    @Singleton
    fun provideCourseApi(retrofit: Retrofit): CourseApi = retrofit.create(CourseApi::class.java)

    @Provides
    @Singleton
    fun provideModuleApi(retrofit: Retrofit): ModuleApi = retrofit.create(ModuleApi::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi = retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi = retrofit.create(OrderApi::class.java)

    @Provides
    @Singleton
    fun provideNutricionistaApi(retrofit: Retrofit): NutricionistaApi = retrofit.create(NutricionistaApi::class.java)

    @Provides
    @Singleton
    fun provideAlimentoApi(retrofit: Retrofit): AlimentoApi = retrofit.create(AlimentoApi::class.java)

    @Provides
    @Singleton
    fun provideConsultaApi(retrofit: Retrofit): ConsultaApi = retrofit.create(ConsultaApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)
}
