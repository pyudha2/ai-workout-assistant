package com.example.aiworkoutlifeassistant.core.di

import com.example.aiworkoutlifeassistant.feature.settings.data.SettingsRepositoryImpl
import com.example.aiworkoutlifeassistant.feature.settings.domain.repository.SettingsRepository
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
    abstract fun bindWorkoutRepository(impl: SettingsRepositoryImpl): SettingsRepository
}