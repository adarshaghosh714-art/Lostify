package com.example.lostify.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "lostify_prefs")

class PreferenceManager(private val context: Context) {

    companion object {
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }

    val isFirstLaunch: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[FIRST_LAUNCH] ?: true
        }

    suspend fun setFirstLaunchCompleted() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH] = false
        }
    }
}