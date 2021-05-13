package com.pad.shapeless.shared.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*

enum class MessageType {
    JOINED,
    JOINED_ACK,
    JOINED_ERR,
    LEFT,
    LEFT_ACK,
    LEFT_ERR,
    UPDATE_LOBBY,
    GAME_ERROR
}

sealed class DesignerPayload(
    @JsonProperty("type")
    val type: MessageType
) : Serializable

data class Joined(
    @JsonProperty("player")
    val player: UUID,
    @JsonProperty("game")
    val game: UUID
) :
    DesignerPayload(MessageType.JOINED)

data class Left(
    @JsonProperty("player")
    val player: UUID,
    @JsonProperty("game")
    val game: UUID
) :
    DesignerPayload(MessageType.LEFT)

data class JoinedAck(
    @JsonProperty("player")
    val player: UUID,
    @JsonProperty("game")
    val game: UUID
) :
    DesignerPayload(MessageType.JOINED_ACK)

data class LeftAck(
    @JsonProperty("player")
    val player: UUID,
    @JsonProperty("game")
    val game: UUID
) :
    DesignerPayload(MessageType.LEFT_ACK)

data class JoinedErr(
    @JsonProperty("player")
    val player: UUID,
    @JsonProperty("game")
    val game: UUID,
    val errorMessage: String
) :
    DesignerPayload(MessageType.JOINED_ERR)

data class LeftErr(
    @JsonProperty("player")
    val player: UUID,
    @JsonProperty("game")
    val game: UUID,
    val errorMessage: String
) :
    DesignerPayload(MessageType.LEFT_ERR)

object UpdateLobby : DesignerPayload(MessageType.UPDATE_LOBBY)

data class GameError(val id: UUID, val message: String) : DesignerPayload(MessageType.GAME_ERROR)
