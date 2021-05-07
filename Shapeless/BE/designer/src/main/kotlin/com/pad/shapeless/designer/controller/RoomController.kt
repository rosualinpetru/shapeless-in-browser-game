package com.pad.shapeless.designer.controller

import com.pad.shapeless.designer.model.MessageType
import com.pad.shapeless.designer.model.RoomMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class RoomController @Autowired constructor(private val messagingTemplate: SimpMessagingTemplate) {
    @MessageMapping("/room/{roomId}/sendMessage")
    fun sendMessage(
        @DestinationVariable roomId: UUID, @Payload roomMessage: RoomMessage
    ) {
        messagingTemplate.convertAndSend("/topic/$roomId", roomMessage)
    }

    @MessageMapping("/room/{roomId}/addUser")
    fun addUser(
        @DestinationVariable roomId: UUID,
        @Payload roomMessage: RoomMessage,
        headerAccessor: SimpMessageHeaderAccessor
    ) {
        headerAccessor.sessionAttributes!!["room_id"] = roomId
        val leaveMessage = RoomMessage(MessageType.LEAVE, roomMessage.sender)
        messagingTemplate.convertAndSend("/topic/$roomId", leaveMessage)

        headerAccessor.sessionAttributes!!["user"] = roomMessage.sender
        messagingTemplate.convertAndSend("/topic/$roomId", roomMessage)

    }
}