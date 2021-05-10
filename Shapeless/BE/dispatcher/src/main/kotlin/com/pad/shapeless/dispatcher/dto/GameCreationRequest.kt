package com.pad.shapeless.dispatcher.dto

import com.pad.shapeless.dispatcher.model.GameDifficulty

data class GameCreationRequest(
    val difficulty: GameDifficulty,
    val maxPlayers: Int,
    val name: String
)