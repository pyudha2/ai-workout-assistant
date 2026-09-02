package com.example.aiworkoutlifeassistant.core.di

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
}