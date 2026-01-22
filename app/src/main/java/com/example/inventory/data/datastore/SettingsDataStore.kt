package com.example.inventory.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")
object SettingsKeys {
    val SORT_ASCENDING = booleanPreferencesKey("sort_ascending")
}

class SettingsDataStore(private val context: Context) {

    val sortAscendingFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[SettingsKeys.SORT_ASCENDING] ?: true
        }

    suspend fun setSortAscending(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.SORT_ASCENDING] = value
        }
    }
}
