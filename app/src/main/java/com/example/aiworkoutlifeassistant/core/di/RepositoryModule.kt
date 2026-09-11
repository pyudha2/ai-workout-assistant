package com.example.aiworkoutlifeassistant.core.di

import com.example.aiworkoutlifeassistant.feature.settings.data.SettingsRepositoryImpl
import com.example.aiworkoutlifeassistant.feature.settings.domain.repository.SettingsRepository
import com.example.aiworkoutlifeassistant.feature.workout.data.repository.WorkoutRepositoryImpl
import com.example.aiworkoutlifeassistant.feature.workout.domain.repository.WorkoutRepository
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
    abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}