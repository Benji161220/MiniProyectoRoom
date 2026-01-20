/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.GamesRepository
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update a game from the [GamesRepository]'s data source.
 */
class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val gamesRepository: GamesRepository
) : ViewModel() {

    /**
     * Holds current game ui state
     */
    var gameUiState by mutableStateOf(GameUiState())
        private set

    private val gameId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    /**
     * Updates the [gameUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(gameDetails: GameDetails) {
        gameUiState =
            GameUiState(itemDetails = gameDetails, isEntryValid = validateInput(gameDetails))
    }

    /**
     * Updates the current game in the database
     */
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
