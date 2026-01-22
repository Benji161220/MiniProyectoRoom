package com.example.inventory.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: SettingsDataStore
) : ViewModel() {

    val sortAscending: StateFlow<Boolean> =
        dataStore.sortAscendingFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            true
        )

    fun toggleSort() {
        viewModelScope.launch {
            dataStore.setSortAscending(!sortAscending.value)
        }
    }
}
