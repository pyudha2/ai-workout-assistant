package com.example.aiworkoutlifeassistant.feature.settings.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository{
    val isDarkMode: Flow<Boolean>
    val notificationsEnabled: Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setNotificationEnabled(enabled: Boolean)
}