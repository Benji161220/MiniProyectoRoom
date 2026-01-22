package com.example.inventory.ui.item

import GamesRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val gamesRepository: GamesRepository
) : ViewModel() {

    var gameUiState by mutableStateOf(GameUiState())
        private set

    private val gameId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    fun updateUiState(gameDetails: GameDetails) {
        gameUiState =
            GameUiState(itemDetails = gameDetails, isEntryValid = validateInput(gameDetails))
    }

    fun saveGame() {
        if (validateInput()) {
            viewModelScope.launch {
                gamesRepository.updateGame(gameUiState.itemDetails.toGame())
            }
        }
    }

    private fun validateInput(uiState: GameDetails = gameUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && rating.isNotBlank()
        }
    }
}
