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

import GamesRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Game
import kotlinx.coroutines.launch
import java.text.NumberFormat

/**
 * ViewModel to validate and insert games in the Room database.
 */
class GameEntryViewModel(private val gamesRepository: GamesRepository) : ViewModel() {

    /**
     * Holds current game ui state
     */
    var gameUiState by mutableStateOf(GameUiState())
        private set

    /**
     * Updates the [gameUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(gameDetails: GameDetails) {
        gameUiState =
            GameUiState(itemDetails = gameDetails, isEntryValid = validateInput(gameDetails))
    }

    /**
     * Saves the current game to the database
     */
    fun saveGame() {
        if (validateInput()) {
            viewModelScope.launch {
                gamesRepository.insertGame(gameUiState.itemDetails.toGame())
            }
        }
    }

    private fun validateInput(uiState: GameDetails = gameUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && rating.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for a Game.
 */
data class GameUiState(
    val itemDetails: GameDetails = GameDetails(),
    val isEntryValid: Boolean = false
)

data class GameDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val rating: String = "",
)

/**
 * Extension function to convert [GameDetails] to [Game]. If the value of [GameDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [GameDetails.rating] is not a valid [Float], then the rating will be set to 0f
 */
fun GameDetails.toGame(): Game = Game(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    rating = rating.toFloatOrNull() ?: 0f
)

fun Game.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

/**
 * Extension function to convert [Game] to [GameUiState]
 */
fun Game.toGameUiState(isEntryValid: Boolean = false): GameUiState = GameUiState(
    itemDetails = this.toGameDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Game] to [GameDetails]
 */
fun Game.toGameDetails(): GameDetails = GameDetails(
    id = id,
    name = name,
    price = price.toString(),
    rating = rating.toString()
)
