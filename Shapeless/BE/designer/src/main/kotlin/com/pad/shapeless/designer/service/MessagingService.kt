package com.pad.shapeless.designer.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pad.shapeless.shared.dto.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class MessagingService @Autowired constructor(private val messagingTemplate: SimpMessagingTemplate) {

    fun handleMessage(payload: DesignerPayload) {
        logger.debug("Received ${payload.javaClass.name}")
        when (payload) {
            is Failure -> when (payload) {
                is PlayerGameActionError ->
                    messagingTemplate.convertAndSend(
                        "/topic/${payload.game}",
                        FrontendAction.GAME_ERROR.feJSON()
                            .plus(mapOf("id" to payload.player, "message" to payload.message))
                    )

                else -> logger.error("This is impossible to happen!")
            }

            is Success -> when (payload) {
                is PlayerGameAction -> when (payload) {
                    is Joined, is Left ->
                        messagingTemplate.convertAndSend(
                            "/topic/${payload.game}",
                            FrontendAction.UPDATE_LOBBY.feJSON()
                        )


                    is Start ->
                        messagingTemplate.convertAndSend(
                            "/topic/${payload.game}",
                            FrontendAction.START.feJSON()
                        )

                    else -> logger.error("This is impossible to happen!")
                }
                else -> logger.error("This is impossible to happen!")
            }

        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MessagingService::class.java)
    }
}