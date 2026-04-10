package com.survivalwiki.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_key") // "dark", "light", "system"
        val FONT_SIZE_KEY = intPreferencesKey("font_size_key") // 0 = Small, 1 = Medium, 2 = Large
        val ACCENT_COLOR_KEY = stringPreferencesKey("accent_color_key") // "orange", "olive", "blue"
        val HOME_LIST_TYPE_KEY = stringPreferencesKey("home_list_type_key") // "recent", "bookmarks"
    }

    val themeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "dark"
    }

    val fontSizeFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[FONT_SIZE_KEY] ?: 1
    }

    val accentColorFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ACCENT_COLOR_KEY] ?: "orange"
    }

    val homeListTypeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[HOME_LIST_TYPE_KEY] ?: "recent"
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    suspend fun setFontSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[FONT_SIZE_KEY] = size
        }
    }

    suspend fun setAccentColor(color: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCENT_COLOR_KEY] = color
        }
    }

    suspend fun setHomeListType(type: String) {
        context.dataStore.edit { preferences ->
            preferences[HOME_LIST_TYPE_KEY] = type
        }
    }
}