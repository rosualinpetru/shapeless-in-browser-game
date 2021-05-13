package com.pad.shapeless.dispatcher.dto

import com.pad.shapeless.dispatcher.model.GameDifficulty
import java.util.*

data class GameDto(
    val id: UUID,
    val name: String,
    val difficulty: GameDifficulty,
    val maxPlayers: Int,
    val designer: String
)