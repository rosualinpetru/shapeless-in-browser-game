package com.pad.shapeless.designer.service

import com.pad.shapeless.shared.dto.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessagingService @Autowired constructor(private val messagingTemplate: SimpMessagingTemplate) {

    fun updateLobby(game: UUID) {
        synchronized(messagingTemplate) {
            messagingTemplate.convertAndSend(
                "/topic/${game}",
                FrontendAction.UPDATE_LOBBY.feJSON()
            )
        }
    }

    fun updateGame(game: UUID) {
        synchronized(messagingTemplate) {
            messagingTemplate.convertAndSend(
                "/topic/${game}",
                FrontendAction.UPDATE_GAME.feJSON()
            )
        }
    }

    fun handleMessage(payload: DesignerPayload) {
        logger.debug("Received ${payload.javaClass.name} ")
        when (payload) {
            is Failure -> when (payload) {
                is PlayerGameActionError -> {
                    logger.debug(payload.message)
                    synchronized(messagingTemplate) {
                        messagingTemplate.convertAndSend(
                            "/topic/${payload.game}",
                            FrontendAction.GAME_ERROR.feJSON()
                                .plus(mapOf("id" to payload.player, "message" to payload.message))
                        )
                    }
                }
                else -> logger.error("This is impossible to happen!")
            }

            is Success -> when (payload) {
                is PlayerGameAction -> when (payload) {
                    is Joined -> updateLobby(payload.game)
                    is Start ->
                        synchronized(messagingTemplate) {
                            messagingTemplate.convertAndSend(
                                "/topic/${payload.game}",
                                FrontendAction.START.feJSON()
                            )
                        }
                    is Guess -> updateGame(payload.game)
                    is Left -> if (payload.hasGameStarted)
                        updateGame(payload.game)
                    else
                        updateLobby(payload.game)
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