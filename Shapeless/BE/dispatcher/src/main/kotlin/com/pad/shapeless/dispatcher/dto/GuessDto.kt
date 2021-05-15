package com.pad.shapeless.dispatcher.dto

import java.util.*

data class GuessDto (
    val guesserId: UUID,
    val guessedId: UUID,
    val gameId: UUID,
    val color: String,
    val shape: String
)
