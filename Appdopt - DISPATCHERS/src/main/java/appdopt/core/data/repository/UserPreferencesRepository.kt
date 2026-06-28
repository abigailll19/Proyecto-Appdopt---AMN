package com.example.appdopt.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.appdopt.core.model.AppLanguage
import com.example.appdopt.core.model.AppSettings
import com.example.appdopt.core.model.ColorBlindMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_HIGH_CONTRAST = booleanPreferencesKey("is_high_contrast")
        val COLOR_BLIND_MODE = stringPreferencesKey("color_blind_mode")
        val FONT_SIZE_MULTIPLIER = floatPreferencesKey("font_size_multiplier")
        val LANGUAGE = stringPreferencesKey("language")
    }

    val userPreferencesFlow: Flow<AppSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            AppSettings(
                isDarkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false,
                isHighContrast = preferences[PreferencesKeys.IS_HIGH_CONTRAST] ?: false,
                colorBlindMode = ColorBlindMode.valueOf(
                    preferences[PreferencesKeys.COLOR_BLIND_MODE] ?: ColorBlindMode.NONE.name
                ),
                fontSizeMultiplier = preferences[PreferencesKeys.FONT_SIZE_MULTIPLIER] ?: 1.0f,
                language = AppLanguage.valueOf(
                    preferences[PreferencesKeys.LANGUAGE] ?: AppLanguage.SPANISH.name
                )
            )
        }

    suspend fun updateDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }

    suspend fun updateHighContrast(isHighContrast: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_HIGH_CONTRAST] = isHighContrast
        }
    }

    suspend fun updateColorBlindMode(mode: ColorBlindMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COLOR_BLIND_MODE] = mode.name
        }
    }

    suspend fun updateFontSize(multiplier: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE_MULTIPLIER] = multiplier
        }
    }

    suspend fun updateLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language.name
        }
    }
}
