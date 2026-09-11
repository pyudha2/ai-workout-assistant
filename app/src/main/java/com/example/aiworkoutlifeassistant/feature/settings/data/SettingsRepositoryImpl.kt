package com.example.aiworkoutlifeassistant.feature.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.room.Database
import com.example.aiworkoutlifeassistant.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
private val NOTIFICATIONS_KEY = booleanPreferencesKey("notification_enabled")

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): SettingsRepository{
    override val isDarkMode: Flow<Boolean> = dataStore.data.map { it[DARK_MODE_KEY] ?: false }
    override val notificationsEnabled: Flow<Boolean> = dataStore.data.map { it[NOTIFICATIONS_KEY] ?: true }
    override suspend fun setDarkMode(enabled: Boolean){
        dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }

    override suspend fun setNotificationEnabled(enabled: Boolean){
        dataStore.edit { it[NOTIFICATIONS_KEY] = enabled }
    }
}