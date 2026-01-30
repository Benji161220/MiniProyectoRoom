package com.example.inventory.ui.home

import GamesRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Game
import com.example.inventory.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val gamesRepository: GamesRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val sortAscending = settingsDataStore.sortAscendingFlow

    val gameUiState: StateFlow<GameUiState> =
        combine(
            gamesRepository.getAllGamesStream(),
            settingsDataStore.sortAscendingFlow
        ) { games, ascending ->
            val sorted = if (ascending) {
                games.sortedBy { it.name }
            } else {
                games.sortedByDescending { it.name }
            }
            GameUiState(sorted)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GameUiState()
        )

    fun toggleSortOrder() {
        viewModelScope.launch {
            settingsDataStore.setSortAscending(!sortAscending.first())
        }
    }
}

data class GameUiState(val itemList: List<Game> = listOf())
