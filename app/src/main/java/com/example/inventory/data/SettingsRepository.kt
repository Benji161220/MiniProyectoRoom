package com.example.inventory.data

import com.example.inventory.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val dataStore: SettingsDataStore) {

    val sortAscending: Flow<Boolean> = dataStore.sortAscendingFlow

    suspend fun toggleSortOrder(current: Boolean) {
        dataStore.setSortAscending(!current)
    }
}
