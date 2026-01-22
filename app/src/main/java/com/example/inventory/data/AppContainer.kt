package com.example.inventory.data

import GamesRepository
import OfflineGamesRepository
import android.content.Context

interface AppContainer {
    val gamesRepository: GamesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val gamesRepository: GamesRepository by lazy {
        OfflineGamesRepository(InventoryDatabase.getDatabase(context).gameDao())
    }
}
