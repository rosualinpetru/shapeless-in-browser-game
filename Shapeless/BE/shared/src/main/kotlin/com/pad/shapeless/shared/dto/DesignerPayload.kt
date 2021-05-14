package com.pad.shapeless.shared.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap


sealed class DesignerPayload (@JsonProperty("className") val className: String) : Serializable


sealed class Success(className: String) : DesignerPayload(className)
sealed class Failure(@JsonProperty("message") val message: String, className: String) : DesignerPayload(className)

sealed class PlayerGameAction(
    @JsonProperty("game")
    val game: UUID,
    @JsonProperty("player")
    val player: UUID,
    className: String
) : Success(className)

sealed class PlayerGameActionError(
    @JsonProperty("game")
    val game: UUID,
    @JsonProperty("player")
    val player: UUID,
    message: String,
    className: String
) : Failure(message,className)

class Joined(player: UUID, game: UUID) : PlayerGameAction(game, player, Joined::class.java.name)

class Left(player: UUID, game: UUID) : PlayerGameAction(game, player, Left::class.java.name)

class Start(player: UUID, game: UUID) : PlayerGameAction(game, player, Start::class.java.name)

class JoinedErr(player: UUID, game: UUID, message: String) : PlayerGameActionError(game, player, message, JoinedErr::class.java.name)

class LeftErr(player: UUID, game: UUID, message: String) : PlayerGameActionError(game, player, message, LeftErr::class.java.name)

class StartErr(player: UUID, game: UUID, message: String) : PlayerGameActionError(game, player, message, StartErr::class.java.name)


enum class FrontendAction {
    UPDATE_LOBBY {
        override fun feJSON() = mapOf("type" to UPDATE_LOBBY)
    },
    START {
        override fun feJSON() = mapOf("type" to START)
    },
    GAME_ERROR {
        override fun feJSON() = mapOf("type" to GAME_ERROR)
    };

    abstract fun feJSON() : Map<String, FrontendAction>

}
