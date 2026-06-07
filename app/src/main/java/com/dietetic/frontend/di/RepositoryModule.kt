package com.dietetic.frontend.di

import com.dietetic.frontend.data.repository.*
import com.dietetic.frontend.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindModuleRepository(impl: ModuleRepositoryImpl): ModuleRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindPacienteRepository(impl: PacienteRepositoryImpl): PacienteRepository

    @Binds
    @Singleton
    abstract fun bindNutricionistaRepository(impl: NutricionistaRepositoryImpl): NutricionistaRepository

    @Binds
    @Singleton
    abstract fun bindAlimentoRepository(impl: AlimentoRepositoryImpl): AlimentoRepository

    @Binds
    @Singleton
    abstract fun bindConsultaRepository(impl: ConsultaRepositoryImpl): ConsultaRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
