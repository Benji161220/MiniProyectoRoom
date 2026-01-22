package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val rating: Float,
    val price: Double,
    val quantity: Int
)
