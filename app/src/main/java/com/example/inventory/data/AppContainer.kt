package com.example.inventory.data

import GamesRepository
import OfflineGamesRepository
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.example.inventory.data.datastore.SettingsDataStore
import androidx.datastore.preferences.core.Preferences
interface AppContainer {
    val gamesRepository: GamesRepository
    val settingsDataStore: SettingsDataStore
}
private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_PREFERENCES_NAME
)
class AppDataContainer(private val context: Context) : AppContainer {
    override val gamesRepository: GamesRepository by lazy {
        OfflineGamesRepository(
            InventoryDatabase.getDatabase(context).gameDao()
        )
    }
    override val settingsDataStore: SettingsDataStore by lazy {
        SettingsDataStore(context.dataStore)
    }
}
