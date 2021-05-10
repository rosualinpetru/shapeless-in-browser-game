package com.pad.shapeless.designer.config

import com.pad.shapeless.shared.dto.Left
import com.pad.shapeless.shared.dto.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.*


@Component
class WebSocketEventListener @Autowired constructor(
    private val session: StompSession
) {
    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {

    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {

        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val userId = headerAccessor.sessionAttributes?.get("user") as UUID?
        val gameId = headerAccessor.sessionAttributes?.get("game_id") as UUID?
        if (userId != null && gameId != null) {
            session.send(
                "/app/dispatcher/left",
                Message(Left(userId, gameId))
            )
        }
    }
}