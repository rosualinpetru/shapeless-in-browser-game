package com.pad.shapeless.designer.config


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pad.shapeless.shared.dto.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.Nullable
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.stereotype.Component
import java.net.InetAddress

@Component
class ClientStompSessionHandler :
    StompSessionHandlerAdapter() {

    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate

    override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
        session.subscribe("/topic/designer/all", this)
        session.subscribe("/topic/designer/${InetAddress.getLocalHost().hostAddress}", this)
        session.send("/app/dispatcher/enroll", Message<Nothing>())
    }

    override fun handleException(
        session: StompSession,
        @Nullable command: StompCommand?,
        headers: StompHeaders,
        payload: ByteArray,
        exception: Throwable
    ) {
        logger.error(exception.message)
    }

    override fun getPayloadType(headers: StompHeaders): Class<Message<*>> {
        return Message::class.java
    }

    override fun handleFrame(headers: StompHeaders, message: Any?) {
        val jom = jacksonObjectMapper()
        val payload = (message as Message<*>).payload
        val messageTypeRaw = (payload as java.util.LinkedHashMap<*, *>)["type"] as String
        when (MessageType.valueOf(messageTypeRaw)) {
            MessageType.JOINED_ACK -> {
                logger.debug("Received JOINED_ACK!")
                val joinedAck = jom.readValue(jom.writeValueAsString(payload), JoinedAck::class.java)
                messagingTemplate.convertAndSend(
                    "/topic/${joinedAck.game}",
                    UpdateLobby
                )
            }
            MessageType.LEFT_ACK -> {
                logger.debug("Received LEFT_ACK!")
                val leftAck = jom.readValue(jom.writeValueAsString(payload), LeftAck::class.java)
                messagingTemplate.convertAndSend(
                    "/topic/${leftAck.game}",
                    UpdateLobby
                )
            }
            MessageType.JOINED_ERR -> {
                logger.debug("Received JOINED_ERR!")
                val joinedErr = jom.readValue(jom.writeValueAsString(payload), JoinedErr::class.java)
                messagingTemplate.convertAndSend(
                    "/topic/${joinedErr.game}",
                    GameError(joinedErr.errorMessage)
                )
            }
            MessageType.LEFT_ERR -> {
                logger.debug("Received LEFT_ERR!")
                val leftErr = jom.readValue(jom.writeValueAsString(payload), LeftErr::class.java)
                messagingTemplate.convertAndSend(
                    "/topic/${leftErr.game}",
                    GameError(leftErr.errorMessage)
                )
            }
            else -> println(payload)
        }
    }

    override fun handleTransportError(session: StompSession, exception: Throwable) {
        logger.error(exception.message)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ClientStompSessionHandler::class.java)
    }
}