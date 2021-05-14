package com.pad.shapeless.designer.config


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pad.shapeless.designer.service.MessagingService
import com.pad.shapeless.shared.dto.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.Nullable
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.stereotype.Component
import java.io.Serializable
import java.net.InetAddress

@Component
class ClientStompSessionHandler :
    StompSessionHandlerAdapter() {

    @Autowired
    private lateinit var messagingService: MessagingService


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

    override fun handleFrame(headers: StompHeaders, payload: Any?) {
        val message = (payload as Message<*>).payload
        val className = (message as java.util.LinkedHashMap<*, *>)["className"] as String
        val jom = jacksonObjectMapper()
        val obj = jom.readValue(jom.writeValueAsString(message), Class.forName(className))
        messagingService.handleMessage(obj as DesignerPayload)

    }

    override fun handleTransportError(session: StompSession, exception: Throwable) {
        logger.error(exception.message)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ClientStompSessionHandler::class.java)
    }
}