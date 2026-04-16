package com.survivalwiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.survivalwiki.data.AppDatabase
import com.survivalwiki.data.ArticleRepository
import com.survivalwiki.data.DatabaseInitializer
import com.survivalwiki.data.SettingsManager
import com.survivalwiki.data.dataStore
import com.survivalwiki.ui.SurvivalViewModel
import com.survivalwiki.ui.SurvivalViewModelFactory
import com.survivalwiki.ui.screens.MainScreen
import com.survivalwiki.ui.theme.SurvivalWikiTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val accent = readSavedAccent()
        setTheme(splashThemeForAccent(accent))
        installSplashScreen()
        super.onCreate(savedInstanceState)

        LauncherIconController.syncFromAccent(this, accent)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ArticleRepository(database.articleDao())
        val settingsManager = SettingsManager(applicationContext)
        val factory = SurvivalViewModelFactory(repository, settingsManager, application)

        setContent {
            val viewModel: SurvivalViewModel = viewModel(factory = factory)
            val themeStr by viewModel.themeFlow.collectAsState(initial = "dark")
            val accentColor by viewModel.accentColorFlow.collectAsState(initial = "orange")
            
            val darkTheme = when (themeStr) {
                "light" -> false
                "system" -> isSystemInDarkTheme()
                else -> true
            }

            SurvivalWikiTheme(darkTheme = darkTheme, accentColorName = accentColor) {
                // Initialize Database from JSON on first launch
                LaunchedEffect(Unit) {
                    val db = AppDatabase.getDatabase(applicationContext)
                    DatabaseInitializer.populateDatabase(applicationContext, db)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }

    private fun readSavedAccent(): String = runBlocking(Dispatchers.IO) {
        applicationContext.dataStore.data.map { prefs ->
            prefs[SettingsManager.ACCENT_COLOR_KEY] ?: "orange"
        }.first()
    }

    private fun splashThemeForAccent(accent: String): Int = when (accent.lowercase()) {
        "olive" -> R.style.Theme_SurvivalWiki_Starting_Olive
        "blue" -> R.style.Theme_SurvivalWiki_Starting_Blue
        else -> R.style.Theme_SurvivalWiki_Starting_Orange
    }
}