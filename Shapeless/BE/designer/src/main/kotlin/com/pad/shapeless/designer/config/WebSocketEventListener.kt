package com.pad.shapeless.designer.config

import com.pad.shapeless.designer.model.MessageType
import com.pad.shapeless.designer.model.RoomMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.*


@Component
class WebSocketEventListener @Autowired constructor(private val messagingTemplate: SimpMessageSendingOperations) {
    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {

    }


    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val userId = headerAccessor.sessionAttributes?.get("user") as UUID?
        val roomId = headerAccessor.sessionAttributes?.get("room_id") as UUID?
        if(userId != null && roomId != null) {
            val roomMessage = RoomMessage(MessageType.LEAVE, userId)
            messagingTemplate.convertAndSend("/topic/$roomId", roomMessage)
        }
    }
}