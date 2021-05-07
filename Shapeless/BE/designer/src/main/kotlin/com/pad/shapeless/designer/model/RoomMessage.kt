package com.pad.shapeless.designer.model

import java.util.*

data class RoomMessage(
        val type: MessageType,
        val senderId: UUID,
        val senderData: SenderData? = null,
        val content: String? = null,
)



