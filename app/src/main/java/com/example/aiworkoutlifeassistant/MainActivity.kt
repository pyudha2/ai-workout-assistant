package com.example.aiworkoutlifeassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiworkoutlifeassistant.feature.settings.presentation.SettingsViewModel
import com.example.aiworkoutlifeassistant.navigation.NavGraph
import com.example.aiworkoutlifeassistant.ui.theme.AIWorkoutLifeAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

            AIWorkoutLifeAssistantTheme(darkTheme = isDarkMode) {
                NavGraph()
            }
        }
    }
}