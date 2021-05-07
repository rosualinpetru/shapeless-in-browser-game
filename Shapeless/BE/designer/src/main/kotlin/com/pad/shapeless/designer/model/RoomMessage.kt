package com.pad.shapeless.designer.model

import java.util.*

enum class MessageType {
    JOIN, LEAVE
}

data class RoomMessage(
    val type: MessageType,
    val sender: UUID
)



