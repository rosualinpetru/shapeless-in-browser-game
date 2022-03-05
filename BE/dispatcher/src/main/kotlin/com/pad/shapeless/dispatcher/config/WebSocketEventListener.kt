package com.pad.shapeless.dispatcher.config


import com.pad.shapeless.dispatcher.service.DesignerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent


@Component
class WebSocketEventListener @Autowired constructor(private val designerService: DesignerService) {
    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        logger.info("Received websocket connection!")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val hostAddress = headerAccessor.sessionAttributes?.get("host_address") as String
        designerService.dismissDesigner(hostAddress)
        logger.info("The designer with ip $hostAddress disconnected! Remaining designers: ${designerService.count()}")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)
    }
}