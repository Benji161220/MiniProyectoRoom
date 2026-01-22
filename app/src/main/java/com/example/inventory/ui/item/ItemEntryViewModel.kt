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

class GameEntryViewModel(private val gamesRepository: GamesRepository) : ViewModel() {

    var gameUiState by mutableStateOf(GameUiState())
        private set

    fun updateUiState(gameDetails: GameDetails) {
        gameUiState =
            GameUiState(itemDetails = gameDetails, isEntryValid = validateInput(gameDetails))
    }

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

data class GameUiState(
    val itemDetails: GameDetails = GameDetails(),
    val isEntryValid: Boolean = false
)

data class GameDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val rating: String = "",
    val quantity: String = ""
)

fun GameDetails.toGame(): Game = Game(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    rating = rating.toFloatOrNull() ?: 0f,
    quantity = quantity.toIntOrNull() ?: 0
)

fun Game.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

fun Game.toGameUiState(isEntryValid: Boolean = false): GameUiState = GameUiState(
    itemDetails = this.toGameDetails(),
    isEntryValid = isEntryValid
)

fun Game.toGameDetails(): GameDetails = GameDetails(
    id = id,
    name = name,
    price = price.toString(),
    rating = rating.toString(),
    quantity = quantity.toString()
)
